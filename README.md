# Job-Radar DB

Job-Radar DB es una aplicación web para gestionar y realizar el seguimiento de ofertas de empleo.

El proyecto permite registrar ofertas, almacenarlas de forma persistente, filtrarlas y gestionar su estado durante el proceso de búsqueda de empleo.

Cada usuario dispone de sus propias ofertas y únicamente puede acceder y modificar las ofertas que le pertenecen.

## Estado del proyecto

El proyecto alcanza su primera release funcional **1.0**.

Actualmente existe:

- Modelo de dominio `JobOffer`.
- Gestión de usuarios y roles.
- Registro de usuarios.
- Autenticación mediante login y sesión HTTP.
- Aislamiento de ofertas por usuario.
- Creación manual de ofertas.
- Consulta de ofertas.
- Listado y filtrado de ofertas.
- Actualización de ofertas.
- Cambio de estado de ofertas.
- Historial de cambios de estado.
- Eliminación de ofertas.
- Confirmación de eliminación desde el frontend.
- Validación de datos de entrada.
- Validación del formato de las URL.
- Identidad de ofertas mediante `user_id + source + external_id`.
- Restricciones de integridad y unicidad en base de datos.
- Persistencia mediante Spring Data JPA.
- PostgreSQL como base de datos.
- Migraciones mediante Flyway.
- API HTTP/REST.
- Frontend desarrollado con React y Vite.
- Configuración de producción mediante variables de entorno.
- Ejecución mediante Docker y Docker Compose.
- Health check mediante Spring Boot Actuator.
- Swagger/OpenAPI desactivado en producción.
- Tests automatizados de backend y frontend.

No se utiliza `score`, `classification` ni ningún sistema de puntuación o clasificación automática de ofertas.

## Stack tecnológico

### Backend

- Java 21
- Spring Boot
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- JUnit
- Mockito

### Frontend

- React
- TypeScript
- Vite
- Vitest
- Testing Library

### Infraestructura

- Docker
- Docker Compose
- Nginx

## Arquitectura

El backend sigue una arquitectura hexagonal, manteniendo separadas las responsabilidades de dominio, aplicación e infraestructura.

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