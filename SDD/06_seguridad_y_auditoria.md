# 06. Seguridad y auditoria

## Objetivo

Definir el control de acceso y la trazabilidad operativa desde la base de datos.

## Principio rector

La seguridad debe ser coherente con los roles del negocio y no depender unicamente de la aplicacion.

## Roles de base de datos

### rol_recursos_humanos

Puede administrar empleados, usuarios y roles operativos.

### rol_administrativo

Puede registrar pacientes, citas, seguros y facturacion.

### rol_enfermeria

Puede registrar signos vitales, administracion y procedimientos de apoyo.

### rol_medico

Puede consultar pacientes asignados, generar diagnosticos, tratamientos y ordenes.

### rol_soporte_informacion

Puede administrar inventarios, catlogos y tareas de integridad operacional.

### rol_auditoria

Acceso de solo lectura a bitacoras y tablas historicas.

## Principios de privilegio

- acceso minimo necesario,
- separacion entre lectura y escritura,
- acceso directo a tablas solo cuando sea estrictamente necesario,
- preferencia por vistas y SPs para la operacion.

## Restricciones del enunciado

Recursos humanos no puede visualizar informacion de pacientes, medicamentos ni procedimientos. Esto se resuelve con:

- no otorgar acceso directo a esas tablas,
- exponer solo vistas filtradas si hace falta,
- usar SPs de administracion de personal sin alcance a dominios clinicos sensibles.

## Auditoria minima

### Campos por tabla

- created_at,
- created_by,
- updated_at,
- updated_by,
- active_flag,
- deleted_at si se usa borrado logico.

### Bitacoras recomendadas

- audit_usuario,
- audit_cambio_empleado,
- audit_cambio_paciente,
- audit_cambio_orden,
- audit_cambio_factura,
- audit_acceso,
- audit_error_proceso.

## Vistas recomendadas

- vw_paciente_resumen,
- vw_factura_resumen,
- vw_orden_resumen,
- vw_historia_clinica_por_paciente,
- vw_inventario_operativo.

## Manejo de secretos

Las contrasenas no deben almacenarse en texto plano. Deben persistirse como hash seguro y, de ser necesario, con salt gestionado por la aplicacion o por rutina de generacion controlada.

## Criterios de cumplimiento

- cada rol accede solo a lo que necesita,
- la auditoria permite reconstruir cambios criticos,
- el acceso a datos sensibles queda trazado,
- los procesos de negocio tienen evidencia de ejecucion.
