# Job-Radar DB

Job-Radar DB es una aplicación backend orientada a la gestión y persistencia de ofertas de empleo.

El objetivo del proyecto es construir una base sólida para recibir, almacenar, identificar y gestionar ofertas de diferentes fuentes, evitando duplicados y manteniendo una arquitectura clara y mantenible.

## Estado del proyecto

El proyecto se encuentra actualmente en fase de desarrollo.

Actualmente existe:

* Modelo de dominio `JobOffer`.
* Puerto de salida `JobOfferRepository`.
* Caso de uso para crear ofertas.
* Adaptador de persistencia.
* Persistencia mediante Spring Data JPA.
* PostgreSQL como base de datos.
* Migraciones mediante Flyway.
* Identidad de ofertas mediante `source + external_id`.
* Tests unitarios del caso de uso.

Todavía no existe un adaptador de entrada HTTP/REST.

## Stack tecnológico

* Java 21
* Spring Boot
* Maven
* PostgreSQL
* Spring Data JPA / Hibernate
* Flyway
* JUnit
* Docker / Docker Compose

## Arquitectura

El proyecto sigue una arquitectura hexagonal, manteniendo separadas las responsabilidades de dominio, aplicación e infraestructura.

```text
                ┌─────────────────────┐
                │    Entrada futura   │
                │ REST / Importador   │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │     Application     │
                │  Use Cases / Logic  │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │       Domain        │
                │ Model + Ports       │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │   Infrastructure    │
                │ JPA / PostgreSQL    │
                └─────────────────────┘
```

### Domain

Contiene el modelo de negocio y los puertos que representan las necesidades del dominio.

Elementos principales:

* `JobOffer`
* `JobOfferRepository`

El dominio no depende de Spring, JPA ni PostgreSQL.

### Application

Contiene los casos de uso de la aplicación.

Actualmente:

* `CreateJobOfferService`

El servicio utiliza el puerto `JobOfferRepository` y no conoce la implementación concreta de persistencia.

### Infrastructure

Contiene las implementaciones técnicas necesarias para conectar la aplicación con tecnologías externas.

Actualmente incluye:

* `JobOfferPersistenceAdapter`
* `JobOfferJpaRepository`
* `JobOfferEntity`
* `JobOfferMapper`
* Flyway
* Configuración Spring Boot

## Flujo actual

El flujo de creación de una oferta es actualmente:

```text
Caller / Test
     │
     ▼
CreateJobOfferService
     │
     ▼
JobOfferRepository
     │
     ▼
JobOfferPersistenceAdapter
     │
     ▼
JobOfferJpaRepository
     │
     ▼
PostgreSQL
```

Antes de guardar una oferta se comprueba su identidad mediante:

```text
source + external_id
```

La base de datos mantiene una restricción de unicidad sobre esta combinación.

## Base de datos

Las modificaciones del esquema se gestionan mediante Flyway.

Migraciones actuales:

```text
V1__create_job_offer_table.sql
V2__create_job_offer_identity.sql
```

La configuración de Hibernate utiliza:

```text
ddl-auto: validate
```

Hibernate valida el esquema existente, pero no crea ni modifica automáticamente las tablas.

## Testing

El proyecto utiliza JUnit para comprobar el comportamiento de los casos de uso.

La estrategia de desarrollo sigue:

```text
DTT
 ↓
Tests
 ↓
Implementación
 ↓
Refactor
 ↓
Tests verdes
```

Una Change Task no se considera terminada hasta que todos los tests correspondientes están en verde.

## Desarrollo

El desarrollo se organiza mediante Change Tasks (CT).

Cada CT debe tener:

1. Objetivo definido.
2. DTT.
3. Tests.
4. Implementación.
5. Tests en verde.
6. Checkpoint.

Las nuevas funcionalidades deben mantener las funcionalidades y tests existentes.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/
│   │   └── com/opc/jobradar/
│   │       ├── domain/
│   │       ├── application/
│   │       ├── infrastructure/
│   │       └── JobRadarApplication.java
│   │
│   └── resources/
│       └── db/
│           └── migration/
│
└── test/
    └── java/
```

## Ejecución

### Requisitos

Es necesario disponer de:

* Java 21
* Maven
* Docker y Docker Compose
* PostgreSQL, si no se utiliza el contenedor proporcionado por el proyecto.

### Compilar

```bash
mvn clean test
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La configuración concreta de la base de datos depende de la configuración del proyecto.

## Git

El repositorio utiliza Git para mantener un historial de cambios estable y trazable.

La recomendación de trabajo es:

```text
CT
 ↓
Tests 🟢
 ↓
Checkpoint
 ↓
Git commit
```

Los commits deben representar cambios coherentes y preferiblemente una Change Task terminada.

## Roadmap

El desarrollo futuro incluirá, entre otras posibles tareas:

* Completar y endurecer la identidad y deduplicación de ofertas.
* Resolver las decisiones pendientes de fechas y validaciones.
* Revisar la unicidad de URL frente a `source + external_id`.
* Añadir pruebas de integración.
* Definir el adaptador de entrada.
* Añadir API REST.
* Evolucionar progresivamente el modelo de dominio.
* Incorporar nuevas funcionalidades según las necesidades del proyecto.

El roadmap se irá actualizando conforme avance el desarrollo.

## Objetivo del proyecto

Job-Radar DB no pretende únicamente ser una aplicación funcional.

También sirve como proyecto práctico para trabajar progresivamente:

* Java
* Spring Boot
* SQL
* PostgreSQL
* Testing
* Arquitectura hexagonal
* Diseño de software
* Git
* Resolución de problemas
