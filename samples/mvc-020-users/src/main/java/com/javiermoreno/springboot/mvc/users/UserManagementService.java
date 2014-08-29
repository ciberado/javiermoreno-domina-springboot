/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.users;

import javax.validation.constraints.Size;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author ciberado
 */
public interface UserManagementService extends UserDetailsService {

    /**
     *
     * @param user
     * @param confirmationPending If the user must confirm the email address.
     *        Will automatically add the CONFIRMATION_PENDING role.
     * @param plainTextPassword the new password of the user in plain text
     *       (will be saved as a hash).
     * @throws org.springframework.dao.DataIntegrityViolationException if
     *         that email address is already registered.
     */
    void registerNewUser(DailyUser user,  @Size(min = 5) String plainTextPassword, boolean confirmationPending);
    
}
