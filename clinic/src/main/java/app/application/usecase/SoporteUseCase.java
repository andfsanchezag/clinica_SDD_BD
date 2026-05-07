package app.application.usecase;

import app.domain.entities.catalogs.*;
import app.domain.entities.masters.*;
import app.domain.entities.transactions.*;
import app.domain.services.soporte.catalogos.*;
import app.domain.services.soporte.maestras.*;
import app.domain.services.soporte.transaccionales.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para el rol SOPORTE DE INFORMACIÓN.
 * Agrupa el acceso CRUD directo a todas las tablas del dominio
 * (catálogos, entidades maestras y entidades transaccionales).
 */
@Service
public class SoporteUseCase {

    // ── Catálogos ────────────────────────────────────────────────────────────
    private final CatRolUsuarioCrudService catRolUsuarioCrudService;
    private final CatTipoDocumentoCrudService catTipoDocumentoCrudService;
    private final CatGeneroCrudService catGeneroCrudService;
    private final CatEstadoCitaCrudService catEstadoCitaCrudService;
    private final CatPrioridadAtencionCrudService catPrioridadAtencionCrudService;
    private final CatEstadoSeguroCrudService catEstadoSeguroCrudService;
    private final CatTipoOrdenCrudService catTipoOrdenCrudService;
    private final CatTipoDetalleOrdenCrudService catTipoDetalleOrdenCrudService;
    private final CatTipoFacturacionCrudService catTipoFacturacionCrudService;
    private final CatEstadoEspecialidadCrudService catEstadoEspecialidadCrudService;
    private final ConfigFacturacionCrudService configFacturacionCrudService;

    // ── Maestras ─────────────────────────────────────────────────────────────
    private final EspecialidadCrudService especialidadCrudService;
    private final EmpleadoCrudService empleadoCrudService;
    private final SeguridadUsuarioCrudService seguridadUsuarioCrudService;
    private final MedicoPerfilCrudService medicoPerfilCrudService;
    private final EnfermeroPerfilCrudService enfermeroPerfilCrudService;
    private final PacienteCrudService pacienteCrudService;
    private final ContactoEmergenciaCrudService contactoEmergenciaCrudService;
    private final SeguroMedicoCrudService seguroMedicoCrudService;
    private final MedicamentoCatalogoCrudService medicamentoCatalogoCrudService;
    private final ProcedimientoCatalogoCrudService procedimientoCatalogoCrudService;
    private final AyudaDiagnosticaCatalogoCrudService ayudaDiagnosticaCatalogoCrudService;

    // ── Transaccionales ──────────────────────────────────────────────────────
    private final CitaCrudService citaCrudService;
    private final EncuentroClinicoCrudService encuentroClinicoCrudService;
    private final SignoVitalCrudService signoVitalCrudService;
    private final AdministracionMedicamentoCrudService administracionMedicamentoCrudService;
    private final ProcedimientoEnfermeriaCrudService procedimientoEnfermeriaCrudService;
    private final OrdenMedicaCrudService ordenMedicaCrudService;
    private final OrdenMedicamentoDetalleCrudService ordenMedicamentoDetalleCrudService;
    private final OrdenProcedimientoDetalleCrudService ordenProcedimientoDetalleCrudService;
    private final OrdenAyudaDiagnosticaDetalleCrudService ordenAyudaDiagnosticaDetalleCrudService;
    private final FacturaCrudService facturaCrudService;
    private final FacturaDetalleCrudService facturaDetalleCrudService;
    private final PagoCrudService pagoCrudService;

