package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatEstadoCita;
import app.domain.repositories.CatEstadoCitaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatEstadoCitaCrudService {

    private final CatEstadoCitaRepository repository;

    public CatEstadoCitaCrudService(CatEstadoCitaRepository repository) {
        this.repository = repository;
    }

    public CatEstadoCita crear(CatEstadoCita entity) {
        return repository.save(entity);
    }

    public Optional<CatEstadoCita> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatEstadoCita> buscarTodos() {
        return repository.findAll();
    }

    public CatEstadoCita actualizar(Short id, CatEstadoCita entity) {
        entity.setEstadoCitaId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
