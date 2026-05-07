package app.domain.entities.transactions;

import app.domain.entities.catalogs.CatEstadoCita;
import app.domain.entities.catalogs.CatPrioridadAtencion;
import app.domain.entities.masters.MedicoPerfil;
import app.domain.entities.masters.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
@Getter @Setter @NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cita_id")
    private Integer citaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoPerfil medico;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_cita_id", nullable = false)
    private CatEstadoCita estadoCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prioridad_id", nullable = false)
    private CatPrioridadAtencion prioridad;

    @Column(name = "motivo", nullable = false, length = 300)
    private String motivo;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
