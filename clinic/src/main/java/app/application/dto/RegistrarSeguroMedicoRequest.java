package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistrarSeguroMedicoRequest {
    @NotNull  private Integer pacienteId;
    @NotBlank private String compania;
    @NotBlank private String numeroPoliza;
    @NotNull  private Short estadoSeguroId;
    @NotNull  private LocalDate fechaVigencia;
    @NotNull  private Integer usuarioOperador;
}
