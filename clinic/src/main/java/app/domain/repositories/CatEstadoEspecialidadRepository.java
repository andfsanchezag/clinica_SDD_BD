package app.domain.repositories;

import app.domain.entities.catalogs.CatEstadoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatEstadoEspecialidadRepository extends JpaRepository<CatEstadoEspecialidad, Short> {
    Optional<CatEstadoEspecialidad> findByCodigo(String codigo);
}
