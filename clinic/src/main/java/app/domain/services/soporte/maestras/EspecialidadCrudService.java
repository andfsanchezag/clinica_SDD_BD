package app.domain.services.soporte.maestras;

import app.domain.entities.masters.Especialidad;
import app.domain.repositories.EspecialidadRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadCrudService {

    private final EspecialidadRepository repository;

    public EspecialidadCrudService(EspecialidadRepository repository) {
        this.repository = repository;
    }

    public Especialidad crear(Especialidad entity) {
        return repository.save(entity);
    }

    public Optional<Especialidad> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Especialidad> buscarTodos() {
        return repository.findAll();
    }

    public Especialidad actualizar(Integer id, Especialidad entity) {
        entity.setEspecialidadId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
