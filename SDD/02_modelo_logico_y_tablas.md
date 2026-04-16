# 02. Modelo logico y tablas

## Objetivo

Definir la estructura relacional base de MySQL, alineada con los agregados del dominio.

## Criterios de diseno

- 3FN como punto de partida.
- Separacion entre maestrias, transacciones y catalogos.
- Tablas hijas para relaciones 1:N y N:M del dominio.
- Claves naturales solo cuando aporten valor de negocio; en el resto, claves surrogate.
- Integridad referencial obligatoria en los vinculos criticos.

## Tablas maestras

### seguridad_usuario

Guarda credenciales y relacion operativa con empleado.

### empleado

Datos personales y funcionales del recurso humano.

### paciente

Datos basicos del paciente.

### contacto_emergencia

Un registro activo por paciente.

### seguro_medico

Poliza y vigencia asociadas al paciente.

### medico_perfil

Subconjunto funcional de empleado que actua como medico.

### enfermero_perfil

Subconjunto funcional de empleado que actua como enfermero.

### especialidad

Catalogo de especialidades medicas.

### medicamento_catalogo

Inventario de medicamentos.

### procedimiento_catalogo

Inventario de procedimientos.

### ayuda_diagnostica_catalogo

Inventario de examenes o ayudas diagnosticas.

### examen_resultado

Resultado de ayudas diagnosticas cuando aplique.

## Tablas transaccionales

### cita

Relacion paciente, medico, fecha, hora y estado.

### encuentro_clinico

Consulta o atencion clinica con motivo, sintomatologia y diagnostico.

### signo_vital

Registro de signos tomados por enfermeria.

### administracion_medicamento

Medicamentos administrados por enfermeria.

### procedimiento_enfermeria

Procedimientos ejecutados por enfermeria.

### orden_medica

Cabecera de la orden.

### orden_medicamento_detalle

Detalle por item para medicamentos.

### orden_procedimiento_detalle

Detalle por item para procedimientos.

### orden_ayuda_diagnostica_detalle

Detalle por item para ayudas diagnosticas.

### factura

Cabecera de facturacion.

### factura_detalle

Linea detallada de la factura.

### pago

Registro del pago o del copago.

## Relaciones principales

- empleado 1:1 seguridad_usuario,
- empleado 1:N roles operativos si aplica,
- paciente 1:N cita,
- paciente 1:1 contacto_emergencia activo,
- paciente 1:1 seguro_medico activo,
- paciente 1:N encuentro_clinico,
- encuentro_clinico 1:N orden_medica,
- orden_medica 1:N detalles por tipo,
- factura 1:N factura_detalle,
- factura 1:N pago,
- cita 0..1 encuentro_clinico.

## Decisiones sobre historia clinica

Se implementa como modelo relacional de encuentros clinicos, no como documento NoSQL externo. Si se requiere flexibilidad para texto libre, se puede agregar un campo JSON controlado en encuentro_clinico para notas estructuradas, sin abandonar MySQL.

## Identificadores

### Recomendados

- empleado_id,
- paciente_id,
- cita_id,
- encuentro_id,
- orden_id,
- factura_id,
- pago_id.

### Naturales de negocio

- cedula_empleado,
- cedula_paciente,
- numero_orden,
- codigo_usuario,
- numero_poliza.

## Campos de auditoria comunes

Todas las tablas criticas deberian incluir:

- created_at,
- created_by,
- updated_at,
- updated_by,
- active_flag,
- deleted_at si se usa borrado logico.
