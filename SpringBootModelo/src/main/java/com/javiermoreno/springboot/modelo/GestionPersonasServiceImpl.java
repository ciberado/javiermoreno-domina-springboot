/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.springboot.modelo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public void registrarNuevaPersona(Persona persona) {
        em.persist(persona);
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.format("Nueva persona registrada por {0}.", username);
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
