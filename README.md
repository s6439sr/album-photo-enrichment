# Album Photo Enrichment Service

## Descripción
El proyecto Album Photo Enrichment es una aplicación diseñada para enriquecer y gestionar álbumes de fotos mediante la integración con una URL proveedora de datos. La aplicación se basa en Spring Boot y utiliza mecanismos de caching para mejorar la eficiencia en el acceso a los datos.
Este proyecto es un servicio de backend construido usando Spring Boot que se encarga de manejar álbumes y fotos obtenidos desde una API externa. Proporciona endpoints para recuperar, enriquecer y almacenar estos datos en una base de datos en memoria H2, utilizando diversas técnicas y patrones de diseño para asegurar eficiencia en tiempo y memoria.

## Arquitectura

### Justificación de la Arquitectura

La arquitectura del proyecto sigue una estructura de capas bien definida para separar las responsabilidades y facilitar el mantenimiento y la escalabilidad:

1. **Controlador (Controller)**: Maneja las solicitudes HTTP y delega la lógica de negocio al servicio correspondiente.
2. **Servicio (Service)**: Contiene la lógica de negocio principal, incluyendo el enriquecimiento de álbumes con fotos.
3. **Repositorio (Repository)**: Maneja las operaciones de persistencia de datos.
4. **Modelo (Model)**: Define las entidades de dominio.
5. **Fábrica (Factory)**: Proporciona instancias configuradas de componentes necesarios, como `RestTemplate` y `ExecutorService`.

Esta arquitectura asegura que cada capa tiene una única responsabilidad, permitiendo cambios y expansiones de forma aislada, en línea con el principio de responsabilidad única (SRP) del SOLID. Además, al usar fábricas para instanciar componentes, facilitamos la configuración y el mantenimiento del código.

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
Se usa RestTemplate para realizar operaciones HTTP de manera sencilla y eficiente.

### 5. Uso de factories para encapsular la creación de objetos complejos y permitir configuraciones consistentes.
El patrón de diseño Factory es utilizado para crear objetos sin tener que especificar la clase exacta del objeto que se va a crear. 
Esto es particularmente útil en escenarios donde la creación de objetos es compleja o necesita ser centralizada por varias razones como la reutilización, la consistencia y el control sobre la instancia.

El patrón Factory nos permite:
-Encapsulamiento de la Creación de Objetos:
Centraliza la creación de objetos en un único lugar, lo que facilita el mantenimiento y la gestión del código.
-Reutilización de Código:
Evita la duplicación de código de creación de objetos en diferentes partes de la aplicación, promoviendo la reutilización y reduciendo el riesgo de errores.
-Control sobre la Instanciación:
Permite controlar y gestionar cómo y cuándo se crean las instancias.

Utilizo el patrón Factory para la creación de instancias de RestTemplate y ExecutorService.

RestTemplateFactory
Esta fábrica proporciona una instancia singleton de RestTemplate, asegurando que todas las partes de la aplicación usen la misma instancia, lo cual puede ser beneficioso para el manejo de recursos y la configuración centralizada.

ExecutorServiceFactory
Esta fábrica proporciona una instancia singleton de ExecutorService con un pool de hilos fijo. Esto es útil para gestionar la concurrencia de manera eficiente, evitando la creación innecesaria de múltiples pools de hilos y asegurando que la aplicación pueda manejar la ejecución concurrente de tareas de manera controlada.

### 6. Arrays y listas para manejar las estructuras de datos.

**Arrays**
- Acceso Directo

Los arrays permiten el acceso directo a sus elementos a través de índices, lo que significa que se puede acceder a cualquier elemento en tiempo constante 
Esto es muy eficiente cuando se necesita acceder frecuentemente a elementos específicos.
Eficiencia de Memoria:

Los arrays son estructuras de datos contiguas en memoria. Esto significa que ocupan menos espacio en comparación con algunas estructuras de datos más complejas como las listas enlazadas.
- Uso en APIs Externas

Muchas APIs y bibliotecas, incluyendo la mayoría de las operaciones de red y serialización/deserialización, utilizan arrays. Por ejemplo, al deserializar JSON a objetos en Java, es común utilizar arrays como estructuras de datos intermedias.
- Inmutabilidad

Si se necesita una colección de tamaño fijo que no va a cambiar, un array es una opción excelente. Esto ayuda a prevenir errores relacionados con la modificación accidental de la colección.


**Listas**
- Flexibilidad:

