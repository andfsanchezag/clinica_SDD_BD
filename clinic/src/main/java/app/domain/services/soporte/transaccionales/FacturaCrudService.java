package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.Factura;
import app.domain.repositories.FacturaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaCrudService {

    private final FacturaRepository repository;

    public FacturaCrudService(FacturaRepository repository) {
        this.repository = repository;
    }

    public Factura crear(Factura entity) {
        return repository.save(entity);
    }

    public Optional<Factura> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Factura> buscarTodos() {
        return repository.findAll();
    }

    public Factura actualizar(Integer id, Factura entity) {
        entity.setFacturaId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
