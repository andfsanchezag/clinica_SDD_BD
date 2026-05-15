import { http } from '../../shared/infrastructure/http.client.js';

const BASE = '/api/enfermera';

export const enfermeraApi = {
  registrarSignos:         (body) => http.post(`${BASE}/signos-vitales`, body),
  registrarAdministracion: (body) => http.post(`${BASE}/administracion-medicamentos`, body),
};
