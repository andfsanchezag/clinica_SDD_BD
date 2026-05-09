package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarAdministracionMedicamentoRequest {
    @NotNull  private Integer encuentroId;
    @NotNull  private Integer enfermeroId;
    @NotNull  private Integer medicamentoId;
    @NotBlank private String dosis;
    private String observacion;
    @NotNull  private Integer usuarioOperador;
}
