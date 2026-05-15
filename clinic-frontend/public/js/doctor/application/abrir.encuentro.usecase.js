import { doctorApi }       from '../infrastructure/doctor.api.js';
import { EncuentroEntity } from '../domain/encuentro.entity.js';
import { AuthStorage }     from '../../shared/infrastructure/auth.storage.js';

export async function abrirEncuentroUseCase(formData) {
  const entity = new EncuentroEntity({
    ...formData,
    medicoId:        AuthStorage.getUserId(),
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await doctorApi.abrirEncuentro(entity);
}
