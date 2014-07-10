/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javiermoreno.springboot.rest;

import com.javiermoreno.springboot.modelo.GestionPersonasService;
import com.javiermoreno.springboot.modelo.Persona;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/public")
public class PublicController {
    
    @Autowired
    private GestionPersonasService service;
    
    @RequestMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    String[] demo() {
        return new String[] {"ok, puedes acceder a la parte pública."};
    }
    
    @RequestMapping("/seguro")
    List<Persona> metodoSeguroPorPreautorizacion() {
        return service.findByIdDocument("11111111A");
    }
}
