// paciente.controller.js — Presentación: Pacientes (registrar paciente, contacto, seguro)

import { guardPage }                  from '../../shared/infrastructure/router.guard.js';
import { renderNavbar }               from '../../shared/components/navbar.js';
import { showBanner }                 from '../../shared/components/result.banner.js';
import { http }                       from '../../shared/infrastructure/http.client.js';
import { registrarPacienteUseCase }   from '../application/registrar.paciente.usecase.js';
import { registrarContactoUseCase }   from '../application/registrar.contacto.usecase.js';
import { registrarSeguroUseCase }     from '../application/registrar.seguro.usecase.js';

if (!guardPage('administrativo')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', async () => {
  renderNavbar('Módulo Administrativo');
  await cargarSelects();

  // ── Registrar paciente ────────────────────────────────
  const formPac = document.getElementById('form-paciente');
  const btnPac  = document.getElementById('btn-reg-paciente');

  formPac?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formPac.reportValidity()) return;
    btnPac.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formPac));
      const res  = await registrarPacienteUseCase(data);
      showBanner('banner-paciente', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formPac.reset();
    } catch (err) {
      showBanner('banner-paciente', false, err.message);
    } finally {
      btnPac.disabled = false;
    }
  });

  // ── Registrar contacto de emergencia ─────────────────
  const formCon = document.getElementById('form-contacto');
  const btnCon  = document.getElementById('btn-reg-contacto');

  formCon?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formCon.reportValidity()) return;
    btnCon.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formCon));
      const res  = await registrarContactoUseCase(data);
      showBanner('banner-paciente', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formCon.reset();
    } catch (err) {
      showBanner('banner-contacto', false, err.message);
    } finally {
      btnCon.disabled = false;
    }
  });

  // ── Registrar seguro médico ───────────────────────────
  const formSeg = document.getElementById('form-seguro');
  const btnSeg  = document.getElementById('btn-reg-seguro');

  formSeg?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formSeg.reportValidity()) return;
    btnSeg.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formSeg));
      const res  = await registrarSeguroUseCase(data);
      showBanner('banner-paciente', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formSeg.reset();
    } catch (err) {
      showBanner('banner-seguro', false, err.message);
    } finally {
      btnSeg.disabled = false;
    }
  });
});

async function cargarSelects() {
  try {
    const [tiposDocs, generos, estadosSeguros] = await Promise.all([
      http.get('/api/soporte/cat-tipos-doc'),
      http.get('/api/soporte/cat-generos'),
      http.get('/api/soporte/cat-estados-seguro'),
    ]);
    llenarSelect('sel-tipo-doc',      tiposDocs,      'tipoDocId',      'descripcion');
    llenarSelect('sel-genero',        generos,        'generoId',       'descripcion');
    llenarSelect('sel-estado-seguro', estadosSeguros, 'estadoSeguroId', 'descripcion');
  } catch { /* si falla, los selects quedan vacíos */ }
}

function llenarSelect(id, items, valueKey, labelKey) {
  const sel = document.getElementById(id);
  if (!sel) return;
  sel.innerHTML = '<option value="">-- Seleccione --</option>';
  items.forEach(item => {
    const opt = document.createElement('option');
    opt.value       = item[valueKey];
    opt.textContent = item[labelKey];
    sel.appendChild(opt);
  });
}
