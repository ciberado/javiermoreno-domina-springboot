/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
