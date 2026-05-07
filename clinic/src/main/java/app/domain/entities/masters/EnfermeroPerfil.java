package app.domain.entities.masters;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enfermero_perfil")
@Getter @Setter @NoArgsConstructor
public class EnfermeroPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enfermero_id")
    private Integer enfermeroId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false, unique = true)
    private Empleado empleado;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
