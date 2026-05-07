package app.domain.repositories;

import app.domain.entities.transactions.FacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacturaDetalleRepository extends JpaRepository<FacturaDetalle, Integer> {
    List<FacturaDetalle> findByFactura_FacturaId(Integer facturaId);
}
