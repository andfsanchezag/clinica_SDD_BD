package app.domain.repositories;

import app.domain.entities.catalogs.CatTipoFacturacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatTipoFacturacionRepository extends JpaRepository<CatTipoFacturacion, Short> {
    Optional<CatTipoFacturacion> findByCodigo(String codigo);
}
