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
-- DATOS DE PRUEBA
-- ============================================

-- gradosCerteza
INSERT INTO gradosCerteza (id, gradoCerteza) VALUES
(64957009, 'presuntivo'),
(255545003, 'confirmado');

-- estadosProblemas
INSERT INTO estadosProblemas (id, estadoProblema) VALUES
(7441000179105, 'problema resuelto'),
(7451000179108, 'problema no resuelto');

-- motivos en "insert_motivos.sql"
