package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.OrdenMedica;
import app.domain.repositories.OrdenMedicaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenMedicaCrudService {

    private final OrdenMedicaRepository repository;

    public OrdenMedicaCrudService(OrdenMedicaRepository repository) {
        this.repository = repository;
    }

    public OrdenMedica crear(OrdenMedica entity) {
        return repository.save(entity);
    }

    public Optional<OrdenMedica> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<OrdenMedica> buscarTodos() {
        return repository.findAll();
    }

    public OrdenMedica actualizar(Integer id, OrdenMedica entity) {
        entity.setOrdenId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
