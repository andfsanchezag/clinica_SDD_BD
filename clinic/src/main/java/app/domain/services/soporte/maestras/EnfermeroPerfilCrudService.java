package app.domain.services.soporte.maestras;

import app.domain.entities.masters.EnfermeroPerfil;
import app.domain.repositories.EnfermeroPerfilRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnfermeroPerfilCrudService {

    private final EnfermeroPerfilRepository repository;

    public EnfermeroPerfilCrudService(EnfermeroPerfilRepository repository) {
        this.repository = repository;
    }

    public EnfermeroPerfil crear(EnfermeroPerfil entity) {
        return repository.save(entity);
    }

    public Optional<EnfermeroPerfil> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<EnfermeroPerfil> buscarTodos() {
        return repository.findAll();
    }

    public EnfermeroPerfil actualizar(Integer id, EnfermeroPerfil entity) {
        entity.setEnfermeroId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
