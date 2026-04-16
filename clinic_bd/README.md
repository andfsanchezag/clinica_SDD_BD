# Clinica IPS — Scripts MySQL (SDD + DDD)

Base de datos completa para un sistema de gestión clínica, diseñada bajo los principios de **Storage-Driven Development (SDD)** y **Domain-Driven Design (DDD)**.

---

## Orden de ejecución

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `00_schema.sql` | Crea la base de datos `clinica_ips` con charset utf8mb4 |
| 2 | `01_catalogos.sql` | Tablas de catálogos (enums) y configuración de facturación |
| 3 | `02_tablas_maestras.sql` | Tablas maestras: Empleado, Paciente e Inventario Clínico |
| 4 | `03_tablas_transaccionales.sql` | Tablas transaccionales: Cita, Encuentro, Órdenes, Facturación |
| 5 | `04_triggers.sql` | Triggers de validación de invariantes del dominio |
| 6 | `05_procedimientos.sql` | Procedimientos almacenados: todos los casos de uso |
| 7 | `06_vistas.sql` | Vistas de consulta y reportes |
| 8 | `07_auditoria.sql` | Tablas y triggers de auditoría |
| 9 | `08_seguridad.sql` | Roles MySQL 8+ y usuarios de servicio |
| 10 | `09_datos_semilla.sql` | Datos de prueba: pacientes, empleados, inventario, encuentro |

---

## Cómo ejecutar

### Opción A — MySQL CLI

```bash
mysql -u root -p < 00_schema.sql
mysql -u root -p clinica_ips < 01_catalogos.sql
mysql -u root -p clinica_ips < 02_tablas_maestras.sql
mysql -u root -p clinica_ips < 03_tablas_transaccionales.sql
mysql -u root -p clinica_ips < 04_triggers.sql
mysql -u root -p clinica_ips < 05_procedimientos.sql
mysql -u root -p clinica_ips < 06_vistas.sql
mysql -u root -p clinica_ips < 07_auditoria.sql
mysql -u root -p clinica_ips < 08_seguridad.sql
mysql -u root -p clinica_ips < 09_datos_semilla.sql
```

### Opción B — MySQL Workbench / DBeaver

Abrir cada archivo en el orden indicado y ejecutar con **`Ctrl+Shift+Enter`**.

---

## Contextos de dominio (DDD Bounded Contexts)

| Contexto | Tablas principales |
|----------|--------------------|
| Identidad y Acceso | `seguridad_usuario`, `cat_rol_usuario` |
| Gestión de Personal | `empleado`, `medico_perfil`, `enfermero_perfil` |
| Gestión de Pacientes | `paciente`, `contacto_emergencia`, `seguro_medico` |
| Agenda y Atención | `cita` |
| Atención Clínica | `encuentro_clinico` |
| Enfermería | `signo_vital`, `administracion_medicamento`, `procedimiento_enfermeria` |
| Órdenes Médicas | `orden_medica`, `orden_medicamento_detalle`, `orden_procedimiento_detalle`, `orden_ayuda_diagnostica_detalle` |
| Inventario Clínico | `medicamento_catalogo`, `procedimiento_catalogo`, `ayuda_diagnostica_catalogo`, `especialidad` |
| Facturación y Seguros | `factura`, `factura_detalle`, `pago` |

---

## Reglas de negocio clave implementadas

| Regla | Implementación |
|-------|----------------|
| Edad máxima 150 años | Trigger `trg_empleado_bi`, `trg_paciente_bi` |
| Teléfono paciente = 10 dígitos exactos | CHECK constraint en `paciente.telefono` |
| Teléfono empleado = 1-10 dígitos | CHECK constraint en `empleado.telefono` |
| Dirección empleado ≤ 30 chars | CHECK constraint en `empleado.direccion` |
| Correo con formato válido | CHECK constraint + trigger |
| Código usuario max 15 chars alfanumérico | CHECK constraint en `seguridad_usuario.codigo_usuario` |
| Número de orden max 6 dígitos numéricos | CHECK constraint en `orden_medica.numero_orden` |
| Un solo contacto de emergencia activo por paciente | Trigger `trg_contacto_emergencia_bi` |
| Un solo seguro activo por paciente | Trigger `trg_seguro_medico_bi` |
| Sin solapamiento de citas por médico (30 min) | Trigger `trg_cita_bi` |
| No cerrar encuentro sin diagnóstico | Trigger `trg_encuentro_clinico_bu` |
| Ayuda diagnóstica NO mezcla con med/proc | Triggers `trg_orden_*_bi` |
| Item de orden empieza en 1, único por orden | CHECK constraint + UNIQUE KEY |
| Copago base = $50.000, tope anual = $1.000.000 | `config_facturacion` + trigger `trg_pago_bi` |

---

## Roles de seguridad (MySQL 8+)

| Rol | Acceso |
|-----|--------|
| `rol_recursos_humanos` | Empleados y usuarios. **Sin acceso** a pacientes ni catálogo clínico |
| `rol_administrativo` | Pacientes, seguros, citas, facturación |
| `rol_enfermeria` | Signos vitales, medicación, procedimientos de enfermería |
| `rol_medico` | Historia clínica completa, órdenes, encuentros |
| `rol_soporte_informacion` | Catálogo de inventario, especialidades, config |
| `rol_auditoria` | Solo lectura en todas las tablas + tablas de auditoría |
| `rol_admin_bd` | Acceso total — solo para DBA |

---

## Versión requerida

- **MySQL 8.0+** (CHECK constraints enforcement, roles nativos, `SIGNAL SQLSTATE`)
- Charset: `utf8mb4` / `utf8mb4_unicode_ci`

---

## Nota sobre historia clínica

El enunciado menciona almacenamiento NoSQL (clave: cédula, subclave: fecha). Por alcance del proyecto (motor único MySQL), la historia clínica se implementa relacionalmente como `encuentro_clinico`, preservando la semántica original: cada fila es indexable por `paciente_id + fecha_encuentro`.
