# Resumen de sesión — Backend Spring Boot Clínica IPS

**Fecha:** 8 de mayo de 2026  
**Proyecto:** `clinic/` — Spring Boot 4.0.6, Java 17, MySQL 8 (`clinica_ips`)  
**Estado final:** Aplicación arranca correctamente en `http://localhost:8083`

---

## Índice

1. [Contexto del proyecto](#1-contexto-del-proyecto)
2. [Infraestructura base — Entidades, Repositorios y Servicios](#2-infraestructura-base--entidades-repositorios-y-servicios)
3. [Capa de aplicación — Use Cases y DTOs](#3-capa-de-aplicación--use-cases-y-dtos)
4. [Capa de presentación — Controladores REST](#4-capa-de-presentación--controladores-rest)
5. [Sistema de autenticación JWT](#5-sistema-de-autenticación-jwt)
6. [Seguridad — Spring Security 7](#6-seguridad--spring-security-7)
7. [Migración SQL — Usuarios de autenticación](#7-migración-sql--usuarios-de-autenticación)
8. [Corrección de errores de compilación y startup](#8-corrección-de-errores-de-compilación-y-startup)
9. [Resumen de archivos creados o modificados](#9-resumen-de-archivos-creados-o-modificados)

---

## 1. Contexto del proyecto

### Base de datos
La base de datos `clinica_ips` ya existía con toda su estructura: 11 tablas catálogo, 11 tablas maestras, 12 tablas transaccionales, triggers, procedimientos almacenados, vistas y tablas de auditoría. El backend no debe generar DDL — sólo conectarse y validar contra el esquema existente.

### Tecnologías
| Componente | Versión |
|---|---|
| Spring Boot | 4.0.6 |
| Spring Framework | 7.0.7 |
| Spring Security | 7.x |
| Hibernate ORM | 7.2.12.Final |
| JJWT | 0.12.6 |
| MySQL Connector/J | (managed by Boot) |
| Lombok | 1.18.46 |
| Java (compile target) | 17 |
| Java (runtime) | 21.0.10 |
| Puerto | 8083 |

### Configuración (`application.properties`)
```properties
spring.application.name=clinic
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_ips?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.default_schema=clinica_ips
app.jwt.secret=clinica-ips-super-secret-key-must-be-32-chars!!
app.jwt.expiration-ms=86400000
server.port=8083
```

---

## 2. Infraestructura base — Entidades, Repositorios y Servicios

### Prompt
> *"Crea todas las entidades JPA, repositorios Spring Data y servicios de procedimientos almacenados para el proyecto Spring Boot, basándote en el modelo de la base de datos clinica_ips."*

### Qué se hizo

#### 34 Entidades JPA

Se crearon en tres paquetes según la capa del modelo:

**`app.domain.entities.catalogs`** (11 entidades — PK tipo `Short`):
- `CatRolUsuario`, `CatTipoDocumento`, `CatGenero`, `CatEstadoCita`
- `CatPrioridadAtencion`, `CatEstadoSeguro`, `CatTipoOrden`, `CatTipoDetalleOrden`
- `CatTipoFacturacion`, `CatEstadoEspecialidad`, `ConfigFacturacion`

**`app.domain.entities.masters`** (11 entidades — PK tipo `Integer`):
- `Especialidad`, `Empleado`, `SeguridadUsuario`, `MedicoPerfil`, `EnfermeroPerfil`
- `Paciente`, `ContactoEmergencia`, `SeguroMedico`
- `MedicamentoCatalogo`, `ProcedimientoCatalogo`, `AyudaDiagnosticaCatalogo`

**`app.domain.entities.transactions`** (12 entidades — PK tipo `Integer`):
- `Cita`, `EncuentroClinico`, `SignoVital`, `AdministracionMedicamento`
- `ProcedimientoEnfermeria`, `OrdenMedica`
- `OrdenMedicamentoDetalle`, `OrdenProcedimientoDetalle`, `OrdenAyudaDiagnosticaDetalle`
- `Factura`, `FacturaDetalle`, `Pago`

**Convenciones aplicadas en todas las entidades:**
- `@Entity @Table(name = "...")` con nombre exacto de la tabla MySQL
- `@Getter @Setter @NoArgsConstructor` de Lombok (en lugar de `@Data` para evitar conflictos)
- Columnas autogeneradas por la BD (`created_at`, etc.) marcadas con `@Column(insertable = false, updatable = false)`
- Columnas `TEXT` con `@Column(columnDefinition = "TEXT")`
- Relaciones `@ManyToOne @JoinColumn(name = "fk_col")` para claves foráneas

#### 34 Repositorios Spring Data JPA

Un repositorio por entidad en `app.domain.repositories.*`, todos extendiendo `JpaRepository<Entidad, TipoPK>`.

Repositorio especial en `SeguridadUsuarioRepository`:
```java
@Query("SELECT u FROM SeguridadUsuario u JOIN FETCH u.empleado e JOIN FETCH e.rol WHERE u.codigoUsuario = :codigo AND u.activo = true")
Optional<SeguridadUsuario> findActivoConRol(@Param("codigo") String codigo);
```

#### 17 Servicios de Procedimientos Almacenados

Ubicados en `app.domain.services.*`, cada uno inyecta `EntityManager` y usa `StoredProcedureQuery` para llamar SPs de MySQL con parámetros `IN`/`OUT`:

| Servicio | SP MySQL |
|---|---|
| `RegistrarPacienteService` | `sp_registrar_paciente` |
| `RegistrarContactoEmergenciaService` | `sp_registrar_contacto_emergencia` |
| `RegistrarSeguroMedicoService` | `sp_registrar_seguro_medico` |
| `ProgramarCitaService` | `sp_programar_cita` |
| `ReprogramarCitaService` | `sp_reprogramar_cita` |
| `CancelarCitaService` | `sp_cancelar_cita` |
| `AbrirEncuentroClinicoService` | `sp_abrir_encuentro_clinico` |
| `CerrarEncuentroClinicoService` | `sp_cerrar_encuentro_clinico` |
| `RegistrarOrdenMedicaService` | `sp_registrar_orden_medica` |
| `AgregarDetalleOrdenService` | `sp_agregar_detalle_orden` |
| `RegistrarSignosVitalesService` | `sp_registrar_signos_vitales` |
| `RegistrarProcedimientoEnfermeriaService` | `sp_registrar_procedimiento_enfermeria` |
| `AdministrarMedicamentoService` | `sp_administrar_medicamento` |
| `EmitirFacturaService` | `sp_emitir_factura` |
| `RegistrarPagoService` | `sp_registrar_pago` |
| `CalcularCopagoService` | `sp_calcular_copago` |
| `CrearUsuarioOperativoService` | `sp_crear_usuario_operativo` |

#### 34 Servicios CRUD

En `app.domain.services.soporte.*` — un servicio CRUD por entidad que usa el repositorio JPA para operaciones `findAll`, `findById`, `save`, `deleteById`.

#### DTOs internos de dominio

- `app.domain.dto.SpResultado` — resultado genérico de SPs con parámetros `OUT`: `codigo (Short)`, `mensaje (String)`, `idGenerado (Integer)`, método `isExitoso()` (código == 0)
- `app.domain.dto.SpResultadoCopago` — resultado del SP de cálculo de copago: `valorCopago (BigDecimal)`, `exento (Boolean)`, `mensaje (String)`

#### Manejo de excepciones de dominio

- `app.domain.exception.ReglaDeNegocioException` → HTTP 409 Conflict
- `app.domain.exception.RecursoNoEncontradoException` → HTTP 404 Not Found

---

## 3. Capa de aplicación — Use Cases y DTOs

### Prompt
> *"Crea los use cases de aplicación y los DTOs de request/response que los controladores REST necesitan para cada rol."*

### Qué se hizo

#### 5 Use Cases (uno por rol)

En `app.application.usecase.*`:

| Use Case | Rol | Operaciones principales |
|---|---|---|
| `AdministrativoUseCase` | administrativo | Registrar paciente, contacto emergencia, seguro médico; programar/reprogramar/cancelar cita; emitir factura; registrar pago; calcular copago |
| `DoctorUseCase` | medico | Abrir/cerrar encuentro clínico; registrar orden médica; agregar detalle a orden |
| `EnfermeraUseCase` | enfermeria | Registrar signos vitales, procedimiento de enfermería, administrar medicamento |
| `RecursosHumanosUseCase` | recursos_humanos | Crear usuario operativo |
| `SoporteUseCase` | soporte_informacion | CRUD completo de todas las 34 entidades |

#### DTOs de Request (con validación `jakarta.validation`)

| DTO | Campos principales |
|---|---|
| `RegistrarPacienteRequest` | cedula, tipoDocId, nombreCompleto, fechaNacimiento, generoId, direccion, telefono, correo |
| `RegistrarContactoEmergenciaRequest` | pacienteId, nombreContacto, parentesco, telefono |
| `RegistrarSeguroMedicoRequest` | pacienteId, numeroPoliza, aseguradora, estadoSeguroId |
| `ProgramarCitaRequest` | pacienteId, medicoId, especialidadId, fechaHora, motivoConsulta, prioridadId |
| `ReprogramarCitaRequest` | citaId, nuevaFechaHora, motivo |
| `CancelarCitaRequest` | citaId, motivo |
| `AbrirEncuentroRequest` | citaId, medicoId, motivoConsulta |
| `CerrarEncuentroRequest` | encuentroId, diagnostico, planTratamiento |
| `RegistrarOrdenMedicaRequest` | encuentroId, tipoOrdenId |
| `AgregarDetalleOrdenRequest` | ordenId, tipoDetalleId, itemId, cantidad, indicaciones |
| `RegistrarSignosVitalesRequest` | encuentroId, enfermeraId, presionSistolica, presionDiastolica, frecuenciaCardiaca, temperatura, saturacionO2, peso, talla |
| `EmitirFacturaRequest` | citaId, tipoFactId |
| `RegistrarPagoRequest` | facturaId, montoPagado, metodoPago |
| `CrearUsuarioOperativoRequest` | empleadoId, codigoUsuario, passwordHash, rolId |
| `LoginRequest` | username, password |

#### DTOs de Response

| DTO | Campos |
|---|---|
| `SpResultadoResponse` | codigo (Short), mensaje (String), idGenerado (Integer) |
| `CopagoResponse` | valorCopago (BigDecimal), exento (Boolean), mensaje (String) |
| `LoginResponse` | token (String), rol (String), usuario (String) |

#### Manejador global de excepciones

`app.infrastructure.exception.GlobalExceptionHandler` (`@RestControllerAdvice`):

| Excepción | Código HTTP |
|---|---|
| `MethodArgumentNotValidException` | 400 Bad Request |
| `UsernameNotFoundException` / `BadCredentialsException` | 401 Unauthorized |
| `AccessDeniedException` | 403 Forbidden |
| `RecursoNoEncontradoException` | 404 Not Found |
| `ReglaDeNegocioException` | 409 Conflict |
| `Exception` (genérico) | 500 Internal Server Error |

---

## 4. Capa de presentación — Controladores REST

### Prompt
> *"Crea los controladores REST con los endpoints para cada rol, protegidos por roles JWT."*

### Qué se hizo

6 controladores en `app.infrastructure.controller.*`:

#### `AuthController` — `/api/auth`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Autenticar con username/password → retorna JWT |

**Ejemplo de request:**
```json
{ "username": "medico", "password": "123456" }
```
**Ejemplo de response:**
```json
{ "token": "eyJ...", "rol": "medico", "usuario": "medico" }
```

#### `AdministrativoController` — `/api/administrativo` (rol: `administrativo`)
| Método | Endpoint | Body |
|---|---|---|
| POST | `/paciente` | `RegistrarPacienteRequest` |
| POST | `/contacto-emergencia` | `RegistrarContactoEmergenciaRequest` |
| POST | `/seguro-medico` | `RegistrarSeguroMedicoRequest` |
| POST | `/cita` | `ProgramarCitaRequest` |
| PUT | `/cita/reprogramar` | `ReprogramarCitaRequest` |
| PUT | `/cita/cancelar` | `CancelarCitaRequest` |
| POST | `/factura` | `EmitirFacturaRequest` |
| POST | `/pago` | `RegistrarPagoRequest` |
| GET | `/copago` | params: `pacienteId`, `tipoFactId` |

#### `DoctorController` — `/api/doctor` (rol: `medico`)
| Método | Endpoint | Body |
|---|---|---|
| POST | `/encuentro/abrir` | `AbrirEncuentroRequest` |
| PUT | `/encuentro/cerrar` | `CerrarEncuentroRequest` |
| POST | `/orden` | `RegistrarOrdenMedicaRequest` |
| POST | `/orden/detalle` | `AgregarDetalleOrdenRequest` |

#### `EnfermeraController` — `/api/enfermera` (rol: `enfermeria`)
| Método | Endpoint | Body |
|---|---|---|
| POST | `/signos-vitales` | `RegistrarSignosVitalesRequest` |

#### `RecursosHumanosController` — `/api/rrhh` (rol: `recursos_humanos`)
| Método | Endpoint | Body |
|---|---|---|
| POST | `/usuario` | `CrearUsuarioOperativoRequest` |

#### `SoporteController` — `/api/soporte` (rol: `soporte_informacion`)
CRUD completo de 34 entidades — endpoints `GET /api/soporte/{entidad}`, `GET /api/soporte/{entidad}/{id}`, `POST`, `PUT`, `DELETE`.

---

## 5. Sistema de autenticación JWT

### Prompt
> *"Implementa autenticación JWT: el empleado hace login con su código de usuario y contraseña, y recibe un token JWT que incluye su rol. El token debe usarse en cada petición posterior para autorización."*

### Arquitectura JWT

```
POST /api/auth/login
  │
  ├─ AuthController  →  AuthenticationManager.authenticate(UsernamePasswordAuthenticationToken)
  │                         │
  │                         └─ DaoAuthenticationProvider
  │                               │
  │                               ├─ UserDetailsServiceImpl.loadUserByUsername(username)
  │                               │     └─ SeguridadUsuarioRepository.findActivoConRol(username)
  │                               │           (JOIN FETCH empleado JOIN FETCH rol)
  │                               │
  │                               └─ BCryptPasswordEncoder.matches(rawPassword, storedHash)
  │
  └─ JwtService.generateToken(username, rol)  →  LoginResponse { token, rol, usuario }

Peticiones protegidas:
  Authorization: Bearer <token>
  │
  └─ JwtAuthFilter (OncePerRequestFilter)
        ├─ JwtService.isValid(token)
        └─ SecurityContextHolder ← UsernamePasswordAuthenticationToken(username, ROLE_<rol>)
```

### Archivos creados

#### `app/infrastructure/security/JwtService.java`
```java
// Inyecta:
@Value("${app.jwt.secret}") String secret
@Value("${app.jwt.expiration-ms:86400000}") long expirationMs

// Métodos:
String generateToken(String username, String rol)
String extractUsername(String token)
String extractRol(String token)
boolean isValid(String token)

// API JJWT 0.12.6:
Jwts.builder().subject(username).claim("rol", rol)
    .issuedAt(new Date()).expiration(new Date(now + expirationMs))
    .signWith(Keys.hmacShaKeyFor(secret.getBytes(UTF_8))).compact()

Jwts.parser().verifyWith(secretKey).build()
    .parseSignedClaims(token).getPayload()
```

#### `app/infrastructure/security/UserDetailsServiceImpl.java`
```java
// Implementa UserDetailsService
// Carga usuario activo con su rol (JOIN FETCH)
// Autoridad: "ROLE_" + empleado.getRol().getCodigo()
// Retorna User.builder().username().password().authorities().build()
```

#### `app/infrastructure/security/JwtAuthFilter.java`
```java
// Extiende OncePerRequestFilter
// Extrae "Authorization: Bearer <token>"
// Valida con JwtService.isValid()
// NO reconsulta la BD — lee el rol directo del claim del token
// Inyecta UsernamePasswordAuthenticationToken en SecurityContext
```

---

## 6. Seguridad — Spring Security 7

### Prompt
> *"Configura Spring Security para que sea stateless con JWT, protege cada endpoint por rol y resuelve los errores de compatibilidad con Spring Security 7."*

### Error encontrado y solución: `DaoAuthenticationProvider`

**Problema:** Spring Security 7 eliminó el constructor sin argumentos de `DaoAuthenticationProvider`.

```
// ❌ Spring Security 6 (anterior) — ya no compila en SS7
DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
provider.setUserDetailsService(userDetailsService);

// ✅ Spring Security 7 — pasar UserDetailsService en el constructor
DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
```

### `app/infrastructure/security/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Rutas públicas y protegidas por rol:
    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
    .requestMatchers("/api/doctor/**").hasRole("medico")
    .requestMatchers("/api/enfermera/**").hasRole("enfermeria")
    .requestMatchers("/api/administrativo/**").hasRole("administrativo")
    .requestMatchers("/api/rrhh/**").hasRole("recursos_humanos")
    .requestMatchers("/api/soporte/**").hasRole("soporte_informacion")
    .anyRequest().authenticated()

    // Stateless + CSRF deshabilitado
    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .csrf(csrf -> csrf.disable())

    // Beans:
    @Bean PasswordEncoder → new BCryptPasswordEncoder()
    @Bean AuthenticationManager → AuthenticationConfiguration.getAuthenticationManager()
}
```

---

## 7. Migración SQL — Usuarios de autenticación

### Prompt
> *"Crea el script SQL para agregar los usuarios de prueba con contraseñas BCrypt y un nuevo empleado de soporte, para que el sistema de login funcione."*

### Archivo: `clinic_bd/10_jwt_auth_migration.sql`

```sql
-- Empleado nuevo: Andres Felipe Vargas (rol soporte_informacion, rol_id=3)
INSERT INTO empleado (cedula, nombre_completo, rol_id, ...)
VALUES ('1234567890', 'Andres Felipe Vargas', 3, ...);

-- Actualiza los 4 empleados existentes: username = código del rol
UPDATE seguridad_usuario SET codigo_usuario = 'medico',             ... WHERE empleado_id = 1;
UPDATE seguridad_usuario SET codigo_usuario = 'enfermeria',         ... WHERE empleado_id = 2;
UPDATE seguridad_usuario SET codigo_usuario = 'administrativo',     ... WHERE empleado_id = 3;
UPDATE seguridad_usuario SET codigo_usuario = 'recursos_humanos',   ... WHERE empleado_id = 4;

-- Contraseña BCrypt de '123456' para todos:
-- $2a$10$icObk/EnqrMSIGi6uTXbSOpVmSMc.RNXa6CeeSjXXfg24KbADVg8C

-- Nuevo usuario para soporte:
INSERT INTO seguridad_usuario (empleado_id, codigo_usuario, password_hash, activo)
VALUES (5, 'soporte_informacion', '$2a$10$icObk...', true);
```

**Usuarios resultantes (todos con contraseña `123456`):**

| Username | Rol |
|---|---|
| `medico` | medico |
| `enfermeria` | enfermeria |
| `administrativo` | administrativo |
| `recursos_humanos` | recursos_humanos |
| `soporte_informacion` | soporte_informacion |

---

## 8. Corrección de errores de compilación y startup

### 8.1 Error: Hibernate schema validation — `TINYINT UNSIGNED` vs `SMALLINT`

**Prompt:** *"Compila la aplicación y corrige los errores."*

**Error en startup:**
```
SchemaManagementException: Schema validation: wrong column type encountered
in column [estado_cita_id] in table [cat_estado_cita];
found [tinyint unsigned (Types#TINYINT)], but expecting [smallint (Types#SMALLINT)]
```

**Causa raíz:**  
Hibernate 7 mapea Java `Short` → SQL `SMALLINT` al hacer la validación del esquema. Pero las PKs de las 11 tablas catálogo en MySQL están declaradas como `TINYINT UNSIGNED`. Hibernate no los considera compatibles con `SMALLINT` y lanza excepción al arrancar.

**Solución:**  
Agregar `columnDefinition = "TINYINT UNSIGNED"` en la anotación `@Column` de cada campo `@Id` de las 11 entidades catálogo:

```java
// Antes:
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "estado_cita_id")
private Short estadoCitaId;

// Después:
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "estado_cita_id", columnDefinition = "TINYINT UNSIGNED")
private Short estadoCitaId;
```

**Archivos modificados (11):**
`CatEstadoCita`, `CatEstadoEspecialidad`, `CatEstadoSeguro`, `CatGenero`, `CatPrioridadAtencion`, `CatRolUsuario`, `CatTipoDetalleOrden`, `CatTipoDocumento`, `CatTipoFacturacion`, `CatTipoOrden`, `ConfigFacturacion`

---

### 8.2 Error: Lombok no genera setters — `cannot find symbol: method setCodigo(short)`

**Error de compilación:**
```
cannot find symbol
  symbol:   method setCodigo(short)
  location: variable resultado of type app.domain.dto.SpResultado
```

**Causa raíz:**  
Spring Boot 4.0.6 configura `annotationProcessorPaths` en su parent POM (para hibernate-processor, spring-boot-autoconfigure-processor, etc.), pero **no incluye Lombok**. Cuando el plugin `maven-compiler-plugin` tiene `annotationProcessorPaths` definido, solo procesa los annotation processors listados explícitamente — Lombok queda excluido y sus anotaciones (`@Getter`, `@Setter`, `@Data`, etc.) no generan código.

**Nota:** Esto afecta a **todas** las clases que usan Lombok: las entidades, los DTOs y los servicios.

**Solución 1 — `SpResultado` y `SpResultadoCopago`:**  
Reemplazar Lombok con getters y setters Java explícitos (solución permanente e independiente del annotation processor):

```java
// SpResultado.java — sin Lombok
public class SpResultado {
    private Short   codigo;
    private String  mensaje;
    private Integer idGenerado;

    public Short getCodigo() { return codigo; }
    public void setCodigo(Short codigo) { this.codigo = codigo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Integer getIdGenerado() { return idGenerado; }
    public void setIdGenerado(Integer idGenerado) { this.idGenerado = idGenerado; }
    public boolean isExitoso() { return codigo != null && codigo == 0; }
}
```

**Solución 2 — Todas las entidades (causa raíz):**  
Agregar Lombok explícitamente al `annotationProcessorPaths` en `pom.xml` con `combine.children="append"` para que se sume a los processors del parent sin reemplazarlos:

```xml
<!-- pom.xml -->
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths combine.children="append">
          <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```

**Resultado:** `BUILD SUCCESS` → `Started ClinicApplication in 14.748 seconds`

---

## 9. Resumen de archivos creados o modificados

### Archivos nuevos — Dominio

| Archivo | Descripción |
|---|---|
| `app/domain/entities/catalogs/*.java` (11) | Entidades catálogo con PKs `Short` |
| `app/domain/entities/masters/*.java` (11) | Entidades maestras con PKs `Integer` |
| `app/domain/entities/transactions/*.java` (12) | Entidades transaccionales con PKs `Integer` |
| `app/domain/repositories/**/*.java` (34) | Repositorios JPA |
| `app/domain/services/paciente/*.java` (3) | SPs de registro de paciente, contacto, seguro |
| `app/domain/services/agenda/*.java` (3) | SPs de programar, reprogramar, cancelar cita |
| `app/domain/services/clinico/*.java` (2) | SPs de abrir/cerrar encuentro clínico |
| `app/domain/services/ordenes/*.java` (2) | SPs de orden médica y detalle |
| `app/domain/services/facturacion/*.java` (3) | SPs de factura, pago, copago |
| `app/domain/services/rrhh/*.java` (1) | SP de crear usuario operativo |
| `app/domain/services/enfermeria/*.java` (3) | SPs de signos vitales, procedimiento, medicamento |
| `app/domain/services/soporte/**/*.java` (34) | CRUD services por entidad |
| `app/domain/dto/SpResultado.java` | DTO resultado SP (con métodos explícitos) |
| `app/domain/dto/SpResultadoCopago.java` | DTO resultado SP copago (con métodos explícitos) |
| `app/domain/exception/ReglaDeNegocioException.java` | HTTP 409 |
| `app/domain/exception/RecursoNoEncontradoException.java` | HTTP 404 |

### Archivos nuevos — Aplicación

| Archivo | Descripción |
|---|---|
| `app/application/dto/LoginRequest.java` | Request de login (`@NotBlank`) |
| `app/application/dto/LoginResponse.java` | Response con token, rol, usuario |
| `app/application/dto/SpResultadoResponse.java` | Response genérico para SPs |
| `app/application/dto/CopagoResponse.java` | Response cálculo de copago |
| `app/application/dto/*.java` (14 más) | DTOs de request por operación |
| `app/application/usecase/AdministrativoUseCase.java` | Orquesta operaciones administrativas |
| `app/application/usecase/DoctorUseCase.java` | Orquesta operaciones médicas |
| `app/application/usecase/EnfermeraUseCase.java` | Orquesta operaciones de enfermería |
| `app/application/usecase/RecursosHumanosUseCase.java` | Orquesta operaciones de RRHH |
| `app/application/usecase/SoporteUseCase.java` | Orquesta CRUDs de soporte |

### Archivos nuevos — Infraestructura

| Archivo | Descripción |
|---|---|
| `app/infrastructure/security/JwtService.java` | Generación y validación de tokens JWT (JJWT 0.12.6) |
| `app/infrastructure/security/UserDetailsServiceImpl.java` | Carga usuario desde BD para Spring Security |
| `app/infrastructure/security/JwtAuthFilter.java` | Filtro HTTP que valida el JWT en cada request |
| `app/infrastructure/security/SecurityConfig.java` | Configuración de Spring Security 7 (stateless, roles) |
| `app/infrastructure/controller/AuthController.java` | `POST /api/auth/login` |
| `app/infrastructure/controller/AdministrativoController.java` | Endpoints de administración |
| `app/infrastructure/controller/DoctorController.java` | Endpoints médicos |
| `app/infrastructure/controller/EnfermeraController.java` | Endpoints de enfermería |
| `app/infrastructure/controller/RecursosHumanosController.java` | Endpoints de RRHH |
| `app/infrastructure/controller/SoporteController.java` | Endpoints CRUD de soporte |
| `app/infrastructure/exception/GlobalExceptionHandler.java` | Manejador global de excepciones HTTP |

### Archivos modificados

| Archivo | Cambio |
|---|---|
| `clinic/pom.xml` | Agregadas dependencias JJWT 0.12.6, spring-boot-starter-validation; Lombok en `annotationProcessorPaths` |
| `clinic/src/main/resources/application.properties` | DataSource MySQL, Hibernate validate, JWT secret, puerto 8083 |
| `app/domain/repositories/SeguridadUsuarioRepository.java` | Agregado `findActivoConRol()` con JOIN FETCH |
| `app/domain/entities/catalogs/*.java` (11) | `@Column` de PK con `columnDefinition = "TINYINT UNSIGNED"` |

### Archivos nuevos — SQL

| Archivo | Descripción |
|---|---|
| `clinic_bd/10_jwt_auth_migration.sql` | Inserta empleado de soporte, actualiza usuarios y contraseñas BCrypt |
