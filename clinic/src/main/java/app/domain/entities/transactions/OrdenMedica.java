package app.domain.entities.transactions;

import app.domain.entities.catalogs.CatTipoOrden;
import app.domain.entities.masters.MedicoPerfil;
import app.domain.entities.masters.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_medica")
@Getter @Setter @NoArgsConstructor
public class OrdenMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orden_id")
    private Integer ordenId;

    @Column(name = "numero_orden", nullable = false, length = 6, unique = true)
    private String numeroOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuentro_id", nullable = false)
    private EncuentroClinico encuentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoPerfil medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_orden_id", nullable = false)
    private CatTipoOrden tipoOrden;

    @Column(name = "fecha_orden", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
