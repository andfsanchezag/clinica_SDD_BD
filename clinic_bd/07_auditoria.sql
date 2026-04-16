-- ============================================================
-- CLINICA IPS - Auditoria de cambios y accesos
-- Principio: inmutabilidad de registros de auditoria (INSERT-only)
-- Contexto: Identidad y Acceso / Auditoria
-- Orden de ejecucion: 8 de 10
-- Dependencias: 06_vistas.sql
-- ============================================================

USE clinica_ips;

-- ============================================================
-- TABLAS DE AUDITORIA
-- ============================================================

-- ------------------------------------------------------------
-- audit_log
-- Registro generico de operaciones: quien, que, cuando
-- ------------------------------------------------------------
CREATE TABLE audit_log (
    log_id        BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    tabla         VARCHAR(60)      NOT NULL,
    operacion     VARCHAR(10)      NOT NULL,   -- INSERT, UPDATE, DELETE
    registro_id   INT UNSIGNED     NOT NULL,
    usuario_id    INT UNSIGNED     NULL,
    descripcion   TEXT             NULL,
    ip_origen     VARCHAR(45)      NULL,
    ejecutado_en  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (log_id),
    INDEX idx_alog_tabla_reg (tabla, registro_id),
    INDEX idx_alog_fecha     (ejecutado_en),
    CONSTRAINT chk_alog_op CHECK (operacion IN ('INSERT','UPDATE','DELETE'))
) ENGINE = InnoDB
  COMMENT = 'Registro generico de operaciones de auditoria';

-- ------------------------------------------------------------
-- audit_cambio_empleado
-- Historial de cambios en el agregado Empleado
-- ------------------------------------------------------------
CREATE TABLE audit_cambio_empleado (
    auditoria_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    empleado_id     INT UNSIGNED    NOT NULL,
    campo_modificado VARCHAR(60)    NOT NULL,
    valor_anterior  TEXT            NULL,
    valor_nuevo     TEXT            NULL,
    usuario_id      INT UNSIGNED    NULL,
    modificado_en   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditoria_id),
    INDEX idx_ace_empleado (empleado_id)
) ENGINE = InnoDB
  COMMENT = 'Historial de modificaciones a datos de empleados';

-- ------------------------------------------------------------
-- audit_cambio_paciente
-- Historial de cambios en el agregado Paciente
-- ------------------------------------------------------------
CREATE TABLE audit_cambio_paciente (
    auditoria_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    paciente_id     INT UNSIGNED    NOT NULL,
    campo_modificado VARCHAR(60)    NOT NULL,
    valor_anterior  TEXT            NULL,
    valor_nuevo     TEXT            NULL,
    usuario_id      INT UNSIGNED    NULL,
    modificado_en   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditoria_id),
    INDEX idx_acp_paciente (paciente_id)
) ENGINE = InnoDB
  COMMENT = 'Historial de modificaciones a datos de pacientes';

-- ------------------------------------------------------------
-- audit_cambio_orden
-- Historial de cambios en el agregado OrdenMedica
-- ------------------------------------------------------------
CREATE TABLE audit_cambio_orden (
    auditoria_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    orden_id       INT UNSIGNED    NOT NULL,
    campo_modificado VARCHAR(60)   NOT NULL,
    valor_anterior TEXT            NULL,
    valor_nuevo    TEXT            NULL,
    usuario_id     INT UNSIGNED    NULL,
    modificado_en  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditoria_id),
    INDEX idx_aco_orden (orden_id)
) ENGINE = InnoDB
  COMMENT = 'Historial de modificaciones a ordenes medicas';

-- ------------------------------------------------------------
-- audit_cambio_factura
-- Historial de cambios en el agregado Facturacion
-- ------------------------------------------------------------
CREATE TABLE audit_cambio_factura (
    auditoria_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    factura_id     INT UNSIGNED    NOT NULL,
    campo_modificado VARCHAR(60)   NOT NULL,
    valor_anterior TEXT            NULL,
    valor_nuevo    TEXT            NULL,
    usuario_id     INT UNSIGNED    NULL,
    modificado_en  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditoria_id),
    INDEX idx_acf_factura (factura_id)
) ENGINE = InnoDB
  COMMENT = 'Historial de modificaciones a facturas';

-- ------------------------------------------------------------
-- audit_acceso
-- Registro de intentos de acceso al sistema
-- ------------------------------------------------------------
CREATE TABLE audit_acceso (
    acceso_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo_usuario VARCHAR(15)   NOT NULL,
    resultado    VARCHAR(20)     NOT NULL,   -- 'exitoso','fallido','bloqueado'
    ip_origen    VARCHAR(45)     NULL,
    ejecutado_en DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (acceso_id),
    INDEX idx_acc_usuario (codigo_usuario),
    INDEX idx_acc_fecha   (ejecutado_en),
    CONSTRAINT chk_acc_resultado CHECK (resultado IN ('exitoso','fallido','bloqueado'))
) ENGINE = InnoDB
  COMMENT = 'Registro de intentos de autenticacion en el sistema';

