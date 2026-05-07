package app.domain.repositories;

import app.domain.entities.transactions.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByPaciente_PacienteId(Integer pacienteId);
    List<Cita> findByMedico_MedicoId(Integer medicoId);
    List<Cita> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
