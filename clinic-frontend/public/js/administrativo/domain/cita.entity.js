// cita.entity.js
export class CitaEntity {
  constructor({ pacienteId, medicoId, fechaHora, prioridadId, motivo, usuarioOperador }) {
    this.pacienteId      = Number(pacienteId);
    this.medicoId        = Number(medicoId);
    this.fechaHora       = fechaHora;           // "YYYY-MM-DDTHH:mm:ss"
    this.prioridadId     = Number(prioridadId);
    this.motivo          = motivo ?? null;
    this.usuarioOperador = Number(usuarioOperador);
  }
}
