# 11. Estructura del Frontend — Clinic (Vanilla JS + Bootstrap + Node.js)

> Revisión 2 — Mayo 2026  
> Stack: Node.js + Express (servidor estático) · HTML5 · Bootstrap 5.3 · JavaScript ES2022 (módulos nativos)

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Servidor | Node.js 20 + Express 4 (servidor de archivos estáticos) |
| Plantillas | HTML5 |
| Estilos | Bootstrap 5.3 (CDN) + CSS personalizado |
| Lógica | JavaScript ES2022 (`type="module"`, sin bundler) |
| Peticiones HTTP | `fetch` API nativa |
| Hash contraseñas | bcryptjs 2.4 (CDN, solo módulo RRHH) |
| Almacenamiento | `localStorage` (token JWT, rol, usuario, ID) |
| Arquitectura | Clean Architecture por módulo de rol |

> No se usa ningún framework JS (sin React, Vue ni Angular). Cada módulo de rol implementa
> sus propias capas de dominio, aplicación, infraestructura y presentación con módulos ES nativos.

---

## Arquitectura por módulo (Clean Architecture)

Cada módulo de rol implementa cuatro capas aisladas:

```
modules/<rol>/
├── domain/          ← Entidades: clases/objetos que representan conceptos del dominio
├── application/     ← Casos de uso: orquestan la lógica sin tocar DOM ni fetch directamente
├── infrastructure/  ← Adaptadores: clientes HTTP, localStorage
└── presentation/    ← Controladores: leen/escriben el DOM y llaman a los use cases
```

### Responsabilidades por capa

| Capa | Responsabilidad | No debe hacer |
|---|---|---|
| `domain` | Definir la forma de cada entidad y validaciones simples | Conocer `fetch`, DOM ni `localStorage` |
| `application` | Ejecutar el caso de uso; llama a infrastructure y retorna datos | Manipular el DOM directamente |
| `infrastructure` | Hacer llamadas HTTP, manejar `localStorage` | Contener lógica de negocio |
| `presentation` | Capturar eventos del DOM, llamar use cases, mostrar resultados | Hacer `fetch` directamente |

---

## Árbol de carpetas

