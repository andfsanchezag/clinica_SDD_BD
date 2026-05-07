package app.domain.services.enfermeria;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class RegistrarSignosVitalesService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer encuentroId,
            Integer enfermeroId,
            String presion,
            BigDecimal temperatura,
            Short pulso,
            BigDecimal oxigeno,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_signos_vitales");
        q.registerStoredProcedureParameter("p_encuentro_id",    Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_enfermero_id",    Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_presion",         String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_temperatura",     BigDecimal.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_pulso",           Short.class,      ParameterMode.IN);
        q.registerStoredProcedureParameter("p_oxigeno",         BigDecimal.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,      ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,     ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class,    ParameterMode.OUT);

        q.setParameter("p_encuentro_id",    encuentroId);
        q.setParameter("p_enfermero_id",    enfermeroId);
        q.setParameter("p_presion",         presion);
        q.setParameter("p_temperatura",     temperatura);
        q.setParameter("p_pulso",           pulso);
        q.setParameter("p_oxigeno",         oxigeno);
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
