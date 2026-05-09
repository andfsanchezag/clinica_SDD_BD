package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarOrdenMedicaRequest {
    @NotBlank private String numeroOrden;
    @NotNull  private Integer encuentroId;
    @NotNull  private Integer pacienteId;
    @NotNull  private Integer medicoId;
    @NotNull  private Short tipoOrdenId;
    @NotNull  private Integer usuarioOperador;
}
