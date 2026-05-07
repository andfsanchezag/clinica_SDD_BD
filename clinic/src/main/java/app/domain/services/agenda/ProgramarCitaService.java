package app.domain.services.agenda;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ProgramarCitaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer pacienteId,
            Integer medicoId,
            LocalDateTime fechaHora,
            Short prioridadId,
            String motivo,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_programar_cita");
        q.registerStoredProcedureParameter("p_paciente_id",     Integer.class,       ParameterMode.IN);
        q.registerStoredProcedureParameter("p_medico_id",       Integer.class,       ParameterMode.IN);
        q.registerStoredProcedureParameter("p_fecha_hora",      LocalDateTime.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_prioridad_id",    Short.class,         ParameterMode.IN);
        q.registerStoredProcedureParameter("p_motivo",          String.class,        ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class,       ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,         ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,        ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class,       ParameterMode.OUT);

        q.setParameter("p_paciente_id",     pacienteId);
        q.setParameter("p_medico_id",       medicoId);
        q.setParameter("p_fecha_hora",      fechaHora);
        q.setParameter("p_prioridad_id",    prioridadId);
        q.setParameter("p_motivo",          motivo);
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
