// usuario.entity.js — Coincide con CrearUsuarioOperativoRequest
export class UsuarioEntity {
  constructor({ empleadoId, codigoUsuario, contrasenaHash, usuarioOperador }) {
    this.empleadoId      = Number(empleadoId);
    this.codigoUsuario   = codigoUsuario;
    this.contrasenaHash  = contrasenaHash;
    this.usuarioOperador = Number(usuarioOperador);
  }
}
