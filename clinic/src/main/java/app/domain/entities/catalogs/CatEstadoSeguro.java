package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_estado_seguro")
@Getter @Setter @NoArgsConstructor
public class CatEstadoSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_seguro_id")
    private Short estadoSeguroId;

    @Column(name = "codigo", nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
