package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.Pago;
import app.domain.repositories.PagoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PagoCrudService {

    private final PagoRepository repository;

    public PagoCrudService(PagoRepository repository) {
        this.repository = repository;
    }

    public Pago crear(Pago entity) {
        return repository.save(entity);
    }

    public Optional<Pago> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Pago> buscarTodos() {
        return repository.findAll();
    }

    public Pago actualizar(Integer id, Pago entity) {
        entity.setPagoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
