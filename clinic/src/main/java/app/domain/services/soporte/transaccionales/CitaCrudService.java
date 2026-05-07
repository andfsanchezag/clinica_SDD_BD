package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.Cita;
import app.domain.repositories.CitaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CitaCrudService {

    private final CitaRepository repository;

    public CitaCrudService(CitaRepository repository) {
        this.repository = repository;
    }

    public Cita crear(Cita entity) {
        return repository.save(entity);
    }

    public Optional<Cita> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Cita> buscarTodos() {
        return repository.findAll();
    }

    public Cita actualizar(Integer id, Cita entity) {
        entity.setCitaId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
