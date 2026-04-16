# 05. Procedimientos almacenados

## Objetivo

Definir los servicios de dominio como procedimientos almacenados, con control de transaccion, validacion de entrada y resultados estandarizados.

## Convencion de nombres

Formato sugerido:

- sp_registrar_paciente,
- sp_actualizar_paciente,
- sp_registrar_empleado,
- sp_asignar_rol_usuario,
- sp_programar_cita,
- sp_reprogramar_cita,
- sp_cancelar_cita,
- sp_abrir_encuentro_clinico,
- sp_cerrar_encuentro_clinico,
- sp_registrar_signos_vitales,
- sp_registrar_orden_medica,
- sp_agregar_detalle_orden,
- sp_emitir_factura,
- sp_calcular_copago,
- sp_registrar_pago,
- sp_ajustar_inventario,
- sp_registrar_resultado_examen.

## Servicios por contexto

### Gestion de pacientes

#### sp_registrar_paciente

Entrada:

- cedula,
- nombre_completo,
- fecha_nacimiento,
- genero_id,
- direccion,
- telefono,
- correo.

Salida:

- paciente_id,
- codigo_respuesta,
- mensaje.

#### sp_registrar_contacto_emergencia

Entrada:

- paciente_id,
- nombre_completo,
- relacion,
- telefono.

### Gestion de personal

#### sp_registrar_empleado

Entrada:

- cedula,
- nombre_completo,
- correo,
- telefono,
- fecha_nacimiento,
- direccion,
- rol_id.

#### sp_crear_usuario_operativo

Entrada:

- empleado_id,
- usuario,
- contrasena_hash,
- estado.

### Agenda

#### sp_programar_cita

Entrada:

- paciente_id,
- medico_id,
- fecha_hora,
- motivo,
- prioridad_id.

### Atencion clinica

#### sp_abrir_encuentro_clinico

Entrada:

- cita_id,
- paciente_id,
- medico_id,
- motivo_consulta,
- sintomatologia.

#### sp_cerrar_encuentro_clinico

Entrada:

- encuentro_id,
- diagnostico_id,
- tratamiento,
- observaciones.

### Enfermeria

#### sp_registrar_signos_vitales

Entrada:

- encuentro_id,
- enfermero_id,
- presion_arterial,
- temperatura,
- pulso,
- oxigeno.

#### sp_registrar_administracion_medicamento

Entrada:

- encuentro_id,
- medicamento_id,
- dosis,
- observacion.

### Ordenes medicas

#### sp_registrar_orden_medica

Entrada:

- encuentro_id,
- medico_id,
- tipo_orden.

#### sp_agregar_detalle_orden

Entrada:

- orden_id,
- tipo_detalle,
- item,
- referencia_catalogo_id,
- cantidad,
- dosis,
- duracion,
- frecuencia,
- requiere_especialista,
- especialidad_id.

### Facturacion

#### sp_emitir_factura

Entrada:

- encuentro_id,
- paciente_id,
- medico_id.

#### sp_calcular_copago

Entrada:

- paciente_id,
- fecha_facturacion,
- valor_base.

#### sp_registrar_pago

Entrada:

- factura_id,
- valor_pagado,
- tipo_pago,
- referencia.

## Reglas tecnicas para todos los SP

1. Validar parametros obligatorios antes de tocar tablas.
2. Abrir transaccion cuando haya multiples escrituras.
3. Usar rollback ante cualquier incumplimiento.
4. Retornar un codigo de resultado estable.
5. Evitar logica de presentacion o formato en el SP.
6. Documentar los efectos colaterales por procedimiento.

## Resultado estandar

Cada SP debe devolver, como minimo:

- estado de ejecucion,
- mensaje funcional,
- identificador generado si aplica,
- detalle de error si falla.
