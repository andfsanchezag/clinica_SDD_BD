-- ============================================================
-- CLINICA IPS - Tablas maestras del dominio
-- Agregados: Empleado, Paciente, Inventario Clinico
-- Orden de ejecucion: 3 de 10
-- Dependencias: 01_catalogos.sql
-- ============================================================

USE clinica_ips;

-- ------------------------------------------------------------
-- ESPECIALIDAD
-- Contexto: Inventario Clinico
-- Catalogo operativo de especialidades medicas
-- ------------------------------------------------------------
CREATE TABLE especialidad (
    especialidad_id INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100)     NOT NULL,
    descripcion     VARCHAR(200)     NULL,
    estado_esp_id   TINYINT UNSIGNED NOT NULL DEFAULT 1,
    created_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED     NULL,
    updated_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      INT UNSIGNED     NULL,
    PRIMARY KEY (especialidad_id),
    UNIQUE KEY uq_especialidad_nombre (nombre),
    CONSTRAINT fk_esp_estado FOREIGN KEY (estado_esp_id)
        REFERENCES cat_estado_especialidad (estado_esp_id)
) ENGINE = InnoDB
  COMMENT = 'Especialidades medicas disponibles en la clinica';

-- ------------------------------------------------------------
-- EMPLEADO
-- Agregado raiz: Empleado
-- Contexto: Gestion de Personal
-- Reglas: cedula unica, correo valido, max 150 anos,
--         telefono 1-10 digitos, direccion max 30 chars
-- ------------------------------------------------------------
CREATE TABLE empleado (
    empleado_id     INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    cedula          VARCHAR(20)      NOT NULL,
    tipo_doc_id     TINYINT UNSIGNED NOT NULL,
    nombre_completo VARCHAR(150)     NOT NULL,
    correo          VARCHAR(120)     NOT NULL,
    telefono        VARCHAR(10)      NOT NULL,
    fecha_nacimiento DATE            NOT NULL,
    direccion       VARCHAR(30)      NOT NULL,
    rol_id          TINYINT UNSIGNED NOT NULL,
    active_flag     TINYINT(1)       NOT NULL DEFAULT 1,
    created_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED     NULL,
    updated_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      INT UNSIGNED     NULL,
    deleted_at      DATETIME         NULL,
    PRIMARY KEY (empleado_id),
    UNIQUE KEY uq_empleado_cedula (cedula),
    CONSTRAINT fk_emp_tipo_doc FOREIGN KEY (tipo_doc_id)
        REFERENCES cat_tipo_documento (tipo_doc_id),
    CONSTRAINT fk_emp_rol      FOREIGN KEY (rol_id)
        REFERENCES cat_rol_usuario (rol_id),
    CONSTRAINT chk_emp_direccion  CHECK (CHAR_LENGTH(direccion) <= 30),
    CONSTRAINT chk_emp_telefono   CHECK (telefono REGEXP '^[0-9]{1,10}$'),
    CONSTRAINT chk_emp_correo     CHECK (correo REGEXP '^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$')
) ENGINE = InnoDB
  COMMENT = 'Entidad raiz del agregado Empleado';

-- ------------------------------------------------------------
-- SEGURIDAD_USUARIO
-- Entidad interna del agregado Empleado
-- Contexto: Identidad y Acceso
-- Reglas: codigo_usuario unico max 15 chars alfanumerico,
--         contrasena almacenada como hash
-- ------------------------------------------------------------
CREATE TABLE seguridad_usuario (
    usuario_id      INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    empleado_id     INT UNSIGNED  NOT NULL,
    codigo_usuario  VARCHAR(15)   NOT NULL,
    contrasena_hash VARCHAR(255)  NOT NULL,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED  NULL,
    updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      INT UNSIGNED  NULL,
    PRIMARY KEY (usuario_id),
    UNIQUE KEY uq_usuario_empleado      (empleado_id),
    UNIQUE KEY uq_usuario_codigo        (codigo_usuario),
    CONSTRAINT fk_su_empleado FOREIGN KEY (empleado_id)
        REFERENCES empleado (empleado_id),
    CONSTRAINT chk_su_codigo CHECK (
        codigo_usuario REGEXP '^[a-zA-Z0-9]{1,15}$'
    )
) ENGINE = InnoDB
  COMMENT = 'Credenciales de acceso al sistema por empleado';

