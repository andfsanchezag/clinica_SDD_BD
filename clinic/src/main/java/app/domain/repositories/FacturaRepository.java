package app.domain.repositories;

import app.domain.entities.transactions.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByPaciente_PacienteId(Integer pacienteId);
    List<Factura> findByEncuentro_EncuentroId(Integer encuentroId);
}
