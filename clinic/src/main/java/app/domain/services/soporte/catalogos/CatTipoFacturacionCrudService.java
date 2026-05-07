package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatTipoFacturacion;
import app.domain.repositories.CatTipoFacturacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatTipoFacturacionCrudService {

    private final CatTipoFacturacionRepository repository;

    public CatTipoFacturacionCrudService(CatTipoFacturacionRepository repository) {
        this.repository = repository;
    }

    public CatTipoFacturacion crear(CatTipoFacturacion entity) {
        return repository.save(entity);
    }

    public Optional<CatTipoFacturacion> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatTipoFacturacion> buscarTodos() {
        return repository.findAll();
    }

    public CatTipoFacturacion actualizar(Short id, CatTipoFacturacion entity) {
        entity.setTipoFactId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
