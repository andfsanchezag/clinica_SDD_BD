package app.domain.services.soporte.maestras;

import app.domain.entities.masters.ContactoEmergencia;
import app.domain.repositories.ContactoEmergenciaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoEmergenciaCrudService {

    private final ContactoEmergenciaRepository repository;

    public ContactoEmergenciaCrudService(ContactoEmergenciaRepository repository) {
        this.repository = repository;
    }

    public ContactoEmergencia crear(ContactoEmergencia entity) {
        return repository.save(entity);
    }

    public Optional<ContactoEmergencia> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<ContactoEmergencia> buscarTodos() {
        return repository.findAll();
    }

    public ContactoEmergencia actualizar(Integer id, ContactoEmergencia entity) {
        entity.setContactoId(id);
        return repository.save(entity);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
