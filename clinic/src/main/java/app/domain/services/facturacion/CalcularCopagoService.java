package app.domain.services.facturacion;

import app.domain.dto.SpResultadoCopago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CalcularCopagoService {

    @PersistenceContext
    private EntityManager entityManager;

    public SpResultadoCopago ejecutar(Integer pacienteId, Short tipoFactId) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_calcular_copago");
        q.registerStoredProcedureParameter("p_paciente_id",  Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tipo_fact_id", Short.class,      ParameterMode.IN);
        q.registerStoredProcedureParameter("o_valor_copago", BigDecimal.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_exento",       Boolean.class,    ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",      String.class,     ParameterMode.OUT);

        q.setParameter("p_paciente_id",  pacienteId);
        q.setParameter("p_tipo_fact_id", tipoFactId);

        q.execute();

        SpResultadoCopago resultado = new SpResultadoCopago();
        Object val = q.getOutputParameterValue("o_valor_copago");
        resultado.setValorCopago(val != null ? (BigDecimal) val : BigDecimal.ZERO);
        Object exento = q.getOutputParameterValue("o_exento");
        resultado.setExento(exento != null && ((Number) exento).intValue() == 1);
        resultado.setMensaje((String) q.getOutputParameterValue("o_mensaje"));
        return resultado;
    }
}
