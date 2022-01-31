# Modulo Testing. Parte 1: Testing en Java con Spring

## Requirements

[Enunciado](../requirements.md)

## Requirements

- Java 17
- Maven 3.5 or higher

## Technologies used

- Java 17
- Spring Boot 2.6.3
- JUnit 5
- [Java Faker](https://github.com/DiUS/java-faker)
- [Test Containers](https://www.testcontainers.org/)
- [Equals Verifier](https://jqno.nl/equalsverifier/)
- [@WebMvcTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html)
- [@DataJpaTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.html)


## Description

La clase que contiene los test es [BookControllerTest](./src/test/java/es/urjc/code/daw/library/unitary/BookControllerTest.java)

En clase vimos como cargar la configuración completa de la aplicación y usar MockMVC, usando *@SpringBootTest* combinado con *@AutoConfigureMockMvc* en lugar de esta anotación.

En esta práctica los test de la capa web se realizan usando [@WebMvcTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html).

Es una anotación que se puede usar para una prueba de Spring MVC que se centra solo en los componentes de Spring MVC.
El uso de esta anotación deshabilitará la configuración automática completa y, en su lugar, aplicará solo la configuración relevante para las pruebas de MVC (es decir, @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter, Filter, WebMvcConfigurer y los beans HandlerMethodArgumentResolver, pero no los beans @Component, @Service o @Repository).

De forma predeterminada, las pruebas anotadas con @WebMvcTest también configurarán automáticamente Spring Security y MockMvc (incluye soporte para HtmlUnit WebClient y Selenium WebDriver). Para un control más detallado de MockMVC, se puede usar la anotación @AutoConfigureMockMvc.

Por lo general, @WebMvcTest se usa en combinación con @MockBean o @Import para crear cualquier colaborador requerido por sus beans @Controller.

Como extra se han incluído test de la capa de datos usando [@DataJpaTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.html) y [Test Containers](https://www.testcontainers.org/)

## Github Repo