    public SoporteUseCase(
            CatRolUsuarioCrudService catRolUsuarioCrudService,
            CatTipoDocumentoCrudService catTipoDocumentoCrudService,
            CatGeneroCrudService catGeneroCrudService,
            CatEstadoCitaCrudService catEstadoCitaCrudService,
            CatPrioridadAtencionCrudService catPrioridadAtencionCrudService,
            CatEstadoSeguroCrudService catEstadoSeguroCrudService,
            CatTipoOrdenCrudService catTipoOrdenCrudService,
            CatTipoDetalleOrdenCrudService catTipoDetalleOrdenCrudService,
            CatTipoFacturacionCrudService catTipoFacturacionCrudService,
            CatEstadoEspecialidadCrudService catEstadoEspecialidadCrudService,
            ConfigFacturacionCrudService configFacturacionCrudService,
            EspecialidadCrudService especialidadCrudService,
            EmpleadoCrudService empleadoCrudService,
            SeguridadUsuarioCrudService seguridadUsuarioCrudService,
            MedicoPerfilCrudService medicoPerfilCrudService,
            EnfermeroPerfilCrudService enfermeroPerfilCrudService,
            PacienteCrudService pacienteCrudService,
            ContactoEmergenciaCrudService contactoEmergenciaCrudService,
            SeguroMedicoCrudService seguroMedicoCrudService,
            MedicamentoCatalogoCrudService medicamentoCatalogoCrudService,
            ProcedimientoCatalogoCrudService procedimientoCatalogoCrudService,
            AyudaDiagnosticaCatalogoCrudService ayudaDiagnosticaCatalogoCrudService,
            CitaCrudService citaCrudService,
            EncuentroClinicoCrudService encuentroClinicoCrudService,
            SignoVitalCrudService signoVitalCrudService,
            AdministracionMedicamentoCrudService administracionMedicamentoCrudService,
            ProcedimientoEnfermeriaCrudService procedimientoEnfermeriaCrudService,
            OrdenMedicaCrudService ordenMedicaCrudService,
            OrdenMedicamentoDetalleCrudService ordenMedicamentoDetalleCrudService,
            OrdenProcedimientoDetalleCrudService ordenProcedimientoDetalleCrudService,
            OrdenAyudaDiagnosticaDetalleCrudService ordenAyudaDiagnosticaDetalleCrudService,
            FacturaCrudService facturaCrudService,
            FacturaDetalleCrudService facturaDetalleCrudService,
            PagoCrudService pagoCrudService) {
        this.catRolUsuarioCrudService = catRolUsuarioCrudService;
        this.catTipoDocumentoCrudService = catTipoDocumentoCrudService;
        this.catGeneroCrudService = catGeneroCrudService;
        this.catEstadoCitaCrudService = catEstadoCitaCrudService;
        this.catPrioridadAtencionCrudService = catPrioridadAtencionCrudService;
        this.catEstadoSeguroCrudService = catEstadoSeguroCrudService;
        this.catTipoOrdenCrudService = catTipoOrdenCrudService;
        this.catTipoDetalleOrdenCrudService = catTipoDetalleOrdenCrudService;
        this.catTipoFacturacionCrudService = catTipoFacturacionCrudService;
        this.catEstadoEspecialidadCrudService = catEstadoEspecialidadCrudService;
        this.configFacturacionCrudService = configFacturacionCrudService;
        this.especialidadCrudService = especialidadCrudService;
        this.empleadoCrudService = empleadoCrudService;
        this.seguridadUsuarioCrudService = seguridadUsuarioCrudService;
        this.medicoPerfilCrudService = medicoPerfilCrudService;
        this.enfermeroPerfilCrudService = enfermeroPerfilCrudService;
        this.pacienteCrudService = pacienteCrudService;
        this.contactoEmergenciaCrudService = contactoEmergenciaCrudService;
        this.seguroMedicoCrudService = seguroMedicoCrudService;
        this.medicamentoCatalogoCrudService = medicamentoCatalogoCrudService;
        this.procedimientoCatalogoCrudService = procedimientoCatalogoCrudService;
        this.ayudaDiagnosticaCatalogoCrudService = ayudaDiagnosticaCatalogoCrudService;
        this.citaCrudService = citaCrudService;
        this.encuentroClinicoCrudService = encuentroClinicoCrudService;
        this.signoVitalCrudService = signoVitalCrudService;
        this.administracionMedicamentoCrudService = administracionMedicamentoCrudService;
        this.procedimientoEnfermeriaCrudService = procedimientoEnfermeriaCrudService;
        this.ordenMedicaCrudService = ordenMedicaCrudService;
        this.ordenMedicamentoDetalleCrudService = ordenMedicamentoDetalleCrudService;
        this.ordenProcedimientoDetalleCrudService = ordenProcedimientoDetalleCrudService;
        this.ordenAyudaDiagnosticaDetalleCrudService = ordenAyudaDiagnosticaDetalleCrudService;
        this.facturaCrudService = facturaCrudService;
        this.facturaDetalleCrudService = facturaDetalleCrudService;
        this.pagoCrudService = pagoCrudService;
    }

    // ════════════════════════════════════════════════════════════════════════
    // CATÁLOGOS
    // ════════════════════════════════════════════════════════════════════════

