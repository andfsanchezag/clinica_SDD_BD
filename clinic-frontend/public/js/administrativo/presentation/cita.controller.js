// cita.controller.js — Presentación: Citas (programar, reprogramar, cancelar)

import { guardPage }                from '../../shared/infrastructure/router.guard.js';
import { showBanner }               from '../../shared/components/result.banner.js';
import { http }                     from '../../shared/infrastructure/http.client.js';
import { programarCitaUseCase }     from '../application/programar.cita.usecase.js';
import { reprogramarCitaUseCase }   from '../application/reprogramar.cita.usecase.js';
import { cancelarCitaUseCase }      from '../application/cancelar.cita.usecase.js';

if (!guardPage('administrativo')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', async () => {
  await cargarSelectPrioridad();

  // ── Programar cita ────────────────────────────────────
  const formProg = document.getElementById('form-programar-cita');
  const btnProg  = document.getElementById('btn-programar-cita');

  formProg?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formProg.reportValidity()) return;
    btnProg.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formProg));
      const res  = await programarCitaUseCase(data);
      showBanner('banner-citas', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formProg.reset();
    } catch (err) {
      showBanner('banner-citas', false, err.message);
    } finally {
      btnProg.disabled = false;
    }
  });

  // ── Reprogramar cita ──────────────────────────────────
  const formRep  = document.getElementById('form-reprogramar-cita');
  const btnRep   = document.getElementById('btn-reprogramar-cita');
  const btnBusca = document.getElementById('btn-buscar-cita-rep');

  btnBusca?.addEventListener('click', async () => {
    const citaId = document.getElementById('rep-cita-id')?.value;
    if (!citaId) return;
    try {
      // Se usa el listado del endpoint de soporte para buscar datos básicos de cita
      const cita = await http.get(`/api/soporte/citas/${citaId}`);
      if (cita?.fechaHora) {
        const fechaEl = document.getElementById('rep-nueva-fecha');
        if (fechaEl) fechaEl.value = cita.fechaHora.substring(0, 16); // datetime-local
      }
    } catch (err) {
      showBanner('banner-citas', false, err.message);
    }
  });

  formRep?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formRep.reportValidity()) return;
    btnRep.disabled = true;
    try {
      const citaId    = document.getElementById('rep-cita-id').value;
      const nuevaFecha = document.getElementById('rep-nueva-fecha').value + ':00'; // add seconds
      const res = await reprogramarCitaUseCase(citaId, nuevaFecha);
      showBanner('banner-citas', res.exitoso, res.mensaje);
      if (res.exitoso) formRep.reset();
    } catch (err) {
      showBanner('banner-citas', false, err.message);
    } finally {
      btnRep.disabled = false;
    }
  });

  // ── Cancelar cita ─────────────────────────────────────
  const formCan = document.getElementById('form-cancelar-cita');
  const btnCan  = document.getElementById('btn-cancelar-cita');

  formCan?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formCan.reportValidity()) return;
    btnCan.disabled = true;
    try {
      const citaId = document.getElementById('can-cita-id').value;
      const res    = await cancelarCitaUseCase(citaId);
      showBanner('banner-citas', res.exitoso, res.mensaje);
      if (res.exitoso) formCan.reset();
    } catch (err) {
      showBanner('banner-citas', false, err.message);
    } finally {
      btnCan.disabled = false;
    }
  });
});

async function cargarSelectPrioridad() {
  try {
    const prioridades = await http.get('/api/soporte/cat-prioridades');
    const sel = document.getElementById('sel-prioridad');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- Seleccione --</option>';
    prioridades.forEach(p => {
      const opt = document.createElement('option');
      opt.value       = p.prioridadId;
      opt.textContent = p.descripcion;
      sel.appendChild(opt);
    });
  } catch { /* silent */ }
}
