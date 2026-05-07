package app.domain.services.soporte.maestras;

import app.domain.entities.masters.SeguroMedico;
import app.domain.repositories.SeguroMedicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SeguroMedicoCrudService {

    private final SeguroMedicoRepository repository;

    public SeguroMedicoCrudService(SeguroMedicoRepository repository) {
        this.repository = repository;
    }

    public SeguroMedico crear(SeguroMedico entity) {
        return repository.save(entity);
    }

    public Optional<SeguroMedico> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<SeguroMedico> buscarTodos() {
        return repository.findAll();
    }

    public SeguroMedico actualizar(Integer id, SeguroMedico entity) {
        entity.setSeguroId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
