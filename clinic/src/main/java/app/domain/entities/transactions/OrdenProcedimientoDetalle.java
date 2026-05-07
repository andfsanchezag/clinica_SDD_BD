package app.domain.entities.transactions;

import app.domain.entities.masters.Especialidad;
import app.domain.entities.masters.ProcedimientoCatalogo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_procedimiento_detalle")
@Getter @Setter @NoArgsConstructor
public class OrdenProcedimientoDetalle {

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
    @JoinColumn(name = "procedimiento_id", nullable = false)
    private ProcedimientoCatalogo procedimiento;

    @Column(name = "cantidad", nullable = false)
    private Short cantidad;

    @Column(name = "frecuencia", nullable = false, length = 100)
    private String frecuencia;

    @Column(name = "requiere_especialista", nullable = false)
    private Boolean requiereEspecialista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @Column(name = "costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
