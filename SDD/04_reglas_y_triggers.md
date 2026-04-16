# 04. Reglas y triggers

## Objetivo

Definir las validaciones que deben ejecutarse en MySQL mediante triggers y constraints para sostener invariantes del dominio.

## Principio de uso

- CHECK para reglas locales simples.
- UNIQUE para unicidad de negocio.
- FK para integridad relacional.
- TRIGGER para coherencia entre tablas o para reglas dependientes de estado.

## Reglas por entidad

### empleado

- la cedula debe ser unica,
- el correo debe tener formato valido,
- la fecha de nacimiento no puede superar 150 anos,
- el telefono debe respetar longitud permitida,
- la direccion debe respetar maximo de caracteres.

### paciente

- la cedula debe ser unica,
- la fecha de nacimiento no puede superar 150 anos,
- el telefono debe tener 10 digitos,
- el correo puede ser nulo pero, si existe, debe ser valido.

### contacto_emergencia

- un solo contacto activo por paciente,
- el telefono debe tener 10 digitos,
- la relacion no puede quedar vacia.

### seguro_medico

- un solo seguro activo por paciente,
- la fecha de vigencia no puede ser menor a la fecha de registro,
- la poliza debe ser unica,
- el estado debe provenir del catalogo.

### cita

- no se puede crear si el medico no existe o no esta activo,
- no debe permitirse sobreposicion de citas para el mismo medico en la misma franja,
- el estado inicial debe ser programada o el que defina el flujo.

### encuentro_clinico

- debe existir paciente y medico,
- no puede cerrarse sin motivo, sintomatologia y diagnostico minimo,
- no debe existir mas de un encuentro clinico abierto para la misma cita si el negocio no lo permite.

### orden_medica y detalles

- numero_orden unico,
- item inicia en 1 por orden,
- item no repetido por orden,
- una orden no puede contener tipos mezclados incompatibles,
- las ayudas diagnosticas no se mezclan con medicamentos o procedimientos,
- cuando aplique especialista, la especialidad no puede ser nula.

### factura

- no se puede emitir sin paciente y medico tratante,
- la factura debe poder reconstruir su base y su cobertura,
- el valor de copago debe respetar configuracion,
- el acumulado anual no debe exceder el tope definido.

### enfermeria

- los signos vitales deben quedar en rangos clinicamente validos,
- no puede registrarse administracion de medicamento sin referencia a orden o indicacion valida,
- no puede registrarse procedimiento sin relacion a la atencion.

## Triggers propuestos

### Trigger de validacion de paciente

Se ejecuta before insert y before update sobre paciente para validar edad, telefono y correo.

### Trigger de validacion de empleado

Se ejecuta before insert y before update sobre empleado para validar cedula, telefono, correo y direccion.

### Trigger de secuencia de items de orden

Se ejecuta before insert en cada tabla de detalle de orden para validar que el item sea consecutivo o, como minimo, no repetido.

### Trigger de incompatibilidad de orden

Se ejecuta before insert sobre detalles de orden para impedir mezcla entre ayudas diagnosticas y los otros tipos.

### Trigger de vigencia de seguro

Se ejecuta before insert y before update sobre seguro_medico para validar fechas y estado.

### Trigger de control de copago anual

Se ejecuta before insert sobre pago o factura para revisar el acumulado anual del paciente.

## Manejo de errores

Todo trigger debe lanzar errores controlados con un codigo de negocio consistente y un mensaje orientado a operacion, no a depuracion tecnica.

## Recomendacion tecnica

Las validaciones de forma deben minimizarse en triggers si pueden resolverse con constraints. Los triggers deben estar reservados a coherencia de dominio y a calculos dependientes de otras tablas.
