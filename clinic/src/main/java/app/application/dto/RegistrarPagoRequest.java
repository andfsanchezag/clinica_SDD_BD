package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegistrarPagoRequest {
    @NotNull  private Integer facturaId;
    @NotNull  private BigDecimal valorPagado;
    @NotBlank private String tipoPago;
    private String referencia;
    @NotNull  private Integer usuarioOperador;
}
