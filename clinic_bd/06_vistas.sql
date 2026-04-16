-- ============================================================
-- CLINICA IPS - Vistas de consulta y reportes
-- Principio: las vistas son la capa de presentacion en SDD
-- Orden de ejecucion: 7 de 10
-- Dependencias: 05_procedimientos.sql
-- ============================================================

USE clinica_ips;

-- ------------------------------------------------------------
-- vw_paciente_resumen
-- Resumen de datos personales, seguro activo y contacto activo
-- Contexto: Gestion de Pacientes
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_paciente_resumen AS
SELECT
    p.paciente_id,
    p.cedula,
    td.descripcion                          AS tipo_documento,
    p.nombre_completo,
    p.fecha_nacimiento,
    TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) AS edad_anios,
    g.descripcion                           AS genero,
    p.direccion,
    p.telefono,
    p.correo,
    p.active_flag,
    sm.seguro_id,
    sm.compania                             AS seguro_compania,
    sm.numero_poliza,
    sm.fecha_vigencia                       AS seguro_vigencia,
    es.descripcion                          AS estado_seguro,
    ce.nombre_completo                      AS contacto_nombre,
    ce.relacion                             AS contacto_relacion,
    ce.telefono                             AS contacto_telefono
FROM paciente p
LEFT JOIN cat_tipo_documento td ON td.tipo_doc_id = p.tipo_doc_id
LEFT JOIN cat_genero g          ON g.genero_id    = p.genero_id
LEFT JOIN seguro_medico sm      ON sm.paciente_id = p.paciente_id AND sm.activo = 1
LEFT JOIN cat_estado_seguro es  ON es.estado_seguro_id = sm.estado_seguro_id
LEFT JOIN contacto_emergencia ce ON ce.paciente_id = p.paciente_id AND ce.activo = 1;

-- ------------------------------------------------------------
-- vw_factura_resumen
-- Vista consolidada de facturas con datos de pago
-- Contexto: Facturacion y Seguros
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_factura_resumen AS
SELECT
    f.factura_id,
    f.fecha_factura,
    p.cedula                                AS cedula_paciente,
    p.nombre_completo                       AS nombre_paciente,
    emp.nombre_completo                     AS nombre_medico,
    sm.compania                             AS seguro_compania,
    sm.numero_poliza,
    tf.descripcion                          AS tipo_facturacion,
    f.valor_total,
    f.valor_copago,
    f.valor_aseguradora,
    f.valor_paciente,
    f.estado,
    COALESCE(SUM(pg.valor_pagado), 0)       AS total_pagado,
    f.valor_total - COALESCE(SUM(pg.valor_pagado), 0) AS saldo_pendiente
FROM factura f
JOIN paciente p         ON p.paciente_id    = f.paciente_id
JOIN medico_perfil mp   ON mp.medico_id     = f.medico_id
JOIN empleado emp       ON emp.empleado_id  = mp.empleado_id
LEFT JOIN seguro_medico sm     ON sm.seguro_id     = f.seguro_id
LEFT JOIN cat_tipo_facturacion tf ON tf.tipo_fact_id = f.tipo_fact_id
LEFT JOIN pago pg       ON pg.factura_id    = f.factura_id
GROUP BY
    f.factura_id, f.fecha_factura, p.cedula, p.nombre_completo,
    emp.nombre_completo, sm.compania, sm.numero_poliza,
    tf.descripcion, f.valor_total, f.valor_copago,
    f.valor_aseguradora, f.valor_paciente, f.estado;

