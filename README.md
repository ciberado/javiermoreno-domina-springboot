
Primer acercamiento a spring boot. Beber con cuidado.

Características completadas:
===

* 100% xml free and full of sugar
* Integración con ssl
* Sessionless rest webservices
* Autentificación usuario/password
* Autentificación por token (comprueba ip y ttl)
* Ejemplos de publicación de ficheros estáticos
* Ejemplos de autorización global a nivel de método de servicio
* CORS implementado con spring
* Integración con jpa/hibernate
* Uso de @ConfigurationProperties para recuperar el password del certificado remotamente (simulado)
* Mínima configuración mediante yaml


Características pendientes:
===

* Conectar con un servicio de configuración centralizado para inicializar la criptografía de token
* Descargar el certificado actual desde una localización remota
* Mejorar los @autowired para facilitar test
* Escribir los test!!!! (prioritario)
