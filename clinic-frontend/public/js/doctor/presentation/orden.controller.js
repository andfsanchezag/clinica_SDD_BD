// orden.controller.js — Presentación: Órdenes médicas

import { guardPage }              from '../../shared/infrastructure/router.guard.js';
import { showBanner }             from '../../shared/components/result.banner.js';
import { http }                   from '../../shared/infrastructure/http.client.js';
import { registrarOrdenUseCase }  from '../application/registrar.orden.usecase.js';
import { agregarDetalleUseCase }  from '../application/agregar.detalle.usecase.js';

if (!guardPage('medico')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', async () => {
  await cargarSelectTipoOrden();

  // Toggle especialidad según checkbox
  const chkEsp = document.getElementById('chk-requiere-esp');
  const divEsp = document.getElementById('div-especialidad');
  chkEsp?.addEventListener('change', () => {
    divEsp?.classList.toggle('d-none', !chkEsp.checked);
    const inp = divEsp?.querySelector('input,select');
    if (inp) inp.required = chkEsp.checked;
  });

  // ── Registrar orden ───────────────────────────────────
  const formOrden = document.getElementById('form-orden');
  const btnOrden  = document.getElementById('btn-registrar-orden');

  formOrden?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formOrden.reportValidity()) return;
    btnOrden.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formOrden));
      if (!chkEsp?.checked) delete data.especialidadId;
      const res = await registrarOrdenUseCase(data);
      showBanner('banner-ordenes', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formOrden.reset();
    } catch (err) {
      showBanner('banner-ordenes', false, err.message);
    } finally {
      btnOrden.disabled = false;
    }
  });

  // ── Agregar detalle ───────────────────────────────────
  const formDet = document.getElementById('form-detalle');
  const btnDet  = document.getElementById('btn-agregar-detalle');

  formDet?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formDet.reportValidity()) return;
    btnDet.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formDet));
      const res  = await agregarDetalleUseCase(data);
      showBanner('banner-ordenes', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formDet.reset();
    } catch (err) {
      showBanner('banner-ordenes', false, err.message);
    } finally {
      btnDet.disabled = false;
    }
  });
});

async function cargarSelectTipoOrden() {
  try {
    const tipos = await http.get('/api/soporte/cat-tipos-orden');
    const sel   = document.getElementById('sel-tipo-orden');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- Seleccione --</option>';
    tipos.forEach(t => {
      const opt = document.createElement('option');
      opt.value       = t.tipoOrdenId;
      opt.textContent = t.descripcion;
      sel.appendChild(opt);
    });
  } catch { /* silent */ }
}
