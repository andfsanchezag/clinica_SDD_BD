package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatTipoOrden;
import app.domain.repositories.CatTipoOrdenRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatTipoOrdenCrudService {

    private final CatTipoOrdenRepository repository;

    public CatTipoOrdenCrudService(CatTipoOrdenRepository repository) {
        this.repository = repository;
    }

    public CatTipoOrden crear(CatTipoOrden entity) {
        return repository.save(entity);
    }

    public Optional<CatTipoOrden> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatTipoOrden> buscarTodos() {
        return repository.findAll();
    }

    public CatTipoOrden actualizar(Short id, CatTipoOrden entity) {
        entity.setTipoOrdenId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
