package app.domain.repositories;

import app.domain.entities.transactions.ProcedimientoEnfermeria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcedimientoEnfermeriaRepository extends JpaRepository<ProcedimientoEnfermeria, Integer> {
    List<ProcedimientoEnfermeria> findByEncuentro_EncuentroId(Integer encuentroId);
}
