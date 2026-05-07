package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatGenero;
import app.domain.repositories.CatGeneroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatGeneroCrudService {

    private final CatGeneroRepository repository;

    public CatGeneroCrudService(CatGeneroRepository repository) {
        this.repository = repository;
    }

    public CatGenero crear(CatGenero entity) {
        return repository.save(entity);
    }

    public Optional<CatGenero> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatGenero> buscarTodos() {
        return repository.findAll();
    }

    public CatGenero actualizar(Short id, CatGenero entity) {
        entity.setGeneroId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
