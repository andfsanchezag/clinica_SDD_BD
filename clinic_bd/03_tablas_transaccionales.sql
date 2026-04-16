-- ============================================================
-- CLINICA IPS - Tablas transaccionales del dominio
-- Agregados: Cita, HistoriaClinica, OrdenMedica, Facturacion
-- Contextos: Agenda, Atencion Clinica, Enfermeria, Ordenes, Facturacion
-- Orden de ejecucion: 4 de 10
-- Dependencias: 02_tablas_maestras.sql
-- ============================================================

USE clinica_ips;

-- ------------------------------------------------------------
-- CITA
-- Agregado raiz: Cita
-- Contexto: Agenda y Atencion
-- Reglas: medico activo, sin solapamiento, estado inicial = programada
-- ------------------------------------------------------------
CREATE TABLE cita (
    cita_id        INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    paciente_id    INT UNSIGNED     NOT NULL,
    medico_id      INT UNSIGNED     NOT NULL,
    fecha_hora     DATETIME         NOT NULL,
    estado_cita_id TINYINT UNSIGNED NOT NULL DEFAULT 1,
    prioridad_id   TINYINT UNSIGNED NOT NULL DEFAULT 2,
    motivo         VARCHAR(300)     NOT NULL,
    created_at     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     INT UNSIGNED     NULL,
    updated_at     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by     INT UNSIGNED     NULL,
    PRIMARY KEY (cita_id),
    INDEX idx_cita_medico_fecha (medico_id, fecha_hora),
    INDEX idx_cita_paciente     (paciente_id),
    CONSTRAINT fk_cita_paciente    FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT fk_cita_medico      FOREIGN KEY (medico_id)
        REFERENCES medico_perfil (medico_id),
    CONSTRAINT fk_cita_estado      FOREIGN KEY (estado_cita_id)
        REFERENCES cat_estado_cita (estado_cita_id),
    CONSTRAINT fk_cita_prioridad   FOREIGN KEY (prioridad_id)
        REFERENCES cat_prioridad_atencion (prioridad_id)
) ENGINE = InnoDB
  COMMENT = 'Entidad raiz del agregado Cita';

-- ------------------------------------------------------------
-- ENCUENTRO_CLINICO
-- Agregado raiz: HistoriaClinica
-- Contexto: Atencion Clinica
-- Historia clinica modelada como encuentros (reemplaza NoSQL)
-- Clave conceptual: paciente + fecha (cedula_paciente + fecha en enunciado)
-- Reglas: requiere paciente y medico, no cerrable sin diagnostico
-- ------------------------------------------------------------
CREATE TABLE encuentro_clinico (
    encuentro_id     INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    cita_id          INT UNSIGNED  NULL,
    paciente_id      INT UNSIGNED  NOT NULL,
    medico_id        INT UNSIGNED  NOT NULL,
    fecha_encuentro  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo_consulta  TEXT          NOT NULL,
    sintomatologia   TEXT          NOT NULL,
    diagnostico      TEXT          NULL,
    tratamiento      TEXT          NULL,
    observaciones    TEXT          NULL,
    estado           VARCHAR(20)   NOT NULL DEFAULT 'abierto',
    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       INT UNSIGNED  NULL,
    updated_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       INT UNSIGNED  NULL,
    PRIMARY KEY (encuentro_id),
    INDEX idx_enc_paciente_fecha (paciente_id, fecha_encuentro),
    INDEX idx_enc_medico         (medico_id),
    CONSTRAINT fk_enc_cita      FOREIGN KEY (cita_id)
        REFERENCES cita (cita_id),
    CONSTRAINT fk_enc_paciente  FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT fk_enc_medico    FOREIGN KEY (medico_id)
        REFERENCES medico_perfil (medico_id),
    CONSTRAINT chk_enc_estado   CHECK (estado IN ('abierto','cerrado'))
) ENGINE = InnoDB
  COMMENT = 'Registro de cada encuentro clinico o consulta (historia clinica relacional)';

