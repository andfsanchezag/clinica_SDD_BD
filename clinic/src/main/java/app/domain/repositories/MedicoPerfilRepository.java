package app.domain.repositories;

import app.domain.entities.masters.MedicoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicoPerfilRepository extends JpaRepository<MedicoPerfil, Integer> {
    Optional<MedicoPerfil> findByRegistroMedico(String registroMedico);
    Optional<MedicoPerfil> findByEmpleado_EmpleadoId(Integer empleadoId);
}
