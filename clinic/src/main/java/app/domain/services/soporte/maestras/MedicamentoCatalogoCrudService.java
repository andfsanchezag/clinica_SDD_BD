package app.domain.services.soporte.maestras;

import app.domain.entities.masters.MedicamentoCatalogo;
import app.domain.repositories.MedicamentoCatalogoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoCatalogoCrudService {

    private final MedicamentoCatalogoRepository repository;

    public MedicamentoCatalogoCrudService(MedicamentoCatalogoRepository repository) {
        this.repository = repository;
    }

    public MedicamentoCatalogo crear(MedicamentoCatalogo entity) {
        return repository.save(entity);
    }

    public Optional<MedicamentoCatalogo> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<MedicamentoCatalogo> buscarTodos() {
        return repository.findAll();
    }

    public MedicamentoCatalogo actualizar(Integer id, MedicamentoCatalogo entity) {
        entity.setMedicamentoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
