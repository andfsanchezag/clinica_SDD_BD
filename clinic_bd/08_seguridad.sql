-- ============================================================
-- CLINICA IPS - Seguridad: Roles y permisos MySQL 8+
-- Principio: minimo privilegio por rol de dominio
-- IMPORTANTE: ejecutar con usuario con GRANT OPTION
--             Ajustar 'localhost' al host de aplicacion real
-- Orden de ejecucion: 9 de 10
-- Dependencias: 07_auditoria.sql
-- ============================================================

USE clinica_ips;

-- ============================================================
-- PASO 1: Crear roles de dominio
-- ============================================================

CREATE ROLE IF NOT EXISTS 'rol_recursos_humanos';
CREATE ROLE IF NOT EXISTS 'rol_administrativo';
CREATE ROLE IF NOT EXISTS 'rol_enfermeria';
CREATE ROLE IF NOT EXISTS 'rol_medico';
CREATE ROLE IF NOT EXISTS 'rol_soporte_informacion';
CREATE ROLE IF NOT EXISTS 'rol_auditoria';
CREATE ROLE IF NOT EXISTS 'rol_admin_bd';

-- ============================================================
-- PASO 2: Privilegios por rol
-- Principio: GRANT solo lo que cada contexto necesita
-- ============================================================

-- ------------------------------------------------------------
-- rol_recursos_humanos
-- Gestion de personal: empleados, usuarios, perfiles
-- PROHIBIDO: acceso a datos clinicos y catalogo de items
-- ------------------------------------------------------------
GRANT SELECT, INSERT, UPDATE ON clinica_ips.empleado            TO 'rol_recursos_humanos';
GRANT SELECT, INSERT         ON clinica_ips.seguridad_usuario   TO 'rol_recursos_humanos';
GRANT SELECT, INSERT         ON clinica_ips.medico_perfil       TO 'rol_recursos_humanos';
GRANT SELECT, INSERT         ON clinica_ips.enfermero_perfil    TO 'rol_recursos_humanos';
GRANT SELECT                 ON clinica_ips.especialidad        TO 'rol_recursos_humanos';
GRANT SELECT                 ON clinica_ips.cat_rol_usuario     TO 'rol_recursos_humanos';
GRANT SELECT                 ON clinica_ips.cat_tipo_documento  TO 'rol_recursos_humanos';
GRANT SELECT                 ON clinica_ips.cat_genero          TO 'rol_recursos_humanos';

-- Procedimientos permitidos para RRHH
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_empleado      TO 'rol_recursos_humanos';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_crear_usuario_operativo  TO 'rol_recursos_humanos';

-- DENEGACIONES EXPLICITAS: acceso nulo a tablas clinicas y de catalogo
-- (En MySQL 8+ la ausencia de GRANT equivale a DENY)
-- Listadas documentalmente para auditoria de diseno:
-- paciente, seguro_medico, contacto_emergencia,
-- medicamento_catalogo, procedimiento_catalogo, ayuda_diagnostica_catalogo,
-- orden_medica, orden_*_detalle, factura, factura_detalle, pago,
-- encuentro_clinico, signo_vital, administracion_medicamento, procedimiento_enfermeria

-- ------------------------------------------------------------
-- rol_administrativo
-- Gestion de pacientes, seguros, citas y facturacion
-- ------------------------------------------------------------
GRANT SELECT, INSERT, UPDATE ON clinica_ips.paciente              TO 'rol_administrativo';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.seguro_medico         TO 'rol_administrativo';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.contacto_emergencia   TO 'rol_administrativo';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.cita                  TO 'rol_administrativo';
GRANT SELECT, INSERT         ON clinica_ips.factura               TO 'rol_administrativo';
GRANT SELECT, INSERT         ON clinica_ips.factura_detalle       TO 'rol_administrativo';
GRANT SELECT, INSERT         ON clinica_ips.pago                  TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_tipo_documento    TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_genero            TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_estado_cita       TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_prioridad_atencion TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_estado_seguro     TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.cat_tipo_facturacion  TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.config_facturacion    TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.medico_perfil         TO 'rol_administrativo';
GRANT SELECT                 ON clinica_ips.empleado              TO 'rol_administrativo';

GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_paciente              TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_contacto_emergencia   TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_seguro_medico         TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_programar_cita                  TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_reprogramar_cita                TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_cancelar_cita                   TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_emitir_factura                  TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_calcular_copago                 TO 'rol_administrativo';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_pago                  TO 'rol_administrativo';

-- Acceso a vistas de gestion
GRANT SELECT ON clinica_ips.vw_paciente_resumen     TO 'rol_administrativo';
GRANT SELECT ON clinica_ips.vw_factura_resumen      TO 'rol_administrativo';

-- ------------------------------------------------------------
-- rol_enfermeria
-- Registrar signos vitales, medicamentos administrados,
-- procedimientos de enfermeria
-- ------------------------------------------------------------
GRANT SELECT                 ON clinica_ips.paciente                     TO 'rol_enfermeria';
GRANT SELECT                 ON clinica_ips.encuentro_clinico            TO 'rol_enfermeria';
GRANT SELECT, INSERT         ON clinica_ips.signo_vital                  TO 'rol_enfermeria';
GRANT SELECT, INSERT         ON clinica_ips.administracion_medicamento   TO 'rol_enfermeria';
GRANT SELECT, INSERT         ON clinica_ips.procedimiento_enfermeria     TO 'rol_enfermeria';
GRANT SELECT                 ON clinica_ips.medicamento_catalogo         TO 'rol_enfermeria';
GRANT SELECT                 ON clinica_ips.procedimiento_catalogo       TO 'rol_enfermeria';
GRANT SELECT                 ON clinica_ips.orden_medica                 TO 'rol_enfermeria';
GRANT SELECT                 ON clinica_ips.orden_medicamento_detalle    TO 'rol_enfermeria';

GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_signos_vitales               TO 'rol_enfermeria';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_administracion_medicamento   TO 'rol_enfermeria';

-- ------------------------------------------------------------
-- rol_medico
-- Acceso completo a historia clinica, ordenes y citas propias
-- ------------------------------------------------------------
GRANT SELECT                 ON clinica_ips.paciente                         TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.seguro_medico                    TO 'rol_medico';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.encuentro_clinico                TO 'rol_medico';
GRANT SELECT, INSERT         ON clinica_ips.orden_medica                     TO 'rol_medico';
GRANT SELECT, INSERT         ON clinica_ips.orden_medicamento_detalle        TO 'rol_medico';
GRANT SELECT, INSERT         ON clinica_ips.orden_procedimiento_detalle      TO 'rol_medico';
GRANT SELECT, INSERT         ON clinica_ips.orden_ayuda_diagnostica_detalle  TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.signo_vital                      TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.administracion_medicamento       TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.procedimiento_enfermeria         TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.cita                             TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.medicamento_catalogo             TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.procedimiento_catalogo           TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.ayuda_diagnostica_catalogo       TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.especialidad                     TO 'rol_medico';
GRANT SELECT                 ON clinica_ips.cat_tipo_orden                   TO 'rol_medico';

GRANT EXECUTE ON PROCEDURE clinica_ips.sp_abrir_encuentro_clinico    TO 'rol_medico';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_cerrar_encuentro_clinico   TO 'rol_medico';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_registrar_orden_medica     TO 'rol_medico';
GRANT EXECUTE ON PROCEDURE clinica_ips.sp_agregar_detalle_orden      TO 'rol_medico';

GRANT SELECT ON clinica_ips.vw_historia_clinica_por_paciente  TO 'rol_medico';
GRANT SELECT ON clinica_ips.vw_orden_resumen                  TO 'rol_medico';
GRANT SELECT ON clinica_ips.vw_inventario_operativo           TO 'rol_medico';

-- ------------------------------------------------------------
-- rol_soporte_informacion
-- Mantenimiento del catalogo de items y especialidades
-- ------------------------------------------------------------
GRANT SELECT, INSERT, UPDATE ON clinica_ips.especialidad                  TO 'rol_soporte_informacion';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.medicamento_catalogo          TO 'rol_soporte_informacion';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.procedimiento_catalogo        TO 'rol_soporte_informacion';
GRANT SELECT, INSERT, UPDATE ON clinica_ips.ayuda_diagnostica_catalogo    TO 'rol_soporte_informacion';
GRANT SELECT, UPDATE         ON clinica_ips.config_facturacion            TO 'rol_soporte_informacion';
GRANT SELECT                 ON clinica_ips.cat_estado_especialidad       TO 'rol_soporte_informacion';

