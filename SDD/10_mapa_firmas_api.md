# 10. Mapa de Firmas de la API REST — Clinic Backend

> **Base URL:** `http://localhost:8083`  
> **Autenticación:** todas las rutas excepto `/api/auth/login` requieren header `Authorization: Bearer <token>`  
> **Content-Type:** `application/json`

---

## Convenciones de respuesta

### Respuesta estándar de escritura (`SpResultadoResponse`)

```json
{
  "codigo":     0,
  "mensaje":    "Operación exitosa",
  "idGenerado": 42,
  "exitoso":    true
}
```

`codigo = 0` → éxito. Cualquier otro valor indica error de negocio (HTTP 422).

### Respuesta de copago (`CopagoResponse`)

```json
{
  "valorCopago": 15000.00,
  "exento":      false,
  "mensaje":     "Copago calculado exitosamente"
}
```

### Respuesta de login (`LoginResponse`)

```json
{
  "token":   "eyJhbGci...",
  "rol":     "administrativo",
  "usuario": "admin01"
}
```

---

## Módulo 1 — Autenticación `/api/auth`

| Acceso | Público (sin token) |
|---|---|

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 1 | `POST` | `/api/auth/login` | Obtener JWT |

### `POST /api/auth/login`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `username` | `String` | ✅ | Código de usuario del empleado |
| `password` | `String` | ✅ | Contraseña en texto plano |

**Response 200:** `LoginResponse`

---

## Módulo 2 — Administrativo `/api/administrativo`

| Rol requerido | `administrativo` |
|---|---|

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 2 | `POST` | `/api/administrativo/pacientes` | Registrar paciente |
| 3 | `POST` | `/api/administrativo/pacientes/contactos-emergencia` | Registrar contacto de emergencia |
| 4 | `POST` | `/api/administrativo/pacientes/seguros` | Registrar seguro médico |
| 5 | `POST` | `/api/administrativo/citas` | Programar cita |
| 6 | `PUT` | `/api/administrativo/citas/reprogramar` | Reprogramar cita |
| 7 | `PUT` | `/api/administrativo/citas/cancelar` | Cancelar cita |
| 8 | `GET` | `/api/administrativo/facturacion/copago` | Calcular copago |
| 9 | `POST` | `/api/administrativo/facturacion/facturas` | Emitir factura |
| 10 | `POST` | `/api/administrativo/facturacion/pagos` | Registrar pago |

### `POST /api/administrativo/pacientes`

**SP:** `sp_registrar_paciente`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `cedula` | `String` | ✅ | Número de documento |
| `tipoDocId` | `Short` | ✅ | ID del tipo de documento (cat_tipo_documento) |
| `nombreCompleto` | `String` | ✅ | Nombre completo del paciente |
| `fechaNacimiento` | `LocalDate` (`"YYYY-MM-DD"`) | ✅ | Fecha de nacimiento |
| `generoId` | `Short` | ✅ | ID de género (cat_genero) |
| `direccion` | `String` | — | Dirección de residencia |
| `telefono` | `String` | — | Teléfono de contacto |
| `correo` | `String` | — | Correo electrónico |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID del paciente creado

---

### `POST /api/administrativo/pacientes/contactos-emergencia`

**SP:** `sp_registrar_contacto_emergencia`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `nombreCompleto` | `String` | ✅ | Nombre del contacto |
| `relacion` | `String` | ✅ | Relación con el paciente (ej. "Madre") |
| `telefono` | `String` | ✅ | Teléfono del contacto |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse`

---

### `POST /api/administrativo/pacientes/seguros`

**SP:** `sp_registrar_seguro_medico`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `compania` | `String` | ✅ | Nombre de la aseguradora |
| `numeroPoliza` | `String` | ✅ | Número de póliza |
| `estadoSeguroId` | `Short` | ✅ | ID del estado del seguro (cat_estado_seguro) |
| `fechaVigencia` | `LocalDate` (`"YYYY-MM-DD"`) | ✅ | Fecha de vencimiento del seguro |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse`

---

### `POST /api/administrativo/citas`

