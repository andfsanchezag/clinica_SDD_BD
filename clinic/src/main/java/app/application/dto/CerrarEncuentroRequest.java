package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CerrarEncuentroRequest {
    @NotNull  private Integer encuentroId;
    @NotBlank private String diagnostico;
    @NotBlank private String tratamiento;
    private String observaciones;
    @NotNull  private Integer usuarioOperador;
}
