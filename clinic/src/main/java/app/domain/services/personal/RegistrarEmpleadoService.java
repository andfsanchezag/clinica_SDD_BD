package app.domain.services.personal;

import app.domain.dto.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class RegistrarEmpleadoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SpResultado ejecutar(
            String cedula,
            Short tipoDocId,
            String nombreCompleto,
            String correo,
            String telefono,
            LocalDate fechaNacimiento,
            String direccion,
            Short rolId,
            Integer usuarioOperador) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("sp_registrar_empleado");
        q.registerStoredProcedureParameter("p_cedula",           String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_tipo_doc_id",      Short.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_nombre_completo",  String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_correo",           String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_telefono",         String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_fecha_nacimiento", LocalDate.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_direccion",        String.class,   ParameterMode.IN);
        q.registerStoredProcedureParameter("p_rol_id",           Short.class,    ParameterMode.IN);
        q.registerStoredProcedureParameter("p_usuario_operador", Integer.class,  ParameterMode.IN);
        q.registerStoredProcedureParameter("o_codigo",           Short.class,    ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_mensaje",          String.class,   ParameterMode.OUT);
        q.registerStoredProcedureParameter("o_id_generado",      Integer.class,  ParameterMode.OUT);

        q.setParameter("p_cedula",           cedula);
        q.setParameter("p_tipo_doc_id",      tipoDocId);
        q.setParameter("p_nombre_completo",  nombreCompleto);
        q.setParameter("p_correo",           correo);
        q.setParameter("p_telefono",         telefono);
        q.setParameter("p_fecha_nacimiento", fechaNacimiento);
        q.setParameter("p_direccion",        direccion);
        q.setParameter("p_rol_id",           rolId);
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
