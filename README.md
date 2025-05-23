# providers_api

Microservicio para la gestión de proveedores de servicios turísticos (CRUD, relación con reservas, y envío de información a otros módulos).

## 📌 Descripción

`providers_api` es un microservicio encargado de manejar toda la información relacionada con **los proveedores de servicios** dentro del ecosistema de turismo de bienestar. Entre sus funcionalidades principales se incluyen:

- Registro y edición de proveedores.
- Gestión de los servicios ofrecidos por cada proveedor.
- Consulta de disponibilidad y reservas asociadas.
- Envío de información hacia colas de mensajería para alimentar dashboards y análisis.

Este microservicio es parte de una arquitectura de microservicios que busca facilitar la interoperabilidad entre actores del ecosistema turístico.

## 🚀 Tecnologías

- Java 17  
- Spring Boot  
- Spring Data JPA  
- Firebase Authentication / JWT  
- Maven  
- PostgreSQL  
- RabbitMQ

## 📡 Endpoints principales

### 📘 Gestión de Proveedores

- `POST /proveedores` → Registrar nuevo proveedor  
- `GET /proveedores/{id}` → Obtener proveedor por ID  
- `PUT /proveedores/{id}` → Actualizar información del proveedor  
- `DELETE /proveedores/{id}` → Eliminar proveedor  
- `GET /proveedores` → Listar todos los proveedores  

### 📘 Gestión de Servicios

- `POST /servicios` → Crear un nuevo servicio  
- `GET /servicios/{id}` → Obtener servicio por ID  
- `PUT /servicios/{id}` → Actualizar servicio  
- `DELETE /servicios/{id}` → Eliminar servicio  
- `GET /servicios?proveedorId=` → Obtener servicios de un proveedor  

### 📘 Reservas

- `GET /reservas/proveedor/{proveedorId}` → Obtener todas las reservas asociadas a los servicios del proveedor  
- Resumen de reservas: confirmadas, completas o canceladas

## 🛡️ Seguridad

La seguridad se implementa mediante JWT o Firebase Authentication.  
Los endpoints están protegidos por roles como `ROLE_PROVEEDOR` y `ROLE_ADMIN`.

## ⚙️ Para su funcionamiento

Este microservicio debe ejecutarse junto con los siguientes componentes:

- **Frontend** [`FrontEnd_IWellness`](https://github.com/IWellnessTesis/FrontEnd_IWellness).
- **Microservicio de gestión de usuarios** [`admin_users_api`](https://github.com/IWellnessTesis/IWellness_data_services/tree/main).
- **Microservicio de gestión de preferencias** [`user_preferences_api`](https://github.com/IWellnessTesis/user_preferences_api).

Y además requiere los siguientes servicios para la mensajería y almacenamiento analítico:

- **Servidor de mensajería**   [`Queue_Rabbit`](https://github.com/IWellnessTesis/Queue-Rabbit).
- **Microservicio de procesamiento de datos**   [`IWellness_Data_Services`](https://github.com/IWellnessTesis/IWellness_data_services/tree/main).
- **Base de datos de persistencia (MySQL)** incluida en [`IWellness_DB`](https://github.com/IWellnessTesis/IWellness-DB).

## 🧪 Pruebas

- JUnit, Mockito y MockMvc para pruebas unitarias y de integración.
