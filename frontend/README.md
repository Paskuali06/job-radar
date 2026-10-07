# Job-Radar Frontend

Frontend de Job-Radar DB desarrollado con React, TypeScript y Vite.

La aplicación proporciona la interfaz web para autenticación, gestión de ofertas de empleo, filtrado, actualización de estados y visualización del dashboard.

## Funcionalidades

Actualmente el frontend permite:

- Registro de usuarios.
- Inicio de sesión.
- Gestión de sesión mediante autenticación HTTP.
- Visualización de la cuenta del usuario.
- Listado de ofertas de empleo.
- Filtrado de ofertas.
- Creación manual de ofertas.
- Edición de ofertas.
- Cambio de estado de ofertas.
- Eliminación de ofertas con confirmación.
- Visualización del enlace original de la oferta.
- Dashboard con resumen de ofertas y estados.
- Manejo de estados de carga y errores.
- Diseño responsive.

## Stack tecnológico

- React 19
- TypeScript
- Vite
- Vitest
- Testing Library
- CSS

## Estructura

```text
src/
├── auth/
├── dashboard/
├── offers/
├── App.tsx
├── App.css
├── main.tsx
└── ...