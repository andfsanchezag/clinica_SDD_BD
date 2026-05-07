package app.domain.entities.transactions;

import app.domain.entities.masters.MedicamentoCatalogo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_medicamento_detalle")
@Getter @Setter @NoArgsConstructor
public class OrdenMedicamentoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_id")
    private Integer detalleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenMedica orden;

    @Column(name = "item", nullable = false)
    private Short item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private MedicamentoCatalogo medicamento;

    @Column(name = "dosis", nullable = false, length = 150)
    private String dosis;

    @Column(name = "duracion", nullable = false, length = 100)
    private String duracion;

    @Column(name = "costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
