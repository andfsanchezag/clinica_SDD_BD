package app.domain.entities.transactions;

import app.domain.entities.masters.EnfermeroPerfil;
import app.domain.entities.masters.MedicamentoCatalogo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "administracion_medicamento")
@Getter @Setter @NoArgsConstructor
public class AdministracionMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_med_id")
    private Integer adminMedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuentro_id", nullable = false)
    private EncuentroClinico encuentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermero_id", nullable = false)
    private EnfermeroPerfil enfermero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private MedicamentoCatalogo medicamento;

    @Column(name = "dosis", nullable = false, length = 100)
    private String dosis;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "fecha_administracion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaAdministracion;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
