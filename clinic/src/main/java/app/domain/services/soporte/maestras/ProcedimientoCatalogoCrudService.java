package app.domain.services.soporte.maestras;

import app.domain.entities.masters.ProcedimientoCatalogo;
import app.domain.repositories.ProcedimientoCatalogoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedimientoCatalogoCrudService {

    private final ProcedimientoCatalogoRepository repository;

    public ProcedimientoCatalogoCrudService(ProcedimientoCatalogoRepository repository) {
        this.repository = repository;
    }

    public ProcedimientoCatalogo crear(ProcedimientoCatalogo entity) {
        return repository.save(entity);
    }

    public Optional<ProcedimientoCatalogo> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<ProcedimientoCatalogo> buscarTodos() {
        return repository.findAll();
    }

    public ProcedimientoCatalogo actualizar(Integer id, ProcedimientoCatalogo entity) {
        entity.setProcedimientoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
