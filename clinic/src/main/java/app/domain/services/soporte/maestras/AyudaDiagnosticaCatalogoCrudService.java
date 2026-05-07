package app.domain.services.soporte.maestras;

import app.domain.entities.masters.AyudaDiagnosticaCatalogo;
import app.domain.repositories.AyudaDiagnosticaCatalogoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AyudaDiagnosticaCatalogoCrudService {

    private final AyudaDiagnosticaCatalogoRepository repository;

    public AyudaDiagnosticaCatalogoCrudService(AyudaDiagnosticaCatalogoRepository repository) {
        this.repository = repository;
    }

    public AyudaDiagnosticaCatalogo crear(AyudaDiagnosticaCatalogo entity) {
        return repository.save(entity);
    }

    public Optional<AyudaDiagnosticaCatalogo> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<AyudaDiagnosticaCatalogo> buscarTodos() {
        return repository.findAll();
    }

    public AyudaDiagnosticaCatalogo actualizar(Integer id, AyudaDiagnosticaCatalogo entity) {
        entity.setAyudaId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
