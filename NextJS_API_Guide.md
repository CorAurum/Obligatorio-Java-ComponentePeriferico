# Next.js API Integration Guide

This guide shows how to send payloads from the Next.js app to the backend API. The backend now uses `dominioSubdominio` (domain names) instead of UUID `tenantId` for cleaner URLs and better UX.

## Base URL Structure

Your Next.js app will have URLs like: `periferico.vercel.app/[dominioSubdominio]/...`

Examples:

- `periferico.vercel.app/centrovida.enbodi.xyz/usuarios`
- `periferico.vercel.app/clinicabc.com/documentos`

Backend API base URL: `http://your-backend-url:8081/api/`

## Authentication & Headers

All requests should include:

```javascript
const headers = {
  "Content-Type": "application/json",
  // Add any auth headers you need
};
```

## API Endpoints

### 1. Clinica Endpoints (Global - No dominioSubdominio needed)

#### Create Clinica

```javascript
const response = await fetch("http://your-backend/api/clinicas", {
  method: "POST",
  headers,
  body: JSON.stringify({
    nombre: "Clínica ABC",
    dominioSubdominio: "clinicabc.com",
    // other clinica fields...
  }),
});
```

#### List All Clinicas

```javascript
const response = await fetch("http://your-backend/api/clinicas", {
  method: "GET",
  headers,
});
```

#### Get Clinica by Domain

```javascript
const response = await fetch("http://your-backend/api/clinicas/example.com", {
  method: "GET",
  headers,
});
```

#### Find Clinic Domain by User CI

```javascript
// Returns the dominioSubdominio for a given CI (identity card number)
const response = await fetch(
  "http://your-backend/api/clinicas/averiguar/12345678",
  {
    method: "GET",
    headers,
  }
);

// Returns: "centrovida.enbodi.xyz"
```

#### Get Clinica Info (for frontend display)

```javascript
const response = await fetch(
  "http://your-backend/api/clinicas/dominio/example.com/info",
  {
    method: "GET",
    headers,
  }
);

// Returns: { id, nombre, dominioSubdominio, direccion, telefono, tipoInstitucion }
```

```javascript
const response = await fetch("http://your-backend/api/clinicas/example.com", {
  method: "GET",
  headers,
});
```

### 2. Administrador Endpoints

#### Create Administrador

```javascript
const response = await fetch("http://your-backend/api/administradores", {
  method: "POST",
  headers,
  body: JSON.stringify({
    dominioSubdominio: "clinicabc.com", // Required
    administrador: {
      nombre: "Juan",
      apellido: "Pérez",
      email: "juan@clinicabc.com",
      usuario: "jperez",
      creadorPor: "admin",
    },
  }),
});
```

#### List Administradores for a Domain

```javascript
const response = await fetch(
  "http://your-backend/api/administradores?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

### 3. Usuario de Salud Endpoints

#### Create Usuario

```javascript
const response = await fetch("http://your-backend/api/usuarios", {
  method: "POST",
  headers,
  body: JSON.stringify({
    dominioSubdominio: "clinicabc.com", // Required
    nombres: "María",
    apellidos: "González",
    fechaNacimiento: "1990-05-15",
    sexo: "F",
    direccion: "Calle 123",
    email: "maria@email.com",
    telefono: "+59899123456",
    identificadores: [
      {
        tipo: "CI",
        valor: "12345678",
        origen: "Ministerio de Salud",
        fechaAlta: "2024-01-01T00:00:00",
      },
    ],
  }),
});
```

#### List Usuarios for a Domain

```javascript
const response = await fetch(
  "http://your-backend/api/usuarios?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

### 4. Profesional de Salud Endpoints

#### Create Profesional

```javascript
const response = await fetch("http://your-backend/api/profesionales", {
  method: "POST",
  headers,
  body: JSON.stringify({
    dominioSubdominio: "clinicabc.com", // Required
    profesional: {
      cedulaIdentidad: "12345678",
      nombre: "Dr. Carlos",
      apellido: "Rodríguez",
      email: "carlos@clinicabc.com",
      telefono: "+59898765432",
    },
    especialidades: ["Medicina General", "Cardiología"],
  }),
});
```

#### List Profesionales for a Domain

```javascript
const response = await fetch(
  "http://your-backend/api/profesionales?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

### 5. Documento Clínico Endpoints

#### Create Documento Clínico

```javascript
const response = await fetch("http://your-backend/api/documentos", {
  method: "POST",
  headers,
  body: JSON.stringify({
    dominioSubdominio: "clinicabc.com", // Required
    idUsuario: "user-uuid-456", // String ID of the user
    idProfesional: 789, // Long ID of the professional
    documento: {
      area: "Medicina General",
      areaProximoControl: "Medicina General",
      titulo: "Consulta de Rutina",
      descripcion: "Paciente presenta síntomas...",
      tipoDocumento: "Consulta",
      fechaCreacion: "2024-01-15",
      urlAlojamiento: "https://storage.example.com/doc123.pdf",
      fechaProximaConsultaRecomendada: "2024-02-15T10:00:00",
      fechaProximaConsultaConfirmada: null,
      motivosConsulta: [
        { id: 1 }, // MotivoConsulta entity ID
        { id: 3 },
      ],
      diagnosticos: [
        {
          gradoCerteza: { id: 1 },
          estadoProblema: { id: 1 },
          descripcion: "Hipertensión arterial",
        },
      ],
    },
  }),
});
```

#### List All Documentos for a Domain

```javascript
const response = await fetch(
  "http://your-backend/api/documentos?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

#### List Documentos by Usuario

```javascript
const response = await fetch(
  "http://your-backend/api/documentos/usuario/user-uuid-456?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

#### List Documentos by Profesional

```javascript
const response = await fetch(
  "http://your-backend/api/documentos/profesional/789?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

#### Get Documento by ID

```javascript
const response = await fetch(
  "http://your-backend/api/documentos/123?dominioSubdominio=clinicabc.com",
  {
    method: "GET",
    headers,
  }
);
```

## Next.js Implementation Examples

### Using dominioSubdominio from URL params

```typescript
// In your Next.js pages: /app/[dominioSubdominio]/usuarios/page.tsx

"use client";

import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function UsuariosPage() {
  const params = useParams();
  const dominioSubdominio = params.dominioSubdominio as string;
  const [clinicInfo, setClinicInfo] = useState(null);
  const [usuarios, setUsuarios] = useState([]);

  useEffect(() => {
    // First get clinic info for display
    const fetchClinicInfo = async () => {
      const response = await fetch(
        `http://your-backend/api/clinicas/dominio/${dominioSubdominio}/info`
      );
      const data = await response.json();
      setClinicInfo(data);
    };

    const fetchUsuarios = async () => {
      const response = await fetch(
        `http://your-backend/api/usuarios?dominioSubdominio=${dominioSubdominio}`
      );
      const data = await response.json();
      setUsuarios(data);
    };

    fetchClinicInfo();
    fetchUsuarios();
  }, [dominioSubdominio]);

  const createUsuario = async (usuarioData: any) => {
    const payload = {
      dominioSubdominio,
      ...usuarioData,
    };

    const response = await fetch("http://your-backend/api/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      // Refresh the list
      const updatedResponse = await fetch(
        `http://your-backend/api/usuarios?dominioSubdominio=${dominioSubdominio}`
      );
      const updatedData = await updatedResponse.json();
      setUsuarios(updatedData);
    }
  };

  return (
    <div>
      <h1>Usuarios de {clinicInfo?.nombre || dominioSubdominio}</h1>
      {/* Your UI components */}
    </div>
  );
}
```

### Custom Hook for API Calls

```typescript
// hooks/useApi.ts