    // CatRolUsuario
    public CatRolUsuario crearCatRolUsuario(CatRolUsuario e)                      { return catRolUsuarioCrudService.crear(e); }
    public Optional<CatRolUsuario> buscarCatRolUsuarioPorId(Short id)             { return catRolUsuarioCrudService.buscarPorId(id); }
    public List<CatRolUsuario> listarCatRolUsuario()                              { return catRolUsuarioCrudService.buscarTodos(); }
    public CatRolUsuario actualizarCatRolUsuario(Short id, CatRolUsuario e)       { return catRolUsuarioCrudService.actualizar(id, e); }
    public void eliminarCatRolUsuario(Short id)                                   { catRolUsuarioCrudService.eliminar(id); }

    // CatTipoDocumento
    public CatTipoDocumento crearCatTipoDocumento(CatTipoDocumento e)                   { return catTipoDocumentoCrudService.crear(e); }
    public Optional<CatTipoDocumento> buscarCatTipoDocumentoPorId(Short id)             { return catTipoDocumentoCrudService.buscarPorId(id); }
    public List<CatTipoDocumento> listarCatTipoDocumento()                              { return catTipoDocumentoCrudService.buscarTodos(); }
    public CatTipoDocumento actualizarCatTipoDocumento(Short id, CatTipoDocumento e)    { return catTipoDocumentoCrudService.actualizar(id, e); }
    public void eliminarCatTipoDocumento(Short id)                                      { catTipoDocumentoCrudService.eliminar(id); }

    // CatGenero
    public CatGenero crearCatGenero(CatGenero e)                       { return catGeneroCrudService.crear(e); }
    public Optional<CatGenero> buscarCatGeneroPorId(Short id)          { return catGeneroCrudService.buscarPorId(id); }
    public List<CatGenero> listarCatGenero()                           { return catGeneroCrudService.buscarTodos(); }
    public CatGenero actualizarCatGenero(Short id, CatGenero e)        { return catGeneroCrudService.actualizar(id, e); }
    public void eliminarCatGenero(Short id)                            { catGeneroCrudService.eliminar(id); }

    // CatEstadoCita
    public CatEstadoCita crearCatEstadoCita(CatEstadoCita e)                    { return catEstadoCitaCrudService.crear(e); }
    public Optional<CatEstadoCita> buscarCatEstadoCitaPorId(Short id)           { return catEstadoCitaCrudService.buscarPorId(id); }
    public List<CatEstadoCita> listarCatEstadoCita()                            { return catEstadoCitaCrudService.buscarTodos(); }
    public CatEstadoCita actualizarCatEstadoCita(Short id, CatEstadoCita e)     { return catEstadoCitaCrudService.actualizar(id, e); }
    public void eliminarCatEstadoCita(Short id)                                 { catEstadoCitaCrudService.eliminar(id); }

    // CatPrioridadAtencion
    public CatPrioridadAtencion crearCatPrioridadAtencion(CatPrioridadAtencion e)                { return catPrioridadAtencionCrudService.crear(e); }
    public Optional<CatPrioridadAtencion> buscarCatPrioridadAtencionPorId(Short id)             { return catPrioridadAtencionCrudService.buscarPorId(id); }
    public List<CatPrioridadAtencion> listarCatPrioridadAtencion()                              { return catPrioridadAtencionCrudService.buscarTodos(); }
    public CatPrioridadAtencion actualizarCatPrioridadAtencion(Short id, CatPrioridadAtencion e){ return catPrioridadAtencionCrudService.actualizar(id, e); }
    public void eliminarCatPrioridadAtencion(Short id)                                          { catPrioridadAtencionCrudService.eliminar(id); }

    // CatEstadoSeguro
    public CatEstadoSeguro crearCatEstadoSeguro(CatEstadoSeguro e)                   { return catEstadoSeguroCrudService.crear(e); }
    public Optional<CatEstadoSeguro> buscarCatEstadoSeguroPorId(Short id)            { return catEstadoSeguroCrudService.buscarPorId(id); }
    public List<CatEstadoSeguro> listarCatEstadoSeguro()                             { return catEstadoSeguroCrudService.buscarTodos(); }
    public CatEstadoSeguro actualizarCatEstadoSeguro(Short id, CatEstadoSeguro e)    { return catEstadoSeguroCrudService.actualizar(id, e); }
    public void eliminarCatEstadoSeguro(Short id)                                    { catEstadoSeguroCrudService.eliminar(id); }

