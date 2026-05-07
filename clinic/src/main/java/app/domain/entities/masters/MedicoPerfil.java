package app.domain.entities.masters;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medico_perfil")
@Getter @Setter @NoArgsConstructor
public class MedicoPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medico_id")
    private Integer medicoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false, unique = true)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @Column(name = "registro_medico", nullable = false, length = 30, unique = true)
    private String registroMedico;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}
