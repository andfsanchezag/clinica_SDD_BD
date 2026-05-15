// doctor.api.js — Adaptadores HTTP del módulo Doctor

import { http } from '../../shared/infrastructure/http.client.js';

const BASE = '/api/doctor';

export const doctorApi = {
  abrirEncuentro:   (body) => http.post(`${BASE}/encuentros`, body),
  cerrarEncuentro:  (body) => http.put(`${BASE}/encuentros/cerrar`, body),
  registrarOrden:   (body) => http.post(`${BASE}/ordenes`, body),
  agregarDetalle:   (body) => http.post(`${BASE}/ordenes/detalle`, body),
};
