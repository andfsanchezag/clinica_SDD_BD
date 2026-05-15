// auth.api.js — Adaptador HTTP para el endpoint de autenticación

import { http } from '../../shared/infrastructure/http.client.js';
import { AuthEntity } from '../domain/auth.entity.js';

/**
 * Llama a POST /api/auth/login y devuelve un AuthEntity.
 * @param {string} username
 * @param {string} password
 * @returns {Promise<AuthEntity>}
 */
export async function loginApi(username, password) {
  const data = await http.post('/api/auth/login', { username, password });
  return new AuthEntity({ token: data.token, rol: data.rol, usuario: data.usuario, usuarioId: data.usuarioId ?? null });
}
