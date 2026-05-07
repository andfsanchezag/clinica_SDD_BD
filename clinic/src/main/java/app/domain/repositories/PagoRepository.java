package app.domain.repositories;

import app.domain.entities.transactions.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByFactura_FacturaId(Integer facturaId);
}
