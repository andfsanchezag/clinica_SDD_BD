import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function registrarSeguroUseCase(formData) {
  const body = {
    pacienteId:      Number(formData.pacienteId),
    compania:        formData.compania,
    numeroPoliza:    formData.numeroPoliza,
    estadoSeguroId:  Number(formData.estadoSeguroId),
    fechaVigencia:   formData.fechaVigencia,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await administrativoApi.registrarSeguro(body);
}
