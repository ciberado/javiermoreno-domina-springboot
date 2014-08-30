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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

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
    
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "GET users", notes = "Paginable.")    
    public HttpEntity<UsersPageResource> showAll(
            @RequestParam(defaultValue = "0") int offset, 
            @RequestParam(defaultValue = "100") int amount,
            @RequestParam(required = false) Sort.Direction direction, 
            @RequestParam(required = false) String propertyName) {
        
        Pair<List<DailyUser>, Long> data = userService.retrieveAllUsers(offset, amount, direction, propertyName);
        
        UsersPageResource resource = new UsersPageResource();
        resource.users = data.getLeft();
        resource.offset = offset;
        resource.amount = amount;
        resource.total = data.getRight();
        resource.add(linkTo(methodOn(UserCtrl.class).showAll(offset, amount, direction, propertyName)).withSelfRel());
        if (offset > 0) {
            int prev = Math.max(offset-amount, 0);
            resource.add(linkTo(methodOn(UserCtrl.class).showAll(prev, amount, direction, propertyName)).withRel("prevPage"));
        }
        if (offset + amount < resource.total) {
            resource.add(linkTo(methodOn(UserCtrl.class).showAll(offset+amount, amount, direction, propertyName)).withRel("nextPage"));
        }
        
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    public class UsersPageResource extends ResourceSupport {
        public List<DailyUser> users;
        
        public int offset;
        public int amount;
        public long total;
    }
}
    

