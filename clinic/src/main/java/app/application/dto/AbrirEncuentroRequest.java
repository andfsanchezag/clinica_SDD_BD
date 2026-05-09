package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbrirEncuentroRequest {
    @NotNull  private Integer citaId;
    @NotNull  private Integer pacienteId;
    @NotNull  private Integer medicoId;
    @NotBlank private String motivoConsulta;
    private String sintomatologia;
    @NotNull  private Integer usuarioOperador;
}
