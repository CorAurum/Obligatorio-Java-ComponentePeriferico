# Postman Test Payloads

This file contains ready-to-copy JSON payloads for testing the refactored API endpoints with Postman/Yaak. All tenant-scoped endpoints require a `tenantId` parameter.

## Setup Instructions

1. **Base URL**: `http://localhost:8081/api/` (adjust port as needed)
2. **Headers**: Add `Content-Type: application/json` to all requests
3. **tenantId**: Use an existing clientId from the database for testing (each Clinic is a tenant)

## 1. Clinica Endpoints (Global - No tenantId needed)

### POST /api/clinicas - Create Clinic

```json
{
  "nombre": "El SUAT",
  "dominioSubdominio": "elsuat.com",
  "direccion": "Av. por ahi 123",
  "telefono": "+59899123456",
  "email": "contacto@elsuat.com",
  "activo": true
}
```

### GET /api/clinicas - List All Clinics

```
GET http://localhost:8081/api/clinicas
```

### GET /api/clinicas/{domain} - Get Clinic by Domain

```
GET http://localhost:8081/api/clinicas/clinicacentral.com
```

## 2. Administrador Endpoints

### POST /api/administradores - Create Administrator

```json
{
  "tenantId": "clinic-uuid-123",
  "administrador": {
    "nombre": "Juan Carlos",
    "apellido": "Pérez",
    "email": "juan.perez@clinicacentral.com",
    "usuario": "jperez",
    "contrasena": "$2a$10$hashed_password_here",
    "creadorPor": "system"
  }
}
```

### GET /api/administradores - List Administrators

```
GET http://localhost:8081/api/administradores?tenantId=clinic-uuid-123
```

## 3. Usuario de Salud Endpoints

### POST /api/usuarios - Create Health User

```json
{
  "tenantId": "clinic-uuid-123",
  "nombres": "María Elena",
  "apellidos": "González Rodríguez",
  "fechaNacimiento": "1990-05-15",
  "sexo": "F",
  "direccion": "Calle de los Álamos 456, Montevideo",
  "email": "maria.gonzalez@email.com",
  "telefono": "+59899123456",
  "identificadores": [
    {
      "tipo": "CI",
      "valor": "12345678",
      "origen": "Ministerio de Salud",
      "fechaAlta": "2024-01-15T10:30:00"
    },
    {
      "tipo": "PASAPORTE",
      "valor": "AB123456",
      "origen": "Dirección Nacional de Identificación Civil",
      "fechaAlta": "2024-01-15T10:30:00"
    }
  ]
}
```

### POST /api/usuarios - Create User with Minimal Data

```json
{
  "tenantId": "clinic-uuid-123",
  "nombres": "Carlos",
  "apellidos": "Martínez",
  "fechaNacimiento": "1985-12-10",
  "sexo": "M",
  "email": "carlos.martinez@email.com",
  "telefono": "+59898765432"
}
```

### GET /api/usuarios - List Health Users

```
GET http://localhost:8081/api/usuarios?tenantId=clinic-uuid-123
```

## 4. Profesional de Salud Endpoints

### POST /api/profesionales - Create Health Professional

```json
{
  "tenantId": "clinic-uuid-123",
  "profesional": {
    "cedulaIdentidad": "87654321",
    "nombre": "Dra. Ana María",
    "apellido": "Silva López",
    "email": "ana.silva@clinicacentral.com",
    "telefono": "+59898123456",
    "contrasena": "$2a$10$hashed_password_here"
  },
  "especialidades": ["Medicina General", "Pediatría"]
}
```

### POST /api/profesionales - Create Professional with Single Specialty

```json
{
  "tenantId": "clinic-uuid-123",
  "profesional": {
    "cedulaIdentidad": "11223344",
    "nombre": "Dr. Roberto",
    "apellido": "Fernández",
    "email": "roberto.fernandez@clinicacentral.com",
    "telefono": "+59898246813",
    "contrasena": "$2a$10$hashed_password_here"
  },
  "especialidades": ["Cardiología"]
}
```

### GET /api/profesionales - List Health Professionals

```
GET http://localhost:8081/api/profesionales?tenantId=clinic-uuid-123
```

## 5. Documento Clínico Endpoints

### POST /api/documentos - Create Clinical Document (Full)

```json
{
  "tenantId": "clinic-uuid-123",
  "idUsuario": "user-uuid-456",
  "idProfesional": 1,
  "documento": {
    "area": "Medicina General",
    "areaProximoControl": "Medicina General",
    "titulo": "Consulta por Dolor Abdominal",
    "descripcion": "Paciente presenta dolor abdominal agudo en cuadrante inferior derecho desde hace 48 horas. Náuseas y vómitos presentes. Fiebre de 38.2°C.",
    "tipoDocumento": "Consulta",
    "fechaCreacion": "2024-01-15",
    "urlAlojamiento": "https://storage.clinicacentral.com/documents/doc123.pdf",
    "fechaProximaConsultaRecomendada": "2024-01-22T09:00:00",
    "fechaProximaConsultaConfirmada": null,
    "motivosConsulta": [{ "id": 1 }, { "id": 5 }],
    "diagnosticos": [
      {
        "gradoCerteza": { "id": 1 },
        "estadoProblema": { "id": 1 },
        "descripcion": "Apendicitis aguda sospechada"
      },
      {
        "gradoCerteza": { "id": 2 },
        "estadoProblema": { "id": 2 },
        "descripcion": "Gastroenteritis viral"
      }
    ]
  }
}
```

