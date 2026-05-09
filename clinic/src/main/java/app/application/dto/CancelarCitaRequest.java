package app.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelarCitaRequest {
    @NotNull private Integer citaId;
    @NotNull private Integer usuarioOperador;
}
