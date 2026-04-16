-- ============================================================
-- CLINICA IPS - Datos semilla y datos de prueba
-- Incluye: especialidades, empleados, pacientes, inventario,
--          citas, encuentros y facturacion de ejemplo
-- Orden de ejecucion: 10 de 10
-- Dependencias: 08_seguridad.sql
-- ============================================================

USE clinica_ips;

-- Desactivar verificaciones FK temporalmente para carga ordenada
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- ESPECIALIDADES
-- ============================================================
INSERT INTO especialidad (nombre, descripcion, estado_esp_id, created_by) VALUES
    ('Medicina General',      'Atencion primaria general',               1, 1),
    ('Cardiologia',           'Enfermedades del corazon y sistema cardiovascular', 1, 1),
    ('Pediatria',             'Atencion medica a ninos y adolescentes',  1, 1),
    ('Ortopedia',             'Trastornos del sistema musculoesqueletico', 1, 1),
    ('Neurologia',            'Enfermedades del sistema nervioso',       1, 1),
    ('Laboratorio Clinico',   'Toma y analisis de muestras biologicas',  1, 1);

-- ============================================================
-- EMPLEADOS (un medico, un enfermero, un administrativo, un RRHH)
-- ============================================================
INSERT INTO empleado (cedula, tipo_doc_id, nombre_completo, correo, telefono, fecha_nacimiento, direccion, rol_id, active_flag, created_by) VALUES
    ('1010200100', 1, 'Carlos Alberto Rios',    'carios@clinicaips.co',     '3001234567', '1980-05-15', 'Cll 10 # 20-30',    5, 1, 1),  -- medico
    ('1010200101', 1, 'Laura Gomez Mendez',     'lgomez@clinicaips.co',     '3009876543', '1990-03-22', 'Kra 45 # 80-10',    4, 1, 1),  -- enfermeria
    ('1010200102', 1, 'Jorge Hernandez Cano',   'jhernandez@clinicaips.co', '3115556677', '1975-11-02', 'Av 30 # 15-50',     2, 1, 1),  -- administrativo
    ('1010200103', 1, 'Maria Lucia Bedoya',     'mbedoya@clinicaips.co',    '3201112233', '1985-07-19', 'Cll 50 # 60-70',    1, 1, 1);  -- recursos_humanos

-- ============================================================
-- PERFILES FUNCIONALES
-- ============================================================
-- Medico: Carlos Alberto Rios (empleado_id=1), Medicina General (especialidad_id=1)
INSERT INTO medico_perfil (empleado_id, especialidad_id, registro_medico, activo) VALUES (1, 1, 'RM-2020-0001', 1);

-- Enfermero: Laura Gomez Mendez (empleado_id=2)
INSERT INTO enfermero_perfil (empleado_id, activo) VALUES (2, 1);

-- ============================================================
-- USUARIOS OPERATIVOS (hash de ejemplo SHA2 - en produccion usar bcrypt)
-- ============================================================
INSERT INTO seguridad_usuario (empleado_id, codigo_usuario, contrasena_hash, activo, created_by) VALUES
    (1, 'carios01',      SHA2('MedPass2025!',256),  1, 1),
    (2, 'lgomez01',      SHA2('EnfPass2025!',256),  1, 1),
    (3, 'jhernandez01',  SHA2('AdmPass2025!',256),  1, 1),
    (4, 'mbedoya01',     SHA2('RRHHPass2025!',256), 1, 1);

-- ============================================================
-- PACIENTES
-- Paciente 1: con seguro activo
-- Paciente 2: sin seguro
-- ============================================================
INSERT INTO paciente (cedula, tipo_doc_id, nombre_completo, fecha_nacimiento, genero_id, direccion, telefono, correo, active_flag, created_by) VALUES
    ('98765432', 1, 'Ana Maria Torres Jimenez', '1992-08-14', 1, 'Cll 25 # 40-60 Apto 302', '3054449988', 'ana.torres@gmail.com', 1, 3),
    ('87654321', 1, 'Pedro Luis Castillo Mora', '1968-02-28', 2, 'Kra 70 # 50-20',           '3108887766', NULL,                  1, 3);

