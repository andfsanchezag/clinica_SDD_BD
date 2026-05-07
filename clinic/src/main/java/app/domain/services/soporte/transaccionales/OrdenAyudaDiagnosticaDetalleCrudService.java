package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.OrdenAyudaDiagnosticaDetalle;
import app.domain.repositories.OrdenAyudaDiagnosticaDetalleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenAyudaDiagnosticaDetalleCrudService {

    private final OrdenAyudaDiagnosticaDetalleRepository repository;

    public OrdenAyudaDiagnosticaDetalleCrudService(OrdenAyudaDiagnosticaDetalleRepository repository) {
        this.repository = repository;
    }

    public OrdenAyudaDiagnosticaDetalle crear(OrdenAyudaDiagnosticaDetalle entity) {
        return repository.save(entity);
    }

    public Optional<OrdenAyudaDiagnosticaDetalle> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<OrdenAyudaDiagnosticaDetalle> buscarTodos() {
        return repository.findAll();
    }

    public OrdenAyudaDiagnosticaDetalle actualizar(Integer id, OrdenAyudaDiagnosticaDetalle entity) {
        entity.setDetalleId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
