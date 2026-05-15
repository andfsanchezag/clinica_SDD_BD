// enfermera.controller.js — Presentación: Signos vitales y Administración de medicamentos

import { guardPage }                     from '../../shared/infrastructure/router.guard.js';
import { renderNavbar }                  from '../../shared/components/navbar.js';
import { showBanner }                    from '../../shared/components/result.banner.js';
import { registrarSignosUseCase }        from '../application/registrar.signos.usecase.js';
import { registrarAdministracionUseCase } from '../application/registrar.administracion.usecase.js';

if (!guardPage('enfermeria')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', () => {
  renderNavbar('Panel Enfermería');

  // ── Signos vitales ────────────────────────────────────
  const formSignos = document.getElementById('form-signos');
  const btnSignos  = document.getElementById('btn-registrar-signos');

  formSignos?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formSignos.reportValidity()) return;
    btnSignos.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formSignos));
      const res  = await registrarSignosUseCase(data);
      showBanner('banner-enfermera', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formSignos.reset();
    } catch (err) {
      showBanner('banner-enfermera', false, err.message);
    } finally {
      btnSignos.disabled = false;
    }
  });

  // ── Administración de medicamentos ────────────────────
  const formMed = document.getElementById('form-medicamentos');
  const btnMed  = document.getElementById('btn-registrar-admin');

  formMed?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formMed.reportValidity()) return;
    btnMed.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formMed));
      const res  = await registrarAdministracionUseCase(data);
      showBanner('banner-enfermera', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formMed.reset();
    } catch (err) {
      showBanner('banner-enfermera', false, err.message);
    } finally {
      btnMed.disabled = false;
    }
  });
});
