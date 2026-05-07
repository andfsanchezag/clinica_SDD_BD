package app.domain.repositories;

import app.domain.entities.masters.ContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergencia, Integer> {
    List<ContactoEmergencia> findByPaciente_PacienteId(Integer pacienteId);
}
