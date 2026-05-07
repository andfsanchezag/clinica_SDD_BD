package app.domain.repositories;

import app.domain.entities.catalogs.ConfigFacturacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfigFacturacionRepository extends JpaRepository<ConfigFacturacion, Short> {
    Optional<ConfigFacturacion> findByParametro(String parametro);
}
