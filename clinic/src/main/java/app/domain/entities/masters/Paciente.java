package app.domain.entities.masters;

import app.domain.entities.catalogs.CatGenero;
import app.domain.entities.catalogs.CatTipoDocumento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente")
@Getter @Setter @NoArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paciente_id")
    private Integer pacienteId;

    @Column(name = "cedula", nullable = false, length = 20, unique = true)
    private String cedula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_doc_id", nullable = false)
    private CatTipoDocumento tipoDocumento;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genero_id", nullable = false)
    private CatGenero genero;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "correo", length = 120)
    private String correo;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
