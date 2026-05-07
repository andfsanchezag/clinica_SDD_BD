package app.domain.repositories;

import app.domain.entities.catalogs.CatEstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatEstadoCitaRepository extends JpaRepository<CatEstadoCita, Short> {
    Optional<CatEstadoCita> findByCodigo(String codigo);
}
