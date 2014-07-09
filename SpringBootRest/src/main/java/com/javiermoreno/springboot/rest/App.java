/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.springboot.rest;

import com.javiermoreno.springboot.modelo.GestionPersonasService;
import com.javiermoreno.springboot.modelo.Persona;
import com.javiermoreno.springboot.modelo.PersonaRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 *
 * @author ciberado
 */
@Configuration
@EnableAutoConfiguration
@EnableWebMvcSecurity
@ComponentScan(value = {"com.javiermoreno.springboot.rest", "com.javiermoreno.springboot.modelo"})
@EntityScan(basePackages = "com.javiermoreno.springboot.modelo")
public class App {

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        //factory.setPort(7777); (está definido en el application.properties
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errores/error404.html"),
                new ErrorPage(HttpStatus.UNAUTHORIZED, "/errores/error401.html"));
        return factory;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            new SpringApplicationBuilder()
                .showBanner(true)
                .sources(App.class)
                .run(args);
        GestionPersonasService service = context.getBean(GestionPersonasService.class);
        service.registrarNuevaPersona(new Persona("11111111A", "Alice", "Wonderland"));
    }
}
