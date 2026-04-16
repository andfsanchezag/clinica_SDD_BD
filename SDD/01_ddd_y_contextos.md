# 01. DDD y contextos delimitados

## Objetivo

Traducir el enunciado a lenguaje ubicuo, contextos delimitados y agregados para evitar un modelo relacional plano sin semantica de dominio.

## Lenguaje ubicuo minimo

- empleado,
- usuario,
- rol,
- paciente,
- contacto de emergencia,
- seguro medico,
- cita,
- consulta,
- historia clinica,
- orden medica,
- medicamento,
- procedimiento,
- ayuda diagnostica,
- signo vital,
- factura,
- copago,
- inventario,
- especialidad,
- tratamiento,
- diagnostico.

## Contextos delimitados

### 1. Identidad y acceso

Responsable de usuarios, roles, permisos, autenticacion y trazabilidad de accesos.

### 2. Gestion de personal

Responsable de datos del empleado, cargo funcional y relacion con el rol operativo.

### 3. Gestion de pacientes

Responsable del registro maestro del paciente, su contacto de emergencia y su seguro.

### 4. Agenda y atencion

Responsable de citas, estados de agenda y relacion paciente-medico-fecha.

### 5. Atencion clinica

Responsable de historia clinica, diagnosticos, tratamientos y cierre de consulta.

### 6. Ordenes medicas

Responsable de ordenar medicamentos, procedimientos y ayudas diagnosticas.

### 7. Enfermeria

Responsable de signos vitales, administracion de medicamentos y procedimientos de apoyo.

### 8. Inventario clinico

Responsable de catalogos operativos y costos asociados a insumos y servicios.

### 9. Facturacion y seguros

Responsable de reglas de cobertura, copago, acumulado anual y emision de factura.

## Agregados propuestos

### Agregado Empleado

Raiz: empleado.

Entidades internas: usuario, asignacion_rol.

### Agregado Paciente

Raiz: paciente.

Entidades internas: contacto_emergencia, seguro_medico.

### Agregado Cita

Raiz: cita.

Entidades internas: estado_cita, historial_estado_cita si aplica.

### Agregado HistoriaClinica

Raiz: encuentro_clinico o consulta.

Entidades internas: diagnostico, tratamiento, observacion, signo_vital.

### Agregado OrdenMedica

Raiz: orden_medica.

Entidades internas: orden_detalle_medicamento, orden_detalle_procedimiento, orden_detalle_ayuda_diagnostica.

### Agregado Facturacion

Raiz: factura.

Entidades internas: factura_detalle, copago, cobertura, liquidacion.

## Invariantes de dominio

- una cedula identifica unicamente a un empleado o paciente segun su contexto,
- un paciente tiene un solo contacto de emergencia activo,
- un paciente tiene un solo seguro medico vigente por registro,
- una orden medica es unica,
- los items de una orden no se repiten,
- una orden no mezcla ayudas diagnosticas con medicamentos o procedimientos,
- un encuentro clinico debe quedar asociado a paciente y medico,
- una factura debe poder reconstruir su base de cobro,
- el copago anual tiene limite maximo acumulado,
- la fecha de nacimiento no puede superar 150 anos de antiguedad.

## Modelo de eventos del dominio

Aunque se implementara directamente en MySQL, conviene pensar en eventos logicos:

- paciente_registrado,
- cita_programada,
- consulta_iniciada,
- orden_creada,
- resultado_registrado,
- factura_emitida,
- pago_aplicado,
- inventario_actualizado.

## Reglas de modelado

1. Cada agregado debe poder validarse de forma consistente dentro de una transaccion.
2. Las relaciones entre agregados se materializan con claves foraneas y procedimientos.
3. Los detalles repetibles viven en tablas hijas por agregados.
4. Los estados operativos deben salir de catlogos, no de literales.
