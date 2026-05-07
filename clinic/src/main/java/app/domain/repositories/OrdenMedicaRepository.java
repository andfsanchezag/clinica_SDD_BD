package app.domain.repositories;

import app.domain.entities.transactions.OrdenMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrdenMedicaRepository extends JpaRepository<OrdenMedica, Integer> {
    Optional<OrdenMedica> findByNumeroOrden(String numeroOrden);
    List<OrdenMedica> findByPaciente_PacienteId(Integer pacienteId);
    List<OrdenMedica> findByEncuentro_EncuentroId(Integer encuentroId);
}
