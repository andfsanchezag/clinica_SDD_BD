import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { CitaEntity }        from '../domain/cita.entity.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function programarCitaUseCase(formData) {
  const entity = new CitaEntity({ ...formData, usuarioOperador: AuthStorage.getUserId() });
  return await administrativoApi.programarCita(entity);
}
