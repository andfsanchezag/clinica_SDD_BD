package app.domain.exception;

/**
 * Se lanza cuando un recurso buscado no existe.
 * Mapea a HTTP 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