-- Contactos de emergencia
INSERT INTO contacto_emergencia (paciente_id, nombre_completo, relacion, telefono, activo, created_by) VALUES
    (1, 'Luis Carlos Torres', 'Esposo',  '3150001122', 1, 3),
    (2, 'Rosa Elena Castillo','Hija',    '3163334455', 1, 3);

-- Seguro activo para paciente 1
INSERT INTO seguro_medico (paciente_id, compania, numero_poliza, estado_seguro_id, fecha_vigencia, activo, created_by) VALUES
    (1, 'Seguros Bolivar SA', 'POL-2025-009988', 1, '2026-12-31', 1, 3);

-- ============================================================
-- INVENTARIO CLINICO
-- ============================================================

-- Medicamentos
INSERT INTO medicamento_catalogo (codigo, nombre, descripcion, costo_base, activo, created_by) VALUES
    ('MED-001', 'Acetaminofen 500mg',    'Analgesico y antipiretico oral',               15000.00, 1, 1),
    ('MED-002', 'Ibuprofeno 400mg',      'Antiinflamatorio no esteroideo oral',           18000.00, 1, 1),
    ('MED-003', 'Amoxicilina 500mg',     'Antibiotico de amplio espectro oral',           22000.00, 1, 1),
    ('MED-004', 'Omeprazol 20mg',        'Inhibidor de bomba de protones',                25000.00, 1, 1),
    ('MED-005', 'Solucion Salina 500ml', 'Cloruro de sodio 0.9% IV',                      8000.00, 1, 1);

-- Procedimientos (incluye hospitalizacion segun enunciado)
INSERT INTO procedimiento_catalogo (codigo, nombre, descripcion, especialidad_id, costo_base, activo, created_by) VALUES
    ('PROC-001', 'Consulta de valoracion',        'Consulta medica inicial de valoracion',      1, 45000.00, 1, 1),
    ('PROC-002', 'Electrocardiograma',             'Registro de actividad electrica cardiaca',   2, 80000.00, 1, 1),
    ('PROC-003', 'Curacion de herida simple',      'Limpieza y vendaje de herida superficial',  NULL, 35000.00, 1, 1),
    ('PROC-004', 'Hospitalizacion (por dia)',       'Hospitalizacion en sala general por dia',  NULL, 350000.00, 1, 1),
    ('PROC-005', 'Aplicacion de inyeccion IV',     'Aplicacion de medicamento via intravenosa', NULL, 15000.00, 1, 1);

-- Ayudas diagnosticas
INSERT INTO ayuda_diagnostica_catalogo (codigo, nombre, descripcion, especialidad_id, costo_base, activo, created_by) VALUES
    ('AYD-001', 'Hemograma completo',              'Recuento celular completo en sangre',        6, 35000.00, 1, 1),
    ('AYD-002', 'Glucosa en ayunas',               'Nivel de glucosa en sangre en ayunas',       6, 18000.00, 1, 1),
    ('AYD-003', 'Radiografia de torax',            'Placa de rayos X frontal de torax',         NULL, 55000.00, 1, 1),
    ('AYD-004', 'Ecografia abdominal',             'Ultrasonido de organos abdominales',        NULL, 90000.00, 1, 1),
    ('AYD-005', 'Perfil lipidico',                 'Colesterol total, HDL, LDL, trigliceridos',  6, 42000.00, 1, 1);

-- ============================================================
-- CITA DE EJEMPLO (Ana Maria Torres con Dr. Carlos Rios)
-- ============================================================
INSERT INTO cita (paciente_id, medico_id, fecha_hora, estado_cita_id, prioridad_id, motivo, created_by)
VALUES (1, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), 1, 2, 'Consulta de control por dolor de cabeza recurrente', 3);