```
clinic-frontend/
├── package.json
├── server.js                           ← Express: sirve public/ en puerto 3000
│
└── public/
    ├── index.html                      ← Redirige a /pages/login.html
    │
    ├── assets/
    │   ├── css/
    │   │   └── styles.css              ← Estilos globales complementarios a Bootstrap
    │   └── img/
    │       └── logo.png
    │
    ├── pages/                          ← Una página HTML por rol
    │   ├── login.html
    │   ├── administrativo.html
    │   ├── doctor.html
    │   ├── enfermera.html
    │   ├── rrhh.html
    │   └── soporte.html
    │
    └── js/
        │
        ├── shared/                     ← Código transversal a todos los módulos
        │   ├── infrastructure/
        │   │   ├── http.client.js      ← Wrapper fetch: adjunta token, intercepta 401/403
        │   │   ├── auth.storage.js     ← CRUD sobre localStorage (token, rol, usuario)
        │   │   └── router.guard.js     ← Protección de páginas: verifica token y rol
        │   └── components/
        │       ├── navbar.js           ← Navbar con usuario activo y botón logout
        │       ├── modal.confirm.js    ← Modal Bootstrap de confirmación para DELETE
        │       ├── result.banner.js    ← Alerta Bootstrap éxito/error (SpResultadoResponse)
        │       └── form.filler.js      ← Rellena campos de un form con un objeto de datos
        │
        ├── login/
        │   ├── domain/
        │   │   └── auth.entity.js           ← { token, rol, usuario }
        │   ├── application/
        │   │   └── login.usecase.js         ← login(username, password): AuthEntity
        │   ├── infrastructure/
        │   │   └── auth.api.js              ← POST /api/auth/login
        │   └── presentation/
        │       └── login.controller.js      ← Captura form; guarda token; redirige por rol
        │
        ├── administrativo/
        │   ├── domain/
        │   │   ├── paciente.entity.js
        │   │   ├── cita.entity.js
        │   │   └── factura.entity.js
        │   ├── application/
        │   │   ├── registrar.paciente.usecase.js
        │   │   ├── registrar.contacto.usecase.js
        │   │   ├── registrar.seguro.usecase.js
        │   │   ├── programar.cita.usecase.js
        │   │   ├── reprogramar.cita.usecase.js
        │   │   ├── cancelar.cita.usecase.js
        │   │   ├── calcular.copago.usecase.js
        │   │   ├── emitir.factura.usecase.js
        │   │   └── registrar.pago.usecase.js
        │   ├── infrastructure/
        │   │   └── administrativo.api.js
        │   └── presentation/
        │       ├── paciente.controller.js      ← Maneja pestañas de pacientes
        │       ├── cita.controller.js          ← Maneja pestañas de citas
        │       └── facturacion.controller.js   ← Maneja pestañas de facturación
        │
        ├── doctor/
        │   ├── domain/
        │   │   ├── encuentro.entity.js
        │   │   └── orden.entity.js
        │   ├── application/
        │   │   ├── abrir.encuentro.usecase.js
        │   │   ├── cerrar.encuentro.usecase.js
        │   │   ├── registrar.orden.usecase.js
        │   │   └── agregar.detalle.usecase.js
        │   ├── infrastructure/
        │   │   └── doctor.api.js
        │   └── presentation/
        │       ├── encuentro.controller.js
        │       └── orden.controller.js
        │
        ├── enfermera/
        │   ├── domain/
        │   │   ├── signo.vital.entity.js
        │   │   └── administracion.entity.js
        │   ├── application/
        │   │   ├── registrar.signos.usecase.js
        │   │   └── registrar.administracion.usecase.js
        │   ├── infrastructure/
        │   │   └── enfermera.api.js
        │   └── presentation/
        │       └── enfermera.controller.js
        │
        ├── rrhh/
        │   ├── domain/
        │   │   ├── empleado.entity.js
        │   │   └── usuario.entity.js
        │   ├── application/
        │   │   ├── registrar.empleado.usecase.js
        │   │   └── crear.usuario.usecase.js     ← Calcula hash bcrypt antes de enviar
        │   ├── infrastructure/
        │   │   └── rrhh.api.js
        │   └── presentation/
        │       └── rrhh.controller.js
        │
        └── soporte/
            ├── domain/
            │   └── recurso.entity.js            ← Representa cualquier recurso CRUD
            ├── application/
            │   └── crud.usecase.factory.js      ← Fábrica: genera un use case CRUD por recurso
            ├── infrastructure/
            │   └── soporte.api.js               ← CRUD genérico: get/getById/create/update/remove
            └── presentation/
                └── crud.controller.js           ← Controlador genérico para todos los recursos
```

---

## server.js (Node.js + Express)

