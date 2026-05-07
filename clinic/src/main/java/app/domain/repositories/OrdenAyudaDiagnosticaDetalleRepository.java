package app.domain.repositories;

import app.domain.entities.transactions.OrdenAyudaDiagnosticaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenAyudaDiagnosticaDetalleRepository extends JpaRepository<OrdenAyudaDiagnosticaDetalle, Integer> {
    List<OrdenAyudaDiagnosticaDetalle> findByOrden_OrdenId(Integer ordenId);
}
