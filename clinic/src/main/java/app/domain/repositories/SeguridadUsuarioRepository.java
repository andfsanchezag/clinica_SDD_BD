package app.domain.repositories;

import app.domain.entities.masters.SeguridadUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeguridadUsuarioRepository extends JpaRepository<SeguridadUsuario, Integer> {
    Optional<SeguridadUsuario> findByCodigoUsuario(String codigoUsuario);
    boolean existsByCodigoUsuario(String codigoUsuario);
}