import { useParams } from "next/navigation";

export function useApi() {
  const params = useParams();
  const dominioSubdominio = params.dominioSubdominio as string;

  const apiCall = async (endpoint: string, options: RequestInit = {}) => {
    const url = endpoint.includes("?")
      ? `${endpoint}&dominioSubdominio=${dominioSubdominio}`
      : `${endpoint}?dominioSubdominio=${dominioSubdominio}`;

    return fetch(`http://your-backend/api${url}`, {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    });
  };

  const apiPost = async (endpoint: string, data: any) => {
    return apiCall(endpoint, {
      method: "POST",
      body: JSON.stringify({ dominioSubdominio, ...data }),
    });
  };

  return { apiCall, apiPost };
}
```

### Usage in Components

```typescript
// components/UsuarioForm.tsx

import { useApi } from "../hooks/useApi";

export function UsuarioForm() {
  const { apiPost } = useApi();

  const handleSubmit = async (formData: any) => {
    try {
      const response = await apiPost("/usuarios", {
        nombres: formData.nombres,
        apellidos: formData.apellidos,
        // ... other fields
      });

      if (response.ok) {
        // Success handling
      }
    } catch (error) {
      // Error handling
    }
  };

  return {
    /* Your form JSX */
  };
}
```

## Error Handling

Always check for these common error scenarios:

```typescript
try {
  const response = await fetch(url, options);

  if (!response.ok) {
    if (response.status === 400) {
      // Validation error - check response body for details
      const errorData = await response.json();
      console.error("Validation error:", errorData);
    } else if (response.status === 404) {
      // Clinic not found or resource not found
      console.error("Resource not found");
    } else {
      // Other server errors
      console.error("Server error:", response.status);
    }
    return;
  }

  const data = await response.json();
  // Handle success
} catch (error) {
  // Network errors
  console.error("Network error:", error);
}
```

## Important Notes

1. **dominioSubdominio is required** for all tenant-scoped endpoints (everything except `/api/clinicas`)
2. **dominioSubdominio maps to Clinica.dominioSubdominio** - this field must be unique across all clinics
3. **All POST requests** now wrap the main payload with dominioSubdominio
4. **GET requests** use query parameters for dominioSubdominio
5. **Dates should be in ISO format** (e.g., "2024-01-15" or "2024-01-15T10:00:00")
6. **Entity IDs are required** when referencing related entities (like MotivoConsulta, GradoCerteza, etc.)
7. **URLs are now clean** - use domain names instead of UUIDs: `periferico.vercel.app/centrovida.enbodi.xyz/...`

## Testing Your Integration

Use tools like Postman or curl to test endpoints before implementing in Next.js:

```bash
# Test creating a user
curl -X POST http://your-backend/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "dominioSubdominio": "clinicacentral.com",
    "nombres": "Test User",
    "apellidos": "Test",
    "email": "test@example.com"
  }'

# Test listing users
curl "http://your-backend/api/usuarios?dominioSubdominio=clinicacentral.com"
```
