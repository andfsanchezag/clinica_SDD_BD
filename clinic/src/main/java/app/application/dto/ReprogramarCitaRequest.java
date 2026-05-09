package app.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReprogramarCitaRequest {
    @NotNull private Integer citaId;
    @NotNull private LocalDateTime nuevaFecha;
    @NotNull private Integer usuarioOperador;
}
