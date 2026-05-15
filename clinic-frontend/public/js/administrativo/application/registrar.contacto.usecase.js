import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function registrarContactoUseCase(formData) {
  const body = {
    pacienteId:      Number(formData.pacienteId),
    nombreCompleto:  formData.nombreCompleto,
    relacion:        formData.relacion,
    telefono:        formData.telefono,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await administrativoApi.registrarContacto(body);
}
