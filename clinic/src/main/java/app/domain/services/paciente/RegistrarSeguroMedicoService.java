package app.domain.services.paciente;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class RegistrarSeguroMedicoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer pacienteId,
            String compania,
            String numeroPoliza,
            Short estadoSeguroId,
            LocalDate fechaVigencia,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_seguro_medico");
        q.registerStoredProcedureParameter("p_paciente_id",      Integer.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_compania",         String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_numero_poliza",    String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_estado_seguro_id", Short.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_fecha_vigencia",   LocalDate.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador", Integer.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",           Short.class,    ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",          String.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",      Integer.class,  ParameterMode.OUT);

        q.setParameter("p_paciente_id",      pacienteId);
        q.setParameter("p_compania",         compania);
        q.setParameter("p_numero_poliza",    numeroPoliza);
        q.setParameter("p_estado_seguro_id", estadoSeguroId);
        q.setParameter("p_fecha_vigencia",   fechaVigencia);
        q.setParameter("p_usuario_operador", usuarioOperador);

        q.execute();

        SpResultado resultado = new SpResultado();
        resultado.setCodigo(((Number) q.getOutputParameterValue("o_codigo")).shortValue());
        resultado.setMensaje((String) q.getOutputParameterValue("o_mensaje"));
        Object id = q.getOutputParameterValue("o_id_generado");
        resultado.setIdGenerado(id != null ? ((Number) id).intValue() : null);
        return resultado;
    }
}
