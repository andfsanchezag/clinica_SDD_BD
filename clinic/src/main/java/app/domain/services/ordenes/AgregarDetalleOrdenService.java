package app.domain.services.ordenes;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AgregarDetalleOrdenService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer ordenId,
            String tipoDetalle,
            Short item,
            Integer referenciaId,
            String dosis,
            String duracion,
            Short cantidad,
            String frecuencia,
            Boolean requiereEsp,
            Integer especialidadId,
            BigDecimal costo,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_agregar_detalle_orden");
        q.registerStoredProcedureParameter("p_orden_id",        Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tipo_detalle",    String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_item",            Short.class,      ParameterMode.IN);
        q.registerStoredProcedureParameter("p_referencia_id",   Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_dosis",           String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_duracion",        String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_cantidad",        Short.class,      ParameterMode.IN);
        q.registerStoredProcedureParameter("p_frecuencia",      String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_requiere_esp",    Boolean.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_especialidad_id", Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_costo",           BigDecimal.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,      ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,     ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class,    ParameterMode.OUT);

        q.setParameter("p_orden_id",        ordenId);
        q.setParameter("p_tipo_detalle",    tipoDetalle);
        q.setParameter("p_item",            item);
        q.setParameter("p_referencia_id",   referenciaId);
        q.setParameter("p_dosis",           dosis);
        q.setParameter("p_duracion",        duracion);
        q.setParameter("p_cantidad",        cantidad);
        q.setParameter("p_frecuencia",      frecuencia);
        q.setParameter("p_requiere_esp",    requiereEsp);
        q.setParameter("p_especialidad_id", especialidadId);
        q.setParameter("p_costo",           costo);
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
