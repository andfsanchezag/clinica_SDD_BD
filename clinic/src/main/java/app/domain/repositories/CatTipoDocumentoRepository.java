package app.domain.repositories;

import app.domain.entities.catalogs.CatTipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatTipoDocumentoRepository extends JpaRepository<CatTipoDocumento, Short> {
    Optional<CatTipoDocumento> findByCodigo(String codigo);
}
