package app.domain.entities.transactions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "factura_detalle")
@Getter @Setter @NoArgsConstructor
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "det_factura_id")
    private Integer detFacturaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Column(name = "tipo_item", nullable = false, length = 30)
    private String tipoItem;

    @Column(name = "nombre_item", nullable = false, length = 150)
    private String nombreItem;

    @Column(name = "dosis", length = 150)
    private String dosis;

    @Column(name = "cantidad", nullable = false)
    private Short cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "costo_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal costoTotal;
}
