package app.application.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Respuesta para el cálculo de copago.
 */
@Data
public class CopagoResponse {
    private BigDecimal valorCopago;
    private Boolean exento;
    private String mensaje;
}
