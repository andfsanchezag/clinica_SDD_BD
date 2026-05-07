package app.domain.repositories;

import app.domain.entities.transactions.OrdenMedicamentoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenMedicamentoDetalleRepository extends JpaRepository<OrdenMedicamentoDetalle, Integer> {
    List<OrdenMedicamentoDetalle> findByOrden_OrdenId(Integer ordenId);
}
