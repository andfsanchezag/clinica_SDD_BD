// administracion.entity.js — Coincide con RegistrarAdministracionMedicamentoRequest
export class AdministracionEntity {
  constructor({ encuentroId, enfermeroId, medicamentoId, dosis, observacion, usuarioOperador }) {
    this.encuentroId     = Number(encuentroId);
    this.enfermeroId     = Number(enfermeroId);
    this.medicamentoId   = Number(medicamentoId);
    this.dosis           = dosis;
    this.observacion     = observacion ?? null;
    this.usuarioOperador = Number(usuarioOperador);
  }
}
