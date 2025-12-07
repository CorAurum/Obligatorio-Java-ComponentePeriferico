# API Guide - Componente Periférico Backend

Complete API documentation for Next.js frontend integration.

## Base Configuration

- **Base URL**: `http://localhost:8081` (development)
- **API Prefix**: `/api`
- **Authentication**: JWT Bearer Token (except `/api/auth/**` endpoints)

---

## 🔐 Authentication

### POST `/api/auth/login`
**Public endpoint** - No authentication required

Login with cedula and password. Returns JWT token for subsequent requests.

**Request:**
```typescript
{
  cedula: string;              // Required: User's cedula (national ID)
  password: string;            // Required: User's password
  dominioSubdominio?: string;  // Optional: Clinic domain
}
```

**Response (200 OK):**
```typescript
{
  token: string;              // JWT access token (24h expiration)
  refreshToken: string;       // Refresh token (7 days expiration)
  type: "Bearer";            // Token type
  id: string;                // User ID (can be Long for admin or String for professional)
  username: string;           // Username (cedula)
  role: "ADMINISTRADOR" | "PROFESIONAL";
  clinicaId: string;          // Clinic ID
}
```

**Error Responses:**
- `400 Bad Request`: "Cédula o contraseña inválida: [error message]"
- `400 Bad Request`: "Cédula is required" / "Password is required"

**Example:**
```typescript
const response = await fetch('http://localhost:8081/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    cedula: '12345678',
    password: 'Password123'
  })
});
const data = await response.json();
// Store data.token for future requests
```

---

### POST `/api/auth/refresh`
**Public endpoint** - No authentication required

Refresh access token using refresh token.

**Request:**
```typescript
// Body: string (refresh token)
```

**Response:**
```typescript
// Currently returns 200 OK (implementation pending)
```

---

### POST `/api/auth/validate`
**Public endpoint** - No authentication required

Validate if a JWT token is still valid.

**Request Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```typescript
"Token válido"
```

**Error Responses:**
- `400 Bad Request`: "Token inválido" or "Formato de token inválido"

---

## 🏥 Clinicas (Clinics)

All endpoints require JWT authentication.

### POST `/api/clinicas`
Create a new clinic.

**Request:**
```typescript
{
  id: string;                    // Required: Unique clinic ID
  nombre: string;                // Required: Clinic name
  direccion: string;             // Required: Address
  telefono: string;              // Required: Phone number
  tipoInstitucion: string;       // Required: Institution type
  dominioSubdominio: string;     // Required: Domain (e.g., "suat")
  fechaAlta?: string;            // Optional: ISO datetime string
  fechaBaja?: string | null;     // Optional: ISO datetime string or null
}
```

**Response (200 OK):**
```typescript
// Returns the created Clinica object
```

**Example:**
```typescript
const response = await fetch('http://localhost:8081/api/clinicas', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    id: 'clinic-001',
    nombre: 'Clínica Ejemplo',
    direccion: 'Av. Principal 123',
    telefono: '+59899123456',
    tipoInstitucion: 'Privada',
    dominioSubdominio: 'ejemplo'
  })
});
```

---

### GET `/api/clinicas`
List all clinics.

**Response (200 OK):**
```typescript
Array<{
  id: string;
  nombre: string;
  direccion: string;
  telefono: string;
  tipoInstitucion: string;
  dominioSubdominio: string;
  fechaAlta: string;        // ISO datetime
  fechaBaja: string | null; // ISO datetime or null
  administradores?: number[];  // Array of admin IDs
  profesionales?: string[];     // Array of professional IDs
  usuariosDeSalud?: number[];   // Array of user IDs
}>
```

---

### GET `/api/clinicas/{dominioSubdominio}`
Get clinic by domain.

**Path Parameters:**
- `dominioSubdominio`: string (e.g., "suat")

**Response (200 OK):**
```typescript
{
  id: string;
  nombre: string;
  direccion: string;
  telefono: string;
  tipoInstitucion: string;
  dominioSubdominio: string;
  fechaAlta: string;
  fechaBaja: string | null;
  // ... other fields
}
```

**Error Response:**
- `404 Not Found`: Clinic not found

---

### GET `/api/clinicas/averiguar/{CI}`
Get clinic domain by user's cedula (CI).

**Path Parameters:**
- `CI`: string - User's cedula

**Response (200 OK):**
```typescript
string  // Returns the dominioSubdominio
```

**Error Response:**
- `404 Not Found`: No clinic found for this CI

---

### GET `/api/clinicas/dominio/{dominio}/info`
Get clinic information as DTO.

