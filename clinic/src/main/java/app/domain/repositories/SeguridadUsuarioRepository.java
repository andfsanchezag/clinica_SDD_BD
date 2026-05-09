package app.domain.repositories;

import app.domain.entities.masters.SeguridadUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SeguridadUsuarioRepository extends JpaRepository<SeguridadUsuario, Integer> {

    Optional<SeguridadUsuario> findByCodigoUsuario(String codigoUsuario);

    boolean existsByCodigoUsuario(String codigoUsuario);

    /** Carga el empleado y su rol en un solo JOIN para evitar N+1 durante autenticación. */
    @Query("SELECT u FROM SeguridadUsuario u " +
           "JOIN FETCH u.empleado e " +
           "JOIN FETCH e.rol " +
           "WHERE u.codigoUsuario = :codigo AND u.activo = true")
    Optional<SeguridadUsuario> findActivoConRol(@Param("codigo") String codigo);
}
