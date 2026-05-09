package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RegistrarPacienteRequest {
    @NotBlank private String cedula;
    @NotNull  private Short tipoDocId;
    @NotBlank private String nombreCompleto;
    @NotNull  private LocalDate fechaNacimiento;
    @NotNull  private Short generoId;
    private String direccion;
    private String telefono;
    private String correo;
    @NotNull  private Integer usuarioOperador;
}
