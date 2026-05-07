package app.domain.repositories;

import app.domain.entities.masters.ProcedimientoCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcedimientoCatalogoRepository extends JpaRepository<ProcedimientoCatalogo, Integer> {
    Optional<ProcedimientoCatalogo> findByCodigo(String codigo);
}
