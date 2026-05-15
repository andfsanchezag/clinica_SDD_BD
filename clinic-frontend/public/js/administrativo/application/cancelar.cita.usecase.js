import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function cancelarCitaUseCase(citaId) {
  const body = {
    citaId:          Number(citaId),
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await administrativoApi.cancelarCita(body);
}
