-- ============================================================
-- CLINICA IPS - Schema principal
-- Agregado: base de datos y configuracion del motor
-- Orden de ejecucion: 1 de 10
-- Motor requerido: MySQL 8.0+
-- ============================================================

DROP DATABASE IF EXISTS clinica_ips;

CREATE DATABASE clinica_ips
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE clinica_ips;

-- Refuerzo de integridad referencial en la sesion
SET FOREIGN_KEY_CHECKS = 1;
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
