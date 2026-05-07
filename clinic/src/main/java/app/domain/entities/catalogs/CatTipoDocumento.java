package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_tipo_documento")
@Getter @Setter @NoArgsConstructor
public class CatTipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_doc_id")
    private Short tipoDocId;

    @Column(name = "codigo", nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
