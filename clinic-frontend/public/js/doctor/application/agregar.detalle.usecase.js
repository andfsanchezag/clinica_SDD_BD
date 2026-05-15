import { doctorApi }   from '../infrastructure/doctor.api.js';
import { AuthStorage } from '../../shared/infrastructure/auth.storage.js';

export async function agregarDetalleUseCase(formData) {
  const body = {
    ordenId:         Number(formData.ordenId),
    tipoDetalle:     formData.tipoDetalle,
    item:            formData.item        ? Number(formData.item)        : null,
    referenciaId:    formData.referenciaId ? Number(formData.referenciaId) : null,
    dosis:           formData.dosis       || null,
    duracion:        formData.duracion    || null,
    cantidad:        formData.cantidad    ? Number(formData.cantidad)    : null,
    frecuencia:      formData.frecuencia  || null,
    requiereEsp:     formData.requiereEsp === 'on' ? true : null,
    especialidadId:  formData.especialidadId ? Number(formData.especialidadId) : null,
    costo:           formData.costo       ? Number(formData.costo)       : null,
    usuarioOperador: AuthStorage.getUserId(),
  };
  return await doctorApi.agregarDetalle(body);
}