    // CatTipoOrden
    public CatTipoOrden crearCatTipoOrden(CatTipoOrden e)                    { return catTipoOrdenCrudService.crear(e); }
    public Optional<CatTipoOrden> buscarCatTipoOrdenPorId(Short id)          { return catTipoOrdenCrudService.buscarPorId(id); }
    public List<CatTipoOrden> listarCatTipoOrden()                           { return catTipoOrdenCrudService.buscarTodos(); }
    public CatTipoOrden actualizarCatTipoOrden(Short id, CatTipoOrden e)     { return catTipoOrdenCrudService.actualizar(id, e); }
    public void eliminarCatTipoOrden(Short id)                               { catTipoOrdenCrudService.eliminar(id); }

    // CatTipoDetalleOrden
    public CatTipoDetalleOrden crearCatTipoDetalleOrden(CatTipoDetalleOrden e)                { return catTipoDetalleOrdenCrudService.crear(e); }
    public Optional<CatTipoDetalleOrden> buscarCatTipoDetalleOrdenPorId(Short id)            { return catTipoDetalleOrdenCrudService.buscarPorId(id); }
    public List<CatTipoDetalleOrden> listarCatTipoDetalleOrden()                             { return catTipoDetalleOrdenCrudService.buscarTodos(); }
    public CatTipoDetalleOrden actualizarCatTipoDetalleOrden(Short id, CatTipoDetalleOrden e){ return catTipoDetalleOrdenCrudService.actualizar(id, e); }
    public void eliminarCatTipoDetalleOrden(Short id)                                        { catTipoDetalleOrdenCrudService.eliminar(id); }

    // CatTipoFacturacion
    public CatTipoFacturacion crearCatTipoFacturacion(CatTipoFacturacion e)                 { return catTipoFacturacionCrudService.crear(e); }
    public Optional<CatTipoFacturacion> buscarCatTipoFacturacionPorId(Short id)             { return catTipoFacturacionCrudService.buscarPorId(id); }
    public List<CatTipoFacturacion> listarCatTipoFacturacion()                              { return catTipoFacturacionCrudService.buscarTodos(); }
    public CatTipoFacturacion actualizarCatTipoFacturacion(Short id, CatTipoFacturacion e)  { return catTipoFacturacionCrudService.actualizar(id, e); }
    public void eliminarCatTipoFacturacion(Short id)                                        { catTipoFacturacionCrudService.eliminar(id); }

    // CatEstadoEspecialidad
    public CatEstadoEspecialidad crearCatEstadoEspecialidad(CatEstadoEspecialidad e)                { return catEstadoEspecialidadCrudService.crear(e); }
    public Optional<CatEstadoEspecialidad> buscarCatEstadoEspecialidadPorId(Short id)              { return catEstadoEspecialidadCrudService.buscarPorId(id); }
    public List<CatEstadoEspecialidad> listarCatEstadoEspecialidad()                               { return catEstadoEspecialidadCrudService.buscarTodos(); }
    public CatEstadoEspecialidad actualizarCatEstadoEspecialidad(Short id, CatEstadoEspecialidad e){ return catEstadoEspecialidadCrudService.actualizar(id, e); }
    public void eliminarCatEstadoEspecialidad(Short id)                                            { catEstadoEspecialidadCrudService.eliminar(id); }

    // ConfigFacturacion
    public ConfigFacturacion crearConfigFacturacion(ConfigFacturacion e)                   { return configFacturacionCrudService.crear(e); }
    public Optional<ConfigFacturacion> buscarConfigFacturacionPorId(Short id)             { return configFacturacionCrudService.buscarPorId(id); }
    public List<ConfigFacturacion> listarConfigFacturacion()                              { return configFacturacionCrudService.buscarTodos(); }
    public ConfigFacturacion actualizarConfigFacturacion(Short id, ConfigFacturacion e)   { return configFacturacionCrudService.actualizar(id, e); }
    public void eliminarConfigFacturacion(Short id)                                       { configFacturacionCrudService.eliminar(id); }

    // ════════════════════════════════════════════════════════════════════════
    // MAESTRAS
    // ════════════════════════════════════════════════════════════════════════

