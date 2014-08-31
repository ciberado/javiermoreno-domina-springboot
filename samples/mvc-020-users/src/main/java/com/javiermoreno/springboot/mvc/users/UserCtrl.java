/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.users;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static java.lang.Math.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/users")
@Api(value = "User management.", 
     description = "CRUD endpoint for users and token generation service.")
public class UserCtrl {
    
    
    private final UserManagementService userService;
    private final DailyUserRepository userRepository;
    
    @Autowired
    public UserCtrl(UserManagementService userService, DailyUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves an user given his/her username or id.
     * @param emailOrId email or id (diferenciated by looking for an @).
     * @return 200 if ok, 404 if not found.
     */
    @RequestMapping(value="/{emailOrId}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "GET /users/{emailOrId}", notes = "404 if user not found")    
    public HttpEntity<DailyUserResource> showUser(@PathVariable String emailOrId) {
        DailyUser user;
        if (emailOrId.contains("@") == true) {
            user = userRepository.findByEmail(emailOrId);
        } else {
            int id = Integer.parseInt(emailOrId);
            user = userRepository.findOne(id);
        }
        if (user != null) {
            DailyUserResource resource = new DailyUserResource();
            resource.user = user;
            resource.add(linkTo(methodOn(UserCtrl.class).showUser(emailOrId)).withSelfRel());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }    
    
    private class DailyUserResource extends ResourceSupport {
        public DailyUser user;
    }
    
    /**
     * Creates a new user with the provided password
     * @param dto An object with a DailyUser and a Password
     * @return 201 Created if ok, 409 Conflict if already exists that username.
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "POST /users", notes = "Returns the created user with its assigned id. 201 if ok, 409 if already exists.")    
    public HttpEntity<DailyUserResource> createNewUser(@RequestBody UserAndPasswordDTO dto) {
        DailyUserResource resource = new DailyUserResource();
        resource.user = dto.user;
        resource.add(linkTo(methodOn(UserCtrl.class).showUser(dto.user.getEmail())).withSelfRel());
        try {           
            userService.registerNewUser(dto.user, dto.password, true);
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (DataIntegrityViolationException exc) {
            return new ResponseEntity<>(resource, HttpStatus.CONFLICT);
        }
    }    
    
    private class UserAndPasswordDTO {
        public DailyUser user;
        public String password;
    }
        
    /**
     * Retrieves the users registered in the application.
     * @param page page of the results
     * @param amount how many results
     * @param direction ASC or DESC
     * @param sortingProperty name of the property used for sorting
     * @return 
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "GET /users", notes = "Paginable.")    
    public HttpEntity<DailyUserPageResource> showAll(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "25") int amount,
            @RequestParam(required = false) Sort.Direction direction, 
            @RequestParam(required = false) String sortingProperty) {
        
        Pair<List<DailyUser>, Long> data = userService.retrieveAllUsers(page, amount, direction, sortingProperty);
        
        DailyUserPageResource resource = new DailyUserPageResource();
        resource.users = data.getLeft();
        resource.page = page;
        resource.amount = amount;
        resource.total = data.getRight();
        resource.lastPageNumber = (int) (resource.total / resource.amount);
        
        resource.add(linkTo(methodOn(UserCtrl.class).showAll(page, amount, direction, sortingProperty)).withSelfRel());
        resource.add(linkTo(methodOn(UserCtrl.class).showAll(0, amount, direction, sortingProperty)).withRel("firstPage"));
        resource.add(linkTo(methodOn(UserCtrl.class).showAll(resource.lastPageNumber, amount, direction, sortingProperty)).withRel("lastPage"));
        if (page > 0) {
            resource.add(linkTo(methodOn(UserCtrl.class).showAll(page-1, amount, direction, sortingProperty)).withRel("prevPage"));
        }
        if (page + amount < resource.total) {
            resource.add(linkTo(methodOn(UserCtrl.class).showAll(page+1, amount, direction, sortingProperty)).withRel("nextPage"));
        }
        for (int i=max(0, page-5); i < min(page+5, resource.lastPageNumber); i++) {
            resource.add(linkTo(methodOn(UserCtrl.class).showAll(i, amount, direction, sortingProperty)).withRel("page" + i));
        }
                
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    
    private class DailyUserPageResource extends ResourceSupport {
        public List<DailyUser> users;
        public int page;
        public int amount;
        public int lastPageNumber;
        public long total;
    }
    
    /**
     * Deletes an user.
     * @param emailOrId identified by looking for an "@". 
     */
    @RequestMapping(value="/{emailOrId}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "DELETE /users/{emailOrId}", notes = "204 no content if ok, 404 if not found.")    
    ResponseEntity deleteUser(@PathVariable String emailOrId) {
        try {
            DailyUser user;
            if (emailOrId.contains("@") == true) {
                int affected = userRepository.deleteByEmail(emailOrId);
            } else {
                int id = Integer.parseInt(emailOrId);
                userRepository.delete(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    
    
}
    

