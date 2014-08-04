/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import com.javiermoreno.springboot.modelo.GestionPersonasService;
import com.javiermoreno.springboot.modelo.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/private")
public class PrivateController {

    @Autowired
    private CounterService counterService;
   
    @Autowired
    private GestionPersonasService service;
    
    @RequestMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    String[] home() {
        counterService.increment("ctrl.private.invoked");
        return new String[]{"Ok, puedes acceder a la parte privada."};
    }
   
    @RequestMapping("/personas/{id}")
    @ResponseStatus(HttpStatus.OK)
    Persona getPersonaPorId(@PathVariable int id) {
        return service.findById(id);
    }
    
}
