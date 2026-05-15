// http.client.js — Wrapper sobre fetch con autenticación JWT

import { AuthStorage } from './auth.storage.js';

const BASE_URL = 'http://localhost:8083';

async function request(method, path, body = null) {
  const headers = { 'Content-Type': 'application/json' };
  const token = AuthStorage.getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const options = { method, headers };
  if (body !== null) options.body = JSON.stringify(body);

  let res;
  try {
    res = await fetch(`${BASE_URL}${path}`, options);
  } catch {
    throw new Error('No se pudo conectar al servidor');
  }

  // Token vencido o sin permiso → volver al login
  if (res.status === 401 || res.status === 403) {
    AuthStorage.clear();
    window.location.replace('/pages/login.html');
    throw new Error('Sesión expirada');
  }

  // Sin contenido (DELETE 204)
  if (res.status === 204) return null;

  const data = await res.json().catch(() => ({}));

  // Error de regla de negocio (422) o error de validación (400)
  if (!res.ok) throw new Error(data.mensaje ?? data.message ?? `Error ${res.status}`);

  return data;
}

export const http = {
  get:    (path)        => request('GET',    path),
  post:   (path, body)  => request('POST',   path, body),
  put:    (path, body)  => request('PUT',    path, body),
  delete: (path)        => request('DELETE', path),
};