    // Especialidad
    public Especialidad crearEspecialidad(Especialidad e)                       { return especialidadCrudService.crear(e); }
    public Optional<Especialidad> buscarEspecialidadPorId(Integer id)           { return especialidadCrudService.buscarPorId(id); }
    public List<Especialidad> listarEspecialidad()                              { return especialidadCrudService.buscarTodos(); }
    public Especialidad actualizarEspecialidad(Integer id, Especialidad e)      { return especialidadCrudService.actualizar(id, e); }
    public void eliminarEspecialidad(Integer id)                                { especialidadCrudService.eliminar(id); }

    // Empleado
    public Empleado crearEmpleado(Empleado e)                       { return empleadoCrudService.crear(e); }
    public Optional<Empleado> buscarEmpleadoPorId(Integer id)       { return empleadoCrudService.buscarPorId(id); }
    public List<Empleado> listarEmpleado()                          { return empleadoCrudService.buscarTodos(); }
    public Empleado actualizarEmpleado(Integer id, Empleado e)      { return empleadoCrudService.actualizar(id, e); }
    public void eliminarEmpleado(Integer id)                        { empleadoCrudService.eliminar(id); }

    // SeguridadUsuario
    public SeguridadUsuario crearSeguridadUsuario(SeguridadUsuario e)                   { return seguridadUsuarioCrudService.crear(e); }
    public Optional<SeguridadUsuario> buscarSeguridadUsuarioPorId(Integer id)           { return seguridadUsuarioCrudService.buscarPorId(id); }
    public List<SeguridadUsuario> listarSeguridadUsuario()                              { return seguridadUsuarioCrudService.buscarTodos(); }
    public SeguridadUsuario actualizarSeguridadUsuario(Integer id, SeguridadUsuario e)  { return seguridadUsuarioCrudService.actualizar(id, e); }
    public void eliminarSeguridadUsuario(Integer id)                                    { seguridadUsuarioCrudService.eliminar(id); }

    // MedicoPerfil
    public MedicoPerfil crearMedicoPerfil(MedicoPerfil e)                       { return medicoPerfilCrudService.crear(e); }
    public Optional<MedicoPerfil> buscarMedicoPerfilPorId(Integer id)           { return medicoPerfilCrudService.buscarPorId(id); }
    public List<MedicoPerfil> listarMedicoPerfil()                              { return medicoPerfilCrudService.buscarTodos(); }
    public MedicoPerfil actualizarMedicoPerfil(Integer id, MedicoPerfil e)      { return medicoPerfilCrudService.actualizar(id, e); }
    public void eliminarMedicoPerfil(Integer id)                                { medicoPerfilCrudService.eliminar(id); }

    // EnfermeroPerfil
    public EnfermeroPerfil crearEnfermeroPerfil(EnfermeroPerfil e)                      { return enfermeroPerfilCrudService.crear(e); }
    public Optional<EnfermeroPerfil> buscarEnfermeroPerfilPorId(Integer id)             { return enfermeroPerfilCrudService.buscarPorId(id); }
    public List<EnfermeroPerfil> listarEnfermeroPerfil()                                { return enfermeroPerfilCrudService.buscarTodos(); }
    public EnfermeroPerfil actualizarEnfermeroPerfil(Integer id, EnfermeroPerfil e)     { return enfermeroPerfilCrudService.actualizar(id, e); }
    public void eliminarEnfermeroPerfil(Integer id)                                     { enfermeroPerfilCrudService.eliminar(id); }

    // Paciente
    public Paciente crearPaciente(Paciente e)                       { return pacienteCrudService.crear(e); }
    public Optional<Paciente> buscarPacientePorId(Integer id)       { return pacienteCrudService.buscarPorId(id); }
    public List<Paciente> listarPaciente()                          { return pacienteCrudService.buscarTodos(); }
    public Paciente actualizarPaciente(Integer id, Paciente e)      { return pacienteCrudService.actualizar(id, e); }
    public void eliminarPaciente(Integer id)                        { pacienteCrudService.eliminar(id); }

    // ContactoEmergencia
    public ContactoEmergencia crearContactoEmergencia(ContactoEmergencia e)                  { return contactoEmergenciaCrudService.crear(e); }
    public Optional<ContactoEmergencia> buscarContactoEmergenciaPorId(Integer id)            { return contactoEmergenciaCrudService.buscarPorId(id); }
    public List<ContactoEmergencia> listarContactoEmergencia()                               { return contactoEmergenciaCrudService.buscarTodos(); }
    public ContactoEmergencia actualizarContactoEmergencia(Integer id, ContactoEmergencia e) { return contactoEmergenciaCrudService.actualizar(id, e); }
    public void eliminarContactoEmergencia(Integer id)                                       { contactoEmergenciaCrudService.eliminar(id); }