-- ------------------------------------------------------------
-- SIGNO_VITAL
-- Entidad interna del agregado HistoriaClinica
-- Contexto: Enfermeria
-- ------------------------------------------------------------
CREATE TABLE signo_vital (
    signo_id        INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    encuentro_id    INT UNSIGNED   NOT NULL,
    enfermero_id    INT UNSIGNED   NOT NULL,
    presion_arterial VARCHAR(20)   NOT NULL,
    temperatura     DECIMAL(4, 1)  NOT NULL,
    pulso           SMALLINT       NOT NULL,
    oxigeno         DECIMAL(5, 2)  NOT NULL,
    fecha_registro  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED   NULL,
    PRIMARY KEY (signo_id),
    INDEX idx_sv_encuentro (encuentro_id),
    CONSTRAINT fk_sv_encuentro  FOREIGN KEY (encuentro_id)
        REFERENCES encuentro_clinico (encuentro_id),
    CONSTRAINT fk_sv_enfermero  FOREIGN KEY (enfermero_id)
        REFERENCES enfermero_perfil (enfermero_id),
    CONSTRAINT chk_sv_temperatura CHECK (temperatura BETWEEN 30.0 AND 45.0),
    CONSTRAINT chk_sv_pulso       CHECK (pulso BETWEEN 20 AND 300),
    CONSTRAINT chk_sv_oxigeno     CHECK (oxigeno BETWEEN 50.00 AND 100.00)
) ENGINE = InnoDB
  COMMENT = 'Registro de signos vitales tomados por enfermeria en cada encuentro';

-- ------------------------------------------------------------
-- ADMINISTRACION_MEDICAMENTO
-- Entidad interna del agregado HistoriaClinica
-- Contexto: Enfermeria
-- ------------------------------------------------------------
CREATE TABLE administracion_medicamento (
    admin_med_id        INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    encuentro_id        INT UNSIGNED  NOT NULL,
    enfermero_id        INT UNSIGNED  NOT NULL,
    medicamento_id      INT UNSIGNED  NOT NULL,
    dosis               VARCHAR(100)  NOT NULL,
    observacion         TEXT          NULL,
    fecha_administracion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          INT UNSIGNED  NULL,
    PRIMARY KEY (admin_med_id),
    CONSTRAINT fk_am_encuentro   FOREIGN KEY (encuentro_id)
        REFERENCES encuentro_clinico (encuentro_id),
    CONSTRAINT fk_am_enfermero   FOREIGN KEY (enfermero_id)
        REFERENCES enfermero_perfil (enfermero_id),
    CONSTRAINT fk_am_medicamento FOREIGN KEY (medicamento_id)
        REFERENCES medicamento_catalogo (medicamento_id)
) ENGINE = InnoDB
  COMMENT = 'Medicamentos administrados por enfermeria durante el encuentro';

-- ------------------------------------------------------------
-- PROCEDIMIENTO_ENFERMERIA
-- Entidad interna del agregado HistoriaClinica
-- Contexto: Enfermeria
-- Incluye intervenciones de enfermeria (per enunciado)
-- ------------------------------------------------------------
CREATE TABLE procedimiento_enfermeria (
    proc_enf_id      INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    encuentro_id     INT UNSIGNED  NOT NULL,
    enfermero_id     INT UNSIGNED  NOT NULL,
    procedimiento_id INT UNSIGNED  NOT NULL,
    observacion      TEXT          NULL,
    fecha_proc       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       INT UNSIGNED  NULL,
    PRIMARY KEY (proc_enf_id),
    CONSTRAINT fk_pe_encuentro    FOREIGN KEY (encuentro_id)
        REFERENCES encuentro_clinico (encuentro_id),
    CONSTRAINT fk_pe_enfermero    FOREIGN KEY (enfermero_id)
        REFERENCES enfermero_perfil (enfermero_id),
    CONSTRAINT fk_pe_procedimiento FOREIGN KEY (procedimiento_id)
        REFERENCES procedimiento_catalogo (procedimiento_id)
) ENGINE = InnoDB
  COMMENT = 'Procedimientos e intervenciones realizadas por enfermeria';

