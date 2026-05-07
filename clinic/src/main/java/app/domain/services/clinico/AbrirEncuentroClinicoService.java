package app.domain.services.clinico;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbrirEncuentroClinicoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer citaId,
            Integer pacienteId,
            Integer medicoId,
            String motivoConsulta,
            String sintomatologia,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_abrir_encuentro_clinico");
        q.registerStoredProcedureParameter("p_cita_id",         Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_paciente_id",     Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_medico_id",       Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_motivo_consulta", String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_sintomatologia",  String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class, ParameterMode.OUT);

        q.setParameter("p_cita_id",         citaId);
        q.setParameter("p_paciente_id",     pacienteId);
        q.setParameter("p_medico_id",       medicoId);
        q.setParameter("p_motivo_consulta", motivoConsulta);
        q.setParameter("p_sintomatologia",  sintomatologia);
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