**Path Parameters:**
- `dominio`: string - Clinic domain

**Response (200 OK):**
```typescript
{
  id: string;
  nombre: string;
  dominioSubdominio: string;
  direccion: string;
  telefono: string;
  tipoInstitucion: string;
}
```

---

### POST `/api/clinicas/baja`
Mark clinic as inactive (set fechaBaja).

**Request:**
```typescript
{
  id: string;                    // Required: Clinic ID
  fechaBaja: string;             // Required: ISO datetime string
}
```

**Response (200 OK):**
```typescript
// Returns updated Clinica object
```

---

## 👥 Usuarios de Salud (Health Users)

All endpoints require JWT authentication.

### POST `/api/usuarios`
Create a new health user.

**Request:**
```typescript
{
  dominioSubdominio: string;     // Required: Clinic domain
  nombres: string;                // Required: First names
  apellidos: string;              // Required: Last names
  fechaNacimiento: string;        // Required: ISO date (YYYY-MM-DD)
  sexo: string;                   // Required: Gender
  direccion: string;              // Required: Address
  email: string;                  // Required: Email
  telefono: string;               // Required: Phone
  identificadores: Array<{        // Required: Array of identifiers
    tipo: string;                 // Identifier type (e.g., "CI", "Pasaporte")
    valor: string;                // Identifier value
    origen: string;               // Origin of identifier
  }>;
}
```

**Response (201 Created):**
```typescript
{
  id: number;                     // Auto-generated ID
  nombre: string;
  apellido: string;
  fechaNacimiento: string;        // ISO date
  sexo: string;
  direccion: string;
  email: string;
  telefono: string;
  fechaRegistro: string;          // ISO datetime
  activo: boolean;
  clinica: string;                // Clinic ID (reference)
  documentos?: number[];           // Array of document IDs
  identificadores?: Array<{
    tipo: string;
    valor: string;
    origen: string;
  }>;
}
```

**Example:**
```typescript
const response = await fetch('http://localhost:8081/api/usuarios', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    dominioSubdominio: 'suat',
    nombres: 'Juan',
    apellidos: 'Pérez',
    fechaNacimiento: '1990-01-15',
    sexo: 'M',
    direccion: 'Av. Principal 456',
    email: 'juan.perez@example.com',
    telefono: '+59898765432',
    identificadores: [
      {
        tipo: 'CI',
        valor: '12345678',
        origen: 'Uruguay'
      }
    ]
  })
});
```

---

### GET `/api/usuarios?dominioSubdominio={domain}`
List all users for a specific clinic.

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<{
  id: number;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  sexo: string;
  direccion: string;
  email: string;
  telefono: string;
  fechaRegistro: string;
  activo: boolean;
  clinica: string;
  documentos?: number[];
  identificadores?: Array<{
    tipo: string;
    valor: string;
    origen: string;
  }>;
}>
```

---

## 👨‍⚕️ Profesionales de Salud (Health Professionals)

All endpoints require JWT authentication.

### POST `/api/profesionales`
Create a new health professional.

**Request:**
```typescript
{
  dominioSubdominio: string;     // Required: Clinic domain
  profesional: {                  // Required: Professional data
    idProfesional: string;        // Required: Unique professional ID
    cedulaIdentidad: string;      // Required: Professional's cedula
    nombre: string;               // Required: First name
    apellido: string;             // Required: Last name
    email: string;                // Required: Email
    telefono: string;             // Required: Phone
    activo?: boolean;              // Optional: Active status (default: true)
    password?: string;            // Optional: Password (if creating with auth)
    role?: "PROFESIONAL";          // Optional: Role (default: PROFESIONAL)
  };
  especialidades: string[];       // Required: Array of specialty IDs
}
```

**Response (200 OK):**
```typescript
{
  idProfesional: string;
  cedulaIdentidad: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  activo: boolean;
  password?: string;               // Only if included in request
  role: "PROFESIONAL";
  clinica: string;                 // Clinic ID (reference)
  especialidades?: Array<{
    id: string;
    nombre: string;
    descripcion: string;
  }>;
  documentos?: string[];           // Array of document IDs
}
```

**Example:**
```typescript
const response = await fetch('http://localhost:8081/api/profesionales', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    dominioSubdominio: 'suat',
    profesional: {
      idProfesional: 'prof-001',
      cedulaIdentidad: '87654321',
      nombre: 'Dra. María',
      apellido: 'González',
      email: 'maria.gonzalez@clinic.com',
      telefono: '+59899112233',
      activo: true
    },
    especialidades: ['esp-001', 'esp-002']
  })
});
```

---

### GET `/api/profesionales?dominioSubdominio={domain}`
List all professionals for a specific clinic.

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<{
  idProfesional: string;
  cedulaIdentidad: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  activo: boolean;
  role: "PROFESIONAL";
  clinica: string;
  especialidades?: Array<{
    id: string;
    nombre: string;
    descripcion: string;
  }>;
  documentos?: string[];
}>
```