-- ============================================================
-- ENCUENTRO CLINICO DE PRUEBA (Pedro Luis Castillo - urgente)
-- Scenario: paciente sin poliza, consulta de urgencias
-- ============================================================
INSERT INTO encuentro_clinico (cita_id, paciente_id, medico_id, motivo_consulta, sintomatologia, diagnostico, tratamiento, estado, created_by)
VALUES (NULL, 2, 1,
        'Dolor epigastrico intenso',
        'Paciente refiere dolor en zona alta del abdomen de 6 horas de evolucion, nauseas sin vomito',
        'Gastritis aguda. CIE-10 K29.7',
        'Omeprazol 20mg cada 12 horas por 14 dias. Dieta blanda. Control en 1 semana.',
        'cerrado',
        1);

-- Signos vitales del encuentro cerrado (se insertan antes del cierre logico)
INSERT INTO signo_vital (encuentro_id, enfermero_id, presion_arterial, temperatura, pulso, oxigeno, created_by)
VALUES (1, 1, '120/80', 36.8, 72, 98.5, 2);

-- Orden medica del encuentro (medicamento)
INSERT INTO orden_medica (numero_orden, encuentro_id, paciente_id, medico_id, tipo_orden_id, created_by)
VALUES ('100001', 1, 2, 1, 1, 1);  -- tipo 1 = medicamento

INSERT INTO orden_medicamento_detalle (orden_id, item, medicamento_id, dosis, duracion, costo, created_by)
VALUES (1, 1, 4, '20mg vía oral', '14 dias - cada 12 horas', 25000.00, 1);

-- Factura del encuentro (sin seguro, copago = 50000)
INSERT INTO factura (encuentro_id, paciente_id, medico_id, seguro_id, tipo_fact_id, valor_total, valor_copago, valor_aseguradora, valor_paciente, estado, created_by)
VALUES (1, 2, 1, NULL, 2, 70000.00, 50000.00, 0.00, 70000.00, 'pagada', 3);
-- tipo_fact_id=2 = sin_poliza

INSERT INTO factura_detalle (factura_id, tipo_item, nombre_item, dosis, cantidad, costo_unitario, costo_total)
VALUES
    (1, 'procedimiento', 'Consulta de valoracion', NULL, 1, 45000.00, 45000.00),
    (1, 'medicamento',   'Omeprazol 20mg', '20mg c/12h x 14 dias', 1, 25000.00, 25000.00);

-- Pago del copago
INSERT INTO pago (factura_id, valor_pagado, tipo_pago, referencia, created_by)
VALUES (1, 70000.00, 'paciente_total', 'EFEC-20250510-001', 3);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- VERIFICACION RAPIDA DE CARGA
-- ============================================================
SELECT 'especialidad'               AS tabla, COUNT(*) AS registros FROM especialidad
UNION ALL
SELECT 'empleado',                            COUNT(*) FROM empleado
UNION ALL
SELECT 'medico_perfil',                       COUNT(*) FROM medico_perfil
UNION ALL
SELECT 'enfermero_perfil',                    COUNT(*) FROM enfermero_perfil
UNION ALL
SELECT 'seguridad_usuario',                   COUNT(*) FROM seguridad_usuario
UNION ALL
SELECT 'paciente',                            COUNT(*) FROM paciente
UNION ALL
SELECT 'seguro_medico',                       COUNT(*) FROM seguro_medico
UNION ALL
SELECT 'contacto_emergencia',                 COUNT(*) FROM contacto_emergencia
UNION ALL
SELECT 'medicamento_catalogo',                COUNT(*) FROM medicamento_catalogo
UNION ALL
SELECT 'procedimiento_catalogo',              COUNT(*) FROM procedimiento_catalogo
UNION ALL
SELECT 'ayuda_diagnostica_catalogo',          COUNT(*) FROM ayuda_diagnostica_catalogo
UNION ALL
SELECT 'cita',                                COUNT(*) FROM cita
UNION ALL
SELECT 'encuentro_clinico',                   COUNT(*) FROM encuentro_clinico
UNION ALL
SELECT 'orden_medica',                        COUNT(*) FROM orden_medica
UNION ALL
SELECT 'factura',                             COUNT(*) FROM factura
UNION ALL
SELECT 'pago',                                COUNT(*) FROM pago;
