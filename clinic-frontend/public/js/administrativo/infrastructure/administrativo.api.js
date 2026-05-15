// administrativo.api.js — Adaptadores HTTP del módulo Administrativo

import { http } from '../../shared/infrastructure/http.client.js';

const BASE = '/api/administrativo';

export const administrativoApi = {
  registrarPaciente:   (body) => http.post(`${BASE}/pacientes`, body),
  registrarContacto:   (body) => http.post(`${BASE}/pacientes/contactos-emergencia`, body),
  registrarSeguro:     (body) => http.post(`${BASE}/pacientes/seguros`, body),

  programarCita:       (body) => http.post(`${BASE}/citas`, body),
  reprogramarCita:     (body) => http.put(`${BASE}/citas/reprogramar`, body),
  cancelarCita:        (body) => http.put(`${BASE}/citas/cancelar`, body),

  calcularCopago:      (pacienteId, tipoFactId) =>
    http.get(`${BASE}/facturacion/copago?pacienteId=${pacienteId}&tipoFactId=${tipoFactId}`),
  emitirFactura:       (body) => http.post(`${BASE}/facturacion/facturas`, body),
  registrarPago:       (body) => http.post(`${BASE}/facturacion/pagos`, body),
};
