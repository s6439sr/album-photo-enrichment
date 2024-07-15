# Album Photo Enrichment Service

## Descripción

Este proyecto es un servicio de backend construido usando Spring Boot que se encarga de manejar álbumes y fotos obtenidos desde una API externa. Proporciona endpoints para recuperar, enriquecer y almacenar estos datos en una base de datos en memoria H2, utilizando diversas técnicas y patrones de diseño para asegurar eficiencia en tiempo y memoria.

## Patrones de Diseño Implementados

### Singleton
El patrón Singleton se ha utilizado para asegurarse de que las instancias de `RestTemplate` y `ExecutorService` sean únicas y compartidas en toda la aplicación. Esto se hace para evitar la sobrecarga de crear múltiples instancias de estos objetos, que pueden ser costosos en términos de recursos.

### Factory Method
Utilizo fábricas (RestTemplateFactory y ExecutorServiceFactory) para encapsular la creación de objetos complejos y permitir configuraciones consistentes.

### 


## Endpoints

Entorno local: http://localhost:8080/

- GET `albums`: Obtiene y devuelve los álbumes enriquecidos con sus fotos.
- PUT `albums/refresh`: Refresca los datos en caché y devuelve los álbumes actualizados sin guardarlos en la base de datos.
- PUT `albums/refresh-and-save`: Refresca los datos en caché y guarda los álbumes actualizados en la base de datos h2.

## Tecnologías utilizadas

- **Java 22**: Última versión de Java que ofrece mejoras significativas en el rendimiento y nuevas características del lenguaje.
- **Spring Boot**: Framework para crear aplicaciones independientes de producción basadas en Spring, lo que facilita la configuración y la puesta en marcha del proyecto.
- **Spring Data JPA**: Abstracción de Spring para el acceso a datos usando JPA (Java Persistence API), simplificando la interacción con bases de datos.
- **H2 Database**: Base de datos en memoria que facilita el desarrollo y pruebas sin la necesidad de un servidor de base de datos externo.
- **Spring Cache**: Abstracción de caché que permite mejorar el rendimiento almacenando resultados de operaciones costosas.
- **RestTemplate**: Cliente HTTP sincrónico que facilita la comunicación con APIs externas.
- **JUnit 5**: Framework de pruebas unitarias para Java que permite realizar pruebas eficaces y mantener la calidad del código.
- **Mockito**: Framework de simulación para pruebas unitarias en Java, que permite crear mocks de dependencias.
- **Sonarlint**: Limpieza de código

## Arquitectura
El proyecto sigue una arquitectura de microservicio con una separación clara en capas:
- **Controller**: Maneja las solicitudes HTTP y devuelve respuestas.
- **Service**: Contiene la lógica de negocio y realiza llamadas a APIs externas.
- **Repository**: Interactúa con la base de datos.
- **Config**: Configuración de caché y otras configuraciones de Spring.

## Decisiones de Diseño y Eficiencia
- Uso de caché para reducir las llamadas repetitivas a la API externa.

**Decisión**: Utilizar `@Cacheable` para almacenar en caché los datos de los álbumes y las fotos y usar @CacheEvict para que si existen cambios en la url proveedora se vean reflejados (Aqui me hubiera gustado implementar una cache que lo revise cada x tiempo, no me ha dado tiempo a mirarlo bien pero creo en este caso CacheEvict no es tan eficiente y puede mejorarse porque parto de que los datos de la url no son muy variables).

**Motivación**: Al almacenar en caché los resultados de las llamadas a la API externa, reducimos la cantidad de llamadas HTTP necesarias, lo que disminuye significativamente el tiempo de respuesta y el uso de ancho de banda. Esto es especialmente útil si los datos de la API externa no cambian frecuentemente.

- RestTemplate para llamadas HTTP eficientes.
- Arrays y listas para manejar las estructuras de datos.
- Diseño siguiendo principios SOLID para facilitar el mantenimiento y la escalabilidad.


## Compilación y Ejecución

### Compilar el proyecto

Ejecuta el siguiente comando en la terminal dentro del directorio del proyecto:

```sh
./mvnw clean install

URL consola h2

http://localhost:8080/h2-console








# Album Photo Enrichment Service

## Descripción

Este proyecto es un microservicio en Spring Boot que enriquece datos de álbumes con fotos obtenidas de una API externa y los almacena en una base de datos en memoria H2. El microservicio expone varios endpoints, incluyendo uno para obtener solo las fotos. El objetivo principal es proporcionar una solución eficiente tanto en tiempo de respuesta como en uso de memoria.

## Endpoints

- `/api/albums/enriched`: Enriquecimiento de álbumes con fotos y almacenamiento en base de datos.
- `/api/albums/enriched-no-db`: Enriquecimiento de álbumes con fotos sin almacenamiento en base de datos.
- `/api/albums`: Recupera todos los álbumes almacenados en la base de datos.
- `/api/photos`: Obtiene todas las fotos desde la API externa.






Concurrent Collection: Utilizamos Collectors.groupingByConcurrent para agrupar fotos de manera concurrente y eficiente.
## Arquitectura

El proyecto sigue una arquitectura de microservicio con una separación clara en capas, siguiendo los principios de diseño SOLID para asegurar un código limpio, mantenible y escalable.

### Capas

1. **Controller**: Maneja las solicitudes HTTP y devuelve respuestas adecuadas. La lógica de negocio no se incluye en esta capa para mantener una separación de responsabilidades clara.
2. **Service**: Contiene la lógica de negocio y realiza llamadas a APIs externas. Aquí es donde se realiza el enriquecimiento de los datos de los álbumes con las fotos.
3. **Repository**: Interactúa con la base de datos. Utiliza Spring Data JPA para simplificar el acceso a los datos.
4. **Model**: Define las entidades del dominio, como `Album` y `Photo`.
5. **Config**: Configuraciones de la aplicación, incluyendo la configuración de caché.

## Decisiones de Diseño y Eficiencia

### Uso de Caché

**Decisión**: Utilizar `@Cacheable` para almacenar en caché los datos de los álbumes y las fotos.

**Motivación**: Al almacenar en caché los resultados de las llamadas a la API externa, reducimos la cantidad de llamadas HTTP necesarias, lo que disminuye significativamente el tiempo de respuesta y el uso de ancho de banda. Esto es especialmente útil si los datos de la API externa no cambian frecuentemente.

**Implementación**:


