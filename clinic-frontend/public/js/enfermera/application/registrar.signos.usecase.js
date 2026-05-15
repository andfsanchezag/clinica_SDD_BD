import { enfermeraApi }    from '../infrastructure/enfermera.api.js';
import { SignoVitalEntity } from '../domain/signo.vital.entity.js';
import { AuthStorage }      from '../../shared/infrastructure/auth.storage.js';

export async function registrarSignosUseCase(formData) {
  const entity = new SignoVitalEntity({
    ...formData,
    enfermeroId:     AuthStorage.getUserId(),
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await enfermeraApi.registrarSignos(entity);
}
