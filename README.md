# Album Photo Enrichment Service

## Descripción
El proyecto Album Photo Enrichment es una aplicación diseñada para enriquecer y gestionar álbumes de fotos mediante la integración con una URL proveedora de datos. La aplicación se basa en Spring Boot y utiliza mecanismos de caching para mejorar la eficiencia en el acceso a los datos.
Este proyecto es un servicio de backend construido usando Spring Boot que se encarga de manejar álbumes y fotos obtenidos desde una API externa. Proporciona endpoints para recuperar, enriquecer y almacenar estos datos en una base de datos en memoria H2, utilizando diversas técnicas y patrones de diseño para asegurar eficiencia en tiempo y memoria.

## Diseño Centrado en la Eficiencia
El diseño y desarrollo de esta aplicación se centraron en la eficiencia debido a varias razones clave:

- Rendimiento:

El uso de caché mejora el tiempo de respuesta de la aplicación al reducir la necesidad de consultas repetitivas a la base de datos.

Escalabilidad:

La configuración de un pool de hilos a través de ExecutorService permite manejar múltiples tareas concurrentes de manera eficiente, mejorando la capacidad de la aplicación para escalar y manejar más solicitudes simultáneamente.
- Mantenimiento:

Spring Boot proporciona una estructura modular y fácilmente configurable, lo que facilita el mantenimiento y la actualización del código a largo plazo.
La separación clara de responsabilidades entre diferentes componentes (servicios, repositorios, controladores) mejora la legibilidad y facilita la colaboración en el desarrollo.

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

## Endpoints

Entorno local: http://localhost:8080/

- GET `albums`: Obtiene, guarda en cache y devuelve los álbumes enriquecidos con sus fotos.
- GET `albums/refresh`: Refresca los datos en caché y devuelve los álbumes actualizados sin guardarlos en la base de datos.
- PUT `albums/refresh-and-save`: Refresca los datos en caché y guarda los álbumes actualizados en la base de datos h2.

## Patrones de Diseño Implementados

### Singleton
El patrón Singleton se ha utilizado para asegurarse de que las instancias de `RestTemplate` y `ExecutorService` sean únicas y compartidas en toda la aplicación. Esto se hace para evitar la sobrecarga de crear múltiples instancias de estos objetos, que pueden ser costosos en términos de recursos.

### Factory Method
Utilizo fábricas (RestTemplateFactory y ExecutorServiceFactory) para encapsular la creación de objetos complejos y permitir configuraciones consistentes.


## Decisiones tomadas

### 1. Uso de Spring Boot
Decisión:
Se utiliza Spring Boot debido a su facilidad de configuración y sus capacidades integradas para desarrollar aplicaciones robustas y escalables rápidamente.

Justificación:
Spring Boot simplifica la configuración inicial y reduce el tiempo de desarrollo. Además, proporciona una integración fluida con Hibernate y otros componentes necesarios para nuestro proyecto.

### 2. Implementación de Cache
Decisión:
- Uso de caché para reducir las llamadas repetitivas a la API externa y mejorar la eficiencia en el acceso a datos.
Utilizar `@Cacheable` para almacenar en caché los datos de los álbumes y las fotos y usar @CacheEvict para que si existen cambios en la url proveedora se vean reflejados.

Justificación:
El uso de una cache reduce significativamente el tiempo de respuesta al evitar consultas repetitivas a la base de datos. 
Esto es crucial para mejorar el rendimiento y la escalabilidad de la aplicación, especialmente cuando se manejan grandes volúmenes de datos.

### 3. Estrategia de Actualización de Caché
Decisión:
- Usar @CacheEvict para que si existen cambios en la url proveedora se vean reflejados.
Inicialmente uso solo @Cacheable, posteriormente añado la funcionalidad @CacheEvict para tener en cuenta el cambio de los datos desde la url provedora. Esta parte pendiente de pruebas ya que mi intención era configurar una cache con tiempo de expiración y cada x tiempo si coger los datos de la url.

Justificación:
Fase de pruebas, no estoy segura de que sea eficiente. Queria implementar un mecanismo para que si los datos cambian que la cache cambie para impedir posibles fallos.

**Motivación**: Al almacenar en caché los resultados de las llamadas a la API externa, reducimos la cantidad de llamadas HTTP necesarias, lo que disminuye significativamente el tiempo de respuesta. Esto es especialmente útil si los datos de la API externa no cambian frecuentemente.

### 4. RestTemplate para llamadas HTTP eficientes.
### 5. Uso de factories para encapsular la creación de objetos complejos y permitir configuraciones consistentes.
### 6. Arrays y listas para manejar las estructuras de datos.
### 7. Diseño siguiendo principios SOLID para facilitar el mantenimiento y la escalabilidad.
### 8. Uso de CompletableFuture para Asincronía en la clase AlbumService: fetchAlbumsAsync() y fetchPhotosAsync(): Estos métodos recuperan los datos de álbumes y fotos de manera asíncrona usando CompletableFuture, lo que permite que las llamadas se realicen en paralelo en lugar de secuencialmente, mejorando el rendimiento.
### 9. Procesamiento en Paralelo:
CompletableFuture.allOf: Espera a que ambos CompletableFuture (álbumes y fotos) se completen antes de proceder, asegurando que los datos están listos para ser procesados en paralelo.
parallelStream: Utiliza parallelStream tanto para agrupar las fotos por álbum (groupingByConcurrent) como para asignar las fotos a cada álbum (forEach). Esto permite que las operaciones se ejecuten en paralelo, aprovechando múltiples núcleos de CPU.
### 10. Inicialización de ExecutorService: Un ExecutorService personalizado, creado a través de ExecutorServiceFactory, se utiliza para manejar las tareas asíncronas. Esto permite un control más fino sobre el manejo de hilos y mejora la capacidad de ajuste del rendimiento.
### 11. Logs de Tiempo de Ejecución: Los métodos enrichAlbumsAndSave y enrichAlbums miden y registran el tiempo de ejecución usando System.nanoTime(), lo que ayuda a identificar cuellos de botella y evaluar el rendimiento.

### 


## Compilación y Ejecución

### Compilar el proyecto

Ejecuta el siguiente comando en la terminal dentro del directorio del proyecto:

- Construir el proyecto con Maven:

mvn clean install

- Ejecutar el proyecto con Maven:

mvn spring-boot:run

- URL consola h2

http://localhost:8080/h2-console

