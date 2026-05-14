# 12. Plan de Implementación — Frontend Clinic

> Este documento describe los pasos para construir el proyecto `clinic-frontend` desde cero.  
> Los pasos marcados con la misma **Rama** pueden ejecutarse en paralelo de forma independiente.  
> Paleta de colores: estilo Sura Colombia (azul corporativo `#003DA5`, azul medio `#0057B8`, azul cielo `#009FE3`).

---

## Resumen de paralelización

```
Paso 0 ──► Paso 1 ──┬──► Rama A (Shared)  ──────────────────────────────────────────┐
                    │                                                                  │
                    ├──► Rama B (Login)    ──────────────────────────────────────────┤
                    │                                                                  ▼
                    └──► Rama C (Módulos de rol) ──┬──► C1 Administrativo            Paso 2 (integración)
                                                   ├──► C2 Doctor
                                                   ├──► C3 Enfermera
                                                   ├──► C4 RRHH
                                                   └──► C5 Soporte
```

> **Regla de dependencia:** ningún módulo de rol puede integrarse hasta que el **Paso 1** (shared + login) esté listo, ya que todos importan `auth.storage.js`, `http.client.js` y `router.guard.js`.

---

## Paso 0 — Creación del proyecto y configuración base

> **Bloqueante:** todos los pasos siguientes dependen de éste.  
> **Rama git:** `main` / `trunk`

| # | Tarea |
|---|---|
| 0.1 | Crear carpeta `clinic-frontend/` en la raíz del workspace |
| 0.2 | Ejecutar `npm init -y` dentro de `clinic-frontend/` |
| 0.3 | Instalar dependencias: `npm install express` y `npm install --save-dev nodemon` |
| 0.4 | Crear `server.js` (Express estático, sirve `public/`, puerto 3000) |
| 0.5 | Ajustar `package.json`: scripts `start` y `dev` |
| 0.6 | Crear la estructura completa de carpetas vacías: |
|     | `public/assets/css/` |
|     | `public/assets/img/` |
|     | `public/pages/` |
|     | `public/js/shared/infrastructure/` |
|     | `public/js/shared/components/` |
|     | `public/js/login/{domain,application,infrastructure,presentation}/` |
|     | `public/js/administrativo/{domain,application,infrastructure,presentation}/` |
|     | `public/js/doctor/{domain,application,infrastructure,presentation}/` |
|     | `public/js/enfermera/{domain,application,infrastructure,presentation}/` |
|     | `public/js/rrhh/{domain,application,infrastructure,presentation}/` |
|     | `public/js/soporte/{domain,application,infrastructure,presentation}/` |
| 0.7 | Crear `public/index.html` (redirige a `/pages/login.html`) |
| 0.8 | Verificar que `node server.js` inicia y sirve `public/` sin errores |

---

## Paso 1 — Capa Shared y módulo Login

> **Depende de:** Paso 0  
> **Las dos ramas de este paso son independientes entre sí y se pueden hacer en paralelo.**

---

### Rama A — Shared: infraestructura y componentes transversales

> **Rama git sugerida:** `feat/shared`

#### A.1 — CSS global con paleta Sura

| # | Archivo | Contenido |
|---|---|---|
| A.1.1 | `public/assets/css/styles.css` | Variables CSS (`--sura-blue`, `--sura-blue-mid`, `--sura-blue-sky`, etc.), estilos de navbar, botones, alertas, sidebar, tablas y cards coherentes con Bootstrap 5.3 |

#### A.2 — Infraestructura shared

| # | Archivo | Responsabilidad |
|---|---|---|
| A.2.1 | `js/shared/infrastructure/auth.storage.js` | CRUD localStorage: `save`, `clear`, `getToken`, `getRol`, `getUserId`, `isTokenExpired` |
| A.2.2 | `js/shared/infrastructure/router.guard.js` | `guardPage(rolRequerido)`: verifica token + rol, redirige si falla |
| A.2.3 | `js/shared/infrastructure/http.client.js` | Wrapper `fetch`: adjunta Bearer, intercepta 401/403, lanza error en 422 |

