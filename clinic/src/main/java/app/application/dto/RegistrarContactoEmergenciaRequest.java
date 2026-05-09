package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarContactoEmergenciaRequest {
    @NotNull  private Integer pacienteId;
    @NotBlank private String nombreCompleto;
    @NotBlank private String relacion;
    @NotBlank private String telefono;
    @NotNull  private Integer usuarioOperador;
}
