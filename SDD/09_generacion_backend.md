# 09. Generacion del Backend Spring Boot

## Objetivo

Documentar de forma ordenada y detallada cada uno de los pasos ejecutados para construir la capa de aplicacion Spring Boot (`clinic/`) que se conecta a la base de datos `clinica_ips` ya existente en MySQL.

---

## Contexto de partida

| Item | Valor |
|---|---|
| Framework | Spring Boot 4.0.6 |
| Lenguaje | Java 17 |
| Base de datos | MySQL 8.0 — `clinica_ips` (ya creada y poblada) |
| Paquete base | `app` |
| Proyecto base | `clinic/` (generado con Spring Initializr) |
| Estrategia DDL | `spring.jpa.hibernate.ddl-auto=validate` (Hibernate valida pero no genera tablas) |
| Librerias adicionales | Lombok, Spring Data JPA, Spring Security, Spring Web, mysql-connector-j |

---

## Paso 1 — Configuracion del proyecto base

**Archivo:** `clinic/pom.xml`

- Se agrego la dependencia de **Lombok** (`org.projectlombok:lombok`, `optional=true`).
- El proyecto ya tenia declaradas: `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-web`, `mysql-connector-j` (scope runtime).

**Archivo:** `clinic/src/main/resources/application.properties`

