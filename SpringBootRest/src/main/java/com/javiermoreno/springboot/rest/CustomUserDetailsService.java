/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author ciberado
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    //@Cacheable
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails = (CustomUserDetails) em
                .createNamedQuery("findByUsername")
                .setParameter("username", username)
                .getSingleResult();
        if (userDetails == null) throw new UsernameNotFoundException(
            String.format("{0} username was not found.", username));
        return userDetails;
    }   
    
    
}
