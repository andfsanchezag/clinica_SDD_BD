// router.guard.js — Protección de páginas por token y rol

import { AuthStorage } from './auth.storage.js';

/**
 * Verifica que el usuario tenga un token válido y el rol correcto.
 * Si falla, limpia la sesión y redirige a login.
 * @param {string} rolRequerido
 * @returns {boolean} true si el acceso es permitido
 */
export function guardPage(rolRequerido) {
  if (AuthStorage.isTokenExpired()) {
    AuthStorage.clear();
    window.location.replace('/pages/login.html');
    return false;
  }
  if (AuthStorage.getRol() !== rolRequerido) {
    window.location.replace('/pages/login.html');
    return false;
  }
  return true;
}