    // SeguroMedico
    public SeguroMedico crearSeguroMedico(SeguroMedico e)                       { return seguroMedicoCrudService.crear(e); }
    public Optional<SeguroMedico> buscarSeguroMedicoPorId(Integer id)           { return seguroMedicoCrudService.buscarPorId(id); }
    public List<SeguroMedico> listarSeguroMedico()                              { return seguroMedicoCrudService.buscarTodos(); }
    public SeguroMedico actualizarSeguroMedico(Integer id, SeguroMedico e)      { return seguroMedicoCrudService.actualizar(id, e); }
    public void eliminarSeguroMedico(Integer id)                                { seguroMedicoCrudService.eliminar(id); }

    // MedicamentoCatalogo
    public MedicamentoCatalogo crearMedicamentoCatalogo(MedicamentoCatalogo e)                  { return medicamentoCatalogoCrudService.crear(e); }
    public Optional<MedicamentoCatalogo> buscarMedicamentoCatalogoPorId(Integer id)             { return medicamentoCatalogoCrudService.buscarPorId(id); }
    public List<MedicamentoCatalogo> listarMedicamentoCatalogo()                                { return medicamentoCatalogoCrudService.buscarTodos(); }
    public MedicamentoCatalogo actualizarMedicamentoCatalogo(Integer id, MedicamentoCatalogo e) { return medicamentoCatalogoCrudService.actualizar(id, e); }
    public void eliminarMedicamentoCatalogo(Integer id)                                         { medicamentoCatalogoCrudService.eliminar(id); }

    // ProcedimientoCatalogo
    public ProcedimientoCatalogo crearProcedimientoCatalogo(ProcedimientoCatalogo e)                  { return procedimientoCatalogoCrudService.crear(e); }
    public Optional<ProcedimientoCatalogo> buscarProcedimientoCatalogoPorId(Integer id)              { return procedimientoCatalogoCrudService.buscarPorId(id); }
    public List<ProcedimientoCatalogo> listarProcedimientoCatalogo()                                 { return procedimientoCatalogoCrudService.buscarTodos(); }
    public ProcedimientoCatalogo actualizarProcedimientoCatalogo(Integer id, ProcedimientoCatalogo e){ return procedimientoCatalogoCrudService.actualizar(id, e); }
    public void eliminarProcedimientoCatalogo(Integer id)                                            { procedimientoCatalogoCrudService.eliminar(id); }

    // AyudaDiagnosticaCatalogo
    public AyudaDiagnosticaCatalogo crearAyudaDiagnosticaCatalogo(AyudaDiagnosticaCatalogo e)                  { return ayudaDiagnosticaCatalogoCrudService.crear(e); }
    public Optional<AyudaDiagnosticaCatalogo> buscarAyudaDiagnosticaCatalogoPorId(Integer id)                 { return ayudaDiagnosticaCatalogoCrudService.buscarPorId(id); }
    public List<AyudaDiagnosticaCatalogo> listarAyudaDiagnosticaCatalogo()                                    { return ayudaDiagnosticaCatalogoCrudService.buscarTodos(); }
    public AyudaDiagnosticaCatalogo actualizarAyudaDiagnosticaCatalogo(Integer id, AyudaDiagnosticaCatalogo e){ return ayudaDiagnosticaCatalogoCrudService.actualizar(id, e); }
    public void eliminarAyudaDiagnosticaCatalogo(Integer id)                                                   { ayudaDiagnosticaCatalogoCrudService.eliminar(id); }

    // ════════════════════════════════════════════════════════════════════════
    // TRANSACCIONALES
    // ════════════════════════════════════════════════════════════════════════

    // Cita
    public Cita crearCita(Cita e)                       { return citaCrudService.crear(e); }
    public Optional<Cita> buscarCitaPorId(Integer id)   { return citaCrudService.buscarPorId(id); }
    public List<Cita> listarCita()                      { return citaCrudService.buscarTodos(); }
    public Cita actualizarCita(Integer id, Cita e)      { return citaCrudService.actualizar(id, e); }
    public void eliminarCita(Integer id)                { citaCrudService.eliminar(id); }

