package app.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SpResultadoCopago {

    private BigDecimal valorCopago;
    private Boolean    exento;
    private String     mensaje;
}
