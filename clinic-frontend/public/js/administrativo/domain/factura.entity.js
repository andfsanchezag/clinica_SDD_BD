// factura.entity.js
export class FacturaEntity {
  constructor({ encuentroId, pacienteId, medicoId, seguroId, tipoFactId, usuarioOperador }) {
    this.encuentroId     = Number(encuentroId);
    this.pacienteId      = Number(pacienteId);
    this.medicoId        = Number(medicoId);
    this.seguroId        = seguroId ? Number(seguroId) : null;
    this.tipoFactId      = Number(tipoFactId);
    this.usuarioOperador = Number(usuarioOperador);
  }
}
