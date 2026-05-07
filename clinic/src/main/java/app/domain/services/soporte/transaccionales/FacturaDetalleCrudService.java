package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.FacturaDetalle;
import app.domain.repositories.FacturaDetalleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaDetalleCrudService {

    private final FacturaDetalleRepository repository;

    public FacturaDetalleCrudService(FacturaDetalleRepository repository) {
        this.repository = repository;
    }

    public FacturaDetalle crear(FacturaDetalle entity) {
        return repository.save(entity);
    }

    public Optional<FacturaDetalle> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<FacturaDetalle> buscarTodos() {
        return repository.findAll();
    }

    public FacturaDetalle actualizar(Integer id, FacturaDetalle entity) {
        entity.setDetFacturaId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
