// empleado.entity.js — Coincide con RegistrarEmpleadoRequest
export class EmpleadoEntity {
  constructor({ cedula, tipoDocId, nombreCompleto, fechaNacimiento, telefono, correo, direccion, rolId, usuarioOperador }) {
    this.cedula          = cedula;
    this.tipoDocId       = Number(tipoDocId);
    this.nombreCompleto  = nombreCompleto;
    this.fechaNacimiento = fechaNacimiento;
    this.telefono        = telefono  ?? null;
    this.correo          = correo    ?? null;
    this.direccion       = direccion ?? null;
    this.rolId           = Number(rolId);
    this.usuarioOperador = Number(usuarioOperador);
  }
}
