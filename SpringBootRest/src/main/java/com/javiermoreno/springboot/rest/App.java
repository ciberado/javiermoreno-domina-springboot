/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import com.javiermoreno.springboot.modelo.GestionPersonasService;
import com.javiermoreno.springboot.modelo.Persona;
import com.mangofactory.swagger.plugin.EnableSwagger;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 *
 * @author ciberado
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableWebMvcSecurity
@ComponentScan(value = {"com.javiermoreno.springboot.rest", "com.javiermoreno.springboot.modelo"})
//@EnableJpaRepositories(...) (no necesaria por autoconfiguración)
//@EnableTransactionManagement (no necesario por autoconfiguración)
@EntityScan(basePackages = "com.javiermoreno.springboot.modelo")
@EnableSwagger
public class App {

    @Autowired
    private RemoteApplicationProperties remoteProps;

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        //factory.setPort(7777); (está definido en el application.properties
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errores/error404.html"),
                new ErrorPage(HttpStatus.UNAUTHORIZED, "/errores/error401.html"),
                new ErrorPage(HttpStatus.FORBIDDEN, "/errores/error403.html"));
        // Activación gzip sobre http (*NO* activar sobre ssl, induce ataques.)
        // http://stackoverflow.com/questions/21410317/using-gzip-compression-with-spring-boot-mvc-javaconfig-with-restful
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
            AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
            httpProtocol.setCompression("on");
            httpProtocol.setCompressionMinSize(256);
            String mimeTypes = httpProtocol.getCompressableMimeTypes();
            String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
            httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
        });

        factory.addAdditionalTomcatConnectors(createSslConnector());
        /* En el caso de que se desee sustituír http por https: ************************
         // keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
         final String keystoreFilePath = "keystore.p12";
         final String keystoreType = "PKCS12";
         final String keystoreProvider = "SunJSSE";
         final String keystoreAlias = "tomcat"; 
         factory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector con) -> {
         con.setScheme("https");
         con.setSecure(true);
         Http11NioProtocol proto = (Http11NioProtocol) con.getProtocolHandler();
         proto.setSSLEnabled(true);
         // @todo: Descarga el fichero con el certificado actual 
         File keystoreFile = new File(keystoreFilePath);
         proto.setKeystoreFile(keystoreFile.getAbsolutePath());
         proto.setKeystorePass(remoteProps.getKeystorePass());
         proto.setKeystoreType(keystoreType);
         proto.setProperty("keystoreProvider", keystoreProvider);
         proto.setKeyAlias(keystoreAlias);
         });
         ***************************************************************************** */
        return factory;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        final String keystoreFilePath = "keystore.p12";
        final String keystoreType = "PKCS12";
        final String keystoreProvider = "SunJSSE";
        final String keystoreAlias = "tomcat";
        File keystoreFile = new File(keystoreFilePath);
        // File truststore = new ClassPathResource("keystore").getFile();
        connector.setScheme("https");
        connector.setSecure(true);
        if (serverPort != 0) {
            connector.setPort(serverPort + 1);
        }
        protocol.setSSLEnabled(true);
        protocol.setKeystoreFile(keystoreFile.getAbsolutePath());
        protocol.setKeystorePass(remoteProps.getKeystorePass());
        protocol.setKeystoreType(keystoreType);
        protocol.setProperty("keystoreProvider", keystoreProvider);
        protocol.setKeyAlias(keystoreAlias);
        // protocol.setTruststoreFile(truststore.getAbsolutePath());
        // protocol.setTruststorePass("changeit");
        // protocol.setKeyAlias("apitester");
        return connector;
    }


    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        return bean;
    }
    
    @Bean MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor bean = new MethodValidationPostProcessor();
        bean.setValidator(localValidatorFactoryBean());
        return bean;
    }
    
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context
                = new SpringApplicationBuilder()
                .showBanner(true)
                .sources(App.class)
                .run(args);
        context.addApplicationListener(new ApplicationPidListener());

        GestionPersonasService service = context.getBean(GestionPersonasService.class);
        service.registrarNuevaPersona(new Persona("11111111A", "Karl", "Marx"));
    }
}
