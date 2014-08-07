/* Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/credentials")
@Api(value = "Autentificación por token", description = "Permite a un usuario autentificado obtener un token de identificación.")
public class CredentialsController {
    @Value("${application.tokenTTL}")
    private int TOKEN_TTL;
    
    @Autowired
    private CryptographyService cryptoService;
    
    @RequestMapping(value="/token", method = RequestMethod.GET)
    @ApiOperation(value = "Generación token", notes = "Retorna un token en Base64 con una duración marcada por TOKEN_TTL.")
    public Token createNewAuthToken(HttpServletRequest request) {
        long ttl = System.currentTimeMillis() + 1000 * TOKEN_TTL;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = principal instanceof UserDetails ? 
                ((UserDetails) principal).getUsername() : 
                principal.toString();
        String ip = request.getRemoteAddr();
        Token token = new Token(cryptoService, username, ip, ttl);
        
        return token;
    }
}
