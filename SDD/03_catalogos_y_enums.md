# 03. Catalogos y enums

## Objetivo

Definir los valores controlados del negocio como tablas auxiliares para mantener trazabilidad, extensibilidad y normalizacion.

## Catalogos obligatorios

### rol_usuario

Valores sugeridos:

- recursos_humanos,
- administrativo,
- soporte_informacion,
- enfermeria,
- medico,
- auditor,
- administrador_bd.

### genero

Valores sugeridos:

- masculino,
- femenino,
- otro.

### estado_cita

Valores sugeridos:

- programada,
- confirmada,
- atendida,
- cancelada,
- no_asistio,
- reprogramada.

### estado_seguro

Valores sugeridos:

- activo,
- vencido,
- suspendido,
- cancelado.

### tipo_documento

Valores sugeridos:

- cedula,
- tarjeta_identidad,
- pasaporte,
- extranjeria.

### tipo_orden

Valores sugeridos:

- medicamento,
- procedimiento,
- ayuda_diagnostica,
- mixta_medicamento_procedimiento.

### tipo_detalle_orden

Valores sugeridos:

- medicamento,
- procedimiento,
- ayuda_diagnostica.

### tipo_facturacion

Valores sugeridos:

- con_poliza,
- sin_poliza,
- solo_copago,
- aseguradora.

### especialidad_estado

Valores sugeridos:

- activa,
- inactiva.

### prioridad_atencion

Valores sugeridos:

- baja,
- media,
- alta,
- urgente.

## Reglas para catalogos

1. Todo catalogo debe tener codigo tecnico y descripcion de negocio.
2. Los codigos no deben reutilizarse aunque el valor cambie de descripcion.
3. Los catalogos deben permitir habilitar o deshabilitar valores sin borrar historico.
4. La aplicacion no debe persistir literales que sustituyan a estos catalogos.

## Catalogos derivados del enunciado

### Cobro anual de copago

No es un catalogo, pero si una regla parametrizable que debe vivir en una tabla de configuracion.

### Configuracion_facturacion

Parametros sugeridos:

- valor_copago_base = 50000,
- tope_anual_copago = 1000000,
- dias_validos_poliza si el negocio lo requiere,
- max_edad_paciente = 150.

## Criterios de mantenimiento

- cada catalogo debe tener insert inicial controlado,
- la semantica del catalogo debe documentarse,
- los SP y triggers deben referenciar catalogos por clave, no por texto.
