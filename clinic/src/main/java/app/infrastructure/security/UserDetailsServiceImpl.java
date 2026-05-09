package app.infrastructure.security;

import app.domain.entities.masters.SeguridadUsuario;
import app.domain.repositories.SeguridadUsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de UserDetailsService que carga el empleado desde
 * la tabla seguridad_usuario usando su codigo_usuario.
 * La autoridad asignada sigue el patrón ROLE_{codigo_rol} para que
 * Spring Security pueda resolver hasRole() y hasAuthority().
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SeguridadUsuarioRepository repository;

    public UserDetailsServiceImpl(SeguridadUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SeguridadUsuario usuario = repository.findActivoConRol(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado o inactivo: " + username));

        String rolCodigo = usuario.getEmpleado().getRol().getCodigo(); // ej: "medico"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rolCodigo);

        return User.builder()
                .username(usuario.getCodigoUsuario())
                .password(usuario.getContrasenaHash())
                .authorities(List.of(authority))
                .build();
    }
}
