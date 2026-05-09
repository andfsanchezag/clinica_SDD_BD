package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistrarEmpleadoRequest {
    @NotBlank private String cedula;
    @NotNull  private Short tipoDocId;
    @NotBlank private String nombreCompleto;
    private String correo;
    private String telefono;
    @NotNull  private LocalDate fechaNacimiento;
    private String direccion;
    @NotNull  private Short rolId;
    @NotNull  private Integer usuarioOperador;
}
