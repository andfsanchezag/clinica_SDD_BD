-- ============================================================
-- CLINICA IPS - Orquestador de migracion completa
-- Motor: MySQL 8.0+
-- Uso desde CLI (ejecutar desde el directorio clinic_bd/):
--
--   mysql -u root -p --delimiter=";" < migrate.sql
--
-- O interactivo:
--   mysql -u root -p
--   SOURCE /ruta/absoluta/clinic_bd/migrate.sql
--
-- IMPORTANTE: ejecutar posicionado en la carpeta clinic_bd/
--   mysql -u root -p --init-command="SET @migration_start=NOW()"
-- ============================================================

-- Configuracion de sesion para migracion segura
SET SESSION sql_mode           = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SET SESSION time_zone          = '-05:00';  -- UTC-5 Colombia
SET SESSION character_set_client     = utf8mb4;
SET SESSION character_set_connection = utf8mb4;
SET SESSION character_set_results    = utf8mb4;
SET @migration_start = NOW();

-- ============================================================
-- PASO 0 — Schema y base de datos
-- NOTA: 00_schema.sql hace DROP DATABASE + CREATE DATABASE,
--       por lo que migration_log se crea DESPUES de este paso.
-- ============================================================
SELECT '>>> [1/10] Ejecutando: 00_schema.sql — Schema de base de datos' AS progreso;

SOURCE 00_schema.sql;

-- La BD ya existe gracias a 00_schema.sql; ahora creamos migration_log
CREATE TABLE IF NOT EXISTS migration_log (
    migration_id   INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    archivo        VARCHAR(100)     NOT NULL,
    descripcion    VARCHAR(200)     NOT NULL,
    ejecutado_en   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado         VARCHAR(20)      NOT NULL DEFAULT 'pendiente',
    PRIMARY KEY (migration_id),
    UNIQUE KEY uq_mlog_archivo (archivo)
) ENGINE = InnoDB COMMENT = 'Historial de ejecucion de scripts de migracion';

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('00_schema.sql', 'Creacion de base de datos clinica_ips con charset utf8mb4', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 00_schema.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 1 — Catalogos y configuracion
-- ============================================================
SELECT '>>> [2/10] Ejecutando: 01_catalogos.sql — Tablas catalogo y configuracion' AS progreso;

SOURCE 01_catalogos.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('01_catalogos.sql', 'Tablas catalogo: roles, documentos, genero, estados, config_facturacion', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 01_catalogos.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 2 — Tablas maestras
-- ============================================================
SELECT '>>> [3/10] Ejecutando: 02_tablas_maestras.sql — Empleado, Paciente, Inventario' AS progreso;

SOURCE 02_tablas_maestras.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('02_tablas_maestras.sql', 'Tablas maestras: empleado, paciente, especialidad, catalogos de inventario', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 02_tablas_maestras.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 3 — Tablas transaccionales
-- ============================================================
SELECT '>>> [4/10] Ejecutando: 03_tablas_transaccionales.sql — Cita, Ordenes, Facturacion' AS progreso;

SOURCE 03_tablas_transaccionales.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('03_tablas_transaccionales.sql', 'Tablas transaccionales: cita, encuentro_clinico, ordenes, factura, pago', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 03_tablas_transaccionales.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 4 — Triggers de validacion de dominio
-- ============================================================
SELECT '>>> [5/10] Ejecutando: 04_triggers.sql — Triggers de invariantes del dominio' AS progreso;

SOURCE 04_triggers.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('04_triggers.sql', 'Triggers: validacion edad, solapamiento citas, mezcla ordenes, tope copago', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 04_triggers.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 5 — Procedimientos almacenados
-- ============================================================
SELECT '>>> [6/10] Ejecutando: 05_procedimientos.sql — Casos de uso del dominio' AS progreso;

SOURCE 05_procedimientos.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('05_procedimientos.sql', 'SPs: paciente, empleado, cita, encuentro, enfermeria, ordenes, facturacion', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 05_procedimientos.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 6 — Vistas de consulta
-- ============================================================
SELECT '>>> [7/10] Ejecutando: 06_vistas.sql — Vistas de reportes y consulta' AS progreso;

SOURCE 06_vistas.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('06_vistas.sql', 'Vistas: paciente_resumen, factura_resumen, orden_resumen, historia_clinica, inventario', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 06_vistas.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 7 — Auditoria
-- ============================================================
SELECT '>>> [8/10] Ejecutando: 07_auditoria.sql — Tablas y triggers de auditoria' AS progreso;

SOURCE 07_auditoria.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('07_auditoria.sql', 'Auditoria: audit_log, cambios, accesos, errores de proceso + triggers after', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 07_auditoria.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 8 — Seguridad: roles y usuarios
-- ============================================================
SELECT '>>> [9/10] Ejecutando: 08_seguridad.sql — Roles MySQL 8+ y cuentas de servicio' AS progreso;

SOURCE 08_seguridad.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('08_seguridad.sql', 'Seguridad: 7 roles de dominio, 6 usuarios de servicio con minimo privilegio', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 08_seguridad.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- PASO 9 — Datos semilla
-- ============================================================
SELECT '>>> [10/10] Ejecutando: 09_datos_semilla.sql — Datos de prueba' AS progreso;

SOURCE 09_datos_semilla.sql;

INSERT INTO migration_log (archivo, descripcion, estado)
VALUES ('09_datos_semilla.sql', 'Semilla: 2 pacientes, 1 medico, 1 enfermero, inventario, encuentro y factura de ejemplo', 'completado')
ON DUPLICATE KEY UPDATE ejecutado_en = NOW(), estado = 'completado';

SELECT CONCAT('[OK] 09_datos_semilla.sql completado a las ', NOW()) AS resultado;

-- ============================================================
-- RESUMEN FINAL DE LA MIGRACION
-- ============================================================
SELECT
    '=============================================' AS separador
UNION ALL
SELECT '  MIGRACION CLINICA IPS COMPLETADA'
UNION ALL
SELECT '=============================================';

SELECT
    archivo,
    descripcion,
    estado,
    ejecutado_en
FROM migration_log
ORDER BY migration_id;

SELECT
    COUNT(*)                                           AS total_scripts,
    SUM(estado = 'completado')                         AS completados,
    SUM(estado = 'pendiente')                          AS pendientes,
    TIMESTAMPDIFF(SECOND, @migration_start, NOW())     AS duracion_segundos
FROM migration_log;