-- ------------------------------------------------------------
-- MEDICO_PERFIL
-- Entidad interna del agregado Empleado (rol medico)
-- Contexto: Gestion de Personal, Atencion Clinica
-- ------------------------------------------------------------
CREATE TABLE medico_perfil (
    medico_id       INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    empleado_id     INT UNSIGNED  NOT NULL,
    especialidad_id INT UNSIGNED  NULL,
    registro_medico VARCHAR(30)   NOT NULL,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,
    PRIMARY KEY (medico_id),
    UNIQUE KEY uq_medico_empleado        (empleado_id),
    UNIQUE KEY uq_medico_registro        (registro_medico),
    CONSTRAINT fk_mp_empleado    FOREIGN KEY (empleado_id)
        REFERENCES empleado (empleado_id),
    CONSTRAINT fk_mp_especialidad FOREIGN KEY (especialidad_id)
        REFERENCES especialidad (especialidad_id)
) ENGINE = InnoDB
  COMMENT = 'Perfil funcional del empleado con rol medico';

-- ------------------------------------------------------------
-- ENFERMERO_PERFIL
-- Entidad interna del agregado Empleado (rol enfermeria)
-- Contexto: Enfermeria
-- ------------------------------------------------------------
CREATE TABLE enfermero_perfil (
    enfermero_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    empleado_id  INT UNSIGNED NOT NULL,
    activo       TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (enfermero_id),
    UNIQUE KEY uq_enfermero_empleado (empleado_id),
    CONSTRAINT fk_ep_empleado FOREIGN KEY (empleado_id)
        REFERENCES empleado (empleado_id)
) ENGINE = InnoDB
  COMMENT = 'Perfil funcional del empleado con rol enfermeria';

-- ------------------------------------------------------------
-- PACIENTE
-- Agregado raiz: Paciente
-- Contexto: Gestion de Pacientes
-- Reglas: cedula unica, max 150 anos, telefono 10 digitos,
--         correo opcional con formato valido
-- ------------------------------------------------------------
CREATE TABLE paciente (
    paciente_id     INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    cedula          VARCHAR(20)      NOT NULL,
    tipo_doc_id     TINYINT UNSIGNED NOT NULL,
    nombre_completo VARCHAR(150)     NOT NULL,
    fecha_nacimiento DATE            NOT NULL,
    genero_id       TINYINT UNSIGNED NOT NULL,
    direccion       VARCHAR(200)     NOT NULL,
    telefono        VARCHAR(10)      NOT NULL,
    correo          VARCHAR(120)     NULL,
    active_flag     TINYINT(1)       NOT NULL DEFAULT 1,
    created_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED     NULL,
    updated_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      INT UNSIGNED     NULL,
    deleted_at      DATETIME         NULL,
    PRIMARY KEY (paciente_id),
    UNIQUE KEY uq_paciente_cedula (cedula),
    CONSTRAINT fk_pac_tipo_doc FOREIGN KEY (tipo_doc_id)
        REFERENCES cat_tipo_documento (tipo_doc_id),
    CONSTRAINT fk_pac_genero   FOREIGN KEY (genero_id)
        REFERENCES cat_genero (genero_id),
    CONSTRAINT chk_pac_telefono CHECK (telefono REGEXP '^[0-9]{10}$'),
    CONSTRAINT chk_pac_correo   CHECK (
        correo IS NULL OR
        correo REGEXP '^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$'
    )
) ENGINE = InnoDB
  COMMENT = 'Entidad raiz del agregado Paciente';

-- ------------------------------------------------------------
-- CONTACTO_EMERGENCIA
-- Entidad interna del agregado Paciente (solo uno activo)
-- Contexto: Gestion de Pacientes
-- Reglas: un solo registro activo por paciente,
--         telefono 10 digitos, relacion obligatoria
-- ------------------------------------------------------------
CREATE TABLE contacto_emergencia (
    contacto_id     INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    paciente_id     INT UNSIGNED  NOT NULL,
    nombre_completo VARCHAR(150)  NOT NULL,
    relacion        VARCHAR(50)   NOT NULL,
    telefono        VARCHAR(10)   NOT NULL,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED  NULL,
    PRIMARY KEY (contacto_id),
    CONSTRAINT fk_ce_paciente FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT chk_ce_telefono  CHECK (telefono REGEXP '^[0-9]{10}$'),
    CONSTRAINT chk_ce_relacion  CHECK (TRIM(relacion) != '')
) ENGINE = InnoDB
  COMMENT = 'Contacto de emergencia del paciente (uno activo por paciente)';