```js
// server.js
const express = require('express');
const path    = require('path');

const app  = express();
const PORT = process.env.PORT || 3000;

// Servir todos los archivos estáticos de public/
app.use(express.static(path.join(__dirname, 'public')));

// Cualquier ruta no reconocida → index.html (que redirige a login)
app.get('*', (_req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Clinic Frontend → http://localhost:${PORT}`);
});
```

**package.json:**

```json
{
  "name": "clinic-frontend",
  "version": "1.0.0",
  "scripts": {
    "start": "node server.js",
    "dev":   "nodemon server.js"
  },
  "dependencies": {
    "express": "^4.19.2"
  },
  "devDependencies": {
    "nodemon": "^3.1.0"
  }
}
```

---

## LocalStorage — Gestión del token

**Archivo:** `js/shared/infrastructure/auth.storage.js`

| Clave `localStorage` | Valor | Descripción |
|---|---|---|
| `clinic_token` | `string` | JWT Bearer Token |
| `clinic_rol` | `string` | Rol del usuario (ej. `administrativo`) |
| `clinic_usuario` | `string` | Código de usuario (ej. `admin01`) |
| `clinic_usuario_id` | `number` | ID del empleado — se usa como `usuarioOperador` |

```js
// auth.storage.js
export const AuthStorage = {

  save({ token, rol, usuario }) {
    localStorage.setItem('clinic_token',    token);
    localStorage.setItem('clinic_rol',      rol);
    localStorage.setItem('clinic_usuario',  usuario);
  },

  saveUserId(id) { localStorage.setItem('clinic_usuario_id', id); },

  getToken()   { return localStorage.getItem('clinic_token'); },
  getRol()     { return localStorage.getItem('clinic_rol'); },
  getUsuario() { return localStorage.getItem('clinic_usuario'); },
  getUserId()  { return Number(localStorage.getItem('clinic_usuario_id')); },

  clear() {
    ['clinic_token','clinic_rol','clinic_usuario','clinic_usuario_id']
      .forEach(k => localStorage.removeItem(k));
  },

  isTokenExpired() {
    const token = this.getToken();
    if (!token) return true;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return Date.now() >= payload.exp * 1000;
    } catch { return true; }
  }
};
```

---

## Guarda de rutas — router.guard.js

**Archivo:** `js/shared/infrastructure/router.guard.js`

Se llama **al inicio de cada página HTML protegida** (las cinco páginas de rol). Verifica:

1. Existencia del token en `localStorage`.
2. Que el token no haya expirado (decodificando el payload base64 del JWT sin librerías).
3. Que el rol almacenado coincida con el `rolRequerido` de esa página.

Si alguna verificación falla → `AuthStorage.clear()` → `window.location.href = '/pages/login.html'`.

```js
// router.guard.js
import { AuthStorage } from './auth.storage.js';

export function guardPage(rolRequerido) {
  if (AuthStorage.isTokenExpired()) {
    AuthStorage.clear();
    window.location.replace('/pages/login.html');
    return false;
  }
  if (AuthStorage.getRol() !== rolRequerido) {
    window.location.replace('/pages/login.html');
    return false;
  }
  return true;
}
```

**Uso al inicio de cada página:**

```html
<!-- administrativo.html — primer script type="module" -->
<script type="module">
  import { guardPage } from '../js/shared/infrastructure/router.guard.js';
  if (!guardPage('administrativo')) throw new Error('Acceso denegado');
</script>
```

---

## Cliente HTTP — http.client.js

**Archivo:** `js/shared/infrastructure/http.client.js`

Wrapper sobre `fetch` que:

- Adjunta `Authorization: Bearer <token>` en cada petición.
- Intercepta **401 / 403** → `AuthStorage.clear()` → redirige a login.
- Lanza error con el mensaje del servidor en caso de HTTP 422 (regla de negocio).

```js
// http.client.js
import { AuthStorage } from './auth.storage.js';

const BASE_URL = 'http://localhost:8083';

async function request(method, path, body = null) {
  const headers = { 'Content-Type': 'application/json' };
  const token = AuthStorage.getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const options = { method, headers };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(`${BASE_URL}${path}`, options);

  // Token vencido o sin permiso → volver al login
  if (res.status === 401 || res.status === 403) {
    AuthStorage.clear();
    window.location.replace('/pages/login.html');
    throw new Error('Sesión expirada');
  }

  const data = await res.json();

  // Error de regla de negocio (422) → el mensaje viene en el body
  if (!res.ok) throw new Error(data.mensaje ?? `Error ${res.status}`);

  return data;
}