**SP:** `sp_programar_cita`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `medicoId` | `Integer` | ✅ | ID del perfil médico |
| `fechaHora` | `LocalDateTime` (`"YYYY-MM-DDTHH:mm:ss"`) | ✅ | Fecha y hora de la cita |
| `prioridadId` | `Short` | ✅ | ID de prioridad (cat_prioridad_atencion) |
| `motivo` | `String` | — | Motivo de la consulta |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID de la cita

---

### `PUT /api/administrativo/citas/reprogramar`

**SP:** `sp_reprogramar_cita`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `citaId` | `Integer` | ✅ | ID de la cita a reprogramar |
| `nuevaFecha` | `LocalDateTime` (`"YYYY-MM-DDTHH:mm:ss"`) | ✅ | Nueva fecha y hora |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que opera |

**Response 200:** `SpResultadoResponse`

---

### `PUT /api/administrativo/citas/cancelar`

**SP:** `sp_cancelar_cita`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `citaId` | `Integer` | ✅ | ID de la cita a cancelar |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que opera |

**Response 200:** `SpResultadoResponse`

---

### `GET /api/administrativo/facturacion/copago`

**SP:** `sp_calcular_copago`

**Query params:**
| Param | Tipo | Req | Descripción |
|---|---|---|---|
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `tipoFactId` | `Short` | ✅ | ID del tipo de facturación (cat_tipo_facturacion) |

**Response 200:** `CopagoResponse`

---

### `POST /api/administrativo/facturacion/facturas`

**SP:** `sp_emitir_factura`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `encuentroId` | `Integer` | ✅ | ID del encuentro clínico |
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `medicoId` | `Integer` | ✅ | ID del médico |
| `seguroId` | `Integer` | — | ID del seguro médico (null si no tiene) |
| `tipoFactId` | `Short` | ✅ | ID del tipo de facturación |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que opera |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID de la factura

---

### `POST /api/administrativo/facturacion/pagos`

**SP:** `sp_registrar_pago`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `facturaId` | `Integer` | ✅ | ID de la factura a pagar |
| `valorPagado` | `BigDecimal` | ✅ | Monto del pago |
| `tipoPago` | `String` | ✅ | Tipo de pago (ej. "EFECTIVO", "TARJETA") |
| `referencia` | `String` | — | Referencia de transacción |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que opera |

**Response 201:** `SpResultadoResponse`

---

## Módulo 3 — Doctor `/api/doctor`

| Rol requerido | `medico` |
|---|---|

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 11 | `POST` | `/api/doctor/encuentros` | Abrir encuentro clínico |
| 12 | `PUT` | `/api/doctor/encuentros/cerrar` | Cerrar encuentro clínico |
| 13 | `POST` | `/api/doctor/ordenes` | Registrar orden médica |
| 14 | `POST` | `/api/doctor/ordenes/detalle` | Agregar detalle a orden |

### `POST /api/doctor/encuentros`

**SP:** `sp_abrir_encuentro_clinico`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `citaId` | `Integer` | ✅ | ID de la cita asociada |
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `medicoId` | `Integer` | ✅ | ID del médico |
| `motivoConsulta` | `String` | ✅ | Descripción del motivo |
| `sintomatologia` | `String` | — | Síntomas reportados |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario médico |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID del encuentro

---

### `PUT /api/doctor/encuentros/cerrar`

**SP:** `sp_cerrar_encuentro_clinico`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `encuentroId` | `Integer` | ✅ | ID del encuentro a cerrar |
| `diagnostico` | `String` | ✅ | Diagnóstico definitivo |
| `tratamiento` | `String` | ✅ | Plan de tratamiento indicado |
| `observaciones` | `String` | — | Observaciones adicionales |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario médico |

**Response 201:** `SpResultadoResponse`

---

### `POST /api/doctor/ordenes`

**SP:** `sp_registrar_orden_medica`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `numeroOrden` | `String` | ✅ | Número único de orden (ej. "ORD-2026-001") |
| `encuentroId` | `Integer` | ✅ | ID del encuentro clínico |
| `pacienteId` | `Integer` | ✅ | ID del paciente |
| `medicoId` | `Integer` | ✅ | ID del médico que ordena |
| `tipoOrdenId` | `Short` | ✅ | ID del tipo de orden (cat_tipo_orden) |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario médico |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID de la orden