---

## 🎓 Especialidades (Specialties)

All endpoints require JWT authentication.

### POST `/api/especialidades`
Create a new specialty.

**Request:**
```typescript
{
  id: string;                     // Required: Unique specialty ID
  nombre: string;                  // Required: Specialty name
  descripcion: string;             // Required: Description
}
```

**Response (201 Created):**
```typescript
{
  id: string;
  nombre: string;
  descripcion: string;
}
```

**Response (200 OK) - If already exists:**
```typescript
"Ya existía en periférico."
```

---

### GET `/api/especialidades`
List all specialties.

**Response (200 OK):**
```typescript
Array<{
  id: string;
  nombre: string;
  descripcion: string;
}>
```

---

### GET `/api/especialidades/{id}`
Get specialty by ID.

**Path Parameters:**
- `id`: string - Specialty ID

**Response (200 OK):**
```typescript
{
  id: string;
  nombre: string;
  descripcion: string;
}
```

**Error Response:**
- `404 Not Found`: "No existe una especialidad con ID: {id}"

---

## 👤 Administradores (Administrators)

All endpoints require JWT authentication.

### POST `/api/administradores`
Create a new administrator.

**Request:**
```typescript
{
  dominioSubdominio: string;      // Required: Clinic domain
  administrador: {                 // Required: Administrator data
    nombre: string;                // Required: First name
    apellido: string;             // Required: Last name
    cedula: string;               // Required: Administrator's cedula
    email: string;                // Required: Email
    usuario: string;               // Required: Username
    creadorPor?: string;          // Optional: Creator identifier
    activo?: boolean;              // Optional: Active status (default: true)
    role?: "ADMINISTRADOR";        // Optional: Role (default: ADMINISTRADOR)
  };
  password: string;               // Required: Raw password (will be hashed)
}
```

**Response (200 OK):**
```typescript
{
  id: number;                      // Auto-generated ID
  nombre: string;
  apellido: string;
  cedula: string;
  email: string;
  usuario: string;
  creadorPor: string;
  activo: boolean;
  password: string;                // Hashed password
  role: "ADMINISTRADOR";
  clinica: string;                 // Clinic ID (reference)
}
```

**Error Response:**
- `400 Bad Request`: "La contraseña es requerida"

**Password Requirements:**
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit

**Example:**
```typescript
const response = await fetch('http://localhost:8081/api/administradores', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    dominioSubdominio: 'suat',
    administrador: {
      nombre: 'Juan',
      apellido: 'Administrador',
      cedula: '12345678',
      email: 'admin@clinic.com',
      usuario: 'jadmin',
      creadorPor: 'system',
      activo: true
    },
    password: 'SecurePass123'
  })
});
```

---

### GET `/api/administradores?dominioSubdominio={domain}`
List all administrators for a specific clinic.

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<{
  id: number;
  nombre: string;
  apellido: string;
  cedula: string;
  email: string;
  usuario: string;
  creadorPor: string;
  activo: boolean;
  role: "ADMINISTRADOR";
  clinica: string;
}>
```

---

## 📄 Documentos Clínicos (Clinical Documents)

All endpoints require JWT authentication.

### POST `/api/documentos`
Create a new clinical document.

**Request:**
```typescript
{
  dominioSubdominio: string;      // Required: Clinic domain
  idUsuario: string;              // Required: User ID (as string)
  idProfesional: string;          // Required: Professional ID
  documento: {                     // Required: Document data
    id: string;                   // Required: Unique document ID
    area: string;                 // Required: Medical area
    areaProximoControl?: string;  // Optional: Next control area
    titulo: string;               // Required: Document title
    descripcion: string;          // Required: Description
    TipoDocumento: string;         // Required: Document type
    UrlAlojamiento?: string;       // Optional: Storage URL
    fechaProximaConsultaRecomendada?: string;  // Optional: ISO datetime
    fechaProximaConsultaConfirmada?: string;    // Optional: ISO datetime
    motivosConsulta?: Array<{      // Optional: Consultation reasons
      id: string;
      nombre: string;
      descripcion: string;
    }>;
    diagnosticos?: Array<{         // Optional: Diagnoses
      id: string;
      codigo: string;
      descripcion: string;
      gradoCerteza: string;
    }>;
  };
}
```

**Response (200 OK):**
```typescript
{
  id: string;
  area: string;
  areaProximoControl: string | null;
  titulo: string;
  descripcion: string;
  TipoDocumento: string;
  fechaCreacion: string;          // ISO datetime (auto-generated)
  UrlAlojamiento: string | null;
  fechaProximaConsultaRecomendada: string | null;
  fechaProximaConsultaConfirmada: string | null;
  usuario: number;                 // User ID (reference)
  profesional: string;             // Professional ID (reference)
  clinica: string;                 // Clinic ID (reference)
  motivosConsulta?: Array<{
    id: string;
    nombre: string;
    descripcion: string;
  }>;
  diagnosticos?: Array<{
    id: string;
    codigo: string;
    descripcion: string;
    gradoCerteza: string;
  }>;
}
```

---

### GET `/api/documentos?dominioSubdominio={domain}`
List all documents for a specific clinic.

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<DocumentoClinico>  // Array of document objects
```

