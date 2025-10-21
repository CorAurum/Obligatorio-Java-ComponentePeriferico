-- ============================================
-- TABLA: Clínicas
-- ============================================
CREATE TABLE IF NOT EXISTS clinicas (
    id SERIAL PRIMARY KEY,                         -- Coincide con Clinica.id
    id_clinica VARCHAR(50),
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(50),
    tipo_institucion VARCHAR(50)
);

-- ============================================
-- TABLA: Usuarios de Salud
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios_salud (
    cedula_identidad VARCHAR(20) PRIMARY KEY,      -- Coincide con UsuarioDeSalud.cedulaIdentidad
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    fecha_nacimiento DATE,
    email VARCHAR(100),
    telefono VARCHAR(50),
    fecha_registro DATE
);

-- ============================================
-- TABLA: Profesionales de Salud
-- ============================================
CREATE TABLE IF NOT EXISTS profesionales_salud (
    id_profesional SERIAL PRIMARY KEY,             -- Coincide con ProfesionalDeSalud.idProfesional
    cedula_identidad VARCHAR(20),
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    numero_registro VARCHAR(50),
    email VARCHAR(100),
    telefono VARCHAR(50)
);

-- ============================================
-- TABLA: Motivos
-- ============================================
CREATE TABLE IF NOT EXISTS motivos (
    id BIGINT PRIMARY KEY,        -- Coincide con Motivos.id
    motivo VARCHAR(255) NOT NULL
);

-- ============================================
-- TABLA: Grados de Certeza
-- ============================================
CREATE TABLE IF NOT EXISTS gradosCerteza (
    id BIGINT PRIMARY KEY,              -- Coincide con GradosCerteza.id
    gradoCerteza VARCHAR(100) NOT NULL
);

-- ============================================
-- TABLA: Estados de Problemas
-- ============================================
CREATE TABLE IF NOT EXISTS estadosProblemas (
    id BIGINT PRIMARY KEY,                 -- Coincide con EstadosProblemas.id
    estadoProblema VARCHAR(150) NOT NULL
);

-- ============================================
-- TABLA: Documentos Clínicos
-- ============================================
CREATE TABLE IF NOT EXISTS documentos_clinicos (
    id_documento SERIAL PRIMARY KEY,               -- Coincide con DocumentoClinico.idDocumento
    titulo VARCHAR(200),
    tipo_documento VARCHAR(50),
    fecha_creacion TIMESTAMP,
    contenido TEXT,
    estado VARCHAR(20),

    id_clinica INT NOT NULL REFERENCES clinicas(id),                 -- Coincide con Clinica.id
    cedula_usuario VARCHAR(20) NOT NULL REFERENCES usuarios_salud(cedula_identidad),
    id_profesional INT NOT NULL REFERENCES profesionales_salud(id_profesional)
);

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Clínica
INSERT INTO clinicas (id_clinica, nombre, direccion, telefono, tipo_institucion)
VALUES ('CL-001', 'Clinica Central', 'Av. Siempre Viva 123', '2900 1234', 'Privada')
ON CONFLICT DO NOTHING;

-- Usuario
INSERT INTO usuarios_salud (cedula_identidad, nombre, apellido, fecha_nacimiento, email, telefono, fecha_registro)
VALUES ('12345678', 'Juan', 'Perez', '1990-05-10', 'juan@correo.com', '099111111', CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- Profesional
INSERT INTO profesionales_salud (cedula_identidad, nombre, apellido, numero_registro, email, telefono)
VALUES ('87654321', 'Maria', 'Gomez', 'REG123', 'maria@correo.com', '099222222')
ON CONFLICT DO NOTHING;

-- gradosCerteza
INSERT INTO gradosCerteza (id, gradoCerteza) VALUES
(64957009, 'presuntivo'),
(255545003, 'confirmado');

-- estadosProblemas
INSERT INTO estadosProblemas (id, estadoProblema) VALUES
(7441000179105, 'problema resuelto'),
(7451000179108, 'problema no resuelto');

-- motivos en "insert_motivos.sql"