export const http = {
  get:    (path)       => request('GET',    path),
  post:   (path, body) => request('POST',   path, body),
  put:    (path, body) => request('PUT',    path, body),
  delete: (path)       => request('DELETE', path),
};
```

---

## Comportamiento esperado por módulo

### Login (`/pages/login.html`)

1. Al cargar la página: si existe un token válido en `localStorage` → redirige directamente a la página del rol (no permite volver a login si ya hay sesión activa).
2. El formulario captura `username` y `password`.
3. Llama `login.usecase.js` → `auth.api.js` → `POST /api/auth/login`.
4. **Éxito:** guarda token, rol y usuario en `localStorage` → redirige según `rol`:
   - `administrativo` → `/pages/administrativo.html`
   - `medico` → `/pages/doctor.html`
   - `enfermeria` → `/pages/enfermera.html`
   - `recursos_humanos` → `/pages/rrhh.html`
   - `soporte_informacion` → `/pages/soporte.html`
5. **Error 401:** muestra alerta Bootstrap "Credenciales incorrectas".
6. **Error de red:** muestra alerta "No se pudo conectar al servidor".

---

### Administrativo (`/pages/administrativo.html`)

`guardPage('administrativo')` → si falla → login.

Navegación interna con Bootstrap `nav-tabs`. Pestañas:

**Pacientes**
- Formulario *Registrar paciente* (POST) — campos: cedula, tipoDocId (select), nombreCompleto, fechaNacimiento, generoId (select), dirección, teléfono, correo
- Formulario *Registrar contacto de emergencia* (POST) — campos: pacienteId, nombreCompleto, relacion, telefono
- Formulario *Registrar seguro médico* (POST) — campos: pacienteId, compania, numeroPoliza, estadoSeguroId (select), fechaVigencia

**Citas**
- Formulario *Programar cita* (POST) — campos: pacienteId, medicoId, fechaHora, prioridadId (select), motivo
- Formulario *Reprogramar cita* (PUT) — campo "ID cita" + botón **[Buscar]** rellena formulario → editar → **[Actualizar]**
- Formulario *Cancelar cita* (PUT) — campo citaId + botón **[Cancelar cita]**

**Facturación**
- Panel *Calcular copago* (GET) — inputs pacienteId + tipoFactId + botón **[Calcular]** → muestra `valorCopago`, `exento`, `mensaje`
- Formulario *Emitir factura* (POST) — campos: encuentroId, pacienteId, medicoId, seguroId (opcional), tipoFactId (select)
- Formulario *Registrar pago* (POST) — campos: facturaId, valorPagado, tipoPago (select), referencia

El campo `usuarioOperador` se lee automáticamente de `AuthStorage.getUserId()` en todos los formularios.

---

### Doctor (`/pages/doctor.html`)

`guardPage('medico')` → si falla → login.

**Encuentros**
- Formulario *Abrir encuentro* (POST) — campos: citaId, pacienteId, motivoConsulta, sintomatologia; `medicoId` y `usuarioOperador` desde sesión
- Formulario *Cerrar encuentro* (PUT) — campo "ID encuentro" + botón **[Buscar]** rellena diagnóstico/tratamiento existente → editar → **[Cerrar encuentro]**

**Órdenes médicas**
- Formulario *Registrar orden* (POST) — campos: numeroOrden, encuentroId, pacienteId, tipoOrdenId (select); `medicoId` desde sesión
- Formulario *Agregar detalle* (POST) — campos: ordenId, tipoDetalle (select), item, referenciaId, dosis, duración, cantidad, frecuencia, requiereEsp (checkbox), especialidadId (se muestra si requiereEsp=true), costo

---

### Enfermera (`/pages/enfermera.html`)

`guardPage('enfermeria')` → si falla → login.

**Signos vitales**
- Formulario *Registrar signos vitales* (POST) — campos: encuentroId, presion, temperatura, pulso, oxigeno; `enfermeroId` y `usuarioOperador` desde sesión

**Medicamentos**
- Formulario *Registrar administración* (POST) — campos: encuentroId, medicamentoId, dosis, observacion; `enfermeroId` desde sesión

---

### Recursos Humanos (`/pages/rrhh.html`)

`guardPage('recursos_humanos')` → si falla → login.

**Empleados**
- Formulario *Registrar empleado* (POST) — campos: cedula, tipoDocId (select), nombreCompleto, correo, telefono, fechaNacimiento, dirección, rolId (select)

**Usuarios**
- Formulario *Crear usuario operativo* (POST):
  - Campos: empleadoId, codigoUsuario, contraseña (input password), confirmar contraseña (input password)
  - Antes de enviar: `crear.usuario.usecase.js` valida que las contraseñas coincidan y computa `bcrypt.hashSync(password, 10)` usando la librería `bcryptjs` cargada por CDN
  - El campo `contrasenaHash` que llega al backend **nunca contiene la contraseña en texto plano**
  - Se muestra advertencia visible: "La contraseña no se almacena. Solo se enviará el hash."

---

### Soporte de Información (`/pages/soporte.html`)

`guardPage('soporte_informacion')` → si falla → login.

Menú lateral con los 16 recursos. Todos usan el mismo componente `crud.controller.js` parametrizado:

```
┌───────────────────────────────────────────────────────┐
│ [Buscar por ID: ____ ]  [Buscar]                      │  GET /{recurso}/{id}
│  ↓ Rellena el formulario con los datos del registro   │
│                                                       │
│  ┌─────────────────────────────────────────────────┐  │
│  │  campo1: [________________________]             │  │
│  │  campo2: [________________________]             │  │
│  └─────────────────────────────────────────────────┘  │
│  [Guardar nuevo]          [Actualizar ID: ___ ]       │  POST / PUT /{recurso}/{id}
│  ─────────────────────────────────────────────────    │
│  [↺ Recargar lista]                                   │  GET /{recurso}
│  ┌──────┬────────────┬───────────┬─────────────────┐  │
│  │ ID   │ Campo 1    │ Campo 2   │ Acciones        │  │
│  ├──────┼────────────┼───────────┼─────────────────┤  │
│  │ 1    │ valor      │ valor     │ [Editar][Borrar] │  │  PUT / DELETE
│  └──────┴────────────┴───────────┴─────────────────┘  │
└───────────────────────────────────────────────────────┘
```

- **[Editar]:** carga el registro en el formulario; cambia "Guardar nuevo" por "Actualizar"; rellena ID en el campo de actualización.
- **[Borrar]:** abre `modal.confirm.js` → al confirmar → DELETE → refresca la lista.
- **[Buscar]:** GET por ID → rellena el formulario con `form.filler.js`.

---

## Manejo de errores en formularios

Todos los formularios siguen este flujo en el evento `submit`:

1. Prevenir envío (`e.preventDefault()`).
2. Validar campos requeridos con la Constraint Validation API del navegador (`reportValidity()`).
3. Deshabilitar el botón mientras la petición está en curso (`btn.disabled = true`).
4. Llamar al use case correspondiente.
5. Al recibir respuesta:
   - **Éxito (`exitoso: true`):** `result.banner.js` muestra alerta verde con `mensaje` e `idGenerado`.
   - **Error de negocio (lanzado por `http.client.js` desde HTTP 422):** alerta roja con el mensaje.
   - **Error 401/403:** `http.client.js` redirige automáticamente a login; el controlador no necesita manejarlo.
   - **Error de red:** alerta roja "No se pudo conectar al servidor".
6. Re-habilitar el botón (`btn.disabled = false`).

---

## Carga de selectores desde la API

Los `<select>` dependientes de catálogos se cargan en el evento `DOMContentLoaded` de cada página (usando el token de sesión, que es válido para cualquier rol autenticado):

| Select | Endpoint | `value` | `textContent` |
|---|---|---|---|
| Tipo de documento | `GET /api/soporte/cat-tipos-doc` | `tipoDocId` | `descripcion` |
| Género | `GET /api/soporte/cat-generos` | `generoId` | `descripcion` |
| Estado de seguro | `GET /api/soporte/cat-estados-seguro` | `estadoSeguroId` | `descripcion` |
| Prioridad de atención | `GET /api/soporte/cat-prioridades` | `prioridadId` | `descripcion` |
| Tipo de orden | `GET /api/soporte/cat-tipos-orden` | `tipoOrdenId` | `descripcion` |
| Tipo de facturación | `GET /api/soporte/cat-tipos-facturacion` | `tipoFactId` | `descripcion` |
| Rol (solo RRHH) | `GET /api/soporte/cat-roles` | `rolId` | `descripcion` |

---

## Protección de rutas — resumen

| Página | Rol requerido | Guard aplicado | Redirección si falla |
|---|---|---|---|
| `login.html` | *(público)* | Redirige a página de rol si ya hay token válido | — |
| `administrativo.html` | `administrativo` | `guardPage('administrativo')` | `/pages/login.html` |
| `doctor.html` | `medico` | `guardPage('medico')` | `/pages/login.html` |
| `enfermera.html` | `enfermeria` | `guardPage('enfermeria')` | `/pages/login.html` |
| `rrhh.html` | `recursos_humanos` | `guardPage('recursos_humanos')` | `/pages/login.html` |
| `soporte.html` | `soporte_informacion` | `guardPage('soporte_informacion')` | `/pages/login.html` |

Un usuario con rol `medico` que intente acceder directamente a `/pages/administrativo.html` será redirigido a login.  
Un token vencido en cualquier página protegida se detecta tanto en el guard al cargar como en el interceptor HTTP al hacer cualquier petición.

---

## Logout

Disponible en el navbar de todas las páginas protegidas (`navbar.js`):

```js
document.getElementById('btn-logout').addEventListener('click', () => {
  AuthStorage.clear();
  window.location.replace('/pages/login.html');
});
```

---

## CDN requeridos en cada página HTML

```html
<!-- Bootstrap CSS -->
<link rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">

