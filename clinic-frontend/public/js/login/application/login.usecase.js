// login.usecase.js — Caso de uso: autenticar usuario

import { loginApi } from '../infrastructure/auth.api.js';

/**
 * Orquesta el login: llama a la API y retorna la entidad de autenticación.
 * @param {string} username
 * @param {string} password
 * @returns {Promise<import('../domain/auth.entity.js').AuthEntity>}
 */
export async function loginUseCase(username, password) {
  return await loginApi(username, password);
}
