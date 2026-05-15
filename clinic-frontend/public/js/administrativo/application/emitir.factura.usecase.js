import { administrativoApi } from '../infrastructure/administrativo.api.js';
import { FacturaEntity }     from '../domain/factura.entity.js';
import { AuthStorage }       from '../../shared/infrastructure/auth.storage.js';

export async function emitirFacturaUseCase(formData) {
  const entity = new FacturaEntity({ ...formData, usuarioOperador: AuthStorage.getUserId() });
  return await administrativoApi.emitirFactura(entity);
}
