package app.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegistrarSignosVitalesRequest {
    @NotNull private Integer encuentroId;
    @NotNull private Integer enfermeroId;
    private String presion;
    private BigDecimal temperatura;
    private Short pulso;
    private BigDecimal oxigeno;
    @NotNull private Integer usuarioOperador;
}
