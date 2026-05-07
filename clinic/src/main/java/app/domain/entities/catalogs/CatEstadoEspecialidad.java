package app.domain.entities.catalogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_estado_especialidad")
@Getter @Setter @NoArgsConstructor
public class CatEstadoEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_esp_id")
    private Short estadoEspId;

    @Column(name = "codigo", nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
