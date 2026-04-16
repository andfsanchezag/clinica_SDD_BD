-- ============================================================
-- CLINICA IPS - Triggers de validacion y coherencia de dominio
-- Principio: CHECK para reglas locales, TRIGGER para reglas
--            que dependen de otras tablas o del estado del agregado
-- Orden de ejecucion: 5 de 10
-- Dependencias: 03_tablas_transaccionales.sql
-- ============================================================

USE clinica_ips;

DELIMITER $$

-- ============================================================
-- AGREGADO: EMPLEADO
-- ============================================================

-- Valida edad max 150 anios antes de insertar empleado
CREATE TRIGGER trg_empleado_bi
BEFORE INSERT ON empleado
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) > 150 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-EMP-001: La fecha de nacimiento supera los 150 anios permitidos';
    END IF;
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-EMP-002: La fecha de nacimiento no puede ser futura';
    END IF;
END$$

-- Valida edad max 150 anios antes de actualizar empleado
CREATE TRIGGER trg_empleado_bu
BEFORE UPDATE ON empleado
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) > 150 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-EMP-001: La fecha de nacimiento supera los 150 anios permitidos';
    END IF;
END$$

-- ============================================================
-- AGREGADO: PACIENTE
-- ============================================================

-- Valida edad max 150 anios y formato de correo opcional antes de insertar
CREATE TRIGGER trg_paciente_bi
BEFORE INSERT ON paciente
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) > 150 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-PAC-001: La fecha de nacimiento supera los 150 anios permitidos';
    END IF;
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-PAC-002: La fecha de nacimiento no puede ser futura';
    END IF;
    -- telefono ya validado por CHECK constraint (10 digitos numericos)
    -- correo ya validado por CHECK constraint
    -- Fecha y hora de creacion automatica (NOT NULL DEFAULT CURRENT_TIMESTAMP)
END$$

-- Valida antes de actualizar paciente
CREATE TRIGGER trg_paciente_bu
BEFORE UPDATE ON paciente
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE()) > 150 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-PAC-001: La fecha de nacimiento supera los 150 anios permitidos';
    END IF;
END$$

-- ============================================================
-- AGREGADO: PACIENTE - CONTACTO_EMERGENCIA
-- Invariante: un solo contacto activo por paciente
-- ============================================================

-- Desactiva contacto previo antes de insertar uno nuevo activo
CREATE TRIGGER trg_contacto_emergencia_bi
BEFORE INSERT ON contacto_emergencia
FOR EACH ROW
BEGIN
    DECLARE v_activos INT;

    IF TRIM(NEW.relacion) = '' OR NEW.relacion IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-CE-001: La relacion del contacto de emergencia es obligatoria';
    END IF;

    -- Cuenta contactos activos existentes para ese paciente
    SELECT COUNT(*) INTO v_activos
    FROM contacto_emergencia
    WHERE paciente_id = NEW.paciente_id AND activo = 1;

    IF v_activos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-CE-002: Ya existe un contacto de emergencia activo. Use sp_actualizar_contacto_emergencia para reemplazarlo';
    END IF;
END$$

-- ============================================================
-- AGREGADO: PACIENTE - SEGURO_MEDICO
-- Invariante: un solo seguro activo por paciente
--             vigencia no puede ser menor a hoy
-- ============================================================

CREATE TRIGGER trg_seguro_medico_bi
BEFORE INSERT ON seguro_medico
FOR EACH ROW
BEGIN
    DECLARE v_activos INT;

    IF NEW.fecha_vigencia < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-SM-001: La fecha de vigencia del seguro no puede ser anterior a la fecha actual';
    END IF;

    SELECT COUNT(*) INTO v_activos
    FROM seguro_medico
    WHERE paciente_id = NEW.paciente_id AND activo = 1;

    IF v_activos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-SM-002: Ya existe un seguro activo para este paciente. Desactive el anterior antes de registrar uno nuevo';
    END IF;
END$$

CREATE TRIGGER trg_seguro_medico_bu
BEFORE UPDATE ON seguro_medico
FOR EACH ROW
BEGIN
    IF NEW.fecha_vigencia < DATE(NEW.created_at) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-SM-003: La fecha de vigencia no puede ser anterior a la fecha de registro del seguro';
    END IF;
