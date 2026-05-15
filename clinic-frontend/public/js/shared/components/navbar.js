// navbar.js — Inyecta la barra de navegación Sura en la página actual

import { AuthStorage } from '../infrastructure/auth.storage.js';

/**
 * Renderiza el navbar en el elemento con id="navbar-container".
 * @param {string} title  Título de la sección actual
 */
export function renderNavbar(title = 'Clínica IPS') {
  const container = document.getElementById('navbar-container');
  if (!container) return;

  const usuario = AuthStorage.getUsuario() ?? '';

  container.innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-sura">
      <div class="container-fluid">
        <span class="navbar-brand">
          <i class="bi bi-hospital me-2"></i>${title}
        </span>
        <div class="d-flex align-items-center gap-2">
          <span class="navbar-text me-2">
            <i class="bi bi-person-circle me-1"></i>${usuario}
          </span>
          <button id="btn-logout" class="btn-logout">
            <i class="bi bi-box-arrow-right me-1"></i>Salir
          </button>
        </div>
      </div>
    </nav>`;

  document.getElementById('btn-logout').addEventListener('click', () => {
    AuthStorage.clear();
    window.location.replace('/pages/login.html');
  });
}
