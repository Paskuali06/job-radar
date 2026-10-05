# Job-Radar DB

Job-Radar DB es una aplicación backend orientada a la gestión, persistencia y seguimiento de ofertas de empleo.

El objetivo del proyecto es construir una base sólida para recibir, almacenar, identificar y gestionar ofertas de diferentes fuentes, evitando duplicados y manteniendo una arquitectura clara y mantenible.

Cada usuario dispone de sus propias ofertas y únicamente puede acceder y modificar las ofertas que le pertenecen.

## Estado del proyecto

El proyecto alcanza su primera release funcional **1.0**.

Actualmente existe:

* Modelo de dominio `JobOffer`.

* Gestión de usuarios y roles.

* Registro de usuarios.

* Autenticación mediante login y sesión HTTP.

* Aislamiento de ofertas por usuario.

* Creación de ofertas.

* Consulta de ofertas por ID.

* Listado y filtrado de ofertas.

* Actualización de ofertas.

* Cambio de estado de ofertas.

* Eliminación de ofertas.

* Historial de cambios de estado.

* Validación de datos de entrada.

* Validación del formato de las URL.

* Prevención de ofertas duplicadas.

* Identidad de ofertas mediante `user_id + source + external_id`.

* Restricción de unicidad en base de datos para `user_id + source + external_id`.

* Persistencia mediante Spring Data JPA.

* PostgreSQL como base de datos.

* Migraciones mediante Flyway.

* API HTTP/REST para las funcionalidades implementadas.

* Configuración de producción mediante variables de entorno.

* Ejecución mediante Docker y Docker Compose.

* Health check mediante Spring Boot Actuator.

* Swagger/OpenAPI desactivado en producción.

* **203 tests automatizados en verde.**

No se utiliza `score`, `classification` ni ningún sistema de puntuación o clasificación automática de ofertas.

## Stack tecnológico

* Java 21

* Spring Boot

* Maven

* PostgreSQL

* Spring Data JPA / Hibernate

* Flyway

* JUnit

* Mockito

* Docker / Docker Compose

## Arquitectura

El proyecto sigue una arquitectura hexagonal, manteniendo separadas las responsabilidades de dominio, aplicación e infraestructura.

```text
                ┌─────────────────────┐
                │   Entrada HTTP/REST │
                │    Controllers      │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │     Application     │
                │   Use Cases / Logic │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │       Domain        │
                │    Model + Ports    │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │    Infrastructure   │
                │ JPA / PostgreSQL /  │
                │    Web / Flyway     │
                └─────────────────────┘