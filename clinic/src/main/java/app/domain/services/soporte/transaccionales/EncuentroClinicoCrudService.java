package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.EncuentroClinico;
import app.domain.repositories.EncuentroClinicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EncuentroClinicoCrudService {

    private final EncuentroClinicoRepository repository;

    public EncuentroClinicoCrudService(EncuentroClinicoRepository repository) {
        this.repository = repository;
    }

    public EncuentroClinico crear(EncuentroClinico entity) {
        return repository.save(entity);
    }

    public Optional<EncuentroClinico> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<EncuentroClinico> buscarTodos() {
        return repository.findAll();
    }

    public EncuentroClinico actualizar(Integer id, EncuentroClinico entity) {
        entity.setEncuentroId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
