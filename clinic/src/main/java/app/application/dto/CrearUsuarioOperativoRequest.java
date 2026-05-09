package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearUsuarioOperativoRequest {
    @NotNull  private Integer empleadoId;
    @NotBlank private String codigoUsuario;
    @NotBlank private String contrasenaHash;
    @NotNull  private Integer usuarioOperador;
}
