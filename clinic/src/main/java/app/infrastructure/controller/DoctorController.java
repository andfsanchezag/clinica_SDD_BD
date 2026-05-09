package app.infrastructure.controller;

import app.application.dto.*;
import app.application.usecase.DoctorUseCase;
import app.domain.dto.SpResultado;
import app.domain.exception.ReglaDeNegocioException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorUseCase useCase;

    public DoctorController(DoctorUseCase useCase) {
        this.useCase = useCase;
    }

    // ── Encuentro clínico ────────────────────────────────────────────────────

    @PostMapping("/encuentros")
    public ResponseEntity<SpResultadoResponse> abrirEncuentro(
            @Valid @RequestBody AbrirEncuentroRequest req) {
        SpResultado resultado = useCase.abrirEncuentroClinico(
                req.getCitaId(), req.getPacienteId(), req.getMedicoId(),
                req.getMotivoConsulta(), req.getSintomatologia(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PutMapping("/encuentros/cerrar")
    public ResponseEntity<SpResultadoResponse> cerrarEncuentro(
            @Valid @RequestBody CerrarEncuentroRequest req) {
        SpResultado resultado = useCase.cerrarEncuentroClinico(
                req.getEncuentroId(), req.getDiagnostico(), req.getTratamiento(),
                req.getObservaciones(), req.getUsuarioOperador());
        return responder(resultado);
    }

    // ── Órdenes médicas ──────────────────────────────────────────────────────

    @PostMapping("/ordenes")
    public ResponseEntity<SpResultadoResponse> registrarOrden(
            @Valid @RequestBody RegistrarOrdenMedicaRequest req) {
        SpResultado resultado = useCase.registrarOrdenMedica(
                req.getNumeroOrden(), req.getEncuentroId(), req.getPacienteId(),
                req.getMedicoId(), req.getTipoOrdenId(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PostMapping("/ordenes/detalle")
    public ResponseEntity<SpResultadoResponse> agregarDetalle(
            @Valid @RequestBody AgregarDetalleOrdenRequest req) {
        SpResultado resultado = useCase.agregarDetalleOrden(
                req.getOrdenId(), req.getTipoDetalle(), req.getItem(),
                req.getReferenciaId(), req.getDosis(), req.getDuracion(),
                req.getCantidad(), req.getFrecuencia(), req.getRequiereEsp(),
                req.getEspecialidadId(), req.getCosto(), req.getUsuarioOperador());
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
