package app.domain.repositories;

import app.domain.entities.transactions.OrdenProcedimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenProcedimientoDetalleRepository extends JpaRepository<OrdenProcedimientoDetalle, Integer> {
    List<OrdenProcedimientoDetalle> findByOrden_OrdenId(Integer ordenId);
}
