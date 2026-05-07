package app.application.usecase;

import app.domain.dto.SpResultado;
import app.domain.services.clinico.AbrirEncuentroClinicoService;
import app.domain.services.clinico.CerrarEncuentroClinicoService;
import app.domain.services.ordenes.AgregarDetalleOrdenService;
import app.domain.services.ordenes.RegistrarOrdenMedicaService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Caso de uso para el rol DOCTOR.
 * Agrupa todas las operaciones del tratamiento médico:
 * apertura/cierre de encuentros clínicos y gestión de órdenes médicas.
 */
@Service
public class DoctorUseCase {

    private final AbrirEncuentroClinicoService abrirEncuentroClinicoService;
    private final CerrarEncuentroClinicoService cerrarEncuentroClinicoService;
    private final RegistrarOrdenMedicaService registrarOrdenMedicaService;
    private final AgregarDetalleOrdenService agregarDetalleOrdenService;

    public DoctorUseCase(
            AbrirEncuentroClinicoService abrirEncuentroClinicoService,
            CerrarEncuentroClinicoService cerrarEncuentroClinicoService,
            RegistrarOrdenMedicaService registrarOrdenMedicaService,
            AgregarDetalleOrdenService agregarDetalleOrdenService) {
        this.abrirEncuentroClinicoService = abrirEncuentroClinicoService;
        this.cerrarEncuentroClinicoService = cerrarEncuentroClinicoService;
        this.registrarOrdenMedicaService = registrarOrdenMedicaService;
        this.agregarDetalleOrdenService = agregarDetalleOrdenService;
    }

    // ── Clínico ─────────────────────────────────────────────────────────────

    public SpResultado abrirEncuentroClinico(
            Integer citaId,
            Integer pacienteId,
            Integer medicoId,
            String motivoConsulta,
            String sintomatologia,
            Integer usuarioOperador) {
        return abrirEncuentroClinicoService.ejecutar(
                citaId, pacienteId, medicoId, motivoConsulta, sintomatologia, usuarioOperador);
    }

    public SpResultado cerrarEncuentroClinico(
            Integer encuentroId,
            String diagnostico,
            String tratamiento,
            String observaciones,
            Integer usuarioOperador) {
        return cerrarEncuentroClinicoService.ejecutar(
                encuentroId, diagnostico, tratamiento, observaciones, usuarioOperador);
    }

    // ── Órdenes médicas ──────────────────────────────────────────────────────

    public SpResultado registrarOrdenMedica(
            String numeroOrden,
            Integer encuentroId,
            Integer pacienteId,
            Integer medicoId,
            Short tipoOrdenId,
            Integer usuarioOperador) {
        return registrarOrdenMedicaService.ejecutar(
                numeroOrden, encuentroId, pacienteId, medicoId, tipoOrdenId, usuarioOperador);
    }

    public SpResultado agregarDetalleOrden(
            Integer ordenId,
            String tipoDetalle,
            Short item,
            Integer referenciaId,
            String dosis,
            String duracion,
            Short cantidad,
            String frecuencia,
            Boolean requiereEsp,
            Integer especialidadId,
            BigDecimal costo,
            Integer usuarioOperador) {
        return agregarDetalleOrdenService.ejecutar(
                ordenId, tipoDetalle, item, referenciaId, dosis, duracion,
                cantidad, frecuencia, requiereEsp, especialidadId, costo, usuarioOperador);
    }
}