---

### GET `/api/documentos/usuario/{usuarioId}?dominioSubdominio={domain}`
List all documents for a specific user.

**Path Parameters:**
- `usuarioId`: number - User ID

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<DocumentoClinico>
```

---

### GET `/api/documentos/profesional/{profesionalId}?dominioSubdominio={domain}`
List all documents created by a specific professional.

**Path Parameters:**
- `profesionalId`: string - Professional ID

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<DocumentoClinico>
```

---

### GET `/api/documentos/{id}?dominioSubdominio={domain}`
Get a specific document by ID.

**Path Parameters:**
- `id`: string - Document ID

**Query Parameters:**
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
DocumentoClinico  // Full document object
```

---

### GET `/api/documentos/{id}/detalle`
Get document detail DTO (for central component).

**Path Parameters:**
- `id`: string - Document ID

**Response (200 OK):**
```typescript
{
  documento: {
    id: string;
    titulo: string;
    descripcion: string;
    tipoDocumento: string;
    fechaCreacion: string;
    area: string;
    // ... other fields
  };
  diagnosticos: Array<{
    codigo: string;
    descripcion: string;
    gradoCerteza: string;
  }>;
  // ... other nested data
}
```

**Error Response:**
- `404 Not Found`: Document not found

---

### GET `/api/documentos/usuario/{usuarioId}/dto?profesionalId={id}&dominioSubdominio={domain}`
Get documents for a user as DTOs (for professionals).

**Path Parameters:**
- `usuarioId`: number - User ID

**Query Parameters:**
- `profesionalId`: string (required) - Professional ID
- `dominioSubdominio`: string (required) - Clinic domain

**Response (200 OK):**
```typescript
Array<{
  id: string;
  titulo: string;
  descripcion: string;
  tipoDocumento: string;
  fechaCreacion: string;
  // ... other DTO fields
}>
```

---

## 🎨 Personalización (Customization)

All endpoints require JWT authentication.

### POST `/api/personalizacion/{clinicaId}`
Create customization for a clinic.

**Path Parameters:**
- `clinicaId`: string - Clinic ID

**Request:**
```typescript
{
  color: string;                  // Required: Primary color (hex code)
  lema: string;                   // Required: Clinic motto/slogan
  logo: string;                   // Required: Logo URL or path
}
```

**Response (200 OK):**
```typescript
{
  id: number;                     // Auto-generated ID
  color: string;
  lema: string;
  logo: string;
  clinica: {
    id: string;
    nombre: string;
    // ... other clinic fields
  };
}
```

---

### GET `/api/personalizacion/{clinicaId}`
Get customization for a clinic.

**Path Parameters:**
- `clinicaId`: string - Clinic ID

**Response (200 OK):**
```typescript
{
  id: number;
  color: string;
  lema: string;
  logo: string;
  clinica: {
    id: string;
    nombre: string;
    // ... other clinic fields
  };
}
```

---

### PATCH `/api/personalizacion/{id}`
Update customization.

**Path Parameters:**
- `id`: number - Customization ID

**Request:**
```typescript
{
  color?: string;                 // Optional: Primary color
  lema?: string;                  // Optional: Clinic motto
  logo?: string;                  // Optional: Logo URL
}
```

**Response (200 OK):**
```typescript
{
  id: number;
  color: string;
  lema: string;
  logo: string;
  clinica: {
    id: string;
    nombre: string;
    // ... other clinic fields
  };
}
```

---

## 🔒 Authentication & Authorization

### Using JWT Tokens

After logging in, include the JWT token in all protected requests:

```typescript
const headers = {
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`  // Token from login response
};

const response = await fetch('http://localhost:8081/api/clinicas', {
  method: 'GET',
  headers: headers
});
```

### Token Expiration

- **Access Token**: 24 hours (86400000ms)
- **Refresh Token**: 7 days (604800000ms)

### Error Responses

**401 Unauthorized:**
- Token missing or invalid
- Token expired

**403 Forbidden:**
- User doesn't have required permissions

**400 Bad Request:**
- Invalid request data
- Validation errors

**404 Not Found:**
- Resource not found

**500 Internal Server Error:**
- Server error

---

## 📝 TypeScript Types

Here are some helpful TypeScript types you can use:

```typescript
// Auth Types
interface LoginRequest {
  cedula: string;
  password: string;
  dominioSubdominio?: string;
}

interface LoginResponse {
  token: string;
  refreshToken: string;
  type: "Bearer";
  id: string;
  username: string;
  role: "ADMINISTRADOR" | "PROFESIONAL";
  clinicaId: string;
}

// Clinic Types
interface Clinica {
  id: string;
  nombre: string;
  direccion: string;
  telefono: string;
  tipoInstitucion: string;
  dominioSubdominio: string;
  fechaAlta: string;
  fechaBaja: string | null;
}

interface ClinicaDTO {
  id: string;
  nombre: string;
  dominioSubdominio: string;
  direccion: string;
  telefono: string;
  tipoInstitucion: string;
}

// User Types
interface UsuarioRequest {
  dominioSubdominio: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;  // ISO date
  sexo: string;
  direccion: string;
  email: string;
  telefono: string;
  identificadores: IdentificadorRequest[];
}

interface IdentificadorRequest {
  tipo: string;
  valor: string;
  origen: string;
}

// Professional Types
interface ProfesionalRequest {
  dominioSubdominio: string;
  profesional: {
    idProfesional: string;
    cedulaIdentidad: string;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    activo?: boolean;
  };
  especialidades: string[];
}

// Administrator Types
interface AdministradorRequest {
  dominioSubdominio: string;
  administrador: {
    nombre: string;
    apellido: string;
    cedula: string;
    email: string;
    usuario: string;
    creadorPor?: string;
    activo?: boolean;
  };
  password: string;
}

// Document Types
interface DocumentoClinicoRequest {
  dominioSubdominio: string;
  idUsuario: string;
  idProfesional: string;
  documento: {
    id: string;
    area: string;
    titulo: string;
    descripcion: string;
    TipoDocumento: string;
    UrlAlojamiento?: string;
    fechaProximaConsultaRecomendada?: string;
    fechaProximaConsultaConfirmada?: string;
  };
}

// Specialty Types
interface Especialidad {
  id: string;
  nombre: string;
  descripcion: string;
}

// Customization Types
interface Personalizacion {
  id: number;
  color: string;
  lema: string;
  logo: string;
  clinica: Clinica;
}
```

---

## 🚀 Quick Start Example

```typescript
// 1. Login
const loginResponse = await fetch('http://localhost:8081/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    cedula: '12345678',
    password: 'Password123'
  })
});

const { token, role, clinicaId } = await loginResponse.json();

// 2. Store token (e.g., in localStorage or session)
localStorage.setItem('token', token);

// 3. Make authenticated request
const clinicsResponse = await fetch('http://localhost:8081/api/clinicas', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const clinics = await clinicsResponse.json();
```

---

## 📌 Important Notes

1. **All endpoints except `/api/auth/**` require JWT authentication**
2. **Always include `Authorization: Bearer <token>` header for protected endpoints**
3. **Dates should be in ISO format (YYYY-MM-DD for dates, ISO 8601 for datetimes)**
4. **The `dominioSubdominio` parameter is used to identify the clinic in multi-tenant scenarios**
5. **Password requirements: minimum 8 chars, 1 uppercase, 1 lowercase, 1 digit**
6. **Token expires after 24 hours - use refresh token to get a new one**

---

## 🐛 Common Issues

**Issue**: Getting 401 Unauthorized
- **Solution**: Make sure you're including the `Authorization: Bearer <token>` header

**Issue**: Getting 400 Bad Request on login
- **Solution**: Check that password meets requirements (uppercase, lowercase, digit, 8+ chars)

**Issue**: CORS errors
- **Solution**: Backend is configured for `http://localhost:3000` and `http://localhost:3001`. Make sure your frontend runs on one of these ports.

---

**Last Updated**: December 2025
**Backend Version**: ComponentePeriferico v0.0.1-SNAPSHOT

