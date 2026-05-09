package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "config_facturacion")
@Getter @Setter @NoArgsConstructor
public class ConfigFacturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id", columnDefinition = "TINYINT UNSIGNED")
    private Short configId;

    @Column(name = "parametro", nullable = false, length = 60, unique = true)
    private String parametro;

    @Column(name = "valor_numerico", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorNumerico;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;
}
