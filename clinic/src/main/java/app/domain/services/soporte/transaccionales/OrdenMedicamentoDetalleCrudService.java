package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.OrdenMedicamentoDetalle;
import app.domain.repositories.OrdenMedicamentoDetalleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenMedicamentoDetalleCrudService {

    private final OrdenMedicamentoDetalleRepository repository;

    public OrdenMedicamentoDetalleCrudService(OrdenMedicamentoDetalleRepository repository) {
        this.repository = repository;
    }

    public OrdenMedicamentoDetalle crear(OrdenMedicamentoDetalle entity) {
        return repository.save(entity);
    }

    public Optional<OrdenMedicamentoDetalle> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<OrdenMedicamentoDetalle> buscarTodos() {
        return repository.findAll();
    }

    public OrdenMedicamentoDetalle actualizar(Integer id, OrdenMedicamentoDetalle entity) {
        entity.setDetalleId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
