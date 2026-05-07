package app.domain.entities.transactions;

import app.domain.entities.masters.EnfermeroPerfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "signo_vital")
@Getter @Setter @NoArgsConstructor
public class SignoVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signo_id")
    private Integer signoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuentro_id", nullable = false)
    private EncuentroClinico encuentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermero_id", nullable = false)
    private EnfermeroPerfil enfermero;

    @Column(name = "presion_arterial", nullable = false, length = 20)
    private String presionArterial;

    @Column(name = "temperatura", nullable = false, precision = 4, scale = 1)
    private BigDecimal temperatura;

    @Column(name = "pulso", nullable = false)
    private Short pulso;

    @Column(name = "oxigeno", nullable = false, precision = 5, scale = 2)
    private BigDecimal oxigeno;

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
