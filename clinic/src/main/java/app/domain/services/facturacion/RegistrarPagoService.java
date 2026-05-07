package app.domain.services.facturacion;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class RegistrarPagoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer facturaId,
            BigDecimal valorPagado,
            String tipoPago,
            String referencia,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_pago");
        q.registerStoredProcedureParameter("p_factura_id",      Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_valor_pagado",    BigDecimal.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tipo_pago",       String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_referencia",      String.class,     ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador",Integer.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",          Short.class,      ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",         String.class,     ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",     Integer.class,    ParameterMode.OUT);

        q.setParameter("p_factura_id",      facturaId);
        q.setParameter("p_valor_pagado",    valorPagado);
        q.setParameter("p_tipo_pago",       tipoPago);
        q.setParameter("p_referencia",      referencia);
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
