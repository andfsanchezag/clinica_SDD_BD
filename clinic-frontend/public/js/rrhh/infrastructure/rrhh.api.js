import { http } from '../../shared/infrastructure/http.client.js';

const BASE = '/api/rrhh';

export const rrhhApi = {
  registrarEmpleado: (body) => http.post(`${BASE}/empleados`, body),
  crearUsuario:      (body) => http.post(`${BASE}/usuarios`,  body),
};
