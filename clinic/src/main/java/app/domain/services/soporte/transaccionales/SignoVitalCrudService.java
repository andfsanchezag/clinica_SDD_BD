package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.SignoVital;
import app.domain.repositories.SignoVitalRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SignoVitalCrudService {

    private final SignoVitalRepository repository;

    public SignoVitalCrudService(SignoVitalRepository repository) {
        this.repository = repository;
    }

    public SignoVital crear(SignoVital entity) {
        return repository.save(entity);
    }

    public Optional<SignoVital> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<SignoVital> buscarTodos() {
        return repository.findAll();
    }

    public SignoVital actualizar(Integer id, SignoVital entity) {
        entity.setSignoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
