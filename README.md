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

**Decisión**: Utilizar `@Cacheable` para almacenar en caché los datos de los álbumes y las fotos y usar @CacheEvict para que si existen cambios en la url proveedora se vean reflejados.
NOTA: Implementar una cache que lo revise cada x tiempo, revisar si en caso de que los datos no cambien demasiado CacheEvict es eficiente o puede mejorarse. (CachePut)

**Motivación**: Al almacenar en caché los resultados de las llamadas a la API externa, reducimos la cantidad de llamadas HTTP necesarias, lo que disminuye significativamente el tiempo de respuesta y el uso de ancho de banda. Esto es especialmente útil si los datos de la API externa no cambian frecuentemente.

- El método evictCache utiliza @CacheEvict para limpiar la caché cuando es necesario, asegurando que los datos más recientes sean recuperados cuando se solicitan explícitamente.

- RestTemplate para llamadas HTTP eficientes.
- Uso de factories para encapsular la creación de objetos complejos y permitir configuraciones consistentes.
- Arrays y listas para manejar las estructuras de datos.
- Diseño siguiendo principios SOLID para facilitar el mantenimiento y la escalabilidad.
- Uso de CompletableFuture para Asincronía en la clase AlbumService: fetchAlbumsAsync() y fetchPhotosAsync(): Estos métodos recuperan los datos de álbumes y fotos de manera asíncrona usando CompletableFuture, lo que permite que las llamadas se realicen en paralelo en lugar de secuencialmente, mejorando el rendimiento.
- Procesamiento en Paralelo:
CompletableFuture.allOf: Espera a que ambos CompletableFuture (álbumes y fotos) se completen antes de proceder, asegurando que los datos están listos para ser procesados en paralelo.
parallelStream: Utiliza parallelStream tanto para agrupar las fotos por álbum (groupingByConcurrent) como para asignar las fotos a cada álbum (forEach). Esto permite que las operaciones se ejecuten en paralelo, aprovechando múltiples núcleos de CPU.
- Inicialización de ExecutorService: Un ExecutorService personalizado, creado a través de ExecutorServiceFactory, se utiliza para manejar las tareas asíncronas. Esto permite un control más fino sobre el manejo de hilos y mejora la capacidad de ajuste del rendimiento.
- Logs de Tiempo de Ejecución: Los métodos enrichAlbumsAndSave y enrichAlbums miden y registran el tiempo de ejecución usando System.nanoTime(), lo que ayuda a identificar cuellos de botella y evaluar el rendimiento.


## Compilación y Ejecución

### Compilar el proyecto

Ejecuta el siguiente comando en la terminal dentro del directorio del proyecto:

```sh
./mvnw clean install

URL consola h2

http://localhost:8080/h2-console

