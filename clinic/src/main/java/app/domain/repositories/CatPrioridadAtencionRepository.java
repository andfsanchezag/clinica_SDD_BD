package app.domain.repositories;

import app.domain.entities.catalogs.CatPrioridadAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatPrioridadAtencionRepository extends JpaRepository<CatPrioridadAtencion, Short> {
    Optional<CatPrioridadAtencion> findByCodigo(String codigo);
}