#### A.3 — Componentes shared

| # | Archivo | Responsabilidad |
|---|---|---|
| A.3.1 | `js/shared/components/navbar.js` | Inyecta navbar con logo, nombre de usuario y botón logout |
| A.3.2 | `js/shared/components/result.banner.js` | Muestra alerta Bootstrap éxito/error con `mensaje` e `idGenerado` |
| A.3.3 | `js/shared/components/modal.confirm.js` | Abre modal Bootstrap de confirmación antes de DELETE |
| A.3.4 | `js/shared/components/form.filler.js` | Rellena un `<form>` con un objeto de datos (GET → form) |

---

### Rama B — Módulo Login

> **Rama git sugerida:** `feat/login`  
> **Depende de:** A.2.1 (auth.storage) y A.2.3 (http.client) — puede codificarse en paralelo y conectarse al mergear

| # | Archivo | Responsabilidad |
|---|---|---|
| B.1 | `js/login/domain/auth.entity.js` | Clase/objeto `{ token, rol, usuario }` |
| B.2 | `js/login/infrastructure/auth.api.js` | `POST /api/auth/login` → retorna `AuthEntity` |
| B.3 | `js/login/application/login.usecase.js` | Orquesta: llama api, construye entidad, retorna |
| B.4 | `js/login/presentation/login.controller.js` | Captura form, llama use case, guarda token, redirige por rol |
| B.5 | `public/pages/login.html` | Página: card centrada, formulario user/pass, logo, paleta Sura |

**Comportamientos a implementar en B.5 + B.4:**
- Si `!AuthStorage.isTokenExpired()` al cargar → redirigir a la página del rol activo
- Éxito login → `AuthStorage.save(...)` → redirect según `rol`
- Error 401 → alerta Bootstrap roja "Credenciales incorrectas"
- Error de red → alerta "No se pudo conectar al servidor"

---

## Paso 2 — Módulos de rol (todos paralelos entre sí)

> **Depende de:** Paso 1 (Shared y Login deben estar merged)  
> **Las cinco ramas C1–C5 son completamente independientes entre sí.**

---

### Rama C1 — Módulo Administrativo

> **Rama git sugerida:** `feat/administrativo`

#### C1.1 — Domain

| Archivo | Entidad |
|---|---|
| `domain/paciente.entity.js` | `{ cedula, tipoDocId, nombreCompleto, fechaNacimiento, generoId, direccion, telefono, correo, usuarioOperador }` |
| `domain/cita.entity.js` | `{ pacienteId, medicoId, fechaHora, prioridadId, motivo, usuarioOperador }` |
| `domain/factura.entity.js` | `{ encuentroId, pacienteId, medicoId, seguroId, tipoFactId, usuarioOperador }` |

#### C1.2 — Infrastructure

| Archivo | Endpoints que cubre |
|---|---|
| `infrastructure/administrativo.api.js` | POST /pacientes, POST /pacientes/contactos-emergencia, POST /pacientes/seguros, POST /citas, PUT /citas/reprogramar, PUT /citas/cancelar, GET /facturacion/copago, POST /facturacion/facturas, POST /facturacion/pagos |

#### C1.3 — Application (un use case por endpoint)

`registrar.paciente.usecase.js`, `registrar.contacto.usecase.js`, `registrar.seguro.usecase.js`, `programar.cita.usecase.js`, `reprogramar.cita.usecase.js`, `cancelar.cita.usecase.js`, `calcular.copago.usecase.js`, `emitir.factura.usecase.js`, `registrar.pago.usecase.js`

#### C1.4 — Presentation

| Archivo | Pestañas que maneja |
|---|---|
| `presentation/paciente.controller.js` | Registrar paciente, contacto de emergencia, seguro médico |
| `presentation/cita.controller.js` | Programar cita, reprogramar cita (GET+PUT), cancelar cita |
| `presentation/facturacion.controller.js` | Calcular copago (GET), emitir factura, registrar pago |