- Se configuro la conexion completa a MySQL:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/clinica_ips?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
  spring.datasource.username=root
  spring.datasource.password=pass
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.hibernate.ddl-auto=validate
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
  ```

---

## Paso 2 — DTOs de resultado de procedimientos almacenados

**Paquete:** `app.domain.dto`

Se crearon dos clases de transferencia de datos que representan las salidas estandar de los stored procedures:

| Clase | Campos | Uso |
|---|---|---|
| `SpResultado` | `Short codigo`, `String mensaje`, `Integer idGenerado`, `boolean isExitoso()` | Resultado estandar de todos los SPs (codigo=0 significa exito) |
| `SpResultadoCopago` | `BigDecimal valorCopago`, `Boolean exento`, `String mensaje` | Resultado especifico de `sp_calcular_copago` |

Ambas clases usan la anotacion `@Data` de Lombok.

---

## Paso 3 — Entidades JPA: Catalogos

**Paquete:** `app.domain.entities.catalogs`

Se crearon **11 entidades** que mapean las tablas de catalogo de la base de datos. Cada entidad usa `@Entity`, `@Table(name="...")`, `@Getter @Setter @NoArgsConstructor` de Lombok, y `@Id @GeneratedValue` con `GenerationType.IDENTITY`. Las PKs son de tipo `Short`.

| Entidad | Tabla | Campos relevantes |
|---|---|---|
| `CatRolUsuario` | `cat_rol_usuario` | `rolId`, `codigo`, `descripcion`, `activo` |
| `CatTipoDocumento` | `cat_tipo_documento` | `tipoDocId`, `codigo`, `descripcion`, `activo` |
| `CatGenero` | `cat_genero` | `generoId`, `codigo`, `descripcion` |
| `CatEstadoCita` | `cat_estado_cita` | `estadoCitaId`, `codigo`, `descripcion` |
| `CatPrioridadAtencion` | `cat_prioridad_atencion` | `prioridadId`, `codigo`, `descripcion`, `nivel` |
| `CatEstadoSeguro` | `cat_estado_seguro` | `estadoSeguroId`, `codigo`, `descripcion` |
| `CatTipoOrden` | `cat_tipo_orden` | `tipoOrdenId`, `codigo`, `descripcion` |
| `CatTipoDetalleOrden` | `cat_tipo_detalle_orden` | `tipoDetalleId`, `codigo`, `descripcion` |
| `CatTipoFacturacion` | `cat_tipo_facturacion` | `tipoFactId`, `codigo`, `descripcion` |
| `CatEstadoEspecialidad` | `cat_estado_especialidad` | `estadoEspId`, `codigo`, `descripcion` |
| `ConfigFacturacion` | `config_facturacion` | `configId`, `parametro`, `valorNumerico (BigDecimal)`, `descripcion` |

**Decisiones tecnicas:**
- Columnas `fecha_creacion` / `fecha_modificacion` con `DEFAULT CURRENT_TIMESTAMP` usan `@Column(insertable=false, updatable=false)` para que Hibernate no intente escribir en ellas.

---

## Paso 4 — Entidades JPA: Tablas maestras

**Paquete:** `app.domain.entities.masters`

Se crearon **11 entidades** que mapean las tablas maestras del dominio. Las PKs son de tipo `Integer`. Se usan asociaciones `@ManyToOne` y `@OneToOne` para las claves foraneas.

| Entidad | Tabla | Asociaciones |
|---|---|---|
| `Especialidad` | `especialidad` | `@ManyToOne` → `CatEstadoEspecialidad` |
| `Empleado` | `empleado` | `@ManyToOne` → `CatTipoDocumento`, `CatRolUsuario` |
| `SeguridadUsuario` | `seguridad_usuario` | `@OneToOne` → `Empleado` |
| `MedicoPerfil` | `medico_perfil` | `@OneToOne` → `Empleado`, `@ManyToOne` → `Especialidad` |
| `EnfermeroPerfil` | `enfermero_perfil` | `@OneToOne` → `Empleado` |
| `Paciente` | `paciente` | `@ManyToOne` → `CatTipoDocumento`, `CatGenero` |
| `ContactoEmergencia` | `contacto_emergencia` | `@ManyToOne` → `Paciente` |
| `SeguroMedico` | `seguro_medico` | `@ManyToOne` → `Paciente`, `CatEstadoSeguro` |
| `MedicamentoCatalogo` | `medicamento_catalogo` | (sin FK) |
| `ProcedimientoCatalogo` | `procedimiento_catalogo` | `@ManyToOne` → `Especialidad` |
| `AyudaDiagnosticaCatalogo` | `ayuda_diagnostica_catalogo` | `@ManyToOne` → `Especialidad` |

**Decisiones tecnicas:**
- Las columnas de tipo `TEXT` en MySQL usan `@Column(columnDefinition="TEXT")` en lugar de `@Lob` por compatibilidad con Hibernate 6+.

---

## Paso 5 — Entidades JPA: Tablas transaccionales

**Paquete:** `app.domain.entities.transactions`

Se crearon **12 entidades** que mapean las tablas operacionales del sistema clinico. Todas con PKs `Integer`.

| Entidad | Tabla | Asociaciones clave |
|---|---|---|
| `Cita` | `cita` | `@ManyToOne` → `Paciente`, `MedicoPerfil`, `CatEstadoCita`, `CatPrioridadAtencion` |
| `EncuentroClinico` | `encuentro_clinico` | `@ManyToOne` → `Cita`, `MedicoPerfil`; campos TEXT: motivoConsulta, sintomatologia, diagnostico, tratamiento, observaciones |
| `SignoVital` | `signo_vital` | `@ManyToOne` → `EncuentroClinico`, `EnfermeroPerfil`; `BigDecimal`: temperatura, oxigeno |
| `AdministracionMedicamento` | `administracion_medicamento` | `@ManyToOne` → `EncuentroClinico`, `EnfermeroPerfil`, `MedicamentoCatalogo` |
| `ProcedimientoEnfermeria` | `procedimiento_enfermeria` | `@ManyToOne` → `EncuentroClinico`, `EnfermeroPerfil`, `ProcedimientoCatalogo` |
| `OrdenMedica` | `orden_medica` | `@ManyToOne` → `EncuentroClinico`, `Paciente`, `MedicoPerfil`, `CatTipoOrden`; `numeroOrden` unique |
| `OrdenMedicamentoDetalle` | `orden_medicamento_detalle` | `@ManyToOne` → `OrdenMedica`, `MedicamentoCatalogo` |
| `OrdenProcedimientoDetalle` | `orden_procedimiento_detalle` | `@ManyToOne` → `OrdenMedica`, `ProcedimientoCatalogo`, `Especialidad` |
| `OrdenAyudaDiagnosticaDetalle` | `orden_ayuda_diagnostica_detalle` | `@ManyToOne` → `OrdenMedica`, `AyudaDiagnosticaCatalogo`, `Especialidad` |
| `Factura` | `factura` | `@ManyToOne` → `EncuentroClinico`, `Paciente`, `MedicoPerfil`, `SeguroMedico`, `CatTipoFacturacion`; `BigDecimal`: valorTotal, valorCopago, valorAseguradora, valorPaciente |
| `FacturaDetalle` | `factura_detalle` | `@ManyToOne` → `Factura` |
| `Pago` | `pago` | `@ManyToOne` → `Factura`; `BigDecimal`: valorPagado |

---

## Paso 6 — Repositorios JPA

**Paquete:** `app.domain.repositories`

Se creo **una interfaz `JpaRepository` por cada entidad** (total: **34 repositorios**). Cada interfaz sigue la convencion de nombre `{NombreEntidad}Repository` y no declara metodos adicionales, delegando toda la logica de consulta a Spring Data JPA.

```
Catalogos (11):  CatRolUsuarioRepository, CatTipoDocumentoRepository,
                 CatGeneroRepository, CatEstadoCitaRepository,
                 CatPrioridadAtencionRepository, CatEstadoSeguroRepository,
                 CatTipoOrdenRepository, CatTipoDetalleOrdenRepository,
                 CatTipoFacturacionRepository, CatEstadoEspecialidadRepository,
                 ConfigFacturacionRepository

