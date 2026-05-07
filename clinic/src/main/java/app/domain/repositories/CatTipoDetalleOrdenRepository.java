package app.domain.repositories;

import app.domain.entities.catalogs.CatTipoDetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatTipoDetalleOrdenRepository extends JpaRepository<CatTipoDetalleOrden, Short> {
    Optional<CatTipoDetalleOrden> findByCodigo(String codigo);
}
