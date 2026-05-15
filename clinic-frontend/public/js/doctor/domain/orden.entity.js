// orden.entity.js
export class OrdenEntity {
  constructor({ numeroOrden, encuentroId, pacienteId, medicoId, tipoOrdenId, usuarioOperador }) {
    this.numeroOrden     = numeroOrden;
    this.encuentroId     = Number(encuentroId);
    this.pacienteId      = Number(pacienteId);
    this.medicoId        = Number(medicoId);
    this.tipoOrdenId     = Number(tipoOrdenId);
    this.usuarioOperador = Number(usuarioOperador);
  }
}
