package app.infrastructure.controller;

import app.application.dto.*;
import app.application.usecase.EnfermeraUseCase;
import app.domain.dto.SpResultado;
import app.domain.exception.ReglaDeNegocioException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enfermera")
public class EnfermeraController {

    private final EnfermeraUseCase useCase;

    public EnfermeraController(EnfermeraUseCase useCase) {
        this.useCase = useCase;
    }

    // ── Signos vitales ───────────────────────────────────────────────────────

    @PostMapping("/signos-vitales")
    public ResponseEntity<SpResultadoResponse> registrarSignosVitales(
            @Valid @RequestBody RegistrarSignosVitalesRequest req) {
        SpResultado resultado = useCase.registrarSignosVitales(
                req.getEncuentroId(), req.getEnfermeroId(), req.getPresion(),
                req.getTemperatura(), req.getPulso(), req.getOxigeno(), req.getUsuarioOperador());
        return responder(resultado);
    }

    // ── Medicamentos ─────────────────────────────────────────────────────────

    @PostMapping("/administracion-medicamentos")
    public ResponseEntity<SpResultadoResponse> registrarAdministracion(
            @Valid @RequestBody RegistrarAdministracionMedicamentoRequest req) {
        SpResultado resultado = useCase.registrarAdministracionMedicamento(
                req.getEncuentroId(), req.getEnfermeroId(), req.getMedicamentoId(),
                req.getDosis(), req.getObservacion(), req.getUsuarioOperador());
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
