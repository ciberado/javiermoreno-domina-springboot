/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * Inspirado en
 * http://stackoverflow.com/questions/10826293/restful-authentication-via-spring
 *
 * @author ciberado
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private final CryptographyService cryptoService;    
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationTokenProcessingFilter(CryptographyService cryptoService, 
            UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.cryptoService = cryptoService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String encryptedToken = request.getHeader("X-Auth-Token");
        if (SecurityContextHolder.getContext().getAuthentication() == null && encryptedToken != null) {
            Token token = new Token(cryptoService, encryptedToken);
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null)  {
                ip = request.getRemoteAddr();
            }
            if (ip.equals(token.getIp()) == true && token.isExpired() == false) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
            }

        }
        chain.doFilter(req, res);
    }


}
