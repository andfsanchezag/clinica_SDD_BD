// auth.entity.js — Entidad de dominio para la autenticación

export class AuthEntity {
  /** @param {{ token: string, rol: string, usuario: string, usuarioId: number|null }} data */
  constructor({ token, rol, usuario, usuarioId = null }) {
    this.token     = token;
    this.rol       = rol;
    this.usuario   = usuario;
    this.usuarioId = usuarioId;
  }
}
