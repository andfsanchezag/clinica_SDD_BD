-- ============================================================
-- CLINICA IPS - Migración: Autenticación JWT
-- Descripción:
--   1. Agrega empleado con rol soporte_informacion (rol_id=3)
--   2. Actualiza codigo_usuario de todos los empleados existentes
--      usando el código de su rol (ej: "medico", "enfermeria")
--   3. Actualiza todas las contraseñas a BCrypt de "123456"
--   4. Crea usuario operativo para el nuevo empleado de soporte
-- BCrypt de "123456" (rounds=10):
--   $2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C
-- ============================================================

USE clinica_ips;

-- ============================================================
-- 1. Nuevo empleado: rol soporte_informacion (rol_id = 3)
-- ============================================================
INSERT INTO empleado
    (cedula, tipo_doc_id, nombre_completo, correo, telefono,
     fecha_nacimiento, direccion, rol_id, active_flag, created_by)
VALUES
    ('1010200104', 1, 'Andres Felipe Vargas', 'avargas@clinicaips.co',
     '3140001234', '1988-04-10', 'Cll 80 # 10-20', 3, 1, 1);
-- empleado_id resultante = 5 (siguiente en AUTO_INCREMENT)

-- ============================================================
-- 2. Actualizar codigo_usuario a código de rol y contraseña BCrypt
--    Empleado 1: Carlos Rios    → rol medico           (rol_id=5)
--    Empleado 2: Laura Gomez    → rol enfermeria        (rol_id=4)
--    Empleado 3: Jorge Hernandez→ rol administrativo   (rol_id=2)
--    Empleado 4: Maria Bedoya   → rol recursos_humanos (rol_id=1)
-- ============================================================
UPDATE seguridad_usuario
SET    codigo_usuario   = 'medico',
       contrasena_hash  = '$2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C'
WHERE  empleado_id = 1;

UPDATE seguridad_usuario
SET    codigo_usuario   = 'enfermeria',
       contrasena_hash  = '$2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C'
WHERE  empleado_id = 2;

UPDATE seguridad_usuario
SET    codigo_usuario   = 'administrativo',
       contrasena_hash  = '$2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C'
WHERE  empleado_id = 3;

UPDATE seguridad_usuario
SET    codigo_usuario   = 'recursos_humanos',
       contrasena_hash  = '$2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C'
WHERE  empleado_id = 4;

-- ============================================================
-- 3. Crear usuario operativo para el empleado de soporte
-- ============================================================
INSERT INTO seguridad_usuario
    (empleado_id, codigo_usuario, contrasena_hash, activo, created_by)
VALUES
    (5, 'soporte_informacion',
     '$2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C',
     1, 1);

-- ============================================================
-- Resumen de usuarios disponibles después de la migración:
--   usuario             contraseña   rol
--   ------------------  -----------  --------------------
--   medico              123456       medico
--   enfermeria          123456       enfermeria
--   administrativo      123456       administrativo
--   recursos_humanos    123456       recursos_humanos
--   soporte_informacion 123456       soporte_informacion
-- ============================================================
