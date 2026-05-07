package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.CatRolUsuario;
import app.domain.repositories.CatRolUsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatRolUsuarioCrudService {

    private final CatRolUsuarioRepository repository;

    public CatRolUsuarioCrudService(CatRolUsuarioRepository repository) {
        this.repository = repository;
    }

    public CatRolUsuario crear(CatRolUsuario entity) {
        return repository.save(entity);
    }

    public Optional<CatRolUsuario> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<CatRolUsuario> buscarTodos() {
        return repository.findAll();
    }

    public CatRolUsuario actualizar(Short id, CatRolUsuario entity) {
        entity.setRolId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
