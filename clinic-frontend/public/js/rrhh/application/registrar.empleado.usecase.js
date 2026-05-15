import { rrhhApi }       from '../infrastructure/rrhh.api.js';
import { EmpleadoEntity } from '../domain/empleado.entity.js';
import { AuthStorage }    from '../../shared/infrastructure/auth.storage.js';

export async function registrarEmpleadoUseCase(formData) {
  const entity = new EmpleadoEntity({
    ...formData,
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await rrhhApi.registrarEmpleado(entity);
}
