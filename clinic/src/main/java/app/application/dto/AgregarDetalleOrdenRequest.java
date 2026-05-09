package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AgregarDetalleOrdenRequest {
    @NotNull  private Integer ordenId;
    @NotBlank private String tipoDetalle;
    @NotNull  private Short item;
    @NotNull  private Integer referenciaId;
    private String dosis;
    private String duracion;
    private Short cantidad;
    private String frecuencia;
    private Boolean requiereEsp;
    private Integer especialidadId;
    private BigDecimal costo;
    @NotNull  private Integer usuarioOperador;
}
