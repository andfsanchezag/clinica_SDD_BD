package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatEstadoSeguro;
import app.domain.repositories.CatEstadoSeguroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatEstadoSeguroCrudService {

    private final CatEstadoSeguroRepository repository;

    public CatEstadoSeguroCrudService(CatEstadoSeguroRepository repository) {
        this.repository = repository;
    }

    public CatEstadoSeguro crear(CatEstadoSeguro entity) {
        return repository.save(entity);
    }

    public Optional<CatEstadoSeguro> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatEstadoSeguro> buscarTodos() {
        return repository.findAll();
    }

    public CatEstadoSeguro actualizar(Short id, CatEstadoSeguro entity) {
        entity.setEstadoSeguroId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
