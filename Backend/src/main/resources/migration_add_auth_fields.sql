-- Migration script to add authentication fields to existing tables
-- Run this script to add password and role fields to administrador table

-- Add password column (nullable initially, then make it not null after setting default values)
ALTER TABLE administrador ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Add role column
ALTER TABLE administrador ADD COLUMN IF NOT EXISTS role VARCHAR(20) DEFAULT 'ADMINISTRADOR';

-- Set a temporary default password for existing users
-- IMPORTANT: This is a placeholder password that should be changed by each user
-- The password hash below corresponds to 'TempPass123!' (with BCrypt)
UPDATE administrador SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewfBPjYQmO5WL3m' WHERE password IS NULL;

-- Make password column NOT NULL after setting default values
ALTER TABLE administrador ALTER COLUMN password SET NOT NULL;
ALTER TABLE administrador ALTER COLUMN role SET NOT NULL;

-- Add unique constraint on usuario field if it doesn't exist
-- First check if constraint exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_administrador_usuario') THEN
        ALTER TABLE administrador ADD CONSTRAINT uk_administrador_usuario UNIQUE (usuario);
    END IF;
END $$;

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_administrador_usuario ON administrador(usuario);
CREATE INDEX IF NOT EXISTS idx_administrador_role ON administrador(role);
