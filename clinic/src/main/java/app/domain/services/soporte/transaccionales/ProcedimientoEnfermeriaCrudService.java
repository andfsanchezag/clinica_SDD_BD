package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.ProcedimientoEnfermeria;
import app.domain.repositories.ProcedimientoEnfermeriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedimientoEnfermeriaCrudService {

    private final ProcedimientoEnfermeriaRepository repository;

    public ProcedimientoEnfermeriaCrudService(ProcedimientoEnfermeriaRepository repository) {
        this.repository = repository;
    }

    public ProcedimientoEnfermeria crear(ProcedimientoEnfermeria entity) {
        return repository.save(entity);
    }

    public Optional<ProcedimientoEnfermeria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<ProcedimientoEnfermeria> buscarTodos() {
        return repository.findAll();
    }

    public ProcedimientoEnfermeria actualizar(Integer id, ProcedimientoEnfermeria entity) {
        entity.setProcEnfId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