-- ------------------------------------------------------------
-- ORDEN_MEDICA
-- Agregado raiz: OrdenMedica
-- Contexto: Ordenes Medicas
-- Reglas: numero_orden unico max 6 digitos, orden unica
-- ------------------------------------------------------------
CREATE TABLE orden_medica (
    orden_id      INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    numero_orden  VARCHAR(6)       NOT NULL,
    encuentro_id  INT UNSIGNED     NOT NULL,
    paciente_id   INT UNSIGNED     NOT NULL,
    medico_id     INT UNSIGNED     NOT NULL,
    tipo_orden_id TINYINT UNSIGNED NOT NULL,
    fecha_orden   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at    DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    INT UNSIGNED     NULL,
    PRIMARY KEY (orden_id),
    UNIQUE KEY uq_orden_numero (numero_orden),
    INDEX idx_ord_paciente   (paciente_id),
    INDEX idx_ord_encuentro  (encuentro_id),
    CONSTRAINT fk_om_encuentro  FOREIGN KEY (encuentro_id)
        REFERENCES encuentro_clinico (encuentro_id),
    CONSTRAINT fk_om_paciente   FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT fk_om_medico     FOREIGN KEY (medico_id)
        REFERENCES medico_perfil (medico_id),
    CONSTRAINT fk_om_tipo_orden FOREIGN KEY (tipo_orden_id)
        REFERENCES cat_tipo_orden (tipo_orden_id),
    CONSTRAINT chk_om_numero_orden CHECK (numero_orden REGEXP '^[0-9]{1,6}$')
) ENGINE = InnoDB
  COMMENT = 'Cabecera de orden medica emitida por medico';

-- ------------------------------------------------------------
-- ORDEN_MEDICAMENTO_DETALLE
-- Entidad interna del agregado OrdenMedica
-- Contexto: Ordenes Medicas
-- Reglas: item >= 1, item unico por orden
-- ------------------------------------------------------------
CREATE TABLE orden_medicamento_detalle (
    detalle_id     INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    orden_id       INT UNSIGNED   NOT NULL,
    item           SMALLINT       NOT NULL,
    medicamento_id INT UNSIGNED   NOT NULL,
    dosis          VARCHAR(150)   NOT NULL,
    duracion       VARCHAR(100)   NOT NULL,
    costo          DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     INT UNSIGNED   NULL,
    PRIMARY KEY (detalle_id),
    UNIQUE KEY uq_omd_orden_item (orden_id, item),
    CONSTRAINT fk_omd_orden       FOREIGN KEY (orden_id)
        REFERENCES orden_medica (orden_id),
    CONSTRAINT fk_omd_medicamento FOREIGN KEY (medicamento_id)
        REFERENCES medicamento_catalogo (medicamento_id),
    CONSTRAINT chk_omd_item  CHECK (item >= 1),
    CONSTRAINT chk_omd_costo CHECK (costo >= 0)
) ENGINE = InnoDB
  COMMENT = 'Detalle de medicamentos en una orden medica';

