import { doctorApi }   from '../infrastructure/doctor.api.js';
import { OrdenEntity } from '../domain/orden.entity.js';
import { AuthStorage } from '../../shared/infrastructure/auth.storage.js';

export async function registrarOrdenUseCase(formData) {
  const entity = new OrdenEntity({
    ...formData,
    medicoId:        AuthStorage.getUserId(),
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await doctorApi.registrarOrden(entity);
}
