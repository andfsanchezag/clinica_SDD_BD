package app.application.usecase;

import app.domain.dto.SpResultado;
import app.domain.services.enfermeria.RegistrarAdministracionMedicamentoService;
import app.domain.services.enfermeria.RegistrarSignosVitalesService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Caso de uso para el rol ENFERMERA.
 * Agrupa todas las operaciones del apartado de enfermería:
 * registro de signos vitales y administración de medicamentos.
 */
@Service
public class EnfermeraUseCase {

    private final RegistrarSignosVitalesService registrarSignosVitalesService;
    private final RegistrarAdministracionMedicamentoService registrarAdministracionMedicamentoService;

    public EnfermeraUseCase(
            RegistrarSignosVitalesService registrarSignosVitalesService,
            RegistrarAdministracionMedicamentoService registrarAdministracionMedicamentoService) {
        this.registrarSignosVitalesService = registrarSignosVitalesService;
        this.registrarAdministracionMedicamentoService = registrarAdministracionMedicamentoService;
    }

    // ── Enfermería ───────────────────────────────────────────────────────────

    public SpResultado registrarSignosVitales(
            Integer encuentroId,
            Integer enfermeroId,
            String presion,
            BigDecimal temperatura,
            Short pulso,
            BigDecimal oxigeno,
            Integer usuarioOperador) {
        return registrarSignosVitalesService.ejecutar(
                encuentroId, enfermeroId, presion, temperatura, pulso, oxigeno, usuarioOperador);
    }

    public SpResultado registrarAdministracionMedicamento(
            Integer encuentroId,
            Integer enfermeroId,
            Integer medicamentoId,
            String dosis,
            String observacion,
            Integer usuarioOperador) {
        return registrarAdministracionMedicamentoService.ejecutar(
                encuentroId, enfermeroId, medicamentoId, dosis, observacion, usuarioOperador);
    }
}
