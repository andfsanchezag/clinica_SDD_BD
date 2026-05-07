package app.domain.repositories;

import app.domain.entities.masters.EnfermeroPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnfermeroPerfilRepository extends JpaRepository<EnfermeroPerfil, Integer> {
    Optional<EnfermeroPerfil> findByEmpleado_EmpleadoId(Integer empleadoId);
}