---

### `POST /api/doctor/ordenes/detalle`

**SP:** `sp_agregar_detalle_orden`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `ordenId` | `Integer` | ✅ | ID de la orden médica |
| `tipoDetalle` | `String` | ✅ | Tipo (ej. "MEDICAMENTO", "PROCEDIMIENTO") |
| `item` | `Short` | ✅ | Número de ítem dentro de la orden |
| `referenciaId` | `Integer` | ✅ | ID del catálogo (medicamento/procedimiento/ayuda) |
| `dosis` | `String` | — | Dosis prescrita |
| `duracion` | `String` | — | Duración del tratamiento |
| `cantidad` | `Short` | — | Cantidad de unidades |
| `frecuencia` | `String` | — | Frecuencia de administración |
| `requiereEsp` | `Boolean` | — | Requiere especialista |
| `especialidadId` | `Integer` | — | ID de especialidad requerida |
| `costo` | `BigDecimal` | — | Costo estimado del ítem |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario médico |

**Response 201:** `SpResultadoResponse`

---

## Módulo 4 — Enfermera `/api/enfermera`

| Rol requerido | `enfermeria` |
|---|---|

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 15 | `POST` | `/api/enfermera/signos-vitales` | Registrar signos vitales |
| 16 | `POST` | `/api/enfermera/administracion-medicamentos` | Registrar administración de medicamento |

### `POST /api/enfermera/signos-vitales`

**SP:** `sp_registrar_signos_vitales`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `encuentroId` | `Integer` | ✅ | ID del encuentro clínico |
| `enfermeroId` | `Integer` | ✅ | ID del perfil enfermero |
| `presion` | `String` | — | Presión arterial (ej. "120/80") |
| `temperatura` | `BigDecimal` | — | Temperatura corporal (°C) |
| `pulso` | `Short` | — | Pulso (lat/min) |
| `oxigeno` | `BigDecimal` | — | Saturación de oxígeno (%) |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse`

---

### `POST /api/enfermera/administracion-medicamentos`

**SP:** `sp_registrar_administracion_medicamento`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `encuentroId` | `Integer` | ✅ | ID del encuentro clínico |
| `enfermeroId` | `Integer` | ✅ | ID del perfil enfermero |
| `medicamentoId` | `Integer` | ✅ | ID del medicamento (medicamento_catalogo) |
| `dosis` | `String` | ✅ | Dosis administrada |
| `observacion` | `String` | — | Observaciones de la administración |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse`

---

## Módulo 5 — Recursos Humanos `/api/rrhh`

| Rol requerido | `recursos_humanos` |
|---|---|

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 17 | `POST` | `/api/rrhh/empleados` | Registrar empleado |
| 18 | `POST` | `/api/rrhh/usuarios` | Crear usuario operativo |

### `POST /api/rrhh/empleados`

