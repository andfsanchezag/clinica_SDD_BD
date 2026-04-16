-- ============================================================
-- CLINICA IPS - Catalogos y tablas de configuracion
-- Contexto DDD: Identidad y Acceso, Gestion de Pacientes,
--               Agenda, Ordenes, Facturacion
-- Orden de ejecucion: 2 de 10
-- ============================================================

USE clinica_ips;

-- ------------------------------------------------------------
-- CAT_ROL_USUARIO
-- Contexto: Identidad y Acceso
-- Representa los roles funcionales del sistema
-- ------------------------------------------------------------
CREATE TABLE cat_rol_usuario (
    rol_id      TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo      VARCHAR(30)      NOT NULL,
    descripcion VARCHAR(100)     NOT NULL,
    activo      TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (rol_id),
    UNIQUE KEY uq_rol_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: roles funcionales del sistema';

INSERT INTO cat_rol_usuario (codigo, descripcion) VALUES
    ('recursos_humanos',    'Recursos Humanos'),
    ('administrativo',      'Personal Administrativo'),
    ('soporte_informacion', 'Soporte de Informacion'),
    ('enfermeria',          'Enfermeria'),
    ('medico',              'Medico'),
    ('auditor',             'Auditor'),
    ('admin_bd',            'Administrador de Base de Datos');

-- ------------------------------------------------------------
-- CAT_TIPO_DOCUMENTO
-- Contexto: Gestion de Personal, Gestion de Pacientes
-- ------------------------------------------------------------
CREATE TABLE cat_tipo_documento (
    tipo_doc_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo      VARCHAR(20)      NOT NULL,
    descripcion VARCHAR(80)      NOT NULL,
    activo      TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (tipo_doc_id),
    UNIQUE KEY uq_tipo_doc_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: tipos de documento de identificacion';

INSERT INTO cat_tipo_documento (codigo, descripcion) VALUES
    ('cedula',           'Cedula de ciudadania'),
    ('tarjeta_identidad','Tarjeta de identidad'),
    ('pasaporte',        'Pasaporte'),
    ('extranjeria',      'Cedula de extranjeria');

-- ------------------------------------------------------------
-- CAT_GENERO
-- Contexto: Gestion de Pacientes
-- ------------------------------------------------------------
CREATE TABLE cat_genero (
    genero_id   TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo      VARCHAR(20)      NOT NULL,
    descripcion VARCHAR(50)      NOT NULL,
    activo      TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (genero_id),
    UNIQUE KEY uq_genero_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: genero del paciente o empleado';

INSERT INTO cat_genero (codigo, descripcion) VALUES
    ('masculino', 'Masculino'),
    ('femenino',  'Femenino'),
    ('otro',      'Otro');

-- ------------------------------------------------------------
-- CAT_ESTADO_CITA
-- Contexto: Agenda y Atencion
-- ------------------------------------------------------------
CREATE TABLE cat_estado_cita (
    estado_cita_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo         VARCHAR(30)      NOT NULL,
    descripcion    VARCHAR(80)      NOT NULL,
    activo         TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (estado_cita_id),
    UNIQUE KEY uq_estado_cita_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: estados del ciclo de vida de una cita';

INSERT INTO cat_estado_cita (codigo, descripcion) VALUES
    ('programada',   'Programada'),
    ('confirmada',   'Confirmada'),
    ('atendida',     'Atendida'),
    ('cancelada',    'Cancelada'),
    ('no_asistio',   'No asistio'),
    ('reprogramada', 'Reprogramada');

-- ------------------------------------------------------------
-- CAT_PRIORIDAD_ATENCION
-- Contexto: Agenda y Atencion
-- ------------------------------------------------------------
CREATE TABLE cat_prioridad_atencion (
    prioridad_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo       VARCHAR(20)      NOT NULL,
    descripcion  VARCHAR(60)      NOT NULL,
    activo       TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (prioridad_id),
    UNIQUE KEY uq_prioridad_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: prioridad de atencion en una cita';

INSERT INTO cat_prioridad_atencion (codigo, descripcion) VALUES
    ('baja',    'Baja'),
    ('media',   'Media'),
    ('alta',    'Alta'),
    ('urgente', 'Urgente');

-- ------------------------------------------------------------
-- CAT_ESTADO_SEGURO
-- Contexto: Gestion de Pacientes, Facturacion
-- ------------------------------------------------------------
CREATE TABLE cat_estado_seguro (
    estado_seguro_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo           VARCHAR(20)      NOT NULL,
    descripcion      VARCHAR(80)      NOT NULL,
    activo           TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (estado_seguro_id),
    UNIQUE KEY uq_estado_seguro_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: estado del seguro medico del paciente';

INSERT INTO cat_estado_seguro (codigo, descripcion) VALUES
    ('activo',     'Activo'),
    ('vencido',    'Vencido'),
    ('suspendido', 'Suspendido'),
    ('cancelado',  'Cancelado');

-- ------------------------------------------------------------
-- CAT_TIPO_ORDEN
-- Contexto: Ordenes Medicas
-- Regla: ayuda_diagnostica NO puede combinarse con med/proc
-- ------------------------------------------------------------
CREATE TABLE cat_tipo_orden (
    tipo_orden_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo        VARCHAR(40)      NOT NULL,
    descripcion   VARCHAR(100)     NOT NULL,
    activo        TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (tipo_orden_id),
    UNIQUE KEY uq_tipo_orden_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: tipo de contenido de una orden medica';

INSERT INTO cat_tipo_orden (codigo, descripcion) VALUES
    ('medicamento',      'Solo Medicamentos'),
    ('procedimiento',    'Solo Procedimientos'),
    ('ayuda_diagnostica','Solo Ayudas Diagnosticas'),
    ('mixta_med_proc',   'Mixta: Medicamentos y Procedimientos');

-- ------------------------------------------------------------
-- CAT_TIPO_DETALLE_ORDEN
-- Contexto: Ordenes Medicas
-- ------------------------------------------------------------
CREATE TABLE cat_tipo_detalle_orden (
    tipo_detalle_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo          VARCHAR(30)      NOT NULL,
    descripcion     VARCHAR(80)      NOT NULL,
    activo          TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (tipo_detalle_id),
    UNIQUE KEY uq_tipo_detalle_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: tipo de linea dentro de una orden medica';

INSERT INTO cat_tipo_detalle_orden (codigo, descripcion) VALUES
    ('medicamento',       'Medicamento'),
    ('procedimiento',     'Procedimiento'),
    ('ayuda_diagnostica', 'Ayuda Diagnostica');

-- ------------------------------------------------------------
-- CAT_TIPO_FACTURACION
-- Contexto: Facturacion y Seguros
-- ------------------------------------------------------------
CREATE TABLE cat_tipo_facturacion (
    tipo_fact_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo       VARCHAR(25)      NOT NULL,
    descripcion  VARCHAR(100)     NOT NULL,
    activo       TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (tipo_fact_id),
    UNIQUE KEY uq_tipo_fact_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: modalidad de facturacion aplicada al paciente';

INSERT INTO cat_tipo_facturacion (codigo, descripcion) VALUES
    ('con_poliza',    'Con Poliza Activa - aplica copago'),
    ('sin_poliza',    'Sin Poliza - pago 100% por paciente'),
    ('exento_copago', 'Exento de Copago - tope anual alcanzado');

-- ------------------------------------------------------------
-- CAT_ESTADO_ESPECIALIDAD
-- Contexto: Inventario Clinico
-- ------------------------------------------------------------
CREATE TABLE cat_estado_especialidad (
    estado_esp_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo        VARCHAR(20)      NOT NULL,
    descripcion   VARCHAR(60)      NOT NULL,
    activo        TINYINT(1)       NOT NULL DEFAULT 1,
    PRIMARY KEY (estado_esp_id),
    UNIQUE KEY uq_estado_esp_codigo (codigo)
) ENGINE = InnoDB
  COMMENT = 'Catalogo: estado operativo de una especialidad medica';

INSERT INTO cat_estado_especialidad (codigo, descripcion) VALUES
    ('activa',   'Activa'),
    ('inactiva', 'Inactiva');

-- ------------------------------------------------------------
-- CONFIG_FACTURACION
-- Contexto: Facturacion y Seguros
-- Parametros de negocio para reglas de cobro
-- ------------------------------------------------------------
CREATE TABLE config_facturacion (
    config_id       TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    parametro       VARCHAR(60)      NOT NULL,
    valor_numerico  DECIMAL(14, 2)   NOT NULL,
    descripcion     VARCHAR(200)     NOT NULL,
    PRIMARY KEY (config_id),
    UNIQUE KEY uq_config_parametro (parametro)
) ENGINE = InnoDB
  COMMENT = 'Parametros de negocio para reglas de facturacion y copago';

INSERT INTO config_facturacion (parametro, valor_numerico, descripcion) VALUES
    ('valor_copago_base',  50000.00,   'Valor fijo de copago cuando hay poliza activa'),
    ('tope_anual_copago',  1000000.00, 'Monto maximo acumulado de copagos en el anio calendario');
