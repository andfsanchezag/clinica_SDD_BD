package app.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio de generación y validación de JSON Web Tokens.
 * El token incluye:
 *   - sub  : codigo_usuario
 *   - rol  : codigo del CatRolUsuario (ej: "medico", "enfermeria")
 *   - iat  : fecha de emisión
 *   - exp  : fecha de expiración (configurable, por defecto 24 h)
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
        this.secretKey  = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /** Genera un JWT firmado con el usuario, su rol y su ID. */
    public String generateToken(String username, String rol, Integer usuarioId) {
        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .claim("uid", usuarioId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    /** Genera un JWT sin usuarioId (compatibilidad). */
    public String generateToken(String username, String rol) {
        return generateToken(username, rol, null);
    }

    /** Extrae el nombre de usuario (subject) del token. */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /** Extrae el rol embebido en el token. */
    public String extractRol(String token) {
        return parseClaims(token).get("rol", String.class);
    }

    /** Retorna true si el token es válido (firma correcta y no expirado). */
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
