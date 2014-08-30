/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.users;

import com.google.common.collect.Lists;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author ciberado
 */
@Service
@Validated
public class UserManagementServiceImpl implements UserManagementService {

    private final DailyUserRepository userRepository;

    @Autowired
    public UserManagementServiceImpl(DailyUserRepository userRepository) {
        this.userRepository = userRepository;
    }
        
    
    @Override
    @Transactional
    public DailyUser loadUserByUsername(String email) throws UsernameNotFoundException {
        DailyUser user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(
            String.format("{0} email was not found.", email));
        return user;
    }   
    
    @Override
    @Transactional
    public void registerNewUser(@NotNull DailyUser user,  @Size(min = 5) String plainTextPassword, boolean confirmationPending) {
        if (confirmationPending == true) {
            user.getRoles().add(DailyUser.RoleType.CONFIRMATION_PENDING);
        }     
        user.setPlainTextPassword(plainTextPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Pair<List<DailyUser>, Long> retrieveAllUsers(
            @Min(0) int offset, int amount, Sort.Direction direction, String propertyName) {
        int pageNumber = offset / amount;
        PageRequest pr;
        if (direction != null && propertyName != null) {
            pr = new PageRequest(pageNumber, amount, direction, propertyName);           
        } else {
            pr = new PageRequest(pageNumber, amount);
        }
        Page page = userRepository.findAll(pr);
        return new ImmutablePair(
                Lists.newArrayList(page.iterator()),
                page.getTotalElements());
    }
    
    
    
}
