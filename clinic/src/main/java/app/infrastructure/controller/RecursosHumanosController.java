package app.infrastructure.controller;

import app.application.dto.*;
import app.application.usecase.RecursosHumanosUseCase;
import app.domain.dto.SpResultado;
import app.domain.exception.ReglaDeNegocioException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rrhh")
public class RecursosHumanosController {

    private final RecursosHumanosUseCase useCase;

    public RecursosHumanosController(RecursosHumanosUseCase useCase) {
        this.useCase = useCase;
    }

    // ── Personal ─────────────────────────────────────────────────────────────

    @PostMapping("/empleados")
    public ResponseEntity<SpResultadoResponse> registrarEmpleado(
            @Valid @RequestBody RegistrarEmpleadoRequest req) {
        SpResultado resultado = useCase.registrarEmpleado(
                req.getCedula(), req.getTipoDocId(), req.getNombreCompleto(),
                req.getCorreo(), req.getTelefono(), req.getFechaNacimiento(),
                req.getDireccion(), req.getRolId(), req.getUsuarioOperador());
        return responder(resultado);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<SpResultadoResponse> crearUsuario(
            @Valid @RequestBody CrearUsuarioOperativoRequest req) {
        SpResultado resultado = useCase.crearUsuarioOperativo(
                req.getEmpleadoId(), req.getCodigoUsuario(),
                req.getContrasenaHash(), req.getUsuarioOperador());
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
