// encuentro.entity.js
export class EncuentroEntity {
  constructor({ citaId, pacienteId, medicoId, motivoConsulta, sintomatologia, usuarioOperador }) {
    this.citaId          = Number(citaId);
    this.pacienteId      = Number(pacienteId);
    this.medicoId        = Number(medicoId);
    this.motivoConsulta  = motivoConsulta;
    this.sintomatologia  = sintomatologia ?? null;
    this.usuarioOperador = Number(usuarioOperador);
  }
}
