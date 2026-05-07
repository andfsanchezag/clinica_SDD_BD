package app.domain.services.soporte.transaccionales;

import app.domain.entities.transactions.AdministracionMedicamento;
import app.domain.repositories.AdministracionMedicamentoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdministracionMedicamentoCrudService {

    private final AdministracionMedicamentoRepository repository;

    public AdministracionMedicamentoCrudService(AdministracionMedicamentoRepository repository) {
        this.repository = repository;
    }

    public AdministracionMedicamento crear(AdministracionMedicamento entity) {
        return repository.save(entity);
    }

    public Optional<AdministracionMedicamento> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<AdministracionMedicamento> buscarTodos() {
        return repository.findAll();
    }

    public AdministracionMedicamento actualizar(Integer id, AdministracionMedicamento entity) {
        entity.setAdminMedId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
