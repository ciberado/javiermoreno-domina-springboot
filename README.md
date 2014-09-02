#Demo Spring Boot

Beber con cuidado.

###Características completadas:


* [x] 100% xml free and full of sugar
* [x] Integración con ssl
* [x] Sessionless rest webservices
* [x] Autentificación usuario/password
* [x] Autentificación por token (comprueba ip y ttl)
* [x] Ejemplos de publicación de ficheros estáticos
* [x] Ejemplos de autorización global a nivel de método de servicio
* [x] Ejemplos de paso de parámetros al controlador (path, query, body...)
* [x] Web service Rest completo, incluyendo gestión de status http y CRUD completo
* [x] CORS implementado con spring
* [x] Integración con jpa/hibernate
* [x] Repositorios JPA con paginación automática
* [x] Swagger REST API docs FTW!
* [x] Hateoas con links de paginación generados automáticamente
* [x] Uso de @ConfigurationProperties para recuperar el password del certificado remotamente (simulado)
* [x] Mínima configuración mediante yaml
* [x] Métricas custom!
* [x] Shutdown por endpoint.
* [x] Compresión Gzip activada sobre http (en https genera vectores de ataque).
* [x] Banner personalizado
* [x] Validación por jsr303 ¡incluso a nivel de parámetros de método!
* [x] Gestión de errores y excepciones centralizado.
* [x] Primeros ejemplos de tests de integración
 
###Características pendientes:


* [ ] Conectar con un servicio de configuración centralizado para inicializar la criptografía de token
* [ ] Descargar el certificado actual desde una localización remota
* [ ] Mejorar los @autowired para facilitar test
* [ ] Escribir ---los--- más test!!!! (prioritario)
* [ ] Utilizar los profiles para diferenciar desarrollo y producción (http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-change-configuration-depending-on-the-environment)
