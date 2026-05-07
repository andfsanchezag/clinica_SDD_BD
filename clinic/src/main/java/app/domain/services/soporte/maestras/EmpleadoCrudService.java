package app.domain.services.soporte.maestras;

import app.domain.entities.masters.Empleado;
import app.domain.repositories.EmpleadoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoCrudService {

    private final EmpleadoRepository repository;

    public EmpleadoCrudService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado crear(Empleado entity) {
        return repository.save(entity);
    }

    public Optional<Empleado> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Empleado> buscarTodos() {
        return repository.findAll();
    }

    public Empleado actualizar(Integer id, Empleado entity) {
        entity.setEmpleadoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
