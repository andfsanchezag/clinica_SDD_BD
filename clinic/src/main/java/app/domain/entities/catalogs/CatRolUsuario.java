package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_rol_usuario")
@Getter @Setter @NoArgsConstructor
public class CatRolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Short rolId;

    @Column(name = "codigo", nullable = false, length = 30, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
