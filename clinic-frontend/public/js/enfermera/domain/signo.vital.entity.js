// signo.vital.entity.js — Coincide con RegistrarSignosVitalesRequest
export class SignoVitalEntity {
  constructor({ encuentroId, enfermeroId, presion, temperatura, pulso, oxigeno, usuarioOperador }) {
    this.encuentroId     = Number(encuentroId);
    this.enfermeroId     = Number(enfermeroId);
    this.presion         = presion         ?? null;
    this.temperatura     = temperatura     ? Number(temperatura)  : null;
    this.pulso           = pulso           ? Number(pulso)        : null;
    this.oxigeno         = oxigeno         ? Number(oxigeno)      : null;
    this.usuarioOperador = Number(usuarioOperador);
  }
}
