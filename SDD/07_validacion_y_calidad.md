# 07. Validacion y calidad

## Objetivo

Establecer los criterios de revision para validar que el disenio de la base de datos cumple el enunciado y las decisiones DDD.

## Capas de validacion

### 1. Validacion de dominio

Verifica que cada regla del enunciado tenga representacion clara en tablas, catalogos, triggers o SPs.

### 2. Validacion estructural

Verifica claves primarias, foraneas, unicidades, normalizacion y cardinalidades.

### 3. Validacion funcional

Verifica que los procedimientos almacenados cubran los casos de uso principales.

### 4. Validacion de seguridad

Verifica roles, permisos y aislamiento de datos sensibles.

### 5. Validacion operativa

Verifica que la facturacion, la agenda, la atencion y las ordenes puedan convivir sin inconsistencias.

## Escenarios de prueba minimo

- registrar paciente valido,
- rechazar paciente menor a 0 o mayor a 150 anos,
- registrar empleado valido,
- rechazar usuario duplicado,
- programar cita valida,
- evitar sobreposicion de cita del medico,
- abrir encuentro clinico,
- registrar signos vitales,
- emitir orden de medicamentos y procedimientos,
- impedir mezcla de ayudas diagnosticas con otras ordenes,
- emitir factura con poliza activa,
- calcular copago anual,
- evitar copago cuando se supera el tope anual,
- registrar auditoria de cambios criticos.

## Datos de prueba sugeridos

- un paciente con poliza activa,
- un paciente sin poliza,
- un medico activo,
- un enfermero activo,
- un inventario de medicamentos,
- un inventario de procedimientos,
- un inventario de ayudas diagnosticas,
- especialidades activas,
- una factura con multiples detalles.

## Criterios de aceptacion

1. Toda regla del enunciado puede mapearse a una pieza concreta del diseno.
2. No existen dependencias criticas fuera de MySQL.
3. Los procedimientos almacenados tienen entradas, salidas y transaccion definidas.
4. Los triggers solo cubren lo que no puede resolverse con constraints.
5. El modelo permite trazabilidad y mantenimiento.
