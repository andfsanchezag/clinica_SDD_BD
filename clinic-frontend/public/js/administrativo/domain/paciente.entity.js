// paciente.entity.js
export class PacienteEntity {
  constructor({ cedula, tipoDocId, nombreCompleto, fechaNacimiento,
                generoId, direccion, telefono, correo, usuarioOperador }) {
    this.cedula           = cedula;
    this.tipoDocId        = Number(tipoDocId);
    this.nombreCompleto   = nombreCompleto;
    this.fechaNacimiento  = fechaNacimiento;
    this.generoId         = Number(generoId);
    this.direccion        = direccion ?? null;
    this.telefono         = telefono  ?? null;
    this.correo           = correo    ?? null;
    this.usuarioOperador  = Number(usuarioOperador);
  }
}
