import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function reprogramarCitaUseCase(citaId, nuevaFecha) {
  const body = {
    citaId:          Number(citaId),
    nuevaFecha:      nuevaFecha,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await administrativoApi.reprogramarCita(body);
}
