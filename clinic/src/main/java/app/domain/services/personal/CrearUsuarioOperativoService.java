package app.domain.services.personal;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearUsuarioOperativoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            Integer empleadoId,
            String codigoUsuario,
            String contrasenaHash,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_crear_usuario_operativo");
        q.registerStoredProcedureParameter("p_empleado_id",      Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_codigo_usuario",   String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_contrasena_hash",  String.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",           Short.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",          String.class,  ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",      Integer.class, ParameterMode.OUT);

        q.setParameter("p_empleado_id",      empleadoId);
        q.setParameter("p_codigo_usuario",   codigoUsuario);
        q.setParameter("p_contrasena_hash",  contrasenaHash);
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
