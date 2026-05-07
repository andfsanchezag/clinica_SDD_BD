package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatPrioridadAtencion;
import app.domain.repositories.CatPrioridadAtencionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatPrioridadAtencionCrudService {

    private final CatPrioridadAtencionRepository repository;

    public CatPrioridadAtencionCrudService(CatPrioridadAtencionRepository repository) {
        this.repository = repository;
    }

    public CatPrioridadAtencion crear(CatPrioridadAtencion entity) {
        return repository.save(entity);
    }

    public Optional<CatPrioridadAtencion> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatPrioridadAtencion> buscarTodos() {
        return repository.findAll();
    }

    public CatPrioridadAtencion actualizar(Short id, CatPrioridadAtencion entity) {
        entity.setPrioridadId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
