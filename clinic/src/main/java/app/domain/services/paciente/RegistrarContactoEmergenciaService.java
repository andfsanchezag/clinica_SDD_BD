package app.domain.services.paciente;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarContactoEmergenciaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer pacienteId,
            String nombreCompleto,
            String relacion,
            String telefono,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_contacto_emergencia");
        q.registerStoredProcedureParameter("p_paciente_id",     Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_nombre_completo", String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_relacion",        String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_telefono",        String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class, ParameterMode.OUT);

        q.setParameter("p_paciente_id",     pacienteId);
        q.setParameter("p_nombre_completo", nombreCompleto);
        q.setParameter("p_relacion",        relacion);
        q.setParameter("p_telefono",        telefono);
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