-- ------------------------------------------------------------
-- audit_error_proceso
-- Errores capturados durante ejecucion de procedimientos almacenados
-- ------------------------------------------------------------
CREATE TABLE audit_error_proceso (
    error_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    procedimiento  VARCHAR(80)     NOT NULL,
    codigo_error   INT             NOT NULL,
    mensaje_error  TEXT            NOT NULL,
    parametros     TEXT            NULL,
    usuario_id     INT UNSIGNED    NULL,
    ejecutado_en   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (error_id),
    INDEX idx_aep_proc  (procedimiento),
    INDEX idx_aep_fecha (ejecutado_en)
) ENGINE = InnoDB
  COMMENT = 'Errores capturados durante la ejecucion de procedimientos almacenados';

-- ============================================================
-- TRIGGERS DE AUDITORIA
-- ============================================================

DELIMITER $$

-- Audita INSERT en empleado
CREATE TRIGGER trg_audit_empleado_ai
AFTER INSERT ON empleado
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('empleado', 'INSERT', NEW.empleado_id, NEW.created_by,
            CONCAT('Nuevo empleado: ', NEW.nombre_completo, ' cedula: ', NEW.cedula));
END$$

-- Audita UPDATE en empleado - campos criticos
CREATE TRIGGER trg_audit_empleado_au
AFTER UPDATE ON empleado
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('empleado', 'UPDATE', NEW.empleado_id, NEW.updated_by,
            CONCAT('Empleado actualizado. ID: ', NEW.empleado_id));

    IF OLD.rol_id != NEW.rol_id THEN
        INSERT INTO audit_cambio_empleado (empleado_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.empleado_id, 'rol_id', OLD.rol_id, NEW.rol_id, NEW.updated_by);
    END IF;

    IF OLD.active_flag != NEW.active_flag THEN
        INSERT INTO audit_cambio_empleado (empleado_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.empleado_id, 'active_flag', OLD.active_flag, NEW.active_flag, NEW.updated_by);
    END IF;

    IF OLD.correo != NEW.correo THEN
        INSERT INTO audit_cambio_empleado (empleado_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.empleado_id, 'correo', OLD.correo, NEW.correo, NEW.updated_by);
    END IF;
END$$

-- Audita INSERT en paciente
CREATE TRIGGER trg_audit_paciente_ai
AFTER INSERT ON paciente
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('paciente', 'INSERT', NEW.paciente_id, NEW.created_by,
            CONCAT('Nuevo paciente: ', NEW.nombre_completo, ' cedula: ', NEW.cedula));
END$$

-- Audita UPDATE en paciente - campos criticos
CREATE TRIGGER trg_audit_paciente_au
AFTER UPDATE ON paciente
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('paciente', 'UPDATE', NEW.paciente_id, NEW.updated_by,
            CONCAT('Paciente actualizado. ID: ', NEW.paciente_id));

    IF OLD.cedula != NEW.cedula THEN
        INSERT INTO audit_cambio_paciente (paciente_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.paciente_id, 'cedula', OLD.cedula, NEW.cedula, NEW.updated_by);
    END IF;

    IF OLD.active_flag != NEW.active_flag THEN
        INSERT INTO audit_cambio_paciente (paciente_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.paciente_id, 'active_flag', OLD.active_flag, NEW.active_flag, NEW.updated_by);
    END IF;

    IF (OLD.correo IS NULL AND NEW.correo IS NOT NULL)
        OR (OLD.correo IS NOT NULL AND OLD.correo != NEW.correo) THEN
        INSERT INTO audit_cambio_paciente (paciente_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.paciente_id, 'correo', OLD.correo, NEW.correo, NEW.updated_by);
    END IF;
END$$

-- Audita INSERT en orden_medica
CREATE TRIGGER trg_audit_orden_ai
AFTER INSERT ON orden_medica
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('orden_medica', 'INSERT', NEW.orden_id, NEW.created_by,
            CONCAT('Nueva orden medica: ', NEW.numero_orden, ' paciente_id: ', NEW.paciente_id));
END$$

-- Audita INSERT en factura
CREATE TRIGGER trg_audit_factura_ai
AFTER INSERT ON factura
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('factura', 'INSERT', NEW.factura_id, NEW.created_by,
            CONCAT('Nueva factura emitida. Total: ', NEW.valor_total, ' paciente_id: ', NEW.paciente_id));
END$$

-- Audita UPDATE en factura - cambio de estado
CREATE TRIGGER trg_audit_factura_au
AFTER UPDATE ON factura
FOR EACH ROW
BEGIN
    IF OLD.estado != NEW.estado THEN
        INSERT INTO audit_cambio_factura (factura_id, campo_modificado, valor_anterior, valor_nuevo, usuario_id)
        VALUES (NEW.factura_id, 'estado', OLD.estado, NEW.estado, NEW.updated_by);

        INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
        VALUES ('factura', 'UPDATE', NEW.factura_id, NEW.updated_by,
                CONCAT('Estado factura cambio de ', OLD.estado, ' a ', NEW.estado));
    END IF;
END$$

-- Audita INSERT en pago
CREATE TRIGGER trg_audit_pago_ai
AFTER INSERT ON pago
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (tabla, operacion, registro_id, usuario_id, descripcion)
    VALUES ('pago', 'INSERT', NEW.pago_id, NEW.created_by,
            CONCAT('Pago registrado. Factura: ', NEW.factura_id, ' Valor: ', NEW.valor_pagado, ' Tipo: ', NEW.tipo_pago));
END$$

DELIMITER ;