Maestras  (11):  EspecialidadRepository, EmpleadoRepository,
                 SeguridadUsuarioRepository, MedicoPerfilRepository,
                 EnfermeroPerfilRepository, PacienteRepository,
                 ContactoEmergenciaRepository, SeguroMedicoRepository,
                 MedicamentoCatalogoRepository, ProcedimientoCatalogoRepository,
                 AyudaDiagnosticaCatalogoRepository

Transacc. (12):  CitaRepository, EncuentroClinicoRepository,
                 SignoVitalRepository, AdministracionMedicamentoRepository,
                 ProcedimientoEnfermeriaRepository, OrdenMedicaRepository,
                 OrdenMedicamentoDetalleRepository, OrdenProcedimientoDetalleRepository,
                 OrdenAyudaDiagnosticaDetalleRepository, FacturaRepository,
                 FacturaDetalleRepository, PagoRepository
```

---

## Paso 7 — Servicios de dominio: Procedimientos almacenados

**Paquetes:**
- `app.domain.services.paciente`
- `app.domain.services.personal`
- `app.domain.services.agenda`
- `app.domain.services.clinico`
- `app.domain.services.enfermeria`
- `app.domain.services.ordenes`
- `app.domain.services.facturacion`

Se crearon **17 servicios** (uno por stored procedure), cada uno anotado con `@Service`. Todos inyectan `EntityManager` mediante `@PersistenceContext` y usan `createStoredProcedureQuery` con parametros registrados via `registerStoredProcedureParameter`.

**Patron de invocacion (SPs estandar):**
```java
StoredProcedureQuery q = em.createStoredProcedureQuery("sp_nombre");
q.registerStoredProcedureParameter("p_param", Type.class, ParameterMode.IN);
q.registerStoredProcedureParameter("o_codigo", Short.class, ParameterMode.OUT);
q.registerStoredProcedureParameter("o_mensaje", String.class, ParameterMode.OUT);
q.registerStoredProcedureParameter("o_id_generado", Integer.class, ParameterMode.OUT);
q.setParameter("p_param", valor);
q.execute();
// Casteo defensivo para compatibilidad con el driver MySQL
Short codigo = ((Number) q.getOutputParameterValue("o_codigo")).shortValue();
```

| Servicio | SP invocado | Retorna |
|---|---|---|
| `RegistrarPacienteService` | `sp_registrar_paciente` | `SpResultado` |
| `RegistrarContactoEmergenciaService` | `sp_registrar_contacto_emergencia` | `SpResultado` |
| `RegistrarSeguroMedicoService` | `sp_registrar_seguro_medico` | `SpResultado` |
| `RegistrarEmpleadoService` | `sp_registrar_empleado` | `SpResultado` |
| `CrearUsuarioOperativoService` | `sp_crear_usuario_operativo` | `SpResultado` |
| `ProgramarCitaService` | `sp_programar_cita` | `SpResultado` |
| `ReprogramarCitaService` | `sp_reprogramar_cita` | `SpResultado` |
| `CancelarCitaService` | `sp_cancelar_cita` | `SpResultado` |
| `AbrirEncuentroClinicoService` | `sp_abrir_encuentro_clinico` | `SpResultado` |
| `CerrarEncuentroClinicoService` | `sp_cerrar_encuentro_clinico` | `SpResultado` |
| `RegistrarSignosVitalesService` | `sp_registrar_signos_vitales` | `SpResultado` |
| `RegistrarAdministracionMedicamentoService` | `sp_registrar_administracion_medicamento` | `SpResultado` |
| `RegistrarOrdenMedicaService` | `sp_registrar_orden_medica` | `SpResultado` |
| `AgregarDetalleOrdenService` | `sp_agregar_detalle_orden` | `SpResultado` |
| `CalcularCopagoService` | `sp_calcular_copago` | `SpResultadoCopago` |
| `EmitirFacturaService` | `sp_emitir_factura` | `SpResultado` |
| `RegistrarPagoService` | `sp_registrar_pago` | `SpResultado` |

**Nota especial — `sp_calcular_copago`:** Es el unico SP que no usa `@Transactional` (solo lectura/calculo) y retorna `SpResultadoCopago`. El campo `o_exento` se castea como `((Number) val).intValue() == 1`.

---

## Paso 8 — Servicios CRUD para el rol Soporte de Informacion

**Paquetes:**
- `app.domain.services.soporte.catalogos` (11 servicios)
- `app.domain.services.soporte.maestras` (11 servicios)
- `app.domain.services.soporte.transaccionales` (12 servicios)

Se crearon **34 servicios CRUD** (uno por entidad). Cada servicio esta anotado con `@Service`, utiliza **inyeccion por constructor** (sin `@Autowired` en campo), y expone exactamente **5 metodos**:

```java
public T crear(T entidad)
public Optional<T> buscarPorId(ID id)
public List<T> buscarTodos()
public T actualizar(ID id, T entidad)
public void eliminar(ID id)
```

El metodo `actualizar` usa `findById` + copia de campos + `save`. El metodo `eliminar` usa `deleteById`.

**Ejemplo — `EspecialidadCrudService`:**
```java
@Service
public class EspecialidadCrudService {
    private final EspecialidadRepository repo;
    public EspecialidadCrudService(EspecialidadRepository repo) { this.repo = repo; }
    public Especialidad crear(Especialidad e) { return repo.save(e); }
    public Optional<Especialidad> buscarPorId(Integer id) { return repo.findById(id); }
    public List<Especialidad> buscarTodos() { return repo.findAll(); }
    public Especialidad actualizar(Integer id, Especialidad datos) { ... }
    public void eliminar(Integer id) { repo.deleteById(id); }
}
```

---

## Paso 9 — Capa de aplicacion: Use Cases por rol

**Paquete:** `app.application.usecase`

Se creo **un use case por cada rol del sistema** (5 en total). Cada use case es un `@Service` con inyeccion por constructor que agrupa y orquesta los servicios de dominio accesibles para ese rol.

### 9.1 `DoctorUseCase`

Servicios inyectados: `AbrirEncuentroClinicoService`, `CerrarEncuentroClinicoService`, `RegistrarOrdenMedicaService`, `AgregarDetalleOrdenService`

Metodos expuestos: `abrirEncuentroClinico`, `cerrarEncuentroClinico`, `registrarOrdenMedica`, `agregarDetalleOrden`

### 9.2 `EnfermeraUseCase`

Servicios inyectados: `RegistrarSignosVitalesService`, `RegistrarAdministracionMedicamentoService`

Metodos expuestos: `registrarSignosVitales`, `registrarAdministracionMedicamento`

### 9.3 `AdministrativoUseCase`

Servicios inyectados (9 total): `RegistrarPacienteService`, `RegistrarContactoEmergenciaService`, `RegistrarSeguroMedicoService`, `ProgramarCitaService`, `ReprogramarCitaService`, `CancelarCitaService`, `CalcularCopagoService`, `EmitirFacturaService`, `RegistrarPagoService`

Metodos expuestos: `registrarPaciente`, `registrarContactoEmergencia`, `registrarSeguroMedico`, `programarCita`, `reprogramarCita`, `cancelarCita`, `calcularCopago` (→ `SpResultadoCopago`), `emitirFactura`, `registrarPago`

### 9.4 `RecursosHumanosUseCase`

Servicios inyectados: `RegistrarEmpleadoService`, `CrearUsuarioOperativoService`

Metodos expuestos: `registrarEmpleado`, `crearUsuarioOperativo`

### 9.5 `SoporteUseCase`

Servicios inyectados: los **34 servicios CRUD** (11 catalogos + 11 maestras + 12 transaccionales)

Metodos expuestos: **170 metodos** con nomenclatura `crear{Entidad}`, `buscar{Entidad}PorId`, `listar{Entidad}`, `actualizar{Entidad}`, `eliminar{Entidad}` para cada una de las 34 entidades.

---

## Resumen de artefactos generados

| Capa | Artefactos | Cantidad |
|---|---|---|
| Configuracion | `pom.xml`, `application.properties` | 2 archivos modificados |
| DTOs | `SpResultado`, `SpResultadoCopago` | 2 clases |
| Entidades JPA | Catalogos + Maestras + Transaccionales | 34 clases |
| Repositorios | `JpaRepository` por entidad | 34 interfaces |
| Servicios SP | Un servicio por stored procedure | 17 clases |
| Servicios CRUD | Un servicio por entidad (rol Soporte) | 34 clases |
| Use Cases | Un use case por rol | 5 clases |
| **Total** | | **128 artefactos** |

---

## Estado actual del backend

```
clinic/src/main/java/app/
├── domain/
│   ├── dto/
│   │   ├── SpResultado.java
│   │   └── SpResultadoCopago.java
│   ├── entities/
│   │   ├── catalogs/         (11 entidades)
│   │   ├── masters/          (11 entidades)
│   │   └── transactions/     (12 entidades)
│   ├── repositories/         (34 interfaces)
│   └── services/
│       ├── paciente/         (3 servicios SP)
│       ├── personal/         (2 servicios SP)
│       ├── agenda/           (3 servicios SP)
│       ├── clinico/          (2 servicios SP)
│       ├── enfermeria/       (2 servicios SP)
│       ├── ordenes/          (2 servicios SP)
│       ├── facturacion/      (3 servicios SP)
│       └── soporte/
│           ├── catalogos/    (11 servicios CRUD)
│           ├── maestras/     (11 servicios CRUD)
│           └── transaccionales/ (12 servicios CRUD)
└── application/
    └── usecase/
        ├── DoctorUseCase.java
        ├── EnfermeraUseCase.java
        ├── AdministrativoUseCase.java
        ├── RecursosHumanosUseCase.java
        └── SoporteUseCase.java
```

## Proximos pasos sugeridos

1. **Capa de infraestructura** — `@RestController` por rol o por use case, mapeando las operaciones a endpoints REST.
2. **Spring Security** — configuracion de roles, filtros JWT o sesion, y autorizacion por endpoint segun el use case correspondiente.
3. **DTOs de entrada/salida** — objetos `Request` / `Response` para desacoplar la API HTTP de las entidades JPA.
4. **Pruebas** — tests de integracion por use case y tests unitarios de los servicios de dominio.
