package app.domain.repositories;

import app.domain.entities.catalogs.CatTipoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatTipoOrdenRepository extends JpaRepository<CatTipoOrden, Short> {
    Optional<CatTipoOrden> findByCodigo(String codigo);
}
