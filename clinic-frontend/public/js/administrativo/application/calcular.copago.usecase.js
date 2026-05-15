import { administrativoApi } from '../infrastructure/administrativo.api.js';

export async function calcularCopagoUseCase(pacienteId, tipoFactId) {
  return await administrativoApi.calcularCopago(Number(pacienteId), Number(tipoFactId));
}