Las listas en Java, ( ArrayList o LinkedList ), son dinámicas y pueden crecer o disminuir en tamaño según sea necesario. Esto es muy útil cuando el número de elementos no se conoce de antemano o puede cambiar durante la ejecución del programa.
- Operaciones de Alto Nivel:

Las listas en Java proporcionan una rica API que incluye métodos para añadir, eliminar, y manipular elementos, además de otras operaciones como ordenación y búsqueda, lo que facilita el manejo de colecciones de datos.
- Compatibilidad con Streams y Operaciones Paralelas:

Las listas en Java son compatibles con las operaciones de streams y pueden ser fácilmente procesadas de manera paralela, lo cual es muy útil para manejar grandes cantidades de datos de manera eficiente. Esto es especialmente relevante en el proyecto albumphotoenrichment donde se utiliza la programación paralela para procesar álbumes y fotos.

### 7. Diseño siguiendo principios SOLID para facilitar el mantenimiento y la escalabilidad.

Aplicar los principios SOLID en el diseño de software es fundamental para crear sistemas que sean mantenibles, escalables y robustos.

** 1. Single Responsibility Principle**
- Separación de Responsabilidades en el Servicio:
La clase AlbumService se encarga únicamente de la lógica de negocio relacionada con los álbumes y las fotos, mientras que la clase AlbumController se encarga de la lógica de presentación (manejar las solicitudes HTTP).
- Clases Factory:
Las clases RestTemplateFactory y ExecutorServiceFactory están dedicadas a la creación de instancias de RestTemplate y ExecutorService respectivamente.


**2. Open/Closed Principle**
Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.

Aplicación en el Proyecto:
- Uso de Interfaces y Herencia:
Si en el futuro se necesitan diferentes implementaciones de AlbumService, se puede crear una interfaz AlbumService e implementar diferentes variantes sin modificar la implementación existente.
- Configuración y Factores Externos:
Utilización de configuraciones externas y constantes (AlbumPhotoConstants) para permitir cambios sin modificar el código base.

**3. Liskov Substitution Principle**
Las clases derivadas deben poder ser sustituidas por sus clases base sin alterar el comportamiento del programa.

Aplicación en el Proyecto:
- Interfaz de Repositorio:
El uso de AlbumRepository, que extiende JpaRepository, asegura que cualquier implementación de AlbumRepository puede ser utilizada sin romper la funcionalidad.

**4. Interface Segregation Principle**
Los clientes no deben verse obligados a depender de interfaces que no utilizan.

Aplicación en el Proyecto:
- Interfaces Específicas:
Definir interfaces específicas para diferentes servicios o componentes si es necesario.

**5. Dependency Inversion Principle**
Las clases de alto nivel no deben depender de clases de bajo nivel. Ambas deben depender de abstracciones.

Aplicación en el Proyecto:
- Inyección de Dependencias:
Uso de Spring para la inyección de dependencias, permitiendo que las clases dependan de interfaces o abstracciones en lugar de implementaciones concretas.

- Factory:
Las clases de Factoty (RestTemplateFactory, ExecutorServiceFactory) abstraen la creación de objetos, permitiendo cambiar la implementación sin afectar a las clases dependientes.

### 8. Uso de CompletableFuture para Asincronía en la clase AlbumService

fetchAlbumsAsync() y fetchPhotosAsync(): Estos métodos recuperan los datos de álbumes y fotos de manera asíncrona usando CompletableFuture, lo que permite que las llamadas se realicen en paralelo en lugar de secuencialmente, mejorando el rendimiento.
### 9. Procesamiento en Paralelo:

CompletableFuture.allOf: Espera a que ambos CompletableFuture (álbumes y fotos) se completen antes de proceder, asegurando que los datos están listos para ser procesados en paralelo.
parallelStream: Utiliza parallelStream tanto para agrupar las fotos por álbum (groupingByConcurrent) como para asignar las fotos a cada álbum (forEach). Esto permite que las operaciones se ejecuten en paralelo, aprovechando múltiples núcleos de CPU.
### 10. Inicialización de ExecutorService

Un ExecutorService personalizado, creado a través de ExecutorServiceFactory, se utiliza para manejar las tareas asíncronas. Esto permite un control más fino sobre el manejo de hilos y mejora la capacidad de ajuste del rendimiento.
### 11. Logs de Tiempo de Ejecución

Los métodos enrichAlbumsAndSave y enrichAlbums miden y registran el tiempo de ejecución usando System.nanoTime(), lo que ayuda a identificar cuellos de botella y evaluar el rendimiento.

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

# Ejecutar pruebas unitarias e integración
mvn test

# Generar informe de cobertura
mvn jacoco:report