-- ------------------------------------------------------------
-- ORDEN_PROCEDIMIENTO_DETALLE
-- Entidad interna del agregado OrdenMedica
-- Contexto: Ordenes Medicas
-- Reglas: item >= 1, si requiere_especialista entonces especialidad_id != NULL
-- ------------------------------------------------------------
CREATE TABLE orden_procedimiento_detalle (
    detalle_id       INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    orden_id         INT UNSIGNED   NOT NULL,
    item             SMALLINT       NOT NULL,
    procedimiento_id INT UNSIGNED   NOT NULL,
    cantidad         SMALLINT       NOT NULL DEFAULT 1,
    frecuencia       VARCHAR(100)   NOT NULL,
    requiere_especialista TINYINT(1) NOT NULL DEFAULT 0,
    especialidad_id  INT UNSIGNED   NULL,
    costo            DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       INT UNSIGNED   NULL,
    PRIMARY KEY (detalle_id),
    UNIQUE KEY uq_opd_orden_item (orden_id, item),
    CONSTRAINT fk_opd_orden        FOREIGN KEY (orden_id)
        REFERENCES orden_medica (orden_id),
    CONSTRAINT fk_opd_procedimiento FOREIGN KEY (procedimiento_id)
        REFERENCES procedimiento_catalogo (procedimiento_id),
    CONSTRAINT fk_opd_especialidad  FOREIGN KEY (especialidad_id)
        REFERENCES especialidad (especialidad_id),
    CONSTRAINT chk_opd_item     CHECK (item >= 1),
    CONSTRAINT chk_opd_cantidad CHECK (cantidad >= 1),
    CONSTRAINT chk_opd_costo    CHECK (costo >= 0)
) ENGINE = InnoDB
  COMMENT = 'Detalle de procedimientos en una orden medica';

-- ------------------------------------------------------------
-- ORDEN_AYUDA_DIAGNOSTICA_DETALLE
-- Entidad interna del agregado OrdenMedica
-- Contexto: Ordenes Medicas
-- Regla: NO puede coexistir con medicamentos ni procedimientos en la misma orden
-- ------------------------------------------------------------
CREATE TABLE orden_ayuda_diagnostica_detalle (
    detalle_id      INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    orden_id        INT UNSIGNED   NOT NULL,
    item            SMALLINT       NOT NULL,
    ayuda_id        INT UNSIGNED   NOT NULL,
    cantidad        SMALLINT       NOT NULL DEFAULT 1,
    requiere_especialista TINYINT(1) NOT NULL DEFAULT 0,
    especialidad_id INT UNSIGNED   NULL,
    costo           DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      INT UNSIGNED   NULL,
    PRIMARY KEY (detalle_id),
    UNIQUE KEY uq_oad_orden_item (orden_id, item),
    CONSTRAINT fk_oad_orden       FOREIGN KEY (orden_id)
        REFERENCES orden_medica (orden_id),
    CONSTRAINT fk_oad_ayuda       FOREIGN KEY (ayuda_id)
        REFERENCES ayuda_diagnostica_catalogo (ayuda_id),
    CONSTRAINT fk_oad_especialidad FOREIGN KEY (especialidad_id)
        REFERENCES especialidad (especialidad_id),
    CONSTRAINT chk_oad_item     CHECK (item >= 1),
    CONSTRAINT chk_oad_cantidad CHECK (cantidad >= 1),
    CONSTRAINT chk_oad_costo    CHECK (costo >= 0)
) ENGINE = InnoDB
  COMMENT = 'Detalle de ayudas diagnosticas en una orden medica';

