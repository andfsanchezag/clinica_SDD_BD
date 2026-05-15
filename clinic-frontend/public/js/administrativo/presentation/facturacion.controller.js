// facturacion.controller.js — Presentación: Facturación (copago, factura, pago)

import { guardPage }             from '../../shared/infrastructure/router.guard.js';
import { showBanner }            from '../../shared/components/result.banner.js';
import { http }                  from '../../shared/infrastructure/http.client.js';
import { calcularCopagoUseCase } from '../application/calcular.copago.usecase.js';
import { emitirFacturaUseCase }  from '../application/emitir.factura.usecase.js';
import { registrarPagoUseCase }  from '../application/registrar.pago.usecase.js';

if (!guardPage('administrativo')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', async () => {
  await cargarSelectTipoFact();

  // ── Calcular copago ───────────────────────────────────
  const btnCopago = document.getElementById('btn-calcular-copago');
  btnCopago?.addEventListener('click', async () => {
    const pacienteId = document.getElementById('cop-paciente-id')?.value;
    const tipoFactId = document.getElementById('cop-tipo-fact')?.value;
    if (!pacienteId || !tipoFactId) {
      showBanner('banner-facturacion', false, 'Ingrese Paciente ID y Tipo de facturación');
      return;
    }
    btnCopago.disabled = true;
    try {
      const res = await calcularCopagoUseCase(pacienteId, tipoFactId);
      const panel = document.getElementById('copago-result');
      if (panel) {
        panel.classList.remove('d-none');
        document.getElementById('cop-valor').textContent  = `$${res.valorCopago?.toFixed(2) ?? 0}`;
        document.getElementById('cop-exento').textContent = res.exento ? 'Sí' : 'No';
        document.getElementById('cop-msg').textContent    = res.mensaje ?? '';
      }
    } catch (err) {
      showBanner('banner-facturacion', false, err.message);
    } finally {
      btnCopago.disabled = false;
    }
  });

  // ── Emitir factura ────────────────────────────────────
  const formFact = document.getElementById('form-factura');
  const btnFact  = document.getElementById('btn-emitir-factura');

  formFact?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formFact.reportValidity()) return;
    btnFact.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formFact));
      const res  = await emitirFacturaUseCase(data);
      showBanner('banner-facturacion', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formFact.reset();
    } catch (err) {
      showBanner('banner-facturacion', false, err.message);
    } finally {
      btnFact.disabled = false;
    }
  });

  // ── Registrar pago ────────────────────────────────────
  const formPago = document.getElementById('form-pago');
  const btnPago  = document.getElementById('btn-registrar-pago');

  formPago?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formPago.reportValidity()) return;
    btnPago.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formPago));
      const res  = await registrarPagoUseCase(data);
      showBanner('banner-facturacion', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formPago.reset();
    } catch (err) {
      showBanner('banner-facturacion', false, err.message);
    } finally {
      btnPago.disabled = false;
    }
  });
});

async function cargarSelectTipoFact() {
  try {
    const tipos = await http.get('/api/soporte/cat-tipos-facturacion');
    ['sel-tipo-fact', 'cop-tipo-fact'].forEach(id => {
      const sel = document.getElementById(id);
      if (!sel) return;
      sel.innerHTML = '<option value="">-- Seleccione --</option>';
      tipos.forEach(t => {
        const opt = document.createElement('option');
        opt.value       = t.tipoFactId;
        opt.textContent = t.descripcion;
        sel.appendChild(opt);
      });
    });
  } catch { /* silent */ }
}
