package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatTipoDocumento;
import app.domain.repositories.CatTipoDocumentoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatTipoDocumentoCrudService {

    private final CatTipoDocumentoRepository repository;

    public CatTipoDocumentoCrudService(CatTipoDocumentoRepository repository) {
        this.repository = repository;
    }

    public CatTipoDocumento crear(CatTipoDocumento entity) {
        return repository.save(entity);
    }

    public Optional<CatTipoDocumento> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatTipoDocumento> buscarTodos() {
        return repository.findAll();
    }

    public CatTipoDocumento actualizar(Short id, CatTipoDocumento entity) {
        entity.setTipoDocId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
