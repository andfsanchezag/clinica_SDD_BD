package app.domain.entities.transactions;

import app.domain.entities.masters.MedicoPerfil;
import app.domain.entities.masters.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "encuentro_clinico")
@Getter @Setter @NoArgsConstructor
public class EncuentroClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "encuentro_id")
    private Integer encuentroId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoPerfil medico;

    @Column(name = "fecha_encuentro", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaEncuentro;

    @Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "sintomatologia", nullable = false, columnDefinition = "TEXT")
    private String sintomatologia;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
