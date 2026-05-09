package app.infrastructure.controller;

import app.application.dto.*;
import app.application.usecase.AdministrativoUseCase;
import app.domain.dto.SpResultado;
import app.domain.dto.SpResultadoCopago;
import app.domain.exception.ReglaDeNegocioException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrativo")
public class AdministrativoController {

    private final AdministrativoUseCase useCase;

    public AdministrativoController(AdministrativoUseCase useCase) {
        this.useCase = useCase;
    }

    // ── Pacientes ────────────────────────────────────────────────────────────

    @PostMapping("/pacientes")
    public ResponseEntity<SpResultadoResponse> registrarPaciente(
            @Valid @RequestBody RegistrarPacienteRequest req) {
        SpResultado resultado = useCase.registrarPaciente(
                req.getCedula(), req.getTipoDocId(), req.getNombreCompleto(),
                req.getFechaNacimiento(), req.getGeneroId(), req.getDireccion(),
                req.getTelefono(), req.getCorreo(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PostMapping("/pacientes/contactos-emergencia")
    public ResponseEntity<SpResultadoResponse> registrarContactoEmergencia(
            @Valid @RequestBody RegistrarContactoEmergenciaRequest req) {
        SpResultado resultado = useCase.registrarContactoEmergencia(
                req.getPacienteId(), req.getNombreCompleto(),
                req.getRelacion(), req.getTelefono(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PostMapping("/pacientes/seguros")
    public ResponseEntity<SpResultadoResponse> registrarSeguroMedico(
            @Valid @RequestBody RegistrarSeguroMedicoRequest req) {
        SpResultado resultado = useCase.registrarSeguroMedico(
                req.getPacienteId(), req.getCompania(), req.getNumeroPoliza(),
                req.getEstadoSeguroId(), req.getFechaVigencia(), req.getUsuarioOperador());
        return responder(resultado);
    }

    // ── Agenda ───────────────────────────────────────────────────────────────

    @PostMapping("/citas")
    public ResponseEntity<SpResultadoResponse> programarCita(
            @Valid @RequestBody ProgramarCitaRequest req) {
        SpResultado resultado = useCase.programarCita(
                req.getPacienteId(), req.getMedicoId(), req.getFechaHora(),
                req.getPrioridadId(), req.getMotivo(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PutMapping("/citas/reprogramar")
    public ResponseEntity<SpResultadoResponse> reprogramarCita(
            @Valid @RequestBody ReprogramarCitaRequest req) {
        SpResultado resultado = useCase.reprogramarCita(
                req.getCitaId(), req.getNuevaFecha(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PutMapping("/citas/cancelar")
    public ResponseEntity<SpResultadoResponse> cancelarCita(
            @Valid @RequestBody CancelarCitaRequest req) {
        SpResultado resultado = useCase.cancelarCita(req.getCitaId(), req.getUsuarioOperador());
        return responder(resultado);
    }

    // ── Facturación ──────────────────────────────────────────────────────────

    @GetMapping("/facturacion/copago")
    public ResponseEntity<CopagoResponse> calcularCopago(
            @RequestParam Integer pacienteId,
            @RequestParam Short tipoFactId) {
        SpResultadoCopago copago = useCase.calcularCopago(pacienteId, tipoFactId);
        CopagoResponse response = new CopagoResponse();
        response.setValorCopago(copago.getValorCopago());
        response.setExento(copago.getExento());
        response.setMensaje(copago.getMensaje());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/facturacion/facturas")
    public ResponseEntity<SpResultadoResponse> emitirFactura(
            @Valid @RequestBody EmitirFacturaRequest req) {
        SpResultado resultado = useCase.emitirFactura(
                req.getEncuentroId(), req.getPacienteId(), req.getMedicoId(),
                req.getSeguroId(), req.getTipoFactId(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PostMapping("/facturacion/pagos")
    public ResponseEntity<SpResultadoResponse> registrarPago(
            @Valid @RequestBody RegistrarPagoRequest req) {
        SpResultado resultado = useCase.registrarPago(
                req.getFacturaId(), req.getValorPagado(), req.getTipoPago(),
                req.getReferencia(), req.getUsuarioOperador());
        return responder(resultado);
    }

    // ── Mapper ───────────────────────────────────────────────────────────────

    private ResponseEntity<SpResultadoResponse> responder(SpResultado resultado) {
        if (!resultado.isExitoso()) {
            throw new ReglaDeNegocioException(resultado.getMensaje());
        }
        SpResultadoResponse body = new SpResultadoResponse();
        body.setCodigo(resultado.getCodigo());
        body.setMensaje(resultado.getMensaje());
        body.setIdGenerado(resultado.getIdGenerado());
        body.setExitoso(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
