package app.domain.repositories;

import app.domain.entities.catalogs.CatEstadoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatEstadoSeguroRepository extends JpaRepository<CatEstadoSeguro, Short> {
    Optional<CatEstadoSeguro> findByCodigo(String codigo);
}
