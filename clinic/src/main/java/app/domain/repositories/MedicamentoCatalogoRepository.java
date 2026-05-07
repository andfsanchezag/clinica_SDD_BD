package app.domain.repositories;

import app.domain.entities.masters.MedicamentoCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicamentoCatalogoRepository extends JpaRepository<MedicamentoCatalogo, Integer> {
    Optional<MedicamentoCatalogo> findByCodigo(String codigo);
}
