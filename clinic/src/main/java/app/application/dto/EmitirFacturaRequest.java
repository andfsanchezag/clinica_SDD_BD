package app.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmitirFacturaRequest {
    @NotNull private Integer encuentroId;
    @NotNull private Integer pacienteId;
    @NotNull private Integer medicoId;
    private Integer seguroId;
    @NotNull private Short tipoFactId;
    @NotNull private Integer usuarioOperador;
}
