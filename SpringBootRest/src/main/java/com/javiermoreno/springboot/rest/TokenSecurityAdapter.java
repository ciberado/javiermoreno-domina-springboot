/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenSecurityAdapter extends WebSecurityConfigurerAdapter {

    private static final String TOKEN_NAME = "X-Token";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("adminadmin").roles("ADMIN", "USER")
                .and()
                .withUser("user").password("useruser").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            .and()
            // Filter order: http://docs.spring.io/spring-security/site/docs/3.2.0.RELEASE/apidocs/org/springframework/security/config/annotation/web/HttpSecurityBuilder.html#addFilter%28javax.servlet.Filter%29
            .addFilterAfter(authenticationTokenProcessingFilterBean(), BasicAuthenticationFilter.class)
            .authorizeRequests()                         
                .antMatchers("/public/**", "/views/**", "/errores/**")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/private/**")
                    .hasRole("ADMIN")
                    //.access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                    .authenticated()
                .and()
                .httpBasic();            
    }

    @Bean
    public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilterBean() throws Exception { 
        return new AuthenticationTokenProcessingFilter(
                tokenCryptographyServiceBean(), userDetailsServiceBean(), authenticationManagerBean());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception  {
        return super.userDetailsServiceBean();
    }
    
    @Bean
    public CryptographyService tokenCryptographyServiceBean() throws Exception {
        return new CryptographyServiceImplBlowfish();
    }
    
}