-- ------------------------------------------------------------
-- SEGURO_MEDICO
-- Entidad interna del agregado Paciente (uno activo)
-- Contexto: Gestion de Pacientes, Facturacion
-- Reglas: un solo registro activo por paciente,
--         poliza unica, vigencia no menor a fecha de registro
-- ------------------------------------------------------------
CREATE TABLE seguro_medico (
    seguro_id        INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    paciente_id      INT UNSIGNED     NOT NULL,
    compania         VARCHAR(120)     NOT NULL,
    numero_poliza    VARCHAR(60)      NOT NULL,
    estado_seguro_id TINYINT UNSIGNED NOT NULL,
    fecha_vigencia   DATE             NOT NULL,
    activo           TINYINT(1)       NOT NULL DEFAULT 1,
    created_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       INT UNSIGNED     NULL,
    updated_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       INT UNSIGNED     NULL,
    PRIMARY KEY (seguro_id),
    UNIQUE KEY uq_seguro_poliza (numero_poliza),
    CONSTRAINT fk_sm_paciente      FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT fk_sm_estado_seguro FOREIGN KEY (estado_seguro_id)
        REFERENCES cat_estado_seguro (estado_seguro_id)
) ENGINE = InnoDB
  COMMENT = 'Seguro medico activo del paciente (uno activo por paciente)';

-- ------------------------------------------------------------
-- MEDICAMENTO_CATALOGO
-- Contexto: Inventario Clinico
-- Gestionado por: Soporte de Informacion
-- ------------------------------------------------------------
CREATE TABLE medicamento_catalogo (
    medicamento_id INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    codigo         VARCHAR(30)    NOT NULL,
    nombre         VARCHAR(150)   NOT NULL,
    descripcion    VARCHAR(300)   NULL,
    costo_base     DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    activo         TINYINT(1)     NOT NULL DEFAULT 1,
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     INT UNSIGNED   NULL,
    updated_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (medicamento_id),
    UNIQUE KEY uq_med_codigo (codigo),
    CONSTRAINT chk_med_costo CHECK (costo_base >= 0)
) ENGINE = InnoDB
  COMMENT = 'Inventario maestro de medicamentos disponibles';

-- ------------------------------------------------------------
-- PROCEDIMIENTO_CATALOGO
-- Contexto: Inventario Clinico
-- Hospitalización = procedimiento (per enunciado)
-- ------------------------------------------------------------
CREATE TABLE procedimiento_catalogo (
    procedimiento_id INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    codigo           VARCHAR(30)      NOT NULL,
    nombre           VARCHAR(150)     NOT NULL,
    descripcion      VARCHAR(300)     NULL,
    especialidad_id  INT UNSIGNED     NULL,
    costo_base       DECIMAL(12, 2)   NOT NULL DEFAULT 0.00,
    activo           TINYINT(1)       NOT NULL DEFAULT 1,
    created_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       INT UNSIGNED     NULL,
    updated_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (procedimiento_id),
    UNIQUE KEY uq_proc_codigo (codigo),
    CONSTRAINT fk_proc_especialidad FOREIGN KEY (especialidad_id)
        REFERENCES especialidad (especialidad_id),
    CONSTRAINT chk_proc_costo CHECK (costo_base >= 0)
) ENGINE = InnoDB
  COMMENT = 'Inventario maestro de procedimientos (incluye hospitalizacion)';

-- ------------------------------------------------------------
-- AYUDA_DIAGNOSTICA_CATALOGO
-- Contexto: Inventario Clinico
-- Nota: NO se mezcla con medicamentos o procedimientos en una misma orden
-- ------------------------------------------------------------
CREATE TABLE ayuda_diagnostica_catalogo (
    ayuda_id        INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    codigo          VARCHAR(30)    NOT NULL,
    nombre          VARCHAR(150)   NOT NULL,
    descripcion     VARCHAR(300)   NULL,
    especialidad_id INT UNSIGNED   NULL,
    costo_base      DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    activo          TINYINT(1)     NOT NULL DEFAULT 1,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED   NULL,
    updated_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ayuda_id),
    UNIQUE KEY uq_ayuda_codigo (codigo),
    CONSTRAINT fk_ayuda_especialidad FOREIGN KEY (especialidad_id)
        REFERENCES especialidad (especialidad_id),
    CONSTRAINT chk_ayuda_costo CHECK (costo_base >= 0)
) ENGINE = InnoDB
  COMMENT = 'Inventario maestro de examenes y ayudas diagnosticas';
