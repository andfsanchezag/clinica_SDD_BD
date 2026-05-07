package app.domain.services.soporte.catalogos;

import app.domain.entities.catalogs.ConfigFacturacion;
import app.domain.repositories.ConfigFacturacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigFacturacionCrudService {

    private final ConfigFacturacionRepository repository;

    public ConfigFacturacionCrudService(ConfigFacturacionRepository repository) {
        this.repository = repository;
    }

    public ConfigFacturacion crear(ConfigFacturacion entity) {
        return repository.save(entity);
    }

    public Optional<ConfigFacturacion> buscarPorId(Short id) {
        return repository.findById(id);
    }

    public List<ConfigFacturacion> buscarTodos() {
        return repository.findAll();
    }

    public ConfigFacturacion actualizar(Short id, ConfigFacturacion entity) {
        entity.setConfigId(id);
        return repository.save(entity);
    }

    public void eliminar(Short id) {
        repository.deleteById(id);
    }
}
