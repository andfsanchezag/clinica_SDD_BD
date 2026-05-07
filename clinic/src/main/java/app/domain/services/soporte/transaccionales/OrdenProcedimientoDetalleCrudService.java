package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.OrdenProcedimientoDetalle;
import app.domain.repositories.OrdenProcedimientoDetalleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenProcedimientoDetalleCrudService {

    private final OrdenProcedimientoDetalleRepository repository;

    public OrdenProcedimientoDetalleCrudService(OrdenProcedimientoDetalleRepository repository) {
        this.repository = repository;
    }

    public OrdenProcedimientoDetalle crear(OrdenProcedimientoDetalle entity) {
        return repository.save(entity);
    }

    public Optional<OrdenProcedimientoDetalle> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<OrdenProcedimientoDetalle> buscarTodos() {
        return repository.findAll();
    }

    public OrdenProcedimientoDetalle actualizar(Integer id, OrdenProcedimientoDetalle entity) {
        entity.setDetalleId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
