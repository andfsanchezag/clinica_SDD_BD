package app.application.usecase;

import app.domain.dto.SpResultado;
import app.domain.dto.SpResultadoCopago;
import app.domain.services.agenda.CancelarCitaService;
import app.domain.services.agenda.ProgramarCitaService;
import app.domain.services.agenda.ReprogramarCitaService;
import app.domain.services.facturacion.CalcularCopagoService;
import app.domain.services.facturacion.EmitirFacturaService;
import app.domain.services.facturacion.RegistrarPagoService;
import app.domain.services.paciente.RegistrarContactoEmergenciaService;
import app.domain.services.paciente.RegistrarPacienteService;
import app.domain.services.paciente.RegistrarSeguroMedicoService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Caso de uso para el rol ADMINISTRATIVO.
 * Agrupa la gestión de pacientes, agenda y facturación.
 */
@Service
public class AdministrativoUseCase {

    private final RegistrarPacienteService registrarPacienteService;
    private final RegistrarContactoEmergenciaService registrarContactoEmergenciaService;
    private final RegistrarSeguroMedicoService registrarSeguroMedicoService;
    private final ProgramarCitaService programarCitaService;
    private final ReprogramarCitaService reprogramarCitaService;
    private final CancelarCitaService cancelarCitaService;
    private final CalcularCopagoService calcularCopagoService;
    private final EmitirFacturaService emitirFacturaService;
    private final RegistrarPagoService registrarPagoService;

    public AdministrativoUseCase(
            RegistrarPacienteService registrarPacienteService,
            RegistrarContactoEmergenciaService registrarContactoEmergenciaService,
            RegistrarSeguroMedicoService registrarSeguroMedicoService,
            ProgramarCitaService programarCitaService,
            ReprogramarCitaService reprogramarCitaService,
            CancelarCitaService cancelarCitaService,
            CalcularCopagoService calcularCopagoService,
            EmitirFacturaService emitirFacturaService,
            RegistrarPagoService registrarPagoService) {
        this.registrarPacienteService = registrarPacienteService;
        this.registrarContactoEmergenciaService = registrarContactoEmergenciaService;
        this.registrarSeguroMedicoService = registrarSeguroMedicoService;
        this.programarCitaService = programarCitaService;
        this.reprogramarCitaService = reprogramarCitaService;
        this.cancelarCitaService = cancelarCitaService;
        this.calcularCopagoService = calcularCopagoService;
        this.emitirFacturaService = emitirFacturaService;
        this.registrarPagoService = registrarPagoService;
    }

    // ── Pacientes ────────────────────────────────────────────────────────────

    public SpResultado registrarPaciente(
            String cedula,
            Short tipoDocId,
            String nombreCompleto,
            LocalDate fechaNacimiento,
            Short generoId,
            String direccion,
            String telefono,
            String correo,
            Integer usuarioOperador) {
        return registrarPacienteService.ejecutar(
                cedula, tipoDocId, nombreCompleto, fechaNacimiento,
                generoId, direccion, telefono, correo, usuarioOperador);
    }

    public SpResultado registrarContactoEmergencia(
            Integer pacienteId,
            String nombreCompleto,
            String relacion,
            String telefono,
            Integer usuarioOperador) {
        return registrarContactoEmergenciaService.ejecutar(
                pacienteId, nombreCompleto, relacion, telefono, usuarioOperador);
    }

    public SpResultado registrarSeguroMedico(
            Integer pacienteId,
            String compania,
            String numeroPoliza,
            Short estadoSeguroId,
            LocalDate fechaVigencia,
            Integer usuarioOperador) {
        return registrarSeguroMedicoService.ejecutar(
                pacienteId, compania, numeroPoliza, estadoSeguroId, fechaVigencia, usuarioOperador);
    }

    // ── Agenda ───────────────────────────────────────────────────────────────

    public SpResultado programarCita(
            Integer pacienteId,
            Integer medicoId,
            LocalDateTime fechaHora,
            Short prioridadId,
            String motivo,
            Integer usuarioOperador) {
        return programarCitaService.ejecutar(
                pacienteId, medicoId, fechaHora, prioridadId, motivo, usuarioOperador);
    }

    public SpResultado reprogramarCita(
            Integer citaId,
            LocalDateTime nuevaFecha,
            Integer usuarioOperador) {
        return reprogramarCitaService.ejecutar(citaId, nuevaFecha, usuarioOperador);
    }

    public SpResultado cancelarCita(Integer citaId, Integer usuarioOperador) {
        return cancelarCitaService.ejecutar(citaId, usuarioOperador);
    }

    // ── Facturación ──────────────────────────────────────────────────────────

    public SpResultadoCopago calcularCopago(Integer pacienteId, Short tipoFactId) {
        return calcularCopagoService.ejecutar(pacienteId, tipoFactId);
    }

    public SpResultado emitirFactura(
            Integer encuentroId,
            Integer pacienteId,
            Integer medicoId,
            Integer seguroId,
            Short tipoFactId,
            Integer usuarioOperador) {
        return emitirFacturaService.ejecutar(
                encuentroId, pacienteId, medicoId, seguroId, tipoFactId, usuarioOperador);
    }

    public SpResultado registrarPago(
            Integer facturaId,
            BigDecimal valorPagado,
            String tipoPago,
            String referencia,
            Integer usuarioOperador) {
        return registrarPagoService.ejecutar(
                facturaId, valorPagado, tipoPago, referencia, usuarioOperador);
    }
}
