package app.domain.entities.transactions;

import app.domain.entities.masters.EnfermeroPerfil;
import app.domain.entities.masters.ProcedimientoCatalogo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "procedimiento_enfermeria")
@Getter @Setter @NoArgsConstructor
public class ProcedimientoEnfermeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proc_enf_id")
    private Integer procEnfId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuentro_id", nullable = false)
    private EncuentroClinico encuentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermero_id", nullable = false)
    private EnfermeroPerfil enfermero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimiento_id", nullable = false)
    private ProcedimientoCatalogo procedimiento;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "fecha_proc", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaProc;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
