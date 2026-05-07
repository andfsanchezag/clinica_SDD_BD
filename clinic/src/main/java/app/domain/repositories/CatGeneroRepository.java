package app.domain.repositories;

import app.domain.entities.catalogs.CatGenero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatGeneroRepository extends JpaRepository<CatGenero, Short> {
    Optional<CatGenero> findByCodigo(String codigo);
}
