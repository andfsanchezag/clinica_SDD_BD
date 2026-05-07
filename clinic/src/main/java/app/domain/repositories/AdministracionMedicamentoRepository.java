package app.domain.repositories;

import app.domain.entities.transactions.AdministracionMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdministracionMedicamentoRepository extends JpaRepository<AdministracionMedicamento, Integer> {
    List<AdministracionMedicamento> findByEncuentro_EncuentroId(Integer encuentroId);
}