-- ------------------------------------------------------------
-- vw_orden_resumen
-- Resumen de ordenes medicas con conteo de items
-- Contexto: Ordenes Medicas
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_orden_resumen AS
SELECT
    om.orden_id,
    om.numero_orden,
    om.fecha_orden,
    p.cedula                                AS cedula_paciente,
    p.nombre_completo                       AS nombre_paciente,
    emp.nombre_completo                     AS nombre_medico,
    co.descripcion                          AS tipo_orden,
    ec.encuentro_id,
    ec.fecha_encuentro,
    (
        SELECT COUNT(*) FROM orden_medicamento_detalle d WHERE d.orden_id = om.orden_id
    )                                       AS items_medicamentos,
    (
        SELECT COUNT(*) FROM orden_procedimiento_detalle d WHERE d.orden_id = om.orden_id
    )                                       AS items_procedimientos,
    (
        SELECT COUNT(*) FROM orden_ayuda_diagnostica_detalle d WHERE d.orden_id = om.orden_id
    )                                       AS items_ayuda_diagnostica
FROM orden_medica om
JOIN paciente p            ON p.paciente_id   = om.paciente_id
JOIN medico_perfil mp      ON mp.medico_id    = om.medico_id
JOIN empleado emp          ON emp.empleado_id = mp.empleado_id
JOIN cat_tipo_orden co     ON co.tipo_orden_id = om.tipo_orden_id
JOIN encuentro_clinico ec  ON ec.encuentro_id = om.encuentro_id;

-- ------------------------------------------------------------
-- vw_historia_clinica_por_paciente
-- Historia clinica completa: encuentros, diagnosticos, ordenes
-- Contexto: Atencion Clinica
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_historia_clinica_por_paciente AS
SELECT
    p.paciente_id,
    p.cedula,
    p.nombre_completo                       AS nombre_paciente,
    ec.encuentro_id,
    ec.fecha_encuentro,
    ec.motivo_consulta,
    ec.sintomatologia,
    ec.diagnostico,
    ec.tratamiento,
    ec.observaciones,
    ec.estado                               AS estado_encuentro,
    emp.nombre_completo                     AS nombre_medico,
    esp.nombre                              AS especialidad_medico,
    (
        SELECT COUNT(*) FROM signo_vital sv WHERE sv.encuentro_id = ec.encuentro_id
    )                                       AS num_registros_signos_vitales,
    (
        SELECT COUNT(*) FROM orden_medica om WHERE om.encuentro_id = ec.encuentro_id
    )                                       AS num_ordenes
FROM encuentro_clinico ec
JOIN paciente p         ON p.paciente_id   = ec.paciente_id
JOIN medico_perfil mp   ON mp.medico_id    = ec.medico_id
JOIN empleado emp       ON emp.empleado_id = mp.empleado_id
LEFT JOIN especialidad esp ON esp.especialidad_id = mp.especialidad_id
ORDER BY ec.paciente_id, ec.fecha_encuentro DESC;

-- ------------------------------------------------------------
-- vw_inventario_operativo
-- Estado actual del inventario: medicamentos, procedimientos y
-- ayudas diagnosticas activos con costo
-- Contexto: Inventario Clinico
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_inventario_operativo AS
SELECT
    'medicamento'          AS tipo_item,
    mc.medicamento_id      AS item_id,
    mc.codigo,
    mc.nombre,
    mc.descripcion,
    NULL                   AS especialidad,
    mc.costo_base,
    mc.activo
FROM medicamento_catalogo mc

UNION ALL

SELECT
    'procedimiento'        AS tipo_item,
    pc.procedimiento_id    AS item_id,
    pc.codigo,
    pc.nombre,
    pc.descripcion,
    esp.nombre             AS especialidad,
    pc.costo_base,
    pc.activo
FROM procedimiento_catalogo pc
LEFT JOIN especialidad esp ON esp.especialidad_id = pc.especialidad_id

UNION ALL

SELECT
    'ayuda_diagnostica'    AS tipo_item,
    ac.ayuda_id            AS item_id,
    ac.codigo,
    ac.nombre,
    ac.descripcion,
    esp.nombre             AS especialidad,
    ac.costo_base,
    ac.activo
FROM ayuda_diagnostica_catalogo ac
LEFT JOIN especialidad esp ON esp.especialidad_id = ac.especialidad_id;