#### C1.5 — HTML

`public/pages/administrativo.html`: navbar Sura, `nav-tabs` Bootstrap (Pacientes / Citas / Facturación), formularios, selects dinámicos.  
Selects a cargar en `DOMContentLoaded`: tipo documento, género, estado seguro, prioridad, tipo facturación.

---

### Rama C2 — Módulo Doctor

> **Rama git sugerida:** `feat/doctor`

#### C2.1 — Domain

`domain/encuentro.entity.js`, `domain/orden.entity.js`

#### C2.2 — Infrastructure

`infrastructure/doctor.api.js` — POST /encuentros, PUT /encuentros/cerrar, POST /ordenes, POST /ordenes/detalle

#### C2.3 — Application

`abrir.encuentro.usecase.js`, `cerrar.encuentro.usecase.js`, `registrar.orden.usecase.js`, `agregar.detalle.usecase.js`

#### C2.4 — Presentation

`presentation/encuentro.controller.js` — abrir (POST) y cerrar (GET+PUT)  
`presentation/orden.controller.js` — registrar orden y agregar detalle; mostrar/ocultar `especialidadId` según checkbox `requiereEsp`

#### C2.5 — HTML

`public/pages/doctor.html`: navbar Sura, `nav-tabs` (Encuentros / Órdenes), formularios con campos condicionales.  
Selects a cargar: tipo de orden.

---

### Rama C3 — Módulo Enfermera

> **Rama git sugerida:** `feat/enfermera`

#### C3.1 — Domain

`domain/signo.vital.entity.js`, `domain/administracion.entity.js`

#### C3.2 — Infrastructure

`infrastructure/enfermera.api.js` — POST /signos-vitales, POST /administracion-medicamentos

#### C3.3 — Application

`registrar.signos.usecase.js`, `registrar.administracion.usecase.js`

#### C3.4 — Presentation

`presentation/enfermera.controller.js` — maneja ambos formularios; `enfermeroId` desde `AuthStorage.getUserId()`

#### C3.5 — HTML

`public/pages/enfermera.html`: navbar Sura, `nav-tabs` (Signos vitales / Medicamentos), formularios simples.

---

### Rama C4 — Módulo Recursos Humanos

> **Rama git sugerida:** `feat/rrhh`

#### C4.1 — Domain

`domain/empleado.entity.js`, `domain/usuario.entity.js`

#### C4.2 — Infrastructure

`infrastructure/rrhh.api.js` — POST /empleados, POST /usuarios

#### C4.3 — Application

`registrar.empleado.usecase.js`  
`crear.usuario.usecase.js` — valida que contraseñas coincidan; calcula `bcrypt.hashSync(password, 10)` antes de llamar a la API

#### C4.4 — Presentation

`presentation/rrhh.controller.js` — maneja ambos formularios; advertencia visible sobre hash de contraseña

#### C4.5 — HTML

`public/pages/rrhh.html`: navbar Sura, `nav-tabs` (Empleados / Usuarios), incluir CDN `bcryptjs`.  
Selects a cargar: tipo documento, rol.

---

### Rama C5 — Módulo Soporte de Información

> **Rama git sugerida:** `feat/soporte`

#### C5.1 — Domain

`domain/recurso.entity.js` — entidad genérica que representa cualquier recurso CRUD

#### C5.2 — Infrastructure

`infrastructure/soporte.api.js` — `getAll(recurso)`, `getById(recurso, id)`, `create(recurso, body)`, `update(recurso, id, body)`, `remove(recurso, id)`

#### C5.3 — Application

`application/crud.usecase.factory.js` — fábrica que genera un objeto `{ listar, buscar, crear, actualizar, eliminar }` para cualquier recurso dado su path

#### C5.4 — Presentation

