package app.domain.services.soporte.maestras;

import app.domain.entities.masters.MedicoPerfil;
import app.domain.repositories.MedicoPerfilRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoPerfilCrudService {

    private final MedicoPerfilRepository repository;

    public MedicoPerfilCrudService(MedicoPerfilRepository repository) {
        this.repository = repository;
    }

    public MedicoPerfil crear(MedicoPerfil entity) {
        return repository.save(entity);
    }

    public Optional<MedicoPerfil> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<MedicoPerfil> buscarTodos() {
        return repository.findAll();
    }

    public MedicoPerfil actualizar(Integer id, MedicoPerfil entity) {
        entity.setMedicoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
