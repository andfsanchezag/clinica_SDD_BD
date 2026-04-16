# 00. Alcance y premisas

## Proposito

Establecer las decisiones base del proyecto de base de datos para la clinica, bajo un criterio DDD y con MySQL como motor de persistencia y de logica de negocio operativa.

## Alcance funcional

El sistema cubre:

- gestion de usuarios y roles,
- gestion de empleados,
- gestion de pacientes,
- gestion de citas,
- gestion de seguros,
- gestion de inventario clinico,
- gestion de enfermeria,
- gestion de historia clinica,
- gestion de ordenes medicas,
- gestion de facturacion y copagos,
- auditoria y control de acceso.

## Fuera de alcance

- interfaz de usuario,
- integracion con proveedores externos,
- motor de reglas fuera de MySQL,
- orquestacion de negocio en la capa de aplicacion,
- analitica avanzada o BI,
- almacenamiento NoSQL separado para historia clinica.

## Premisas de arquitectura

1. MySQL es la fuente de verdad.
2. Cada concepto del dominio debe tener una representacion persistente clara.
3. Las reglas de invariancia deben vivir en la base de datos.
4. Los procedimientos almacenados se usan como servicios de dominio.
5. Los triggers se reservan para reglas locales y validaciones de consistencia.
6. Los catlogos controlan valores enumerados y estados.

## Decisiones clave

### Historia clinica

El enunciado menciona NoSQL, pero para mantener el alcance en un unico motor se adopta una representacion relacional en MySQL, centrada en episodios o consultas clinicas.

### Seguridad

Los roles funcionales del enunciado se traducen a usuarios, roles, grants, vistas y procedimientos con privilegios reducidos.

### Facturacion

La logica de copagos, vigencia de poliza y acumulado anual se implementa con procedimientos almacenados y tablas de soporte.

## Criterios de exito

- el modelo cubre todos los escenarios del enunciado,
- no hay reglas criticas fuera de la BD,
- cada caso de uso principal tiene un SP definido,
- los enums quedan externalizados en tablas,
- la estructura permite que agentes de IA generen el proyecto sin ambiguedad.