    // EncuentroClinico
    public EncuentroClinico crearEncuentroClinico(EncuentroClinico e)                   { return encuentroClinicoCrudService.crear(e); }
    public Optional<EncuentroClinico> buscarEncuentroClinicoPorId(Integer id)           { return encuentroClinicoCrudService.buscarPorId(id); }
    public List<EncuentroClinico> listarEncuentroClinico()                              { return encuentroClinicoCrudService.buscarTodos(); }
    public EncuentroClinico actualizarEncuentroClinico(Integer id, EncuentroClinico e)  { return encuentroClinicoCrudService.actualizar(id, e); }
    public void eliminarEncuentroClinico(Integer id)                                    { encuentroClinicoCrudService.eliminar(id); }

    // SignoVital
    public SignoVital crearSignoVital(SignoVital e)                      { return signoVitalCrudService.crear(e); }
    public Optional<SignoVital> buscarSignoVitalPorId(Integer id)        { return signoVitalCrudService.buscarPorId(id); }
    public List<SignoVital> listarSignoVital()                           { return signoVitalCrudService.buscarTodos(); }
    public SignoVital actualizarSignoVital(Integer id, SignoVital e)     { return signoVitalCrudService.actualizar(id, e); }
    public void eliminarSignoVital(Integer id)                           { signoVitalCrudService.eliminar(id); }

    // AdministracionMedicamento
    public AdministracionMedicamento crearAdministracionMedicamento(AdministracionMedicamento e)                  { return administracionMedicamentoCrudService.crear(e); }
    public Optional<AdministracionMedicamento> buscarAdministracionMedicamentoPorId(Integer id)                  { return administracionMedicamentoCrudService.buscarPorId(id); }
    public List<AdministracionMedicamento> listarAdministracionMedicamento()                                     { return administracionMedicamentoCrudService.buscarTodos(); }
    public AdministracionMedicamento actualizarAdministracionMedicamento(Integer id, AdministracionMedicamento e){ return administracionMedicamentoCrudService.actualizar(id, e); }
    public void eliminarAdministracionMedicamento(Integer id)                                                    { administracionMedicamentoCrudService.eliminar(id); }

    // ProcedimientoEnfermeria
    public ProcedimientoEnfermeria crearProcedimientoEnfermeria(ProcedimientoEnfermeria e)                  { return procedimientoEnfermeriaCrudService.crear(e); }
    public Optional<ProcedimientoEnfermeria> buscarProcedimientoEnfermeriaPorId(Integer id)                 { return procedimientoEnfermeriaCrudService.buscarPorId(id); }
    public List<ProcedimientoEnfermeria> listarProcedimientoEnfermeria()                                    { return procedimientoEnfermeriaCrudService.buscarTodos(); }
    public ProcedimientoEnfermeria actualizarProcedimientoEnfermeria(Integer id, ProcedimientoEnfermeria e) { return procedimientoEnfermeriaCrudService.actualizar(id, e); }
    public void eliminarProcedimientoEnfermeria(Integer id)                                                 { procedimientoEnfermeriaCrudService.eliminar(id); }

    // OrdenMedica
    public OrdenMedica crearOrdenMedica(OrdenMedica e)                      { return ordenMedicaCrudService.crear(e); }
    public Optional<OrdenMedica> buscarOrdenMedicaPorId(Integer id)         { return ordenMedicaCrudService.buscarPorId(id); }
    public List<OrdenMedica> listarOrdenMedica()                            { return ordenMedicaCrudService.buscarTodos(); }
    public OrdenMedica actualizarOrdenMedica(Integer id, OrdenMedica e)     { return ordenMedicaCrudService.actualizar(id, e); }
    public void eliminarOrdenMedica(Integer id)                             { ordenMedicaCrudService.eliminar(id); }

    // OrdenMedicamentoDetalle
    public OrdenMedicamentoDetalle crearOrdenMedicamentoDetalle(OrdenMedicamentoDetalle e)                  { return ordenMedicamentoDetalleCrudService.crear(e); }
    public Optional<OrdenMedicamentoDetalle> buscarOrdenMedicamentoDetallePorId(Integer id)                 { return ordenMedicamentoDetalleCrudService.buscarPorId(id); }
    public List<OrdenMedicamentoDetalle> listarOrdenMedicamentoDetalle()                                    { return ordenMedicamentoDetalleCrudService.buscarTodos(); }
    public OrdenMedicamentoDetalle actualizarOrdenMedicamentoDetalle(Integer id, OrdenMedicamentoDetalle e) { return ordenMedicamentoDetalleCrudService.actualizar(id, e); }
    public void eliminarOrdenMedicamentoDetalle(Integer id)                                                 { ordenMedicamentoDetalleCrudService.eliminar(id); }

