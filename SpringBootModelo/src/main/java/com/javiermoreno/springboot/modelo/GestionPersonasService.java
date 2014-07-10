/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javiermoreno.springboot.modelo;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author ciberado
 */
public interface GestionPersonasService {

    Persona findById(int id);

    void registrarNuevaPersona(Persona persona);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Persona> findByIdDocument(String a);
    
}
