package app.application.usecase;

import app.domain.dto.SpResultado;
import app.domain.services.personal.CrearUsuarioOperativoService;
import app.domain.services.personal.RegistrarEmpleadoService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

/**
 * Caso de uso para el rol RECURSOS HUMANOS.
 * Agrupa el control del manejo de personal:
 * registro de empleados y creación de usuarios operativos.
 */
@Service
public class RecursosHumanosUseCase {

    private final RegistrarEmpleadoService registrarEmpleadoService;
    private final CrearUsuarioOperativoService crearUsuarioOperativoService;

    public RecursosHumanosUseCase(
            RegistrarEmpleadoService registrarEmpleadoService,
            CrearUsuarioOperativoService crearUsuarioOperativoService) {
        this.registrarEmpleadoService = registrarEmpleadoService;
        this.crearUsuarioOperativoService = crearUsuarioOperativoService;
    }

    // ── Personal ─────────────────────────────────────────────────────────────

    public SpResultado registrarEmpleado(
            String cedula,
            Short tipoDocId,
            String nombreCompleto,
            String correo,
            String telefono,
            LocalDate fechaNacimiento,
            String direccion,
            Short rolId,
            Integer usuarioOperador) {
        return registrarEmpleadoService.ejecutar(
                cedula, tipoDocId, nombreCompleto, correo, telefono,
                fechaNacimiento, direccion, rolId, usuarioOperador);
    }

    public SpResultado crearUsuarioOperativo(
            Integer empleadoId,
            String codigoUsuario,
            String contrasenaHash,
            Integer usuarioOperador) {
        return crearUsuarioOperativoService.ejecutar(
                empleadoId, codigoUsuario, contrasenaHash, usuarioOperador);
    }
}
