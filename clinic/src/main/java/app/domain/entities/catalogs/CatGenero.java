package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_genero")
@Getter @Setter @NoArgsConstructor
public class CatGenero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genero_id", columnDefinition = "TINYINT UNSIGNED")
    private Short generoId;

    @Column(name = "codigo", nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
