// soporte.api.js — Adaptador CRUD genérico para recursos de soporte

import { http } from '../../shared/infrastructure/http.client.js';

const BASE = '/api/soporte';

export const soporteApi = {
  getAll:    (recurso)       => http.get(`${BASE}/${recurso}`),
  getById:   (recurso, id)   => http.get(`${BASE}/${recurso}/${id}`),
  create:    (recurso, body) => http.post(`${BASE}/${recurso}`, body),
  update:    (recurso, id, body) => http.put(`${BASE}/${recurso}/${id}`, body),
  remove:    (recurso, id)   => http.delete(`${BASE}/${recurso}/${id}`),
};