END$$

-- ============================================================
-- AGREGADO: CITA
-- Invariante: medico activo, sin solapamiento en la misma franja (30 min)
-- ============================================================

CREATE TRIGGER trg_cita_bi
BEFORE INSERT ON cita
FOR EACH ROW
BEGIN
    DECLARE v_medico_activo  INT;
    DECLARE v_solapamiento   INT;

    -- Medico debe estar activo
    SELECT COUNT(*) INTO v_medico_activo
    FROM medico_perfil
    WHERE medico_id = NEW.medico_id AND activo = 1;

    IF v_medico_activo = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-CIT-001: El medico no existe o no esta activo';
    END IF;

    -- Sin solapamiento: franja de 30 minutos por medico
    SELECT COUNT(*) INTO v_solapamiento
    FROM cita
    WHERE medico_id = NEW.medico_id
      AND estado_cita_id NOT IN (
            SELECT estado_cita_id FROM cat_estado_cita
            WHERE codigo IN ('cancelada','no_asistio')
          )
      AND ABS(TIMESTAMPDIFF(MINUTE, fecha_hora, NEW.fecha_hora)) < 30;

    IF v_solapamiento > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-CIT-002: El medico ya tiene una cita programada en esa franja horaria (ventana de 30 minutos)';
    END IF;
END$$

-- ============================================================
-- AGREGADO: ENCUENTRO_CLINICO
-- Invariante: no puede cerrarse sin diagnostico
-- ============================================================

CREATE TRIGGER trg_encuentro_clinico_bu
BEFORE UPDATE ON encuentro_clinico
FOR EACH ROW
BEGIN
    IF NEW.estado = 'cerrado' AND (NEW.diagnostico IS NULL OR TRIM(NEW.diagnostico) = '') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ENC-001: No se puede cerrar un encuentro clinico sin registrar el diagnostico';
    END IF;

    IF NEW.estado = 'cerrado' AND (NEW.motivo_consulta IS NULL OR TRIM(NEW.motivo_consulta) = '') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ENC-002: No se puede cerrar un encuentro clinico sin motivo de consulta';
    END IF;

    -- No reabrir un encuentro ya cerrado
    IF OLD.estado = 'cerrado' AND NEW.estado = 'abierto' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ENC-003: No se puede reabrir un encuentro clinico cerrado';
    END IF;
END$$

-- ============================================================
-- AGREGADO: ORDEN_MEDICA - DETALLE MEDICAMENTO
-- Invariante: item >= 1, sin repeticion por orden
--             la orden debe ser de tipo compatible (no ayuda_diagnostica)
-- ============================================================

CREATE TRIGGER trg_orden_medicamento_detalle_bi
BEFORE INSERT ON orden_medicamento_detalle
FOR EACH ROW
BEGIN
    DECLARE v_tipo_orden VARCHAR(40);
    DECLARE v_tiene_ayuda INT;

    -- Obtener tipo de orden
    SELECT co.codigo INTO v_tipo_orden
    FROM orden_medica om
    JOIN cat_tipo_orden co ON co.tipo_orden_id = om.tipo_orden_id
    WHERE om.orden_id = NEW.orden_id;

    IF v_tipo_orden = 'ayuda_diagnostica' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-001: Una orden de ayuda diagnostica no puede contener medicamentos';
    END IF;

    -- Verificar que no existan ayudas en la misma orden
    SELECT COUNT(*) INTO v_tiene_ayuda
    FROM orden_ayuda_diagnostica_detalle
    WHERE orden_id = NEW.orden_id;

    IF v_tiene_ayuda > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-002: No se puede mezclar medicamentos con ayudas diagnosticas en la misma orden';
    END IF;

    IF NEW.item < 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-003: El item de la orden debe iniciar en 1';
    END IF;
END$$

-- ============================================================
-- AGREGADO: ORDEN_MEDICA - DETALLE PROCEDIMIENTO
-- Invariante: si requiere_especialista = 1, especialidad_id no puede ser NULL
-- ============================================================

