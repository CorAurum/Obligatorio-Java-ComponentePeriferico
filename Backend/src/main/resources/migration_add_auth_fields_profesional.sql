-- Migration script to add authentication fields to profesional_de_salud table
-- Run this script to add password, role, and activo fields to profesional_de_salud table

-- Add password column (nullable initially, then make it not null after setting default values)
ALTER TABLE profesional_de_salud ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Add role column
ALTER TABLE profesional_de_salud ADD COLUMN IF NOT EXISTS role VARCHAR(20) DEFAULT 'PROFESIONAL';

-- Add activo column for account status
ALTER TABLE profesional_de_salud ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;

-- Set a temporary default password for existing users
-- IMPORTANT: This is a placeholder password that should be changed by each user
-- The password hash below corresponds to 'TempPass123!' (with BCrypt)
UPDATE profesional_de_salud SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewfBPjYQmO5WL3m' WHERE password IS NULL;

-- Make password column NOT NULL after setting default values
ALTER TABLE profesional_de_salud ALTER COLUMN password SET NOT NULL;
ALTER TABLE profesional_de_salud ALTER COLUMN role SET NOT NULL;
ALTER TABLE profesional_de_salud ALTER COLUMN activo SET NOT NULL;

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_profesional_cedula ON profesional_de_salud(cedula_identidad);
CREATE INDEX IF NOT EXISTS idx_profesional_role ON profesional_de_salud(role);

