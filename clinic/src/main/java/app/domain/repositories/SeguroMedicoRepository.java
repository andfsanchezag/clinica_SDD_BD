package app.domain.repositories;

import app.domain.entities.masters.SeguroMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SeguroMedicoRepository extends JpaRepository<SeguroMedico, Integer> {
    Optional<SeguroMedico> findByNumeroPoliza(String numeroPoliza);
    List<SeguroMedico> findByPaciente_PacienteId(Integer pacienteId);
}
