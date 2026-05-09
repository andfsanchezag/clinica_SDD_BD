package app.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProgramarCitaRequest {
    @NotNull private Integer pacienteId;
    @NotNull private Integer medicoId;
    @NotNull private LocalDateTime fechaHora;
    @NotNull private Short prioridadId;
    private String motivo;
    @NotNull private Integer usuarioOperador;
}
