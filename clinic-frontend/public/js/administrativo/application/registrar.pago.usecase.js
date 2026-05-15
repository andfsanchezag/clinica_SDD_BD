import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function registrarPagoUseCase(formData) {
  const body = {
    facturaId:       Number(formData.facturaId),
    valorPagado:     Number(formData.valorPagado),
    tipoPago:        formData.tipoPago,
    referencia:      formData.referencia || null,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await administrativoApi.registrarPago(body);
}
