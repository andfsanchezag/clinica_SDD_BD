-- ============================================================
-- CLINICA IPS - Procedimientos Almacenados
-- Principio SDD: toda logica de negocio en la BD
-- Patron: validacion -> transaccion -> respuesta estandar
-- OUT estandar: o_codigo SMALLINT, o_mensaje VARCHAR(200),
--               o_id_generado INT UNSIGNED
-- Orden de ejecucion: 6 de 10
-- Dependencias: 04_triggers.sql
-- ============================================================

USE clinica_ips;

DELIMITER $$

-- ============================================================
-- CONTEXTO: GESTION DE PACIENTES
-- ============================================================

-- ------------------------------------------------------------
-- sp_registrar_paciente
-- Crea un nuevo paciente validando reglas del agregado
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_paciente(
    IN  p_cedula          VARCHAR(20),
    IN  p_tipo_doc_id     TINYINT UNSIGNED,
    IN  p_nombre_completo VARCHAR(150),
    IN  p_fecha_nacimiento DATE,
    IN  p_genero_id       TINYINT UNSIGNED,
    IN  p_direccion       VARCHAR(200),
    IN  p_telefono        VARCHAR(10),
    IN  p_correo          VARCHAR(120),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo          SMALLINT,
    OUT o_mensaje         VARCHAR(200),
    OUT o_id_generado     INT UNSIGNED
)
sp_registrar_paciente: BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1;
        SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    -- Validaciones de parametros de entrada
    IF p_cedula IS NULL OR TRIM(p_cedula) = '' THEN
        SET o_codigo = 1; SET o_mensaje = 'La cedula del paciente es obligatoria'; LEAVE sp_registrar_paciente;
    END IF;
    IF p_nombre_completo IS NULL OR TRIM(p_nombre_completo) = '' THEN
        SET o_codigo = 2; SET o_mensaje = 'El nombre completo es obligatorio'; LEAVE sp_registrar_paciente;
    END IF;
    IF p_fecha_nacimiento IS NULL THEN
        SET o_codigo = 3; SET o_mensaje = 'La fecha de nacimiento es obligatoria'; LEAVE sp_registrar_paciente;
    END IF;
    IF p_telefono IS NULL OR p_telefono NOT REGEXP '^[0-9]{10}$' THEN
        SET o_codigo = 4; SET o_mensaje = 'El telefono debe contener exactamente 10 digitos numericos'; LEAVE sp_registrar_paciente;
    END IF;
    IF p_correo IS NOT NULL AND p_correo NOT REGEXP '^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$' THEN
        SET o_codigo = 5; SET o_mensaje = 'El formato del correo electronico no es valido'; LEAVE sp_registrar_paciente;
    END IF;

    -- Cedula unica
    SELECT COUNT(*) INTO v_existe FROM paciente WHERE cedula = p_cedula;
    IF v_existe > 0 THEN
        SET o_codigo = 6; SET o_mensaje = 'Ya existe un paciente con esa cedula'; LEAVE sp_registrar_paciente;
    END IF;

    START TRANSACTION;

    INSERT INTO paciente (
        cedula, tipo_doc_id, nombre_completo, fecha_nacimiento,
        genero_id, direccion, telefono, correo,
        active_flag, created_by
    ) VALUES (
        p_cedula, p_tipo_doc_id, p_nombre_completo, p_fecha_nacimiento,
        p_genero_id, p_direccion, p_telefono, p_correo,
        1, p_usuario_operador
    );

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Paciente registrado correctamente. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_registrar_contacto_emergencia
-- Desactiva el contacto previo e inserta el nuevo
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_contacto_emergencia(
    IN  p_paciente_id     INT UNSIGNED,
    IN  p_nombre_completo VARCHAR(150),
    IN  p_relacion        VARCHAR(50),
    IN  p_telefono        VARCHAR(10),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo          SMALLINT,
    OUT o_mensaje         VARCHAR(200),
    OUT o_id_generado     INT UNSIGNED
)
sp_registrar_contacto_emergencia: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_telefono IS NULL OR p_telefono NOT REGEXP '^[0-9]{10}$' THEN
        SET o_codigo = 1; SET o_mensaje = 'El telefono del contacto debe contener exactamente 10 digitos'; LEAVE sp_registrar_contacto_emergencia;
    END IF;
    IF TRIM(p_relacion) = '' OR p_relacion IS NULL THEN
        SET o_codigo = 2; SET o_mensaje = 'La relacion del contacto es obligatoria'; LEAVE sp_registrar_contacto_emergencia;
    END IF;

    START TRANSACTION;

    -- Desactivar contacto previo
    UPDATE contacto_emergencia SET activo = 0
    WHERE paciente_id = p_paciente_id AND activo = 1;

    INSERT INTO contacto_emergencia (paciente_id, nombre_completo, relacion, telefono, activo, created_by)
    VALUES (p_paciente_id, p_nombre_completo, p_relacion, p_telefono, 1, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Contacto de emergencia registrado. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_registrar_seguro_medico
-- Registra seguro verificando que no haya otro activo
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_seguro_medico(
    IN  p_paciente_id      INT UNSIGNED,
    IN  p_compania         VARCHAR(120),
    IN  p_numero_poliza    VARCHAR(60),
    IN  p_estado_seguro_id TINYINT UNSIGNED,
    IN  p_fecha_vigencia   DATE,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo           SMALLINT,
    OUT o_mensaje          VARCHAR(200),
    OUT o_id_generado      INT UNSIGNED
)
sp_registrar_seguro_medico: BEGIN
    DECLARE v_activos INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_fecha_vigencia < CURDATE() THEN
        SET o_codigo = 1; SET o_mensaje = 'La fecha de vigencia no puede ser anterior a hoy'; LEAVE sp_registrar_seguro_medico;
    END IF;

    SELECT COUNT(*) INTO v_activos FROM seguro_medico WHERE paciente_id = p_paciente_id AND activo = 1;
    IF v_activos > 0 THEN
        SET o_codigo = 2; SET o_mensaje = 'El paciente ya tiene un seguro activo. Desactivelo antes de registrar uno nuevo'; LEAVE sp_registrar_seguro_medico;
    END IF;

    START TRANSACTION;

    INSERT INTO seguro_medico (paciente_id, compania, numero_poliza, estado_seguro_id, fecha_vigencia, activo, created_by)
    VALUES (p_paciente_id, p_compania, p_numero_poliza, p_estado_seguro_id, p_fecha_vigencia, 1, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Seguro medico registrado. ID: ', o_id_generado);

    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: GESTION DE PERSONAL
-- ============================================================

-- ------------------------------------------------------------
-- sp_registrar_empleado
-- Crea empleado y opcionalmente su perfil funcional
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_empleado(
    IN  p_cedula          VARCHAR(20),
    IN  p_tipo_doc_id     TINYINT UNSIGNED,
    IN  p_nombre_completo VARCHAR(150),
    IN  p_correo          VARCHAR(120),
    IN  p_telefono        VARCHAR(10),
    IN  p_fecha_nacimiento DATE,
    IN  p_direccion       VARCHAR(30),
    IN  p_rol_id          TINYINT UNSIGNED,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo          SMALLINT,
    OUT o_mensaje         VARCHAR(200),
    OUT o_id_generado     INT UNSIGNED
)
sp_registrar_empleado: BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_cedula IS NULL OR TRIM(p_cedula) = '' THEN
        SET o_codigo = 1; SET o_mensaje = 'La cedula del empleado es obligatoria'; LEAVE sp_registrar_empleado;
    END IF;
    IF CHAR_LENGTH(p_direccion) > 30 THEN
        SET o_codigo = 2; SET o_mensaje = 'La direccion no puede superar 30 caracteres'; LEAVE sp_registrar_empleado;
    END IF;
    IF p_telefono NOT REGEXP '^[0-9]{1,10}$' THEN
        SET o_codigo = 3; SET o_mensaje = 'El telefono debe ser numerico de hasta 10 digitos'; LEAVE sp_registrar_empleado;
    END IF;
    IF p_correo NOT REGEXP '^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$' THEN
        SET o_codigo = 4; SET o_mensaje = 'El formato del correo no es valido'; LEAVE sp_registrar_empleado;
    END IF;

    SELECT COUNT(*) INTO v_existe FROM empleado WHERE cedula = p_cedula;
    IF v_existe > 0 THEN
        SET o_codigo = 5; SET o_mensaje = 'Ya existe un empleado con esa cedula'; LEAVE sp_registrar_empleado;
    END IF;

    START TRANSACTION;

    INSERT INTO empleado (cedula, tipo_doc_id, nombre_completo, correo, telefono, fecha_nacimiento, direccion, rol_id, active_flag, created_by)
    VALUES (p_cedula, p_tipo_doc_id, p_nombre_completo, p_correo, p_telefono, p_fecha_nacimiento, p_direccion, p_rol_id, 1, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Empleado registrado correctamente. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_crear_usuario_operativo
-- Crea credenciales de acceso para un empleado
-- ------------------------------------------------------------
CREATE PROCEDURE sp_crear_usuario_operativo(
    IN  p_empleado_id     INT UNSIGNED,
    IN  p_codigo_usuario  VARCHAR(15),
    IN  p_contrasena_hash VARCHAR(255),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo          SMALLINT,
    OUT o_mensaje         VARCHAR(200),
    OUT o_id_generado     INT UNSIGNED
)
sp_crear_usuario_operativo: BEGIN
    DECLARE v_existe_emp   INT DEFAULT 0;
    DECLARE v_tiene_user   INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_codigo_usuario NOT REGEXP '^[a-zA-Z0-9]{1,15}$' THEN
        SET o_codigo = 1; SET o_mensaje = 'El codigo de usuario debe ser alfanumerico y tener maximo 15 caracteres'; LEAVE sp_crear_usuario_operativo;
    END IF;
    IF p_contrasena_hash IS NULL OR CHAR_LENGTH(p_contrasena_hash) < 20 THEN
        SET o_codigo = 2; SET o_mensaje = 'La contrasena debe ser proporcionada como hash (minimo 20 caracteres)'; LEAVE sp_crear_usuario_operativo;
    END IF;

    SELECT COUNT(*) INTO v_existe_emp FROM empleado WHERE empleado_id = p_empleado_id AND active_flag = 1;
    IF v_existe_emp = 0 THEN
        SET o_codigo = 3; SET o_mensaje = 'El empleado no existe o no esta activo'; LEAVE sp_crear_usuario_operativo;
    END IF;

    SELECT COUNT(*) INTO v_tiene_user FROM seguridad_usuario WHERE empleado_id = p_empleado_id;
    IF v_tiene_user > 0 THEN
        SET o_codigo = 4; SET o_mensaje = 'El empleado ya tiene un usuario asignado'; LEAVE sp_crear_usuario_operativo;
    END IF;

    START TRANSACTION;

    INSERT INTO seguridad_usuario (empleado_id, codigo_usuario, contrasena_hash, activo, created_by)
    VALUES (p_empleado_id, p_codigo_usuario, p_contrasena_hash, 1, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Usuario operativo creado. ID: ', o_id_generado);

    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: AGENDA Y ATENCION
-- ============================================================

-- ------------------------------------------------------------
-- sp_programar_cita
-- ------------------------------------------------------------
CREATE PROCEDURE sp_programar_cita(
    IN  p_paciente_id    INT UNSIGNED,
    IN  p_medico_id      INT UNSIGNED,
    IN  p_fecha_hora     DATETIME,
    IN  p_prioridad_id   TINYINT UNSIGNED,
    IN  p_motivo         VARCHAR(300),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_programar_cita: BEGIN
    DECLARE v_paciente_activo INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_fecha_hora <= NOW() THEN
        SET o_codigo = 1; SET o_mensaje = 'La fecha y hora de la cita debe ser futura'; LEAVE sp_programar_cita;
    END IF;
    IF p_motivo IS NULL OR TRIM(p_motivo) = '' THEN
        SET o_codigo = 2; SET o_mensaje = 'El motivo de la cita es obligatorio'; LEAVE sp_programar_cita;
    END IF;

    SELECT COUNT(*) INTO v_paciente_activo FROM paciente WHERE paciente_id = p_paciente_id AND active_flag = 1;
    IF v_paciente_activo = 0 THEN
        SET o_codigo = 3; SET o_mensaje = 'El paciente no existe o no esta activo'; LEAVE sp_programar_cita;
    END IF;

    START TRANSACTION;

    -- El trigger trg_cita_bi valida solapamiento y medico activo
    INSERT INTO cita (paciente_id, medico_id, fecha_hora, estado_cita_id, prioridad_id, motivo, created_by)
    VALUES (p_paciente_id, p_medico_id, p_fecha_hora, 1, p_prioridad_id, p_motivo, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Cita programada. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_reprogramar_cita
-- ------------------------------------------------------------
CREATE PROCEDURE sp_reprogramar_cita(
    IN  p_cita_id        INT UNSIGNED,
    IN  p_nueva_fecha    DATETIME,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_reprogramar_cita: BEGIN
    DECLARE v_estado_actual VARCHAR(30);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT co.codigo INTO v_estado_actual
    FROM cita c
    JOIN cat_estado_cita co ON co.estado_cita_id = c.estado_cita_id
    WHERE c.cita_id = p_cita_id;

    IF v_estado_actual IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Cita no encontrada'; LEAVE sp_reprogramar_cita;
    END IF;
    IF v_estado_actual IN ('atendida','cancelada') THEN
        SET o_codigo = 2; SET o_mensaje = CONCAT('No se puede reprogramar una cita en estado: ', v_estado_actual); LEAVE sp_reprogramar_cita;
    END IF;
    IF p_nueva_fecha <= NOW() THEN
        SET o_codigo = 3; SET o_mensaje = 'La nueva fecha debe ser futura'; LEAVE sp_reprogramar_cita;
    END IF;

    START TRANSACTION;

    UPDATE cita
    SET fecha_hora     = p_nueva_fecha,
        estado_cita_id = (SELECT estado_cita_id FROM cat_estado_cita WHERE codigo = 'reprogramada'),
        updated_by     = p_usuario_operador
    WHERE cita_id = p_cita_id;

    SET o_id_generado = p_cita_id;
    SET o_codigo = 0;
    SET o_mensaje = 'Cita reprogramada correctamente';

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_cancelar_cita
-- ------------------------------------------------------------
CREATE PROCEDURE sp_cancelar_cita(
    IN  p_cita_id        INT UNSIGNED,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_cancelar_cita: BEGIN
    DECLARE v_estado_actual VARCHAR(30);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT co.codigo INTO v_estado_actual
    FROM cita c
    JOIN cat_estado_cita co ON co.estado_cita_id = c.estado_cita_id
    WHERE c.cita_id = p_cita_id;

    IF v_estado_actual IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Cita no encontrada'; LEAVE sp_cancelar_cita;
    END IF;
    IF v_estado_actual IN ('atendida','cancelada') THEN
        SET o_codigo = 2; SET o_mensaje = CONCAT('No se puede cancelar una cita en estado: ', v_estado_actual); LEAVE sp_cancelar_cita;
    END IF;

    START TRANSACTION;

    UPDATE cita
    SET estado_cita_id = (SELECT estado_cita_id FROM cat_estado_cita WHERE codigo = 'cancelada'),
        updated_by     = p_usuario_operador
    WHERE cita_id = p_cita_id;

    SET o_id_generado = p_cita_id;
    SET o_codigo = 0;
    SET o_mensaje = 'Cita cancelada correctamente';

    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: ATENCION CLINICA (ENCUENTRO / HISTORIA CLINICA)
-- ============================================================

-- ------------------------------------------------------------
-- sp_abrir_encuentro_clinico
-- ------------------------------------------------------------
CREATE PROCEDURE sp_abrir_encuentro_clinico(
    IN  p_cita_id        INT UNSIGNED,
    IN  p_paciente_id    INT UNSIGNED,
    IN  p_medico_id      INT UNSIGNED,
    IN  p_motivo_consulta TEXT,
    IN  p_sintomatologia  TEXT,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_abrir_encuentro_clinico: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_motivo_consulta IS NULL OR TRIM(p_motivo_consulta) = '' THEN
        SET o_codigo = 1; SET o_mensaje = 'El motivo de consulta es obligatorio'; LEAVE sp_abrir_encuentro_clinico;
    END IF;

    START TRANSACTION;

    INSERT INTO encuentro_clinico (cita_id, paciente_id, medico_id, motivo_consulta, sintomatologia, estado, created_by)
    VALUES (p_cita_id, p_paciente_id, p_medico_id, p_motivo_consulta, COALESCE(p_sintomatologia,''), 'abierto', p_usuario_operador);

    -- Marcar cita como atendida si fue presencial
    IF p_cita_id IS NOT NULL THEN
        UPDATE cita
        SET estado_cita_id = (SELECT estado_cita_id FROM cat_estado_cita WHERE codigo = 'atendida'),
            updated_by     = p_usuario_operador
        WHERE cita_id = p_cita_id;
    END IF;

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Encuentro clinico abierto. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_cerrar_encuentro_clinico
-- ------------------------------------------------------------
CREATE PROCEDURE sp_cerrar_encuentro_clinico(
    IN  p_encuentro_id   INT UNSIGNED,
    IN  p_diagnostico    TEXT,
    IN  p_tratamiento    TEXT,
    IN  p_observaciones  TEXT,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_cerrar_encuentro_clinico: BEGIN
    DECLARE v_estado_actual VARCHAR(30);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT estado INTO v_estado_actual FROM encuentro_clinico WHERE encuentro_id = p_encuentro_id;

    IF v_estado_actual IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Encuentro clinico no encontrado'; LEAVE sp_cerrar_encuentro_clinico;
    END IF;
    IF v_estado_actual = 'cerrado' THEN
        SET o_codigo = 2; SET o_mensaje = 'El encuentro ya esta cerrado'; LEAVE sp_cerrar_encuentro_clinico;
    END IF;
    IF p_diagnostico IS NULL OR TRIM(p_diagnostico) = '' THEN
        SET o_codigo = 3; SET o_mensaje = 'El diagnostico es obligatorio para cerrar el encuentro'; LEAVE sp_cerrar_encuentro_clinico;
    END IF;

    START TRANSACTION;

    UPDATE encuentro_clinico
    SET diagnostico   = p_diagnostico,
        tratamiento   = p_tratamiento,
        observaciones = p_observaciones,
        estado        = 'cerrado',
        updated_by    = p_usuario_operador
    WHERE encuentro_id = p_encuentro_id;

    SET o_id_generado = p_encuentro_id;
    SET o_codigo = 0;
    SET o_mensaje = 'Encuentro clinico cerrado correctamente';

    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: ENFERMERIA
-- ============================================================

-- ------------------------------------------------------------
-- sp_registrar_signos_vitales
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_signos_vitales(
    IN  p_encuentro_id   INT UNSIGNED,
    IN  p_enfermero_id   INT UNSIGNED,
    IN  p_presion        VARCHAR(20),
    IN  p_temperatura    DECIMAL(4,1),
    IN  p_pulso          SMALLINT,
    IN  p_oxigeno        DECIMAL(5,2),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_registrar_signos_vitales: BEGIN
    DECLARE v_estado_enc VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT estado INTO v_estado_enc FROM encuentro_clinico WHERE encuentro_id = p_encuentro_id;
    IF v_estado_enc IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Encuentro clinico no encontrado'; LEAVE sp_registrar_signos_vitales;
    END IF;
    IF v_estado_enc = 'cerrado' THEN
        SET o_codigo = 2; SET o_mensaje = 'No se pueden registrar signos vitales en un encuentro cerrado'; LEAVE sp_registrar_signos_vitales;
    END IF;

    START TRANSACTION;

    INSERT INTO signo_vital (encuentro_id, enfermero_id, presion_arterial, temperatura, pulso, oxigeno, created_by)
    VALUES (p_encuentro_id, p_enfermero_id, p_presion, p_temperatura, p_pulso, p_oxigeno, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Signos vitales registrados. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_registrar_administracion_medicamento
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_administracion_medicamento(
    IN  p_encuentro_id   INT UNSIGNED,
    IN  p_enfermero_id   INT UNSIGNED,
    IN  p_medicamento_id INT UNSIGNED,
    IN  p_dosis          VARCHAR(100),
    IN  p_observacion    TEXT,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_registrar_administracion_medicamento: BEGIN
    DECLARE v_estado_enc VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT estado INTO v_estado_enc FROM encuentro_clinico WHERE encuentro_id = p_encuentro_id;
    IF v_estado_enc IS NULL OR v_estado_enc = 'cerrado' THEN
        SET o_codigo = 1; SET o_mensaje = 'El encuentro clinico no existe o esta cerrado'; LEAVE sp_registrar_administracion_medicamento;
    END IF;

    START TRANSACTION;

    INSERT INTO administracion_medicamento (encuentro_id, enfermero_id, medicamento_id, dosis, observacion, created_by)
    VALUES (p_encuentro_id, p_enfermero_id, p_medicamento_id, p_dosis, p_observacion, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Administracion de medicamento registrada. ID: ', o_id_generado);

    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: ORDENES MEDICAS
-- ============================================================

-- ------------------------------------------------------------
-- sp_registrar_orden_medica
-- Crea la cabecera de una nueva orden
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_orden_medica(
    IN  p_numero_orden   VARCHAR(6),
    IN  p_encuentro_id   INT UNSIGNED,
    IN  p_paciente_id    INT UNSIGNED,
    IN  p_medico_id      INT UNSIGNED,
    IN  p_tipo_orden_id  TINYINT UNSIGNED,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_registrar_orden_medica: BEGIN
    DECLARE v_existe_orden INT DEFAULT 0;
    DECLARE v_enc_estado   VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_numero_orden IS NULL OR p_numero_orden NOT REGEXP '^[0-9]{1,6}$' THEN
        SET o_codigo = 1; SET o_mensaje = 'El numero de orden debe ser numerico y tener maximo 6 digitos'; LEAVE sp_registrar_orden_medica;
    END IF;

    SELECT COUNT(*) INTO v_existe_orden FROM orden_medica WHERE numero_orden = p_numero_orden;
    IF v_existe_orden > 0 THEN
        SET o_codigo = 2; SET o_mensaje = 'Ya existe una orden con ese numero'; LEAVE sp_registrar_orden_medica;
    END IF;

    SELECT estado INTO v_enc_estado FROM encuentro_clinico WHERE encuentro_id = p_encuentro_id;
    IF v_enc_estado IS NULL THEN
        SET o_codigo = 3; SET o_mensaje = 'El encuentro clinico no existe'; LEAVE sp_registrar_orden_medica;
    END IF;
    IF v_enc_estado = 'cerrado' THEN
        SET o_codigo = 4; SET o_mensaje = 'No se puede emitir ordenes sobre un encuentro cerrado'; LEAVE sp_registrar_orden_medica;
    END IF;

    START TRANSACTION;

    INSERT INTO orden_medica (numero_orden, encuentro_id, paciente_id, medico_id, tipo_orden_id, created_by)
    VALUES (p_numero_orden, p_encuentro_id, p_paciente_id, p_medico_id, p_tipo_orden_id, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Orden medica creada. ID: ', o_id_generado);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_agregar_detalle_orden
-- Enruta al detalle correcto segun el tipo de item
-- ------------------------------------------------------------
CREATE PROCEDURE sp_agregar_detalle_orden(
    IN  p_orden_id       INT UNSIGNED,
    IN  p_tipo_detalle   VARCHAR(20),   -- 'medicamento','procedimiento','ayuda_diagnostica'
    IN  p_item           SMALLINT,
    IN  p_referencia_id  INT UNSIGNED,  -- medicamento_id, procedimiento_id, ayuda_id
    IN  p_dosis          VARCHAR(150),  -- solo medicamento
    IN  p_duracion       VARCHAR(100),  -- solo medicamento
    IN  p_cantidad       SMALLINT,      -- proc/ayuda
    IN  p_frecuencia     VARCHAR(100),  -- solo procedimiento
    IN  p_requiere_esp   TINYINT(1),    -- proc/ayuda
    IN  p_especialidad_id INT UNSIGNED, -- proc/ayuda nullable
    IN  p_costo          DECIMAL(12,2),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_agregar_detalle_orden: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    IF p_item < 1 THEN
        SET o_codigo = 1; SET o_mensaje = 'El item debe ser >= 1'; LEAVE sp_agregar_detalle_orden;
    END IF;
    IF p_tipo_detalle NOT IN ('medicamento','procedimiento','ayuda_diagnostica') THEN
        SET o_codigo = 2; SET o_mensaje = 'Tipo de detalle invalido. Use: medicamento, procedimiento o ayuda_diagnostica'; LEAVE sp_agregar_detalle_orden;
    END IF;

    START TRANSACTION;

    IF p_tipo_detalle = 'medicamento' THEN
        INSERT INTO orden_medicamento_detalle
            (orden_id, item, medicamento_id, dosis, duracion, costo, created_by)
        VALUES
            (p_orden_id, p_item, p_referencia_id, p_dosis, COALESCE(p_duracion,''), p_costo, p_usuario_operador);
        SET o_id_generado = LAST_INSERT_ID();
        SET o_mensaje = CONCAT('Medicamento agregado a la orden. Detalle ID: ', o_id_generado);

    ELSEIF p_tipo_detalle = 'procedimiento' THEN
        INSERT INTO orden_procedimiento_detalle
            (orden_id, item, procedimiento_id, cantidad, frecuencia, requiere_especialista, especialidad_id, costo, created_by)
        VALUES
            (p_orden_id, p_item, p_referencia_id, COALESCE(p_cantidad,1), COALESCE(p_frecuencia,''), COALESCE(p_requiere_esp,0), p_especialidad_id, p_costo, p_usuario_operador);
        SET o_id_generado = LAST_INSERT_ID();
        SET o_mensaje = CONCAT('Procedimiento agregado a la orden. Detalle ID: ', o_id_generado);

    ELSEIF p_tipo_detalle = 'ayuda_diagnostica' THEN
        INSERT INTO orden_ayuda_diagnostica_detalle
            (orden_id, item, ayuda_id, cantidad, requiere_especialista, especialidad_id, costo, created_by)
        VALUES
            (p_orden_id, p_item, p_referencia_id, COALESCE(p_cantidad,1), COALESCE(p_requiere_esp,0), p_especialidad_id, p_costo, p_usuario_operador);
        SET o_id_generado = LAST_INSERT_ID();
        SET o_mensaje = CONCAT('Ayuda diagnostica agregada a la orden. Detalle ID: ', o_id_generado);
    END IF;

    SET o_codigo = 0;
    COMMIT;
END$$

-- ============================================================
-- CONTEXTO: FACTURACION Y SEGUROS
-- ============================================================

-- ------------------------------------------------------------
-- sp_calcular_copago
-- Devuelve el copago a cobrar respetando el tope anual
-- ------------------------------------------------------------
CREATE PROCEDURE sp_calcular_copago(
    IN  p_paciente_id    INT UNSIGNED,
    IN  p_tipo_fact_id   TINYINT UNSIGNED,
    OUT o_valor_copago   DECIMAL(14,2),
    OUT o_exento         TINYINT(1),
    OUT o_mensaje        VARCHAR(200)
)
sp_calcular_copago: BEGIN
    DECLARE v_copago_base  DECIMAL(14,2);
    DECLARE v_tope_anual   DECIMAL(14,2);
    DECLARE v_acumulado    DECIMAL(14,2);
    DECLARE v_tipo_codigo  VARCHAR(30);

    SET o_valor_copago = 0; SET o_exento = 0; SET o_mensaje = '';

    SELECT codigo INTO v_tipo_codigo FROM cat_tipo_facturacion WHERE tipo_fact_id = p_tipo_fact_id;

    IF v_tipo_codigo = 'exento_copago' THEN
        SET o_exento = 1; SET o_mensaje = 'Paciente exento de copago'; LEAVE sp_calcular_copago;
    END IF;

    SELECT valor_numerico INTO v_copago_base FROM config_facturacion WHERE parametro = 'valor_copago_base';
    SELECT valor_numerico INTO v_tope_anual  FROM config_facturacion WHERE parametro = 'tope_anual_copago';

    SELECT COALESCE(SUM(p.valor_pagado), 0) INTO v_acumulado
    FROM pago p
    JOIN factura f ON f.factura_id = p.factura_id
    WHERE f.paciente_id = p_paciente_id
      AND p.tipo_pago   = 'copago'
      AND YEAR(p.fecha_pago) = YEAR(CURDATE());

    IF v_acumulado >= v_tope_anual THEN
        SET o_exento = 1;
        SET o_valor_copago = 0;
        SET o_mensaje = 'El paciente ha alcanzado el tope anual de copago. Queda exento';
    ELSE
        SET o_exento = 0;
        SET o_valor_copago = LEAST(v_copago_base, v_tope_anual - v_acumulado);
        SET o_mensaje = CONCAT('Copago calculado: ', o_valor_copago);
    END IF;
END$$

-- ------------------------------------------------------------
-- sp_emitir_factura
-- Genera factura a partir de las ordenes de un encuentro
-- ------------------------------------------------------------
CREATE PROCEDURE sp_emitir_factura(
    IN  p_encuentro_id   INT UNSIGNED,
    IN  p_paciente_id    INT UNSIGNED,
    IN  p_medico_id      INT UNSIGNED,
    IN  p_seguro_id      INT UNSIGNED,
    IN  p_tipo_fact_id   TINYINT UNSIGNED,
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_emitir_factura: BEGIN
    DECLARE v_total_med   DECIMAL(14,2) DEFAULT 0;
    DECLARE v_total_proc  DECIMAL(14,2) DEFAULT 0;
    DECLARE v_total_ayuda DECIMAL(14,2) DEFAULT 0;
    DECLARE v_total       DECIMAL(14,2) DEFAULT 0;
    DECLARE v_copago      DECIMAL(14,2) DEFAULT 0;
    DECLARE v_aseg        DECIMAL(14,2) DEFAULT 0;
    DECLARE v_pac         DECIMAL(14,2) DEFAULT 0;
    DECLARE v_exento      TINYINT(1)    DEFAULT 0;
    DECLARE v_msg_copago  VARCHAR(200);
    DECLARE v_factura_id  INT UNSIGNED;
    DECLARE v_enc_estado  VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT estado INTO v_enc_estado FROM encuentro_clinico WHERE encuentro_id = p_encuentro_id;
    IF v_enc_estado IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Encuentro clinico no encontrado'; LEAVE sp_emitir_factura;
    END IF;

    -- Acumular costos de medicamentos
    SELECT COALESCE(SUM(d.costo), 0) INTO v_total_med
    FROM orden_medica om
    JOIN orden_medicamento_detalle d ON d.orden_id = om.orden_id
    WHERE om.encuentro_id = p_encuentro_id;

    -- Acumular costos de procedimientos
    SELECT COALESCE(SUM(d.costo), 0) INTO v_total_proc
    FROM orden_medica om
    JOIN orden_procedimiento_detalle d ON d.orden_id = om.orden_id
    WHERE om.encuentro_id = p_encuentro_id;

    -- Acumular costos de ayudas diagnosticas
    SELECT COALESCE(SUM(d.costo), 0) INTO v_total_ayuda
    FROM orden_medica om
    JOIN orden_ayuda_diagnostica_detalle d ON d.orden_id = om.orden_id
    WHERE om.encuentro_id = p_encuentro_id;

    SET v_total = v_total_med + v_total_proc + v_total_ayuda;

    -- Calcular copago
    CALL sp_calcular_copago(p_paciente_id, p_tipo_fact_id, v_copago, v_exento, v_msg_copago);

    IF p_seguro_id IS NOT NULL THEN
        SET v_aseg = v_total - v_copago;
        IF v_aseg < 0 THEN SET v_aseg = 0; END IF;
        SET v_pac = v_copago;
    ELSE
        SET v_aseg = 0;
        SET v_pac  = v_total;
    END IF;

    START TRANSACTION;

    INSERT INTO factura (encuentro_id, paciente_id, medico_id, seguro_id, tipo_fact_id,
                         valor_total, valor_copago, valor_aseguradora, valor_paciente,
                         estado, created_by)
    VALUES (p_encuentro_id, p_paciente_id, p_medico_id, p_seguro_id, p_tipo_fact_id,
            v_total, v_copago, v_aseg, v_pac, 'pendiente', p_usuario_operador);

    SET v_factura_id = LAST_INSERT_ID();

    -- Detalle de medicamentos
    INSERT INTO factura_detalle (factura_id, tipo_item, nombre_item, dosis, cantidad, costo_unitario, costo_total)
    SELECT v_factura_id, 'medicamento', mc.nombre, d.dosis, 1, d.costo, d.costo
    FROM orden_medica om
    JOIN orden_medicamento_detalle d ON d.orden_id = om.orden_id
    JOIN medicamento_catalogo mc     ON mc.medicamento_id = d.medicamento_id
    WHERE om.encuentro_id = p_encuentro_id;

    -- Detalle de procedimientos
    INSERT INTO factura_detalle (factura_id, tipo_item, nombre_item, dosis, cantidad, costo_unitario, costo_total)
    SELECT v_factura_id, 'procedimiento', pc.nombre, NULL, d.cantidad, d.costo, (d.costo * d.cantidad)
    FROM orden_medica om
    JOIN orden_procedimiento_detalle d ON d.orden_id = om.orden_id
    JOIN procedimiento_catalogo pc     ON pc.procedimiento_id = d.procedimiento_id
    WHERE om.encuentro_id = p_encuentro_id;

    -- Detalle de ayudas diagnosticas
    INSERT INTO factura_detalle (factura_id, tipo_item, nombre_item, dosis, cantidad, costo_unitario, costo_total)
    SELECT v_factura_id, 'ayuda_diagnostica', ac.nombre, NULL, d.cantidad, d.costo, (d.costo * d.cantidad)
    FROM orden_medica om
    JOIN orden_ayuda_diagnostica_detalle d ON d.orden_id = om.orden_id
    JOIN ayuda_diagnostica_catalogo ac     ON ac.ayuda_id = d.ayuda_id
    WHERE om.encuentro_id = p_encuentro_id;

    SET o_id_generado = v_factura_id;
    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Factura emitida. ID: ', v_factura_id, '. Total: ', v_total);

    COMMIT;
END$$

-- ------------------------------------------------------------
-- sp_registrar_pago
-- Registra un pago asociado a una factura
-- ------------------------------------------------------------
CREATE PROCEDURE sp_registrar_pago(
    IN  p_factura_id     INT UNSIGNED,
    IN  p_valor_pagado   DECIMAL(14,2),
    IN  p_tipo_pago      VARCHAR(30),
    IN  p_referencia     VARCHAR(100),
    IN  p_usuario_operador INT UNSIGNED,
    OUT o_codigo         SMALLINT,
    OUT o_mensaje        VARCHAR(200),
    OUT o_id_generado    INT UNSIGNED
)
sp_registrar_pago: BEGIN
    DECLARE v_estado_fac VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 o_mensaje = MESSAGE_TEXT;
        SET o_codigo = -1; SET o_id_generado = NULL;
    END;

    SET o_codigo = 0; SET o_mensaje = ''; SET o_id_generado = NULL;

    SELECT estado INTO v_estado_fac FROM factura WHERE factura_id = p_factura_id;
    IF v_estado_fac IS NULL THEN
        SET o_codigo = 1; SET o_mensaje = 'Factura no encontrada'; LEAVE sp_registrar_pago;
    END IF;
    IF v_estado_fac = 'anulada' THEN
        SET o_codigo = 2; SET o_mensaje = 'No se puede pagar una factura anulada'; LEAVE sp_registrar_pago;
    END IF;
    IF v_estado_fac = 'pagada' THEN
        SET o_codigo = 3; SET o_mensaje = 'La factura ya esta pagada'; LEAVE sp_registrar_pago;
    END IF;
    IF p_valor_pagado <= 0 THEN
        SET o_codigo = 4; SET o_mensaje = 'El valor del pago debe ser mayor a cero'; LEAVE sp_registrar_pago;
    END IF;

    START TRANSACTION;

    -- El trigger trg_pago_bi valida el tope anual de copago
    INSERT INTO pago (factura_id, valor_pagado, tipo_pago, referencia, created_by)
    VALUES (p_factura_id, p_valor_pagado, p_tipo_pago, p_referencia, p_usuario_operador);

    SET o_id_generado = LAST_INSERT_ID();

    -- Actualizar estado de la factura si se cubre el total
    UPDATE factura f
    JOIN (
        SELECT factura_id, SUM(valor_pagado) AS total_pagado
        FROM pago WHERE factura_id = p_factura_id
        GROUP BY factura_id
    ) tp ON tp.factura_id = f.factura_id
    SET f.estado = IF(tp.total_pagado >= f.valor_total, 'pagada', 'pendiente'),
        f.updated_by = p_usuario_operador
    WHERE f.factura_id = p_factura_id;

    SET o_codigo = 0;
    SET o_mensaje = CONCAT('Pago registrado. ID: ', o_id_generado);

    COMMIT;
END$$

DELIMITER ;
