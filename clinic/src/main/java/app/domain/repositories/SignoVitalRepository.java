package app.domain.repositories;

import app.domain.entities.transactions.SignoVital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SignoVitalRepository extends JpaRepository<SignoVital, Integer> {
    List<SignoVital> findByEncuentro_EncuentroId(Integer encuentroId);
}
