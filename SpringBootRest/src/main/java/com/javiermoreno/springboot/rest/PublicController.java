/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import com.javiermoreno.springboot.modelo.GestionPersonasService;
import com.javiermoreno.springboot.modelo.Persona;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/public")
@Api(value = "Recursos públicos", description = "Demo de recursos accesibles públicamente sin autentificación.")
public class PublicController {

    @Autowired
    private GestionPersonasService service;

    @Value("${application.identification}")
    private String appIdentification;
    
    @RequestMapping(value="/test", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Testear conectividad", notes = "Permite comprobar el acceso al api publica.")
    String[] demo() {
        return new String[]{"ooooooooook, puedes acceder a la parte pública de " + appIdentification + "."};
    }

    @RequestMapping(value="/vip", method=RequestMethod.GET)
    @ApiOperation(value = "Testear seguridad", notes = "El servicio requiere autorización por lo que "
            + "a pesar de superar el control http fallará si no se indican credenciales.")
    List<Persona> metodoSeguroPorPreautorizacion() {
        return service.findByIdDocument("11111111A");
    }
    
    @RequestMapping(value="/personas", method=RequestMethod.POST)
    @ApiOperation(value="Registrar nueva persona", notes ="Demo parámetro pasado en el body.")
    Persona altaPersona(@RequestBody Persona persona) {
        return service.registrarNuevaPersona(persona);
    }
    
    
    
}
