package app.infrastructure.controller;

import app.application.usecase.SoporteUseCase;
import app.domain.entities.catalogs.*;
import app.domain.entities.masters.*;
import app.domain.entities.transactions.*;
import app.domain.exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el rol SOPORTE DE INFORMACIÓN.
 * Expone CRUD completo sobre todas las tablas del dominio.
 * GET /buscar/{id}  → 200 OK | 404 Not Found
 * GET /             → 200 OK
 * POST /            → 201 Created
 * PUT /{id}         → 200 OK | 404 Not Found
 * DELETE /{id}      → 204 No Content | 404 Not Found
 */
@RestController
@RequestMapping("/api/soporte")
public class SoporteController {

    private final SoporteUseCase useCase;

    public SoporteController(SoporteUseCase useCase) {
        this.useCase = useCase;
    }

    // ════════════════════════════════════════════════════════════════════════
    // CATÁLOGOS — PK Short
    // ════════════════════════════════════════════════════════════════════════

    // CatRolUsuario
    @PostMapping("/cat-roles") public ResponseEntity<CatRolUsuario> crearRol(@RequestBody CatRolUsuario e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatRolUsuario(e)); }
    @GetMapping("/cat-roles") public ResponseEntity<List<CatRolUsuario>> listarRoles() { return ResponseEntity.ok(useCase.listarCatRolUsuario()); }
    @GetMapping("/cat-roles/{id}") public ResponseEntity<CatRolUsuario> buscarRol(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatRolUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado: " + id))); }
    @PutMapping("/cat-roles/{id}") public ResponseEntity<CatRolUsuario> actualizarRol(@PathVariable Short id, @RequestBody CatRolUsuario e) { return ResponseEntity.ok(useCase.actualizarCatRolUsuario(id, e)); }
    @DeleteMapping("/cat-roles/{id}") public ResponseEntity<Void> eliminarRol(@PathVariable Short id) { useCase.eliminarCatRolUsuario(id); return ResponseEntity.noContent().build(); }

    // CatTipoDocumento
    @PostMapping("/cat-tipos-doc") public ResponseEntity<CatTipoDocumento> crearTipoDoc(@RequestBody CatTipoDocumento e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatTipoDocumento(e)); }
    @GetMapping("/cat-tipos-doc") public ResponseEntity<List<CatTipoDocumento>> listarTiposDoc() { return ResponseEntity.ok(useCase.listarCatTipoDocumento()); }
    @GetMapping("/cat-tipos-doc/{id}") public ResponseEntity<CatTipoDocumento> buscarTipoDoc(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatTipoDocumentoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("TipoDocumento no encontrado: " + id))); }
    @PutMapping("/cat-tipos-doc/{id}") public ResponseEntity<CatTipoDocumento> actualizarTipoDoc(@PathVariable Short id, @RequestBody CatTipoDocumento e) { return ResponseEntity.ok(useCase.actualizarCatTipoDocumento(id, e)); }
    @DeleteMapping("/cat-tipos-doc/{id}") public ResponseEntity<Void> eliminarTipoDoc(@PathVariable Short id) { useCase.eliminarCatTipoDocumento(id); return ResponseEntity.noContent().build(); }

    // CatGenero
    @PostMapping("/cat-generos") public ResponseEntity<CatGenero> crearGenero(@RequestBody CatGenero e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatGenero(e)); }
    @GetMapping("/cat-generos") public ResponseEntity<List<CatGenero>> listarGeneros() { return ResponseEntity.ok(useCase.listarCatGenero()); }
    @GetMapping("/cat-generos/{id}") public ResponseEntity<CatGenero> buscarGenero(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatGeneroPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Género no encontrado: " + id))); }
    @PutMapping("/cat-generos/{id}") public ResponseEntity<CatGenero> actualizarGenero(@PathVariable Short id, @RequestBody CatGenero e) { return ResponseEntity.ok(useCase.actualizarCatGenero(id, e)); }
    @DeleteMapping("/cat-generos/{id}") public ResponseEntity<Void> eliminarGenero(@PathVariable Short id) { useCase.eliminarCatGenero(id); return ResponseEntity.noContent().build(); }

    // CatEstadoCita
    @PostMapping("/cat-estados-cita") public ResponseEntity<CatEstadoCita> crearEstadoCita(@RequestBody CatEstadoCita e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatEstadoCita(e)); }
    @GetMapping("/cat-estados-cita") public ResponseEntity<List<CatEstadoCita>> listarEstadosCita() { return ResponseEntity.ok(useCase.listarCatEstadoCita()); }
    @GetMapping("/cat-estados-cita/{id}") public ResponseEntity<CatEstadoCita> buscarEstadoCita(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatEstadoCitaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("EstadoCita no encontrado: " + id))); }
    @PutMapping("/cat-estados-cita/{id}") public ResponseEntity<CatEstadoCita> actualizarEstadoCita(@PathVariable Short id, @RequestBody CatEstadoCita e) { return ResponseEntity.ok(useCase.actualizarCatEstadoCita(id, e)); }
    @DeleteMapping("/cat-estados-cita/{id}") public ResponseEntity<Void> eliminarEstadoCita(@PathVariable Short id) { useCase.eliminarCatEstadoCita(id); return ResponseEntity.noContent().build(); }

    // CatPrioridadAtencion
    @PostMapping("/cat-prioridades") public ResponseEntity<CatPrioridadAtencion> crearPrioridad(@RequestBody CatPrioridadAtencion e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatPrioridadAtencion(e)); }
    @GetMapping("/cat-prioridades") public ResponseEntity<List<CatPrioridadAtencion>> listarPrioridades() { return ResponseEntity.ok(useCase.listarCatPrioridadAtencion()); }
    @GetMapping("/cat-prioridades/{id}") public ResponseEntity<CatPrioridadAtencion> buscarPrioridad(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatPrioridadAtencionPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Prioridad no encontrada: " + id))); }
    @PutMapping("/cat-prioridades/{id}") public ResponseEntity<CatPrioridadAtencion> actualizarPrioridad(@PathVariable Short id, @RequestBody CatPrioridadAtencion e) { return ResponseEntity.ok(useCase.actualizarCatPrioridadAtencion(id, e)); }
    @DeleteMapping("/cat-prioridades/{id}") public ResponseEntity<Void> eliminarPrioridad(@PathVariable Short id) { useCase.eliminarCatPrioridadAtencion(id); return ResponseEntity.noContent().build(); }

    // CatEstadoSeguro
    @PostMapping("/cat-estados-seguro") public ResponseEntity<CatEstadoSeguro> crearEstadoSeguro(@RequestBody CatEstadoSeguro e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatEstadoSeguro(e)); }
    @GetMapping("/cat-estados-seguro") public ResponseEntity<List<CatEstadoSeguro>> listarEstadosSeguro() { return ResponseEntity.ok(useCase.listarCatEstadoSeguro()); }
    @GetMapping("/cat-estados-seguro/{id}") public ResponseEntity<CatEstadoSeguro> buscarEstadoSeguro(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatEstadoSeguroPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("EstadoSeguro no encontrado: " + id))); }
    @PutMapping("/cat-estados-seguro/{id}") public ResponseEntity<CatEstadoSeguro> actualizarEstadoSeguro(@PathVariable Short id, @RequestBody CatEstadoSeguro e) { return ResponseEntity.ok(useCase.actualizarCatEstadoSeguro(id, e)); }
    @DeleteMapping("/cat-estados-seguro/{id}") public ResponseEntity<Void> eliminarEstadoSeguro(@PathVariable Short id) { useCase.eliminarCatEstadoSeguro(id); return ResponseEntity.noContent().build(); }

    // CatTipoOrden
    @PostMapping("/cat-tipos-orden") public ResponseEntity<CatTipoOrden> crearTipoOrden(@RequestBody CatTipoOrden e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatTipoOrden(e)); }
    @GetMapping("/cat-tipos-orden") public ResponseEntity<List<CatTipoOrden>> listarTiposOrden() { return ResponseEntity.ok(useCase.listarCatTipoOrden()); }
    @GetMapping("/cat-tipos-orden/{id}") public ResponseEntity<CatTipoOrden> buscarTipoOrden(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatTipoOrdenPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("TipoOrden no encontrado: " + id))); }
    @PutMapping("/cat-tipos-orden/{id}") public ResponseEntity<CatTipoOrden> actualizarTipoOrden(@PathVariable Short id, @RequestBody CatTipoOrden e) { return ResponseEntity.ok(useCase.actualizarCatTipoOrden(id, e)); }
    @DeleteMapping("/cat-tipos-orden/{id}") public ResponseEntity<Void> eliminarTipoOrden(@PathVariable Short id) { useCase.eliminarCatTipoOrden(id); return ResponseEntity.noContent().build(); }

    // CatTipoDetalleOrden
    @PostMapping("/cat-tipos-detalle-orden") public ResponseEntity<CatTipoDetalleOrden> crearTipoDetalleOrden(@RequestBody CatTipoDetalleOrden e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatTipoDetalleOrden(e)); }
    @GetMapping("/cat-tipos-detalle-orden") public ResponseEntity<List<CatTipoDetalleOrden>> listarTiposDetalleOrden() { return ResponseEntity.ok(useCase.listarCatTipoDetalleOrden()); }
    @GetMapping("/cat-tipos-detalle-orden/{id}") public ResponseEntity<CatTipoDetalleOrden> buscarTipoDetalleOrden(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatTipoDetalleOrdenPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("TipoDetalleOrden no encontrado: " + id))); }
    @PutMapping("/cat-tipos-detalle-orden/{id}") public ResponseEntity<CatTipoDetalleOrden> actualizarTipoDetalleOrden(@PathVariable Short id, @RequestBody CatTipoDetalleOrden e) { return ResponseEntity.ok(useCase.actualizarCatTipoDetalleOrden(id, e)); }
    @DeleteMapping("/cat-tipos-detalle-orden/{id}") public ResponseEntity<Void> eliminarTipoDetalleOrden(@PathVariable Short id) { useCase.eliminarCatTipoDetalleOrden(id); return ResponseEntity.noContent().build(); }

    // CatTipoFacturacion
    @PostMapping("/cat-tipos-facturacion") public ResponseEntity<CatTipoFacturacion> crearTipoFact(@RequestBody CatTipoFacturacion e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatTipoFacturacion(e)); }
    @GetMapping("/cat-tipos-facturacion") public ResponseEntity<List<CatTipoFacturacion>> listarTiposFact() { return ResponseEntity.ok(useCase.listarCatTipoFacturacion()); }
    @GetMapping("/cat-tipos-facturacion/{id}") public ResponseEntity<CatTipoFacturacion> buscarTipoFact(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatTipoFacturacionPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("TipoFacturacion no encontrado: " + id))); }
    @PutMapping("/cat-tipos-facturacion/{id}") public ResponseEntity<CatTipoFacturacion> actualizarTipoFact(@PathVariable Short id, @RequestBody CatTipoFacturacion e) { return ResponseEntity.ok(useCase.actualizarCatTipoFacturacion(id, e)); }
    @DeleteMapping("/cat-tipos-facturacion/{id}") public ResponseEntity<Void> eliminarTipoFact(@PathVariable Short id) { useCase.eliminarCatTipoFacturacion(id); return ResponseEntity.noContent().build(); }

    // CatEstadoEspecialidad
    @PostMapping("/cat-estados-especialidad") public ResponseEntity<CatEstadoEspecialidad> crearEstadoEsp(@RequestBody CatEstadoEspecialidad e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCatEstadoEspecialidad(e)); }
    @GetMapping("/cat-estados-especialidad") public ResponseEntity<List<CatEstadoEspecialidad>> listarEstadosEsp() { return ResponseEntity.ok(useCase.listarCatEstadoEspecialidad()); }
    @GetMapping("/cat-estados-especialidad/{id}") public ResponseEntity<CatEstadoEspecialidad> buscarEstadoEsp(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarCatEstadoEspecialidadPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("EstadoEspecialidad no encontrado: " + id))); }
    @PutMapping("/cat-estados-especialidad/{id}") public ResponseEntity<CatEstadoEspecialidad> actualizarEstadoEsp(@PathVariable Short id, @RequestBody CatEstadoEspecialidad e) { return ResponseEntity.ok(useCase.actualizarCatEstadoEspecialidad(id, e)); }
    @DeleteMapping("/cat-estados-especialidad/{id}") public ResponseEntity<Void> eliminarEstadoEsp(@PathVariable Short id) { useCase.eliminarCatEstadoEspecialidad(id); return ResponseEntity.noContent().build(); }

    // ConfigFacturacion
    @PostMapping("/config-facturacion") public ResponseEntity<ConfigFacturacion> crearConfig(@RequestBody ConfigFacturacion e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearConfigFacturacion(e)); }
    @GetMapping("/config-facturacion") public ResponseEntity<List<ConfigFacturacion>> listarConfig() { return ResponseEntity.ok(useCase.listarConfigFacturacion()); }
    @GetMapping("/config-facturacion/{id}") public ResponseEntity<ConfigFacturacion> buscarConfig(@PathVariable Short id) { return ResponseEntity.ok(useCase.buscarConfigFacturacionPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("ConfigFacturacion no encontrada: " + id))); }
    @PutMapping("/config-facturacion/{id}") public ResponseEntity<ConfigFacturacion> actualizarConfig(@PathVariable Short id, @RequestBody ConfigFacturacion e) { return ResponseEntity.ok(useCase.actualizarConfigFacturacion(id, e)); }
    @DeleteMapping("/config-facturacion/{id}") public ResponseEntity<Void> eliminarConfig(@PathVariable Short id) { useCase.eliminarConfigFacturacion(id); return ResponseEntity.noContent().build(); }

    // ════════════════════════════════════════════════════════════════════════
    // MAESTRAS — PK Integer
    // ════════════════════════════════════════════════════════════════════════

    // Especialidad
    @PostMapping("/especialidades") public ResponseEntity<Especialidad> crearEsp(@RequestBody Especialidad e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearEspecialidad(e)); }
    @GetMapping("/especialidades") public ResponseEntity<List<Especialidad>> listarEsp() { return ResponseEntity.ok(useCase.listarEspecialidad()); }
    @GetMapping("/especialidades/{id}") public ResponseEntity<Especialidad> buscarEsp(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarEspecialidadPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada: " + id))); }
    @PutMapping("/especialidades/{id}") public ResponseEntity<Especialidad> actualizarEsp(@PathVariable Integer id, @RequestBody Especialidad e) { return ResponseEntity.ok(useCase.actualizarEspecialidad(id, e)); }
    @DeleteMapping("/especialidades/{id}") public ResponseEntity<Void> eliminarEsp(@PathVariable Integer id) { useCase.eliminarEspecialidad(id); return ResponseEntity.noContent().build(); }

    // Empleado
    @PostMapping("/empleados") public ResponseEntity<Empleado> crearEmp(@RequestBody Empleado e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearEmpleado(e)); }
    @GetMapping("/empleados") public ResponseEntity<List<Empleado>> listarEmps() { return ResponseEntity.ok(useCase.listarEmpleado()); }
    @GetMapping("/empleados/{id}") public ResponseEntity<Empleado> buscarEmp(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarEmpleadoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id))); }
    @PutMapping("/empleados/{id}") public ResponseEntity<Empleado> actualizarEmp(@PathVariable Integer id, @RequestBody Empleado e) { return ResponseEntity.ok(useCase.actualizarEmpleado(id, e)); }
    @DeleteMapping("/empleados/{id}") public ResponseEntity<Void> eliminarEmp(@PathVariable Integer id) { useCase.eliminarEmpleado(id); return ResponseEntity.noContent().build(); }

    // SeguridadUsuario
    @PostMapping("/usuarios") public ResponseEntity<SeguridadUsuario> crearUsuario(@RequestBody SeguridadUsuario e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearSeguridadUsuario(e)); }
    @GetMapping("/usuarios") public ResponseEntity<List<SeguridadUsuario>> listarUsuarios() { return ResponseEntity.ok(useCase.listarSeguridadUsuario()); }
    @GetMapping("/usuarios/{id}") public ResponseEntity<SeguridadUsuario> buscarUsuario(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarSeguridadUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id))); }
    @PutMapping("/usuarios/{id}") public ResponseEntity<SeguridadUsuario> actualizarUsuario(@PathVariable Integer id, @RequestBody SeguridadUsuario e) { return ResponseEntity.ok(useCase.actualizarSeguridadUsuario(id, e)); }
    @DeleteMapping("/usuarios/{id}") public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) { useCase.eliminarSeguridadUsuario(id); return ResponseEntity.noContent().build(); }

    // MedicoPerfil
    @PostMapping("/medicos-perfil") public ResponseEntity<MedicoPerfil> crearMedico(@RequestBody MedicoPerfil e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearMedicoPerfil(e)); }
    @GetMapping("/medicos-perfil") public ResponseEntity<List<MedicoPerfil>> listarMedicos() { return ResponseEntity.ok(useCase.listarMedicoPerfil()); }
    @GetMapping("/medicos-perfil/{id}") public ResponseEntity<MedicoPerfil> buscarMedico(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarMedicoPerfilPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("MedicoPerfil no encontrado: " + id))); }
    @PutMapping("/medicos-perfil/{id}") public ResponseEntity<MedicoPerfil> actualizarMedico(@PathVariable Integer id, @RequestBody MedicoPerfil e) { return ResponseEntity.ok(useCase.actualizarMedicoPerfil(id, e)); }
    @DeleteMapping("/medicos-perfil/{id}") public ResponseEntity<Void> eliminarMedico(@PathVariable Integer id) { useCase.eliminarMedicoPerfil(id); return ResponseEntity.noContent().build(); }

    // EnfermeroPerfil
    @PostMapping("/enfermeros-perfil") public ResponseEntity<EnfermeroPerfil> crearEnfermero(@RequestBody EnfermeroPerfil e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearEnfermeroPerfil(e)); }
    @GetMapping("/enfermeros-perfil") public ResponseEntity<List<EnfermeroPerfil>> listarEnfermeros() { return ResponseEntity.ok(useCase.listarEnfermeroPerfil()); }
    @GetMapping("/enfermeros-perfil/{id}") public ResponseEntity<EnfermeroPerfil> buscarEnfermero(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarEnfermeroPerfilPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("EnfermeroPerfil no encontrado: " + id))); }
    @PutMapping("/enfermeros-perfil/{id}") public ResponseEntity<EnfermeroPerfil> actualizarEnfermero(@PathVariable Integer id, @RequestBody EnfermeroPerfil e) { return ResponseEntity.ok(useCase.actualizarEnfermeroPerfil(id, e)); }
    @DeleteMapping("/enfermeros-perfil/{id}") public ResponseEntity<Void> eliminarEnfermero(@PathVariable Integer id) { useCase.eliminarEnfermeroPerfil(id); return ResponseEntity.noContent().build(); }

    // Paciente
    @PostMapping("/pacientes") public ResponseEntity<Paciente> crearPac(@RequestBody Paciente e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearPaciente(e)); }
    @GetMapping("/pacientes") public ResponseEntity<List<Paciente>> listarPacs() { return ResponseEntity.ok(useCase.listarPaciente()); }
    @GetMapping("/pacientes/{id}") public ResponseEntity<Paciente> buscarPac(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarPacientePorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado: " + id))); }
    @PutMapping("/pacientes/{id}") public ResponseEntity<Paciente> actualizarPac(@PathVariable Integer id, @RequestBody Paciente e) { return ResponseEntity.ok(useCase.actualizarPaciente(id, e)); }
    @DeleteMapping("/pacientes/{id}") public ResponseEntity<Void> eliminarPac(@PathVariable Integer id) { useCase.eliminarPaciente(id); return ResponseEntity.noContent().build(); }

    // ContactoEmergencia
    @PostMapping("/contactos-emergencia") public ResponseEntity<ContactoEmergencia> crearContacto(@RequestBody ContactoEmergencia e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearContactoEmergencia(e)); }
    @GetMapping("/contactos-emergencia") public ResponseEntity<List<ContactoEmergencia>> listarContactos() { return ResponseEntity.ok(useCase.listarContactoEmergencia()); }
    @GetMapping("/contactos-emergencia/{id}") public ResponseEntity<ContactoEmergencia> buscarContacto(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarContactoEmergenciaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("ContactoEmergencia no encontrado: " + id))); }
    @PutMapping("/contactos-emergencia/{id}") public ResponseEntity<ContactoEmergencia> actualizarContacto(@PathVariable Integer id, @RequestBody ContactoEmergencia e) { return ResponseEntity.ok(useCase.actualizarContactoEmergencia(id, e)); }
    @DeleteMapping("/contactos-emergencia/{id}") public ResponseEntity<Void> eliminarContacto(@PathVariable Integer id) { useCase.eliminarContactoEmergencia(id); return ResponseEntity.noContent().build(); }

    // SeguroMedico
    @PostMapping("/seguros") public ResponseEntity<SeguroMedico> crearSeguro(@RequestBody SeguroMedico e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearSeguroMedico(e)); }
    @GetMapping("/seguros") public ResponseEntity<List<SeguroMedico>> listarSeguros() { return ResponseEntity.ok(useCase.listarSeguroMedico()); }
    @GetMapping("/seguros/{id}") public ResponseEntity<SeguroMedico> buscarSeguro(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarSeguroMedicoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("SeguroMedico no encontrado: " + id))); }
    @PutMapping("/seguros/{id}") public ResponseEntity<SeguroMedico> actualizarSeguro(@PathVariable Integer id, @RequestBody SeguroMedico e) { return ResponseEntity.ok(useCase.actualizarSeguroMedico(id, e)); }
    @DeleteMapping("/seguros/{id}") public ResponseEntity<Void> eliminarSeguro(@PathVariable Integer id) { useCase.eliminarSeguroMedico(id); return ResponseEntity.noContent().build(); }

    // MedicamentoCatalogo
    @PostMapping("/medicamentos") public ResponseEntity<MedicamentoCatalogo> crearMedicamento(@RequestBody MedicamentoCatalogo e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearMedicamentoCatalogo(e)); }
    @GetMapping("/medicamentos") public ResponseEntity<List<MedicamentoCatalogo>> listarMedicamentos() { return ResponseEntity.ok(useCase.listarMedicamentoCatalogo()); }
    @GetMapping("/medicamentos/{id}") public ResponseEntity<MedicamentoCatalogo> buscarMedicamento(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarMedicamentoCatalogoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado: " + id))); }
    @PutMapping("/medicamentos/{id}") public ResponseEntity<MedicamentoCatalogo> actualizarMedicamento(@PathVariable Integer id, @RequestBody MedicamentoCatalogo e) { return ResponseEntity.ok(useCase.actualizarMedicamentoCatalogo(id, e)); }
    @DeleteMapping("/medicamentos/{id}") public ResponseEntity<Void> eliminarMedicamento(@PathVariable Integer id) { useCase.eliminarMedicamentoCatalogo(id); return ResponseEntity.noContent().build(); }

    // ProcedimientoCatalogo
    @PostMapping("/procedimientos") public ResponseEntity<ProcedimientoCatalogo> crearProc(@RequestBody ProcedimientoCatalogo e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearProcedimientoCatalogo(e)); }
    @GetMapping("/procedimientos") public ResponseEntity<List<ProcedimientoCatalogo>> listarProcs() { return ResponseEntity.ok(useCase.listarProcedimientoCatalogo()); }
    @GetMapping("/procedimientos/{id}") public ResponseEntity<ProcedimientoCatalogo> buscarProc(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarProcedimientoCatalogoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Procedimiento no encontrado: " + id))); }
    @PutMapping("/procedimientos/{id}") public ResponseEntity<ProcedimientoCatalogo> actualizarProc(@PathVariable Integer id, @RequestBody ProcedimientoCatalogo e) { return ResponseEntity.ok(useCase.actualizarProcedimientoCatalogo(id, e)); }
    @DeleteMapping("/procedimientos/{id}") public ResponseEntity<Void> eliminarProc(@PathVariable Integer id) { useCase.eliminarProcedimientoCatalogo(id); return ResponseEntity.noContent().build(); }

    // AyudaDiagnosticaCatalogo
    @PostMapping("/ayudas-diagnosticas") public ResponseEntity<AyudaDiagnosticaCatalogo> crearAyuda(@RequestBody AyudaDiagnosticaCatalogo e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearAyudaDiagnosticaCatalogo(e)); }
    @GetMapping("/ayudas-diagnosticas") public ResponseEntity<List<AyudaDiagnosticaCatalogo>> listarAyudas() { return ResponseEntity.ok(useCase.listarAyudaDiagnosticaCatalogo()); }
    @GetMapping("/ayudas-diagnosticas/{id}") public ResponseEntity<AyudaDiagnosticaCatalogo> buscarAyuda(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarAyudaDiagnosticaCatalogoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("AyudaDiagnostica no encontrada: " + id))); }
    @PutMapping("/ayudas-diagnosticas/{id}") public ResponseEntity<AyudaDiagnosticaCatalogo> actualizarAyuda(@PathVariable Integer id, @RequestBody AyudaDiagnosticaCatalogo e) { return ResponseEntity.ok(useCase.actualizarAyudaDiagnosticaCatalogo(id, e)); }
    @DeleteMapping("/ayudas-diagnosticas/{id}") public ResponseEntity<Void> eliminarAyuda(@PathVariable Integer id) { useCase.eliminarAyudaDiagnosticaCatalogo(id); return ResponseEntity.noContent().build(); }

    // ════════════════════════════════════════════════════════════════════════
    // TRANSACCIONALES — PK Integer
    // ════════════════════════════════════════════════════════════════════════

    // Cita
    @PostMapping("/citas") public ResponseEntity<Cita> crearCita(@RequestBody Cita e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearCita(e)); }
    @GetMapping("/citas") public ResponseEntity<List<Cita>> listarCitas() { return ResponseEntity.ok(useCase.listarCita()); }
    @GetMapping("/citas/{id}") public ResponseEntity<Cita> buscarCita(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarCitaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada: " + id))); }
    @PutMapping("/citas/{id}") public ResponseEntity<Cita> actualizarCita(@PathVariable Integer id, @RequestBody Cita e) { return ResponseEntity.ok(useCase.actualizarCita(id, e)); }
    @DeleteMapping("/citas/{id}") public ResponseEntity<Void> eliminarCita(@PathVariable Integer id) { useCase.eliminarCita(id); return ResponseEntity.noContent().build(); }

    // EncuentroClinico
    @PostMapping("/encuentros") public ResponseEntity<EncuentroClinico> crearEncuentro(@RequestBody EncuentroClinico e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearEncuentroClinico(e)); }
    @GetMapping("/encuentros") public ResponseEntity<List<EncuentroClinico>> listarEncuentros() { return ResponseEntity.ok(useCase.listarEncuentroClinico()); }
    @GetMapping("/encuentros/{id}") public ResponseEntity<EncuentroClinico> buscarEncuentro(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarEncuentroClinicoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Encuentro no encontrado: " + id))); }
    @PutMapping("/encuentros/{id}") public ResponseEntity<EncuentroClinico> actualizarEncuentro(@PathVariable Integer id, @RequestBody EncuentroClinico e) { return ResponseEntity.ok(useCase.actualizarEncuentroClinico(id, e)); }
    @DeleteMapping("/encuentros/{id}") public ResponseEntity<Void> eliminarEncuentro(@PathVariable Integer id) { useCase.eliminarEncuentroClinico(id); return ResponseEntity.noContent().build(); }

    // SignoVital
    @PostMapping("/signos-vitales") public ResponseEntity<SignoVital> crearSigno(@RequestBody SignoVital e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearSignoVital(e)); }
    @GetMapping("/signos-vitales") public ResponseEntity<List<SignoVital>> listarSignos() { return ResponseEntity.ok(useCase.listarSignoVital()); }
    @GetMapping("/signos-vitales/{id}") public ResponseEntity<SignoVital> buscarSigno(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarSignoVitalPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("SignoVital no encontrado: " + id))); }
    @PutMapping("/signos-vitales/{id}") public ResponseEntity<SignoVital> actualizarSigno(@PathVariable Integer id, @RequestBody SignoVital e) { return ResponseEntity.ok(useCase.actualizarSignoVital(id, e)); }
    @DeleteMapping("/signos-vitales/{id}") public ResponseEntity<Void> eliminarSigno(@PathVariable Integer id) { useCase.eliminarSignoVital(id); return ResponseEntity.noContent().build(); }

    // AdministracionMedicamento
    @PostMapping("/admin-medicamentos") public ResponseEntity<AdministracionMedicamento> crearAdminMed(@RequestBody AdministracionMedicamento e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearAdministracionMedicamento(e)); }
    @GetMapping("/admin-medicamentos") public ResponseEntity<List<AdministracionMedicamento>> listarAdminMeds() { return ResponseEntity.ok(useCase.listarAdministracionMedicamento()); }
    @GetMapping("/admin-medicamentos/{id}") public ResponseEntity<AdministracionMedicamento> buscarAdminMed(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarAdministracionMedicamentoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("AdminMedicamento no encontrado: " + id))); }
    @PutMapping("/admin-medicamentos/{id}") public ResponseEntity<AdministracionMedicamento> actualizarAdminMed(@PathVariable Integer id, @RequestBody AdministracionMedicamento e) { return ResponseEntity.ok(useCase.actualizarAdministracionMedicamento(id, e)); }
    @DeleteMapping("/admin-medicamentos/{id}") public ResponseEntity<Void> eliminarAdminMed(@PathVariable Integer id) { useCase.eliminarAdministracionMedicamento(id); return ResponseEntity.noContent().build(); }

    // ProcedimientoEnfermeria
    @PostMapping("/procedimientos-enfermeria") public ResponseEntity<ProcedimientoEnfermeria> crearProcEnf(@RequestBody ProcedimientoEnfermeria e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearProcedimientoEnfermeria(e)); }
    @GetMapping("/procedimientos-enfermeria") public ResponseEntity<List<ProcedimientoEnfermeria>> listarProcsEnf() { return ResponseEntity.ok(useCase.listarProcedimientoEnfermeria()); }
    @GetMapping("/procedimientos-enfermeria/{id}") public ResponseEntity<ProcedimientoEnfermeria> buscarProcEnf(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarProcedimientoEnfermeriaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("ProcedimientoEnfermeria no encontrado: " + id))); }
    @PutMapping("/procedimientos-enfermeria/{id}") public ResponseEntity<ProcedimientoEnfermeria> actualizarProcEnf(@PathVariable Integer id, @RequestBody ProcedimientoEnfermeria e) { return ResponseEntity.ok(useCase.actualizarProcedimientoEnfermeria(id, e)); }
    @DeleteMapping("/procedimientos-enfermeria/{id}") public ResponseEntity<Void> eliminarProcEnf(@PathVariable Integer id) { useCase.eliminarProcedimientoEnfermeria(id); return ResponseEntity.noContent().build(); }

    // OrdenMedica
    @PostMapping("/ordenes-medicas") public ResponseEntity<OrdenMedica> crearOrden(@RequestBody OrdenMedica e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearOrdenMedica(e)); }
    @GetMapping("/ordenes-medicas") public ResponseEntity<List<OrdenMedica>> listarOrdenes() { return ResponseEntity.ok(useCase.listarOrdenMedica()); }
    @GetMapping("/ordenes-medicas/{id}") public ResponseEntity<OrdenMedica> buscarOrden(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarOrdenMedicaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("OrdenMedica no encontrada: " + id))); }
    @PutMapping("/ordenes-medicas/{id}") public ResponseEntity<OrdenMedica> actualizarOrden(@PathVariable Integer id, @RequestBody OrdenMedica e) { return ResponseEntity.ok(useCase.actualizarOrdenMedica(id, e)); }
    @DeleteMapping("/ordenes-medicas/{id}") public ResponseEntity<Void> eliminarOrden(@PathVariable Integer id) { useCase.eliminarOrdenMedica(id); return ResponseEntity.noContent().build(); }

    // OrdenMedicamentoDetalle
    @PostMapping("/ordenes-medicamento-detalle") public ResponseEntity<OrdenMedicamentoDetalle> crearDetMed(@RequestBody OrdenMedicamentoDetalle e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearOrdenMedicamentoDetalle(e)); }
    @GetMapping("/ordenes-medicamento-detalle") public ResponseEntity<List<OrdenMedicamentoDetalle>> listarDetMed() { return ResponseEntity.ok(useCase.listarOrdenMedicamentoDetalle()); }
    @GetMapping("/ordenes-medicamento-detalle/{id}") public ResponseEntity<OrdenMedicamentoDetalle> buscarDetMed(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarOrdenMedicamentoDetallePorId(id).orElseThrow(() -> new RecursoNoEncontradoException("OrdenMedicamentoDetalle no encontrado: " + id))); }
    @PutMapping("/ordenes-medicamento-detalle/{id}") public ResponseEntity<OrdenMedicamentoDetalle> actualizarDetMed(@PathVariable Integer id, @RequestBody OrdenMedicamentoDetalle e) { return ResponseEntity.ok(useCase.actualizarOrdenMedicamentoDetalle(id, e)); }
    @DeleteMapping("/ordenes-medicamento-detalle/{id}") public ResponseEntity<Void> eliminarDetMed(@PathVariable Integer id) { useCase.eliminarOrdenMedicamentoDetalle(id); return ResponseEntity.noContent().build(); }

    // OrdenProcedimientoDetalle
    @PostMapping("/ordenes-procedimiento-detalle") public ResponseEntity<OrdenProcedimientoDetalle> crearDetProc(@RequestBody OrdenProcedimientoDetalle e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearOrdenProcedimientoDetalle(e)); }
    @GetMapping("/ordenes-procedimiento-detalle") public ResponseEntity<List<OrdenProcedimientoDetalle>> listarDetProc() { return ResponseEntity.ok(useCase.listarOrdenProcedimientoDetalle()); }
    @GetMapping("/ordenes-procedimiento-detalle/{id}") public ResponseEntity<OrdenProcedimientoDetalle> buscarDetProc(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarOrdenProcedimientoDetallePorId(id).orElseThrow(() -> new RecursoNoEncontradoException("OrdenProcedimientoDetalle no encontrado: " + id))); }
    @PutMapping("/ordenes-procedimiento-detalle/{id}") public ResponseEntity<OrdenProcedimientoDetalle> actualizarDetProc(@PathVariable Integer id, @RequestBody OrdenProcedimientoDetalle e) { return ResponseEntity.ok(useCase.actualizarOrdenProcedimientoDetalle(id, e)); }
    @DeleteMapping("/ordenes-procedimiento-detalle/{id}") public ResponseEntity<Void> eliminarDetProc(@PathVariable Integer id) { useCase.eliminarOrdenProcedimientoDetalle(id); return ResponseEntity.noContent().build(); }

    // OrdenAyudaDiagnosticaDetalle
    @PostMapping("/ordenes-ayuda-detalle") public ResponseEntity<OrdenAyudaDiagnosticaDetalle> crearDetAyuda(@RequestBody OrdenAyudaDiagnosticaDetalle e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearOrdenAyudaDiagnosticaDetalle(e)); }
    @GetMapping("/ordenes-ayuda-detalle") public ResponseEntity<List<OrdenAyudaDiagnosticaDetalle>> listarDetAyuda() { return ResponseEntity.ok(useCase.listarOrdenAyudaDiagnosticaDetalle()); }
    @GetMapping("/ordenes-ayuda-detalle/{id}") public ResponseEntity<OrdenAyudaDiagnosticaDetalle> buscarDetAyuda(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarOrdenAyudaDiagnosticaDetallePorId(id).orElseThrow(() -> new RecursoNoEncontradoException("OrdenAyudaDiagnosticaDetalle no encontrado: " + id))); }
    @PutMapping("/ordenes-ayuda-detalle/{id}") public ResponseEntity<OrdenAyudaDiagnosticaDetalle> actualizarDetAyuda(@PathVariable Integer id, @RequestBody OrdenAyudaDiagnosticaDetalle e) { return ResponseEntity.ok(useCase.actualizarOrdenAyudaDiagnosticaDetalle(id, e)); }
    @DeleteMapping("/ordenes-ayuda-detalle/{id}") public ResponseEntity<Void> eliminarDetAyuda(@PathVariable Integer id) { useCase.eliminarOrdenAyudaDiagnosticaDetalle(id); return ResponseEntity.noContent().build(); }

    // Factura
    @PostMapping("/facturas") public ResponseEntity<Factura> crearFactura(@RequestBody Factura e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearFactura(e)); }
    @GetMapping("/facturas") public ResponseEntity<List<Factura>> listarFacturas() { return ResponseEntity.ok(useCase.listarFactura()); }
    @GetMapping("/facturas/{id}") public ResponseEntity<Factura> buscarFactura(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarFacturaPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada: " + id))); }
    @PutMapping("/facturas/{id}") public ResponseEntity<Factura> actualizarFactura(@PathVariable Integer id, @RequestBody Factura e) { return ResponseEntity.ok(useCase.actualizarFactura(id, e)); }
    @DeleteMapping("/facturas/{id}") public ResponseEntity<Void> eliminarFactura(@PathVariable Integer id) { useCase.eliminarFactura(id); return ResponseEntity.noContent().build(); }

    // FacturaDetalle
    @PostMapping("/facturas-detalle") public ResponseEntity<FacturaDetalle> crearFactDet(@RequestBody FacturaDetalle e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearFacturaDetalle(e)); }
    @GetMapping("/facturas-detalle") public ResponseEntity<List<FacturaDetalle>> listarFactsDet() { return ResponseEntity.ok(useCase.listarFacturaDetalle()); }
    @GetMapping("/facturas-detalle/{id}") public ResponseEntity<FacturaDetalle> buscarFactDet(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarFacturaDetallePorId(id).orElseThrow(() -> new RecursoNoEncontradoException("FacturaDetalle no encontrado: " + id))); }
    @PutMapping("/facturas-detalle/{id}") public ResponseEntity<FacturaDetalle> actualizarFactDet(@PathVariable Integer id, @RequestBody FacturaDetalle e) { return ResponseEntity.ok(useCase.actualizarFacturaDetalle(id, e)); }
    @DeleteMapping("/facturas-detalle/{id}") public ResponseEntity<Void> eliminarFactDet(@PathVariable Integer id) { useCase.eliminarFacturaDetalle(id); return ResponseEntity.noContent().build(); }

    // Pago
    @PostMapping("/pagos") public ResponseEntity<Pago> crearPago(@RequestBody Pago e) { return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearPago(e)); }
    @GetMapping("/pagos") public ResponseEntity<List<Pago>> listarPagos() { return ResponseEntity.ok(useCase.listarPago()); }
    @GetMapping("/pagos/{id}") public ResponseEntity<Pago> buscarPago(@PathVariable Integer id) { return ResponseEntity.ok(useCase.buscarPagoPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado: " + id))); }
    @PutMapping("/pagos/{id}") public ResponseEntity<Pago> actualizarPago(@PathVariable Integer id, @RequestBody Pago e) { return ResponseEntity.ok(useCase.actualizarPago(id, e)); }
    @DeleteMapping("/pagos/{id}") public ResponseEntity<Void> eliminarPago(@PathVariable Integer id) { useCase.eliminarPago(id); return ResponseEntity.noContent().build(); }
}
