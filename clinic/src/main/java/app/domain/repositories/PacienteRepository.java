package app.domain.repositories;

import app.domain.entities.masters.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}
