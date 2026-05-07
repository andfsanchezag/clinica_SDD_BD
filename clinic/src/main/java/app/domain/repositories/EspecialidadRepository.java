package app.domain.repositories;

import app.domain.entities.masters.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    List<Especialidad> findByNombreContainingIgnoreCase(String nombre);
}
