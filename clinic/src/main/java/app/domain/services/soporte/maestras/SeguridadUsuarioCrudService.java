package app.domain.services.soporte.maestras;

import app.domain.entities.masters.SeguridadUsuario;
import app.domain.repositories.SeguridadUsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SeguridadUsuarioCrudService {

    private final SeguridadUsuarioRepository repository;

    public SeguridadUsuarioCrudService(SeguridadUsuarioRepository repository) {
        this.repository = repository;
    }

    public SeguridadUsuario crear(SeguridadUsuario entity) {
        return repository.save(entity);
    }

    public Optional<SeguridadUsuario> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<SeguridadUsuario> buscarTodos() {
        return repository.findAll();
    }

    public SeguridadUsuario actualizar(Integer id, SeguridadUsuario entity) {
        entity.setUsuarioId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
