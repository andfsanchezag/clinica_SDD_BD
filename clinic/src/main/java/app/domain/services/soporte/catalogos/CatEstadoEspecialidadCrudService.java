package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatEstadoEspecialidad;
import app.domain.repositories.CatEstadoEspecialidadRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatEstadoEspecialidadCrudService {

    private final CatEstadoEspecialidadRepository repository;

    public CatEstadoEspecialidadCrudService(CatEstadoEspecialidadRepository repository) {
        this.repository = repository;
    }

    public CatEstadoEspecialidad crear(CatEstadoEspecialidad entity) {
        return repository.save(entity);
    }

    public Optional<CatEstadoEspecialidad> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatEstadoEspecialidad> buscarTodos() {
        return repository.findAll();
    }

    public CatEstadoEspecialidad actualizar(Short id, CatEstadoEspecialidad entity) {
        entity.setEstadoEspId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
