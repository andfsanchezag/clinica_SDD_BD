package app.domain.services.soporte.maestras;

import app.domain.entities.masters.Paciente;
import app.domain.repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteCrudService {

    private final PacienteRepository repository;

    public PacienteCrudService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente crear(Paciente entity) {
        return repository.save(entity);
    }

    public Optional<Paciente> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Paciente> buscarTodos() {
        return repository.findAll();
    }

    public Paciente actualizar(Integer id, Paciente entity) {
        entity.setPacienteId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
