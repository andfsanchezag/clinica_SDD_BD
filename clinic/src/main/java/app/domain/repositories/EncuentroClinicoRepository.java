package app.domain.repositories;

import app.domain.entities.transactions.EncuentroClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EncuentroClinicoRepository extends JpaRepository<EncuentroClinico, Integer> {
    List<EncuentroClinico> findByPaciente_PacienteId(Integer pacienteId);
    List<EncuentroClinico> findByMedico_MedicoId(Integer medicoId);
}
