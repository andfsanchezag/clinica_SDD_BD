// login.controller.js — Controlador de presentación para login

import { loginUseCase } from '../application/login.usecase.js';
import { AuthStorage }  from '../../shared/infrastructure/auth.storage.js';

const ROL_REDIRECT = {
  administrativo:     '/pages/administrativo.html',
  medico:             '/pages/doctor.html',
  enfermeria:         '/pages/enfermera.html',
  recursos_humanos:   '/pages/rrhh.html',
  soporte_informacion:'/pages/soporte.html',
};

function showError(msg) {
  const el = document.getElementById('login-error');
  if (!el) return;
  el.textContent = msg;
  el.classList.remove('d-none');
}

function hideError() {
  const el = document.getElementById('login-error');
  if (el) el.classList.add('d-none');
}

document.addEventListener('DOMContentLoaded', () => {
  // Si ya hay sesión activa → redirigir directamente
  if (!AuthStorage.isTokenExpired()) {
    const dest = ROL_REDIRECT[AuthStorage.getRol()];
    if (dest) { window.location.replace(dest); return; }
  }

  const form = document.getElementById('form-login');
  const btn  = document.getElementById('btn-login');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    hideError();

    if (!form.reportValidity()) return;

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    btn.disabled = true;

    try {
      const auth = await loginUseCase(username, password);
      AuthStorage.save({ token: auth.token, rol: auth.rol, usuario: auth.usuario });

      const dest = ROL_REDIRECT[auth.rol] ?? '/pages/login.html';
      window.location.replace(dest);
    } catch (err) {
      const msg = err.message.includes('conectar')
        ? 'No se pudo conectar al servidor'
        : 'Credenciales incorrectas';
      showError(msg);
    } finally {
      btn.disabled = false;
    }
  });
});
