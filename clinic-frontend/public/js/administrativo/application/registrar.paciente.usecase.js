import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { PacienteEntity }    from '../domain/paciente.entity.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function registrarPacienteUseCase(formData) {
  const entity = new PacienteEntity({ ...formData, usuarioOperador: AuthStorage.getUserId() });
  return await administrativoApi.registrarPaciente(entity);
}
