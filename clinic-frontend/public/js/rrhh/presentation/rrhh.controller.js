// rrhh.controller.js — Presentación: Recursos Humanos

import { guardPage }               from '../../shared/infrastructure/router.guard.js';
import { renderNavbar }            from '../../shared/components/navbar.js';
import { showBanner }              from '../../shared/components/result.banner.js';
import { http }                    from '../../shared/infrastructure/http.client.js';
import { registrarEmpleadoUseCase } from '../application/registrar.empleado.usecase.js';
import { crearUsuarioUseCase }      from '../application/crear.usuario.usecase.js';

if (!guardPage('recursos_humanos')) throw new Error('Acceso denegado');

document.addEventListener('DOMContentLoaded', async () => {
  renderNavbar('Recursos Humanos');
  await Promise.all([cargarSelectTipoDoc(), cargarSelectRoles()]);

  // ── Registrar empleado ────────────────────────────────
  const formEmp = document.getElementById('form-empleado');
  const btnEmp  = document.getElementById('btn-reg-empleado');

  formEmp?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formEmp.reportValidity()) return;
    btnEmp.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formEmp));
      const res  = await registrarEmpleadoUseCase(data);
      showBanner('banner-rrhh', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formEmp.reset();
    } catch (err) {
      showBanner('banner-rrhh', false, err.message);
    } finally {
      btnEmp.disabled = false;
    }
  });

  // ── Crear usuario ─────────────────────────────────────
  const formUsr = document.getElementById('form-usuario');
  const btnUsr  = document.getElementById('btn-crear-usuario');

  formUsr?.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!formUsr.reportValidity()) return;
    btnUsr.disabled = true;
    try {
      const data = Object.fromEntries(new FormData(formUsr));
      const res  = await crearUsuarioUseCase(data);
      showBanner('banner-rrhh', res.exitoso, res.mensaje, res.idGenerado);
      if (res.exitoso) formUsr.reset();
    } catch (err) {
      showBanner('banner-rrhh', false, err.message);
    } finally {
      btnUsr.disabled = false;
    }
  });
});

async function cargarSelectTipoDoc() {
  try {
    const tipos = await http.get('/api/soporte/cat-tipos-doc');
    const sel   = document.getElementById('sel-tipo-doc-emp');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- Seleccione --</option>';
    tipos.forEach(t => {
      const opt = document.createElement('option');
      opt.value       = t.tipoDocId;
      opt.textContent = t.descripcion;
      sel.appendChild(opt);
    });
  } catch { /* silent */ }
}

async function cargarSelectRoles() {
  try {
    const roles = await http.get('/api/soporte/cat-roles');
    const sel   = document.getElementById('sel-rol-emp');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- Seleccione --</option>';
    roles.forEach(r => {
      const opt = document.createElement('option');
      opt.value       = r.rolId;
      opt.textContent = r.descripcion;
      sel.appendChild(opt);
    });
  } catch { /* silent */ }
}
