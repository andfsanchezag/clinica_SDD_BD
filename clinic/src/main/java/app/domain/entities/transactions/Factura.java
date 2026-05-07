package app.domain.entities.transactions;

import app.domain.entities.catalogs.CatTipoFacturacion;
import app.domain.entities.masters.MedicoPerfil;
import app.domain.entities.masters.Paciente;
import app.domain.entities.masters.SeguroMedico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Getter @Setter @NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Integer facturaId;

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
    @JoinColumn(name = "seguro_id")
    private SeguroMedico seguro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_fact_id", nullable = false)
    private CatTipoFacturacion tipoFacturacion;

    @Column(name = "valor_total",       nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_copago",      nullable = false, precision = 14, scale = 2)
    private BigDecimal valorCopago;

    @Column(name = "valor_aseguradora", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorAseguradora;

    @Column(name = "valor_paciente",    nullable = false, precision = 14, scale = 2)
    private BigDecimal valorPaciente;

    @Column(name = "fecha_factura",     nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaFactura;

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
