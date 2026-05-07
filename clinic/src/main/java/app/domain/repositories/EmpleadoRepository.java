package app.domain.repositories;

import app.domain.entities.masters.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}