**SP:** `sp_registrar_empleado`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `cedula` | `String` | ✅ | Número de documento |
| `tipoDocId` | `Short` | ✅ | ID del tipo de documento |
| `nombreCompleto` | `String` | ✅ | Nombre completo |
| `correo` | `String` | — | Correo institucional |
| `telefono` | `String` | — | Teléfono |
| `fechaNacimiento` | `LocalDate` (`"YYYY-MM-DD"`) | ✅ | Fecha de nacimiento |
| `direccion` | `String` | — | Dirección de residencia |
| `rolId` | `Short` | ✅ | ID del rol (cat_rol_usuario) |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse` — `idGenerado` = ID del empleado

---

### `POST /api/rrhh/usuarios`

**SP:** `sp_crear_usuario_operativo`

**Request:**
| Campo | Tipo | Req | Descripción |
|---|---|---|---|
| `empleadoId` | `Integer` | ✅ | ID del empleado al que se le asigna acceso |
| `codigoUsuario` | `String` | ✅ | Nombre de usuario único |
| `contrasenaHash` | `String` | ✅ | Hash BCrypt de la contraseña |
| `usuarioOperador` | `Integer` | ✅ | ID del usuario que registra |

**Response 201:** `SpResultadoResponse`

> ⚠️ El campo `contrasenaHash` debe enviarse como hash BCrypt generado en el frontend. **Nunca texto plano.**

---

## Módulo 6 — Soporte de Información `/api/soporte`

| Rol requerido | `soporte_informacion` |
|---|---|

Patrón CRUD uniforme para **16 recursos**. Las PKs tipo `Short` son para catálogos; tipo `Integer` para maestras.

### Firma genérica CRUD

| Método | Firma | HTTP Status |
|---|---|---|
| `GET /api/soporte/{recurso}` | Listar todos | 200 |
| `GET /api/soporte/{recurso}/{id}` | Buscar por ID | 200 / 404 |
| `POST /api/soporte/{recurso}` | Crear | 201 |
| `PUT /api/soporte/{recurso}/{id}` | Actualizar | 200 / 404 |
| `DELETE /api/soporte/{recurso}/{id}` | Eliminar | 204 / 404 |

### Recursos y sus campos (catálogos — PK `Short`)

| Recurso | Campos principales |
|---|---|
| `/cat-roles` | `rolId`, `codigo`, `descripcion`, `activo` |
| `/cat-tipos-doc` | `tipoDocId`, `codigo`, `descripcion`, `activo` |
| `/cat-generos` | `generoId`, `codigo`, `descripcion` |
| `/cat-estados-cita` | `estadoCitaId`, `codigo`, `descripcion` |
| `/cat-prioridades` | `prioridadId`, `codigo`, `descripcion`, `nivel` |
| `/cat-estados-seguro` | `estadoSeguroId`, `codigo`, `descripcion` |
| `/cat-tipos-orden` | `tipoOrdenId`, `codigo`, `descripcion` |
| `/cat-tipos-detalle-orden` | `tipoDetalleId`, `codigo`, `descripcion` |
| `/cat-tipos-facturacion` | `tipoFactId`, `codigo`, `descripcion` |
| `/cat-estados-especialidad` | `estadoEspId`, `codigo`, `descripcion` |
| `/config-facturacion` | `configId`, `parametro`, `valorNumerico`, `descripcion` |

### Recursos y sus campos (maestras — PK `Integer`)

| Recurso | Campos principales |
|---|---|
| `/especialidades` | `especialidadId`, `nombre`, `descripcion`, `estadoEsp` |
| `/empleados` | `empleadoId`, `cedula`, `nombreCompleto`, `correo`, `telefono`, `fechaNacimiento`, `direccion`, `rolId` |
| `/usuarios` | `usuarioId`, `empleado`, `codigoUsuario`, `activo` |
| `/medicos-perfil` | `medicoId`, `empleado`, `especialidad`, `numeroTarjeta` |
| `/enfermeros-perfil` | `enfermeroId`, `empleado`, `numeroRegistro` |

---

## Códigos HTTP globales

| Status | Cuándo ocurre |
|---|---|
| `200 OK` | Lectura o actualización exitosa |
| `201 Created` | Creación exitosa |
| `204 No Content` | Eliminación exitosa |
| `400 Bad Request` | Fallo de validación Bean Validation (`@NotNull`, `@NotBlank`) |
| `401 Unauthorized` | Token ausente, expirado o inválido |
| `403 Forbidden` | Token válido pero rol sin acceso a la ruta |
| `404 Not Found` | Recurso no encontrado (solo módulo soporte) |
| `422 Unprocessable Entity` | Stored procedure rechazó la operación (regla de negocio) |

---

## Resumen de firmas por módulo

| Módulo | Rutas base | POST | PUT | GET | DELETE | Total |
|---|---|---|---|---|---|---|
| Auth | `/api/auth` | 1 | 0 | 0 | 0 | **1** |
| Administrativo | `/api/administrativo` | 6 | 2 | 1 | 0 | **9** |
| Doctor | `/api/doctor` | 3 | 1 | 0 | 0 | **4** |
| Enfermera | `/api/enfermera` | 2 | 0 | 0 | 0 | **2** |
| RRHH | `/api/rrhh` | 2 | 0 | 0 | 0 | **2** |
| Soporte | `/api/soporte` | 16 | 16 | 32 | 16 | **80** |
| **Total** | | **30** | **19** | **33** | **16** | **98** |
