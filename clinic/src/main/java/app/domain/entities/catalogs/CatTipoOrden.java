package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_tipo_orden")
@Getter @Setter @NoArgsConstructor
public class CatTipoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_orden_id")
    private Short tipoOrdenId;

    @Column(name = "codigo", nullable = false, length = 40, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
