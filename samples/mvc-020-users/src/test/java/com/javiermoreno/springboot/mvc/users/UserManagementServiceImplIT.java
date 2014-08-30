/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.users;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import javax.validation.ValidationException;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ciberado
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {App.class})
public class UserManagementServiceImplIT {
    
    @Autowired
    private UserManagementService service;
    
    public UserManagementServiceImplIT() {
    }

    @Test
    public void checkNewUserIsSavedAndContainsCorrectPasswordHash() {
        DailyUser user = new DailyUser();
        user.setEmail("alice@wonderland.com");
        user.setBirthday(
                Date.from(LocalDate.of(1976, Month.DECEMBER, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        service.registerNewUser(user, "secret", true);
       
        Assert.assertNotSame("Id correctly assigned", 0, user.getId());
        Assert.assertEquals("Password md5 is correctely encoded.", "5ebe2294ecd0e0f08eab7690d2a6ee69", user.getPassword());
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void checkDuplicatedEmailNotAllowed() {
        service.registerNewUser(new DailyUser("bob@esponja.com", new Date()), "12345", true);
        service.registerNewUser(new DailyUser("bob@esponja.com", new Date()), "12345", true);
    }
    
    @Test(expected = ValidationException.class)
    public void checkPasswordTooShortWhenCreatingNewUser() {
        service.registerNewUser(new DailyUser(), "", false);
    }
    
}
