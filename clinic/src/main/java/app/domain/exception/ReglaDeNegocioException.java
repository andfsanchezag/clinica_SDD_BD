package app.domain.exception;

/**
 * Se lanza cuando un SP o una regla de negocio retorna un error controlado.
 * Mapea a HTTP 409 Conflict.
 */
public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
