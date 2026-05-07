package app.domain.services.clinico;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CerrarEncuentroClinicoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer encuentroId,
            String diagnostico,
            String tratamiento,
            String observaciones,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_cerrar_encuentro_clinico");
        q.registerStoredProcedureParameter("p_encuentro_id",    Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_diagnostico",     String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tratamiento",     String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_observaciones",   String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class, ParameterMode.OUT);

        q.setParameter("p_encuentro_id",    encuentroId);
        q.setParameter("p_diagnostico",     diagnostico);
        q.setParameter("p_tratamiento",     tratamiento);
        q.setParameter("p_observaciones",   observaciones);
        q.setParameter("p_usuario_operador",usuarioOperador);

        q.execute();

        SpResultado resultado = new SpResultado();
        resultado.setCodigo(((Number) q.getOutputParameterValue("o_codigo")).shortValue());
        resultado.setMensaje((String) q.getOutputParameterValue("o_mensaje"));
        Object id = q.getOutputParameterValue("o_id_generado");
        resultado.setIdGenerado(id != null ? ((Number) id).intValue() : null);
        return resultado;
    }
}