`presentation/crud.controller.js` — controlador genérico parametrizado:
- Menú lateral con los 16 recursos
- Tabla de listado con paginación básica
- Formulario único que se reconfigura por recurso (campos y labels dinámicos)
- Botón **[Buscar por ID]** → rellena formulario con `form.filler.js`
- Botones **[Guardar nuevo]** / **[Actualizar]** → POST / PUT
- Botón **[Borrar]** → `modal.confirm.js` → DELETE → refresca lista

#### C5.5 — HTML

`public/pages/soporte.html`: navbar Sura, sidebar izquierdo con los 16 recursos, área principal con el panel CRUD genérico.

**Los 16 recursos configurados:**

| Grupo | Recursos |
|---|---|
| Catálogos (PK Short) | cat-roles, cat-tipos-doc, cat-generos, cat-estados-cita, cat-prioridades, cat-estados-seguro, cat-tipos-orden, cat-tipos-detalle-orden, cat-tipos-facturacion, cat-estados-especialidad, config-facturacion |
| Maestras (PK Integer) | especialidades, empleados, usuarios, medicos-perfil, enfermeros-perfil |

---

## Paso 3 — Integración y pruebas cruzadas

> **Depende de:** todos los pasos anteriores merged en `main`  
> **No es paralelizable — requiere el proyecto completo integrado**

| # | Tarea |
|---|---|
| 3.1 | Levantar backend `clinic` (Spring Boot puerto 8083) |
| 3.2 | Levantar frontend `clinic-frontend` (`npm run dev`, puerto 3000) |
| 3.3 | Probar flujo de login con cada uno de los 5 usuarios de prueba |
| 3.4 | Verificar protección de rutas: acceder a URL de otro rol → debe redirigir a login |
| 3.5 | Verificar redirección automática si ya hay token válido al abrir login |
| 3.6 | Verificar expiración de token: manipular `clinic_token` en localStorage → petición siguiente debe limpiar sesión y redirigir |
| 3.7 | Probar cada formulario POST de todos los módulos con datos válidos |
| 3.8 | Probar cada PUT con búsqueda previa (GET → rellena form → actualizar) |
| 3.9 | Probar DELETE en módulo Soporte con confirmación del modal |
| 3.10 | Verificar carga de selects dinámicos en todos los formularios que los usan |
| 3.11 | Verificar que `usuarioOperador` nunca es null (siempre viene de `AuthStorage.getUserId()`) |
| 3.12 | Probar RRHH: contraseñas distintas → error; contraseñas iguales → hash bcrypt enviado al backend |
| 3.13 | Revisar consola de navegador: sin errores CORS, sin errores de módulo ES |

---

## Resumen de ramas y dependencias

```
main
 └── Paso 0: scaffold (bloqueante)
      ├── feat/shared     (Rama A — independiente)
      ├── feat/login      (Rama B — independiente, se conecta al shared al mergear)
      │
      └── [merge A + B → main]
           ├── feat/administrativo   (C1 — paralela)
           ├── feat/doctor           (C2 — paralela)
           ├── feat/enfermera        (C3 — paralela)
           ├── feat/rrhh             (C4 — paralela)
           └── feat/soporte          (C5 — paralela)
                └── [merge C1–C5 → main]
                     └── Paso 3: integración (secuencial)
```

---

## Conteo de artefactos por paso

| Paso | Archivos JS | Archivos HTML | Archivos CSS | Total |
|---|---|---|---|---|
| 0 — Scaffold | 0 | 1 (index.html) | 0 | **2** (+ server.js + package.json) |
| 1A — Shared | 7 | 0 | 1 | **8** |
| 1B — Login | 4 | 1 | 0 | **5** |
| 2 C1 — Administrativo | 16 | 1 | 0 | **17** |
| 2 C2 — Doctor | 9 | 1 | 0 | **10** |
| 2 C3 — Enfermera | 6 | 1 | 0 | **7** |
| 2 C4 — RRHH | 6 | 1 | 0 | **7** |
| 2 C5 — Soporte | 4 | 1 | 0 | **5** |
| **Total** | **52** | **6** | **1** | **61** |