    // OrdenProcedimientoDetalle
    public OrdenProcedimientoDetalle crearOrdenProcedimientoDetalle(OrdenProcedimientoDetalle e)                  { return ordenProcedimientoDetalleCrudService.crear(e); }
    public Optional<OrdenProcedimientoDetalle> buscarOrdenProcedimientoDetallePorId(Integer id)                  { return ordenProcedimientoDetalleCrudService.buscarPorId(id); }
    public List<OrdenProcedimientoDetalle> listarOrdenProcedimientoDetalle()                                     { return ordenProcedimientoDetalleCrudService.buscarTodos(); }
    public OrdenProcedimientoDetalle actualizarOrdenProcedimientoDetalle(Integer id, OrdenProcedimientoDetalle e){ return ordenProcedimientoDetalleCrudService.actualizar(id, e); }
    public void eliminarOrdenProcedimientoDetalle(Integer id)                                                    { ordenProcedimientoDetalleCrudService.eliminar(id); }

    // OrdenAyudaDiagnosticaDetalle
    public OrdenAyudaDiagnosticaDetalle crearOrdenAyudaDiagnosticaDetalle(OrdenAyudaDiagnosticaDetalle e)                  { return ordenAyudaDiagnosticaDetalleCrudService.crear(e); }
    public Optional<OrdenAyudaDiagnosticaDetalle> buscarOrdenAyudaDiagnosticaDetallePorId(Integer id)                     { return ordenAyudaDiagnosticaDetalleCrudService.buscarPorId(id); }
    public List<OrdenAyudaDiagnosticaDetalle> listarOrdenAyudaDiagnosticaDetalle()                                        { return ordenAyudaDiagnosticaDetalleCrudService.buscarTodos(); }
    public OrdenAyudaDiagnosticaDetalle actualizarOrdenAyudaDiagnosticaDetalle(Integer id, OrdenAyudaDiagnosticaDetalle e){ return ordenAyudaDiagnosticaDetalleCrudService.actualizar(id, e); }
    public void eliminarOrdenAyudaDiagnosticaDetalle(Integer id)                                                           { ordenAyudaDiagnosticaDetalleCrudService.eliminar(id); }

    // Factura
    public Factura crearFactura(Factura e)                      { return facturaCrudService.crear(e); }
    public Optional<Factura> buscarFacturaPorId(Integer id)     { return facturaCrudService.buscarPorId(id); }
    public List<Factura> listarFactura()                        { return facturaCrudService.buscarTodos(); }
    public Factura actualizarFactura(Integer id, Factura e)     { return facturaCrudService.actualizar(id, e); }
    public void eliminarFactura(Integer id)                     { facturaCrudService.eliminar(id); }

    // FacturaDetalle
    public FacturaDetalle crearFacturaDetalle(FacturaDetalle e)                     { return facturaDetalleCrudService.crear(e); }
    public Optional<FacturaDetalle> buscarFacturaDetallePorId(Integer id)           { return facturaDetalleCrudService.buscarPorId(id); }
    public List<FacturaDetalle> listarFacturaDetalle()                              { return facturaDetalleCrudService.buscarTodos(); }
    public FacturaDetalle actualizarFacturaDetalle(Integer id, FacturaDetalle e)    { return facturaDetalleCrudService.actualizar(id, e); }
    public void eliminarFacturaDetalle(Integer id)                                  { facturaDetalleCrudService.eliminar(id); }

    // Pago
    public Pago crearPago(Pago e)                       { return pagoCrudService.crear(e); }
    public Optional<Pago> buscarPagoPorId(Integer id)   { return pagoCrudService.buscarPorId(id); }
    public List<Pago> listarPago()                      { return pagoCrudService.buscarTodos(); }
    public Pago actualizarPago(Integer id, Pago e)      { return pagoCrudService.actualizar(id, e); }
    public void eliminarPago(Integer id)                { pagoCrudService.eliminar(id); }
}
