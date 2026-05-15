package app.infrastructure.controller;

import app.application.dto.LoginRequest;
import app.application.dto.LoginResponse;
import app.domain.entities.masters.SeguridadUsuario;
import app.domain.repositories.SeguridadUsuarioRepository;
import app.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de autenticación.
 * POST /api/auth/login  →  retorna JWT con el rol del empleado.
 *
 * El token debe enviarse en las siguientes peticiones como:
 *   Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final SeguridadUsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          SeguridadUsuarioRepository usuarioRepository) {
        this.authManager        = authManager;
        this.jwtService         = jwtService;
        this.usuarioRepository  = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        // El authority tiene prefijo ROLE_; se elimina para obtener el código limpio
        String rol = user.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replace("ROLE_", ""))
                .orElse("");

        // Obtener el usuarioId para incluirlo en la respuesta
        Integer usuarioId = usuarioRepository.findActivoConRol(user.getUsername())
                .map(SeguridadUsuario::getUsuarioId)
                .orElse(null);

        String token = jwtService.generateToken(user.getUsername(), rol, usuarioId);

        return ResponseEntity.ok(new LoginResponse(token, rol, user.getUsername(), usuarioId));
    }
}