CREATE TRIGGER trg_orden_procedimiento_detalle_bi
BEFORE INSERT ON orden_procedimiento_detalle
FOR EACH ROW
BEGIN
    DECLARE v_tipo_orden VARCHAR(40);
    DECLARE v_tiene_ayuda INT;

    SELECT co.codigo INTO v_tipo_orden
    FROM orden_medica om
    JOIN cat_tipo_orden co ON co.tipo_orden_id = om.tipo_orden_id
    WHERE om.orden_id = NEW.orden_id;

    IF v_tipo_orden = 'ayuda_diagnostica' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-004: Una orden de ayuda diagnostica no puede contener procedimientos';
    END IF;

    SELECT COUNT(*) INTO v_tiene_ayuda
    FROM orden_ayuda_diagnostica_detalle
    WHERE orden_id = NEW.orden_id;

    IF v_tiene_ayuda > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-005: No se puede mezclar procedimientos con ayudas diagnosticas en la misma orden';
    END IF;

    IF NEW.requiere_especialista = 1 AND NEW.especialidad_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-006: Si el procedimiento requiere especialista, debe indicar la especialidad';
    END IF;

    IF NEW.item < 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-003: El item de la orden debe iniciar en 1';
    END IF;
END$$

-- ============================================================
-- AGREGADO: ORDEN_MEDICA - DETALLE AYUDA DIAGNOSTICA
-- Invariante: NO puede coexistir con medicamentos ni procedimientos
-- ============================================================

CREATE TRIGGER trg_orden_ayuda_diagnostica_detalle_bi
BEFORE INSERT ON orden_ayuda_diagnostica_detalle
FOR EACH ROW
BEGIN
    DECLARE v_tiene_med  INT;
    DECLARE v_tiene_proc INT;

    SELECT COUNT(*) INTO v_tiene_med
    FROM orden_medicamento_detalle
    WHERE orden_id = NEW.orden_id;

    SELECT COUNT(*) INTO v_tiene_proc
    FROM orden_procedimiento_detalle
    WHERE orden_id = NEW.orden_id;

    IF v_tiene_med > 0 OR v_tiene_proc > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-007: Las ayudas diagnosticas no se pueden mezclar con medicamentos o procedimientos en la misma orden';
    END IF;

    IF NEW.requiere_especialista = 1 AND NEW.especialidad_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-008: Si la ayuda diagnostica requiere especialista, debe indicar la especialidad';
    END IF;

    IF NEW.item < 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ERR-ORD-003: El item de la orden debe iniciar en 1';
    END IF;
END$$

-- ============================================================
-- AGREGADO: FACTURACION - PAGO
-- Invariante: el acumulado anual de copago no puede superar el tope
-- ============================================================

CREATE TRIGGER trg_pago_bi
BEFORE INSERT ON pago
FOR EACH ROW
BEGIN
    DECLARE v_tope_anual     DECIMAL(14, 2);
    DECLARE v_acumulado      DECIMAL(14, 2);
    DECLARE v_paciente_id    INT UNSIGNED;

    IF NEW.tipo_pago = 'copago' THEN
        -- Obtener tope desde config
        SELECT valor_numerico INTO v_tope_anual
        FROM config_facturacion
        WHERE parametro = 'tope_anual_copago';

        -- Obtener paciente de la factura
        SELECT paciente_id INTO v_paciente_id
        FROM factura
        WHERE factura_id = NEW.factura_id;

        -- Sumar copagos del paciente en el anio calendario actual
        SELECT COALESCE(SUM(p.valor_pagado), 0) INTO v_acumulado
        FROM pago p
        JOIN factura f ON f.factura_id = p.factura_id
        WHERE f.paciente_id = v_paciente_id
          AND p.tipo_pago = 'copago'
          AND YEAR(p.fecha_pago) = YEAR(CURDATE());

        IF (v_acumulado + NEW.valor_pagado) > v_tope_anual THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'ERR-PAG-001: El copago supera el tope anual acumulado. El paciente queda exento de copago para el resto del anio';
        END IF;
    END IF;
END$$

DELIMITER ;
