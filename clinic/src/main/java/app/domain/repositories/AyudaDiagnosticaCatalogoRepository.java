package app.domain.repositories;

import app.domain.entities.masters.AyudaDiagnosticaCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AyudaDiagnosticaCatalogoRepository extends JpaRepository<AyudaDiagnosticaCatalogo, Integer> {
    Optional<AyudaDiagnosticaCatalogo> findByCodigo(String codigo);
}