-- ------------------------------------------------------------
-- FACTURA
-- Agregado raiz: Facturacion
-- Contexto: Facturacion y Seguros
-- Reglas: requiere paciente y medico, valor_copago segun poliza
-- ------------------------------------------------------------
CREATE TABLE factura (
    factura_id          INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    encuentro_id        INT UNSIGNED     NOT NULL,
    paciente_id         INT UNSIGNED     NOT NULL,
    medico_id           INT UNSIGNED     NOT NULL,
    seguro_id           INT UNSIGNED     NULL,
    tipo_fact_id        TINYINT UNSIGNED NOT NULL,
    valor_total         DECIMAL(14, 2)   NOT NULL DEFAULT 0.00,
    valor_copago        DECIMAL(14, 2)   NOT NULL DEFAULT 0.00,
    valor_aseguradora   DECIMAL(14, 2)   NOT NULL DEFAULT 0.00,
    valor_paciente      DECIMAL(14, 2)   NOT NULL DEFAULT 0.00,
    fecha_factura       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado              VARCHAR(20)      NOT NULL DEFAULT 'pendiente',
    created_at          DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          INT UNSIGNED     NULL,
    updated_at          DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by          INT UNSIGNED     NULL,
    PRIMARY KEY (factura_id),
    INDEX idx_fac_paciente     (paciente_id),
    INDEX idx_fac_fecha_estado (fecha_factura, estado),
    CONSTRAINT fk_fac_encuentro  FOREIGN KEY (encuentro_id)
        REFERENCES encuentro_clinico (encuentro_id),
    CONSTRAINT fk_fac_paciente   FOREIGN KEY (paciente_id)
        REFERENCES paciente (paciente_id),
    CONSTRAINT fk_fac_medico     FOREIGN KEY (medico_id)
        REFERENCES medico_perfil (medico_id),
    CONSTRAINT fk_fac_seguro     FOREIGN KEY (seguro_id)
        REFERENCES seguro_medico (seguro_id),
    CONSTRAINT fk_fac_tipo_fact  FOREIGN KEY (tipo_fact_id)
        REFERENCES cat_tipo_facturacion (tipo_fact_id),
    CONSTRAINT chk_fac_estado CHECK (estado IN ('pendiente','pagada','anulada')),
    CONSTRAINT chk_fac_valores CHECK (valor_total >= 0 AND valor_copago >= 0)
) ENGINE = InnoDB
  COMMENT = 'Cabecera de factura generada a partir de un encuentro clinico';

-- ------------------------------------------------------------
-- FACTURA_DETALLE
-- Entidad interna del agregado Facturacion
-- Contexto: Facturacion y Seguros
-- Representa la linea de cobro por medicamento, procedimiento o examen
-- ------------------------------------------------------------
CREATE TABLE factura_detalle (
    det_factura_id  INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    factura_id      INT UNSIGNED   NOT NULL,
    tipo_item       VARCHAR(30)    NOT NULL,
    nombre_item     VARCHAR(150)   NOT NULL,
    dosis           VARCHAR(150)   NULL,
    cantidad        SMALLINT       NOT NULL DEFAULT 1,
    costo_unitario  DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    costo_total     DECIMAL(14, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (det_factura_id),
    CONSTRAINT fk_fd_factura FOREIGN KEY (factura_id)
        REFERENCES factura (factura_id),
    CONSTRAINT chk_fd_tipo CHECK (
        tipo_item IN ('medicamento','procedimiento','ayuda_diagnostica')
    ),
    CONSTRAINT chk_fd_cantidad     CHECK (cantidad >= 1),
    CONSTRAINT chk_fd_costo_unit   CHECK (costo_unitario >= 0),
    CONSTRAINT chk_fd_costo_total  CHECK (costo_total >= 0)
) ENGINE = InnoDB
  COMMENT = 'Lineas de detalle de cada factura clinica';

-- ------------------------------------------------------------
-- PAGO
-- Entidad interna del agregado Facturacion
-- Contexto: Facturacion y Seguros
-- ------------------------------------------------------------
CREATE TABLE pago (
    pago_id      INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    factura_id   INT UNSIGNED   NOT NULL,
    valor_pagado DECIMAL(14, 2) NOT NULL,
    tipo_pago    VARCHAR(30)    NOT NULL,
    referencia   VARCHAR(100)   NULL,
    fecha_pago   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   INT UNSIGNED   NULL,
    PRIMARY KEY (pago_id),
    INDEX idx_pago_factura (factura_id),
    CONSTRAINT fk_pago_factura FOREIGN KEY (factura_id)
        REFERENCES factura (factura_id),
    CONSTRAINT chk_pago_tipo CHECK (
        tipo_pago IN ('copago','aseguradora','paciente_total','ajuste')
    ),
    CONSTRAINT chk_pago_valor CHECK (valor_pagado > 0)
) ENGINE = InnoDB
  COMMENT = 'Registro de pagos y copagos aplicados a una factura';