<!-- Bootstrap Icons (opcional) -->
<link rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

<!-- Estilos propios -->
<link rel="stylesheet" href="../assets/css/styles.css">

<!-- Bootstrap JS Bundle (incluye Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- bcryptjs — SOLO en rrhh.html -->
<script src="https://cdn.jsdelivr.net/npm/bcryptjs@2.4.3/dist/bcrypt.min.js"></script>

<!-- Módulo de entrada del rol (type="module" habilita ES imports) -->
<script type="module" src="../js/administrativo/presentation/paciente.controller.js"></script>
```

> Los módulos JS del mismo rol se importan entre sí con `import` relativo.  
> `type="module"` activa el scope de módulo y habilita `import/export` sin bundler.

---

## Inicialización del proyecto

```bash
# 1. Crear carpeta e inicializar Node.js
mkdir clinic-frontend && cd clinic-frontend
npm init -y

# 2. Instalar dependencias
npm install express
npm install --save-dev nodemon

# 3. Crear estructura de carpetas
mkdir -p public/assets/css public/assets/img public/pages public/js/shared/infrastructure
mkdir -p public/js/shared/components
mkdir -p public/js/login/domain public/js/login/application public/js/login/infrastructure public/js/login/presentation
# (repetir para administrativo, doctor, enfermera, rrhh, soporte)

# 4. Crear server.js (ver sección anterior)

# 5. Arrancar en modo desarrollo
npx nodemon server.js

# 6. Abrir navegador
# http://localhost:3000  →  redirige a /pages/login.html
```

---

## Resumen de artefactos JS por módulo

| Módulo | Domain | Application | Infrastructure | Presentation | Total JS |
|---|---|---|---|---|---|
| Shared | — | — | 3 | 4 | **7** |
| Login | 1 | 1 | 1 | 1 | **4** |
| Administrativo | 3 | 9 | 1 | 3 | **16** |
| Doctor | 2 | 4 | 1 | 2 | **9** |
| Enfermera | 2 | 2 | 1 | 1 | **6** |
| RRHH | 2 | 2 | 1 | 1 | **6** |
| Soporte | 1 | 1 | 1 | 1 | **4** |
| **Total** | **11** | **19** | **9** | **13** | **52** |

| Artefactos HTML/CSS | Cantidad |
|---|---|
| Páginas HTML | 6 (login + 5 roles) |
| Hojas de estilo CSS | 1 (styles.css global) |
| server.js | 1 |
| package.json | 1 |
