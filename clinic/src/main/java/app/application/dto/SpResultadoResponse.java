package app.application.dto;

import lombok.Data;

/**
 * Respuesta genérica para operaciones que invocan stored procedures.
 */
@Data
public class SpResultadoResponse {
    private Short codigo;
    private String mensaje;
    private Integer idGenerado;
    private boolean exitoso;
}
