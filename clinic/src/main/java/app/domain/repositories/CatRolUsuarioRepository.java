package app.domain.repositories;

import app.domain.entities.catalogs.CatRolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CatRolUsuarioRepository extends JpaRepository<CatRolUsuario, Short> {
    Optional<CatRolUsuario> findByCodigo(String codigo);
}