GRANT SELECT ON clinica_ips.vw_inventario_operativo TO 'rol_soporte_informacion';

-- ------------------------------------------------------------
-- rol_auditoria
-- Solo lectura en todas las tablas y vistas
-- Acceso completo a tablas de auditoria
-- ------------------------------------------------------------
GRANT SELECT ON clinica_ips.audit_log              TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_cambio_empleado  TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_cambio_paciente  TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_cambio_orden     TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_cambio_factura   TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_acceso           TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.audit_error_proceso    TO 'rol_auditoria';

-- Lectura de tablas operativas para cruzar con auditoria
GRANT SELECT ON clinica_ips.empleado               TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.paciente               TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.factura                TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.pago                   TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.orden_medica           TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.seguridad_usuario      TO 'rol_auditoria';

GRANT SELECT ON clinica_ips.vw_paciente_resumen          TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.vw_factura_resumen           TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.vw_orden_resumen             TO 'rol_auditoria';
GRANT SELECT ON clinica_ips.vw_historia_clinica_por_paciente TO 'rol_auditoria';

-- ------------------------------------------------------------
-- rol_admin_bd
-- Acceso total para DBA: uso exclusivo de administracion
-- ------------------------------------------------------------
GRANT ALL PRIVILEGES ON clinica_ips.* TO 'rol_admin_bd';

-- ============================================================
-- PASO 3: Usuarios de aplicacion (cuentas de servicio)
-- IMPORTANTE: cambiar contrasenas en produccion
-- ============================================================

-- Cuenta de servicio para recursos humanos
CREATE USER IF NOT EXISTS 'srv_rrhh'@'localhost' IDENTIFIED BY 'Rrhh$Srv2025!';
GRANT 'rol_recursos_humanos' TO 'srv_rrhh'@'localhost';
SET DEFAULT ROLE 'rol_recursos_humanos' FOR 'srv_rrhh'@'localhost';

-- Cuenta de servicio para administrativo
CREATE USER IF NOT EXISTS 'srv_admin'@'localhost' IDENTIFIED BY 'Admin$Srv2025!';
GRANT 'rol_administrativo' TO 'srv_admin'@'localhost';
SET DEFAULT ROLE 'rol_administrativo' FOR 'srv_admin'@'localhost';

-- Cuenta de servicio para enfermeria
CREATE USER IF NOT EXISTS 'srv_enf'@'localhost' IDENTIFIED BY 'Enf$Srv2025!';
GRANT 'rol_enfermeria' TO 'srv_enf'@'localhost';
SET DEFAULT ROLE 'rol_enfermeria' FOR 'srv_enf'@'localhost';

-- Cuenta de servicio para medicos
CREATE USER IF NOT EXISTS 'srv_medico'@'localhost' IDENTIFIED BY 'Med$Srv2025!';
GRANT 'rol_medico' TO 'srv_medico'@'localhost';
SET DEFAULT ROLE 'rol_medico' FOR 'srv_medico'@'localhost';

-- Cuenta de servicio para soporte de informacion
CREATE USER IF NOT EXISTS 'srv_soporte'@'localhost' IDENTIFIED BY 'Sop$Srv2025!';
GRANT 'rol_soporte_informacion' TO 'srv_soporte'@'localhost';
SET DEFAULT ROLE 'rol_soporte_informacion' FOR 'srv_soporte'@'localhost';

-- Cuenta de servicio para auditoria (solo lectura)
CREATE USER IF NOT EXISTS 'srv_auditoria'@'localhost' IDENTIFIED BY 'Audit$Srv2025!';
GRANT 'rol_auditoria' TO 'srv_auditoria'@'localhost';
SET DEFAULT ROLE 'rol_auditoria' FOR 'srv_auditoria'@'localhost';

FLUSH PRIVILEGES;
