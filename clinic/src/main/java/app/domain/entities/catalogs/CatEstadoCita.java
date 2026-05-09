package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_estado_cita")
@Getter @Setter @NoArgsConstructor
public class CatEstadoCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_cita_id", columnDefinition = "TINYINT UNSIGNED")
    private Short estadoCitaId;

    @Column(name = "codigo", nullable = false, length = 30, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
