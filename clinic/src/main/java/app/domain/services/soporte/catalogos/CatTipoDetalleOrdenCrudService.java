package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatTipoDetalleOrden;
import app.domain.repositories.CatTipoDetalleOrdenRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatTipoDetalleOrdenCrudService {

    private final CatTipoDetalleOrdenRepository repository;

    public CatTipoDetalleOrdenCrudService(CatTipoDetalleOrdenRepository repository) {
        this.repository = repository;
    }

    public CatTipoDetalleOrden crear(CatTipoDetalleOrden entity) {
        return repository.save(entity);
    }

    public Optional<CatTipoDetalleOrden> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatTipoDetalleOrden> buscarTodos() {
        return repository.findAll();
    }

    public CatTipoDetalleOrden actualizar(Short id, CatTipoDetalleOrden entity) {
        entity.setTipoDetalleId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
