/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javiermoreno.springboot.modelo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ciberado
 */
@Component
@Repository
public interface PersonaRepository extends CrudRepository<Persona, Integer>{
    
    List<Persona> findByNombre(String nombre);
    
    List<Persona> findByApellidos(String apellidos);
}
