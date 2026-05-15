import { enfermeraApi }       from '../infrastructure/enfermera.api.js';
import { AdministracionEntity } from '../domain/administracion.entity.js';
import { AuthStorage }          from '../../shared/infrastructure/auth.storage.js';

export async function registrarAdministracionUseCase(formData) {
  const entity = new AdministracionEntity({
    ...formData,
    enfermeroId:     AuthStorage.getUserId(),
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await enfermeraApi.registrarAdministracion(entity);
}