### POST /api/documentos - Create Simple Clinical Document

```json
{
  "tenantId": "clinic-uuid-123",
  "idUsuario": "user-uuid-456",
  "idProfesional": 1,
  "documento": {
    "area": "Medicina General",
    "titulo": "Consulta de Control",
    "descripcion": "Consulta rutinaria de control. Paciente estable.",
    "tipoDocumento": "Consulta",
    "fechaCreacion": "2024-01-15",
    "motivosConsulta": [{ "id": 3 }],
    "diagnosticos": [
      {
        "gradoCerteza": { "id": 1 },
        "estadoProblema": { "id": 1 },
        "descripcion": "Paciente en buen estado general"
      }
    ]
  }
}
```

### GET /api/documentos - List All Clinical Documents

```
GET http://localhost:8081/api/documentos?tenantId=clinic-uuid-123
```

### GET /api/documentos/usuario/{userId} - List Documents by User

```
GET http://localhost:8081/api/documentos/usuario/user-uuid-456?tenantId=clinic-uuid-123
```

### GET /api/documentos/profesional/{professionalId} - List Documents by Professional

```
GET http://localhost:8081/api/documentos/profesional/1?tenantId=clinic-uuid-123
```

### GET /api/documentos/{documentId} - Get Document by ID

```
GET http://localhost:8081/api/documentos/1?tenantId=clinic-uuid-123
```

## Error Testing Payloads

### Invalid tenantId

```json
{
  "tenantId": "invalid-clinic-id",
  "nombres": "Test",
  "apellidos": "User",
  "email": "test@example.com"
}
```

**Expected**: 400 Bad Request - "Clínica no encontrada con ID: invalid-clinic-id"

### Missing required fields

```json
{
  "tenantId": "clinic-uuid-123",
  "apellidos": "User",
  "email": "test@example.com"
}
```

**Expected**: 400 Bad Request - validation error for missing "nombres"

### Duplicate email (for admin/professional creation)

```json
{
  "tenantId": "clinic-uuid-123",
  "administrador": {
    "nombre": "Test",
    "apellido": "Admin",
    "email": "juan.perez@clinicacentral.com", // Same email as above
    "usuario": "testadmin",
    "contrasena": "password",
    "creadorPor": "system"
  }
}
```

**Expected**: 400 Bad Request - "Ya existe un administrador con ese correo"

## Complete Test Sequence

Here's a suggested order to test the API:

1. **Create a Clinic** (POST /api/clinicas) - **Response will include generated `id` to use as `tenantId`**
2. **Create an Administrator** (POST /api/administradores) - Use clinic's `id` as `tenantId`
3. **Create a Health User** (POST /api/usuarios) - Use clinic's `id` as `tenantId`
4. **Create a Health Professional** (POST /api/profesionales) - Use clinic's `id` as `tenantId`
5. **Create a Clinical Document** (POST /api/documentos) - Use clinic's `id` as `tenantId`
6. **List all resources** (GET endpoints) - Use clinic's `id` as `tenantId`
7. **Test error scenarios**

### Postman Test Flow Example:

1. **Create Clinic** → Copy the returned `id` from response
2. **Set Environment Variable**: `tenant_id` = `{{clinic_response.id}}`
3. **Use `{{tenant_id}}`** in all subsequent requests

### Expected Clinic Creation Response:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000", // ← Use this as tenantId
  "nombre": "El SUAT",
  "dominioSubdominio": "elsuat.com",
  "direccion": "Av. por ahi 123",
  "telefono": "+59899123456",
  "email": "contacto@elsuat.com",
  "tipoInstitucion": null,
  "fechaAlta": "2024-01-15T10:30:00",
  "fechaBaja": null
}
```

## Postman Environment Variables

Set up these environment variables in Postman for easy testing:

```
base_url: http://localhost:8081/api
tenant_id: {{clinic_response.id}}  // Set this from clinic creation response
user_id: user-uuid-456
professional_id: 1
clinic_domain: clinicacentral.com
```

### Setting tenant_id from Clinic Response:

1. Create a clinic (POST /api/clinicas)
2. In the response, copy the `id` field value
3. Set `tenant_id` environment variable to that value
4. Use `{{tenant_id}}` in all subsequent requests

**Example**: If clinic creation returns `"id": "550e8400-e29b-41d4-a716-446655440000"`, set `tenant_id = 550e8400-e29b-41d4-a716-446655440000`

Then use `{{base_url}}` in your requests instead of the full URL.

## Authentication Headers

If you implement authentication later, add these headers:

```
Authorization: Bearer {{auth_token}}
X-API-Key: {{api_key}}
```

## Response Validation

For successful requests, expect:

- **POST**: 200 OK or 201 Created with the created resource
- **GET**: 200 OK with array or single object
- **404**: Resource not found
- **400**: Validation errors
- **500**: Server errors

## Data Types Reference

- **String IDs**: Use UUID format like "clinic-uuid-123"
- **Long IDs**: Use numbers like 1, 2, 3
- **Dates**: ISO format "YYYY-MM-DD" or "YYYY-MM-DDTHH:mm:ss"
- **Booleans**: true/false
- **Enums**: Check entity classes for valid values

## Quick Copy-Paste Examples

### Create Everything for a New Clinic:

1. Clinic → Admin → User → Professional → Document

### Bulk Testing:

Use Postman's Runner to execute multiple requests in sequence

### Data Generation:

Consider using tools like Mockaroo or JSON Generator for larger datasets
