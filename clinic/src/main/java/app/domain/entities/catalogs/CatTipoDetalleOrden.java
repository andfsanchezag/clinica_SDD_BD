package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_tipo_detalle_orden")
@Getter @Setter @NoArgsConstructor
public class CatTipoDetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_detalle_id")
    private Short tipoDetalleId;

    @Column(name = "codigo", nullable = false, length = 30, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
