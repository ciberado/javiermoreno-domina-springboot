/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.modelo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ciberado
 */
@Service
public class GestionPersonasServiceImpl implements GestionPersonasService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Persona findById(int id) {
        return em.find(Persona.class, id);
    }

    @Transactional
    @Override
    public Persona registrarNuevaPersona(Persona persona) {
        em.persist(persona);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            System.out.format("Nueva persona registrada por {0}.", username);
        } 
        return persona;
    }

    @Override
    public List<Persona> findByIdDocument(String doc) {
        List<Persona> resultado = em
                .createQuery("select p from Persona as p where docIdentificacion = :doc")
                .setParameter("doc", doc)
                .getResultList();
        return resultado;
    }

}
