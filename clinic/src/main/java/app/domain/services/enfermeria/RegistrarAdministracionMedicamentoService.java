package app.domain.services.enfermeria;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarAdministracionMedicamentoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer encuentroId,
            Integer enfermeroId,
            Integer medicamentoId,
            String dosis,
            String observacion,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_administracion_medicamento");
        q.registerStoredProcedureParameter("p_encuentro_id",    Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_enfermero_id",    Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_medicamento_id",  Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_dosis",           String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_observacion",     String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class, ParameterMode.OUT);

        q.setParameter("p_encuentro_id",    encuentroId);
        q.setParameter("p_enfermero_id",    enfermeroId);
        q.setParameter("p_medicamento_id",  medicamentoId);
        q.setParameter("p_dosis",           dosis);
        q.setParameter("p_observacion",     observacion);
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
