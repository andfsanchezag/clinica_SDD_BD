package app.domain.entities.masters;

import app.domain.entities.catalogs.CatEstadoSeguro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguro_medico")
@Getter @Setter @NoArgsConstructor
public class SeguroMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seguro_id")
    private Integer seguroId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "compania", nullable = false, length = 120)
    private String compania;

    @Column(name = "numero_poliza", nullable = false, length = 60, unique = true)
    private String numeroPoliza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_seguro_id", nullable = false)
    private CatEstadoSeguro estadoSeguro;

    @Column(name = "fecha_vigencia", nullable = false)
    private LocalDate fechaVigencia;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
