/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.users;

import java.util.Date;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Microservicio de demostración. Permite gestionar la entidad users.
 * Incluye jsr303 a nivel de método, repositories y tests.
 * 
 * 
 * @author ciberado
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackageClasses = {App.class})
@EntityScan(basePackageClasses = {App.class})
public class App {

    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        return bean;
    }
    
    @Bean MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor bean = new MethodValidationPostProcessor();
        bean.setValidator(localValidatorFactoryBean());
        return bean;
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context
                = new SpringApplicationBuilder()
                .showBanner(true)
                .sources(App.class)
                .run(args);

        UserManagementService userService = context.getBean(UserManagementService.class);

        DailyUser user = new DailyUser();
        user.setEmail("alice@wonderland.com");
        user.setBirthday(new Date());

        userService.registerNewUser(user, "secret-alice", true);
    }
}
