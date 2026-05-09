package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_tipo_facturacion")
@Getter @Setter @NoArgsConstructor
public class CatTipoFacturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_fact_id", columnDefinition = "TINYINT UNSIGNED")
    private Short tipoFactId;

    @Column(name = "codigo", nullable = false, length = 25, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
