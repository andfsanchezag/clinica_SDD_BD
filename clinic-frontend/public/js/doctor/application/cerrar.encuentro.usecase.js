import { doctorApi }   from '../infrastructure/doctor.api.js';
import { AuthStorage } from '../../shared/infrastructure/auth.storage.js';

export async function cerrarEncuentroUseCase(encuentroId, diagnostico, tratamiento, observaciones) {
  const body = {
    encuentroId:     Number(encuentroId),
    diagnostico,
    tratamiento,
    observaciones:   observaciones || null,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await doctorApi.cerrarEncuentro(body);
}
