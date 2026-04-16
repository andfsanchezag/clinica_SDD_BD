# SDD - Clinica IPS

Este directorio concentra las especificaciones de diseno para construir la base de datos de la clinica bajo una perspectiva DDD y con responsabilidad centralizada en MySQL.

## Proposito

Definir el plan completo de analisis, diseno y construccion para que agentes de IA puedan generar posteriormente el proyecto de base de datos con criterios consistentes de dominio, validacion, seguridad y orquestacion.

## Principios de diseno

- El dominio se modela como tablas y catalogos en MySQL.
- Los enums del negocio se representan como tablas auxiliares.
- Las validaciones basicas viven en constraints y triggers.
- Los casos de uso se exponen como procedimientos almacenados.
- La historia clinica se implementa en MySQL, no como dependencia externa, para mantener coherencia con el alcance del proyecto.
- La seguridad se define desde la base de datos, no solo desde la aplicacion.

## Orden recomendado de lectura

1. [00_alcance_y_premisas.md](00_alcance_y_premisas.md)
2. [01_ddd_y_contextos.md](01_ddd_y_contextos.md)
3. [02_modelo_logico_y_tablas.md](02_modelo_logico_y_tablas.md)
4. [03_catalogos_y_enums.md](03_catalogos_y_enums.md)
5. [04_reglas_y_triggers.md](04_reglas_y_triggers.md)
6. [05_procedimientos_almacenados.md](05_procedimientos_almacenados.md)
7. [06_seguridad_y_auditoria.md](06_seguridad_y_auditoria.md)
8. [07_validacion_y_calidad.md](07_validacion_y_calidad.md)
9. [08_plan_de_implementacion.md](08_plan_de_implementacion.md)

## Resultado esperado

Al completar estas especificaciones, se debe poder generar:

- el modelo de dominio,
- el modelo relacional,
- el catalogo de enums,
- los triggers,
- los procedimientos almacenados,
- las vistas de seguridad,
- y el plan de pruebas de la base de datos.
