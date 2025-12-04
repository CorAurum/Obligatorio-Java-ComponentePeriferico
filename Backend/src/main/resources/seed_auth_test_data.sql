-- ============================================
-- SEED DATA FOR AUTHENTICATION TESTING
-- ============================================
-- This file creates test data for authentication flow testing
-- 
-- RECOMMENDED: Use the Java-based TestDataSeeder instead!
-- The TestDataSeeder automatically runs on startup and properly hashes passwords.
-- To use it, just start the application (it runs automatically in dev/test profiles).
--
-- If you need to use this SQL file instead:
--   1. Run migration scripts first:
--      - migration_add_auth_fields.sql
--      - migration_add_auth_fields_profesional.sql
--   2. Generate BCrypt hashes for passwords using PasswordHashGenerator utility
--   3. Update the password hashes below before running this script
--
-- Test Credentials:
--   Admin: cedula = "12345678", password = "AdminPass123!"
--   Professional: cedula = "87654321", password = "ProfPass123!"
-- ============================================

-- Clean up existing test data (optional - comment out if you want to keep existing data)
-- DELETE FROM profesional_especialidad WHERE profesional_id IN (SELECT id_profesional FROM profesional_de_salud WHERE cedula_identidad = '87654321');
-- DELETE FROM profesional_de_salud WHERE cedula_identidad = '87654321';
-- DELETE FROM administrador WHERE cedula = '12345678';
-- DELETE FROM clinica WHERE dominio_subdominio = 'suat';

-- ============================================
-- 1. CREATE TEST CLINIC
-- ============================================
INSERT INTO clinica (id, nombre, direccion, telefono, dominio_subdominio, fecha_alta, fecha_baja, tipo_institucion)
VALUES (
    'test-clinic-auth-001',
    'suat',
    'Av. Test 123',
    '+59899123456',
    'suat',
    NOW(),
    NULL,
    'Privada'
) ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 2. CREATE TEST ESPECIALIDAD (needed for professional)
-- ============================================
INSERT INTO especialidad (id, nombre, descripcion)
VALUES (
    'esp-test-001',
    'Medicina General',
    'Especialidad de medicina general para testing'
) ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 3. CREATE TEST ADMINISTRATOR
-- ============================================
-- Password: "AdminPass123!" (BCrypt hash with 12 rounds)
-- Cedula: 12345678
INSERT INTO administrador (
    nombre,
    apellido,
    cedula,
    email,
    usuario,
    creador_por,
    activo,
    password,
    role,
    clinica_id
)
VALUES (
    'Juan',
    'Administrador',
    '12345678',
    'admin@testclinic.com',
    'jadmin',
    'system',
    true,
    '$2a$12$X8K5vJ3mN9Q2wL4pR7tY.eFgH6iJ8kL2mN4pQ6rS8tU0vW2xY4zA6bC8dE0fG2hI',
    'ADMINISTRADOR',
    'test-clinic-auth-001'
) ON CONFLICT (cedula) DO UPDATE SET
    password = EXCLUDED.password,
    role = EXCLUDED.role,
    activo = EXCLUDED.activo;

-- ============================================
-- 4. CREATE TEST PROFESSIONAL
-- ============================================
-- Password: "ProfPass123!" (BCrypt hash with 12 rounds)
-- Cedula: 87654321
INSERT INTO profesional_de_salud (
    id_profesional,
    cedula_identidad,
    nombre,
    apellido,
    email,
    telefono,
    activo,
    password,
    role,
    clinica_id
)
VALUES (
    'prof-test-001',
    '87654321',
    'Dra. María',
    'Profesional',
    'maria.profesional@testclinic.com',
    '+59898765432',
    true,
    '$2a$12$Y9L6wK4nO0R3xM5qS8uZ.fGhI7jK9lM3nO5qR7sT9uV1wX3yZ5aB7cD9eF1gH3jK',
    'PROFESIONAL',
    'test-clinic-auth-001'
) ON CONFLICT (id_profesional) DO UPDATE SET
    password = EXCLUDED.password,
    role = EXCLUDED.role,
    activo = EXCLUDED.activo;

-- ============================================
-- 5. LINK PROFESSIONAL TO ESPECIALIDAD
-- ============================================
INSERT INTO profesional_especialidad (profesional_id, especialidad_id)
VALUES (
    'prof-test-001',
    'esp-test-001'
) ON CONFLICT DO NOTHING;

-- ============================================
-- VERIFICATION QUERIES (run these to verify data)
-- ============================================
-- SELECT * FROM clinica WHERE dominio_subdominio = 'suat';
-- SELECT id, nombre, apellido, cedula, email, role, activo FROM administrador WHERE cedula = '12345678';
-- SELECT id_profesional, nombre, apellido, cedula_identidad, email, role, activo FROM profesional_de_salud WHERE cedula_identidad = '87654321';

