package app.domain.services.ordenes;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarOrdenMedicaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            String numeroOrden,
            Integer encuentroId,
            Integer pacienteId,
            Integer medicoId,
            Short tipoOrdenId,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_orden_medica");
        q.registerStoredProcedureParameter("p_numero_orden",    String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_encuentro_id",    Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_paciente_id",     Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_medico_id",       Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tipo_orden_id",   Short.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class, ParameterMode.OUT);

        q.setParameter("p_numero_orden",    numeroOrden);
        q.setParameter("p_encuentro_id",    encuentroId);
        q.setParameter("p_paciente_id",     pacienteId);
        q.setParameter("p_medico_id",       medicoId);
        q.setParameter("p_tipo_orden_id",   tipoOrdenId);
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
