// encuentro.controller.js — Presentación: Encuentros médicos (abrir / cerrar)

import { guardPage }               from '../../shared/infrastructure/router.guard.js';
import { renderNavbar }            from '../../shared/components/navbar.js';
import { showBanner }              from '../../shared/components/result.banner.js';
import { abrirEncuentroUseCase }   from '../application/abrir.encuentro.usecase.js';
import { cerrarEncuentroUseCase }  from '../application/cerrar.encuentro.usecase.js';

if (!guardPage('medico')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', () => {
  renderNavbar('Panel Médico');

  // ── Abrir encuentro ───────────────────────────────────
  const formAbrir = document.getElementById('form-abrir-encuentro');
  const btnAbrir  = document.getElementById('btn-abrir-encuentro');

  formAbrir?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formAbrir.reportValidity()) return;
    btnAbrir.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formAbrir));
      const res  = await abrirEncuentroUseCase(data);
      showBanner('banner-encuentros', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formAbrir.reset();
    } catch (err) {
      showBanner('banner-encuentros', false, err.message);
    } finally {
      btnAbrir.disabled = false;
    }
  });

  // ── Cerrar encuentro ──────────────────────────────────
  const formCerrar = document.getElementById('form-cerrar-encuentro');
  const btnCerrar  = document.getElementById('btn-cerrar-encuentro');

  formCerrar?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formCerrar.reportValidity()) return;
    btnCerrar.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formCerrar));
      const res  = await cerrarEncuentroUseCase(
        data.encuentroId, data.diagnostico, data.tratamiento, data.observaciones
      );
      showBanner('banner-encuentros', res.exitoso, res.mensaje);
      if (res.exitoso) formCerrar.reset();
    } catch (err) {
      showBanner('banner-encuentros', false, err.message);
    } finally {
      btnCerrar.disabled = false;
    }
  });
});
