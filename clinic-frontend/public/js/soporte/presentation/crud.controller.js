// crud.controller.js — Controlador genérico CRUD para el módulo Soporte

import { guardPage }           from '../../shared/infrastructure/router.guard.js';
import { renderNavbar }        from '../../shared/components/navbar.js';
import { showBanner }          from '../../shared/components/result.banner.js';
import { openConfirmModal }    from '../../shared/components/modal.confirm.js';
import { fillForm, clearForm } from '../../shared/components/form.filler.js';
import { crudUseCaseFactory }  from '../application/crud.usecase.factory.js';

if (!guardPage('soporte_informacion')) throw new Error('Acceso denegado');

// ── Definición de recursos ────────────────────────────────────────────────────
const RECURSOS = [
  // Catálogos (PK Short)
  { id: 'cat-roles',                 label: 'Roles',                  pkField: 'rolId',              campos: ['descripcion'] },
  { id: 'cat-tipos-doc',             label: 'Tipos de documento',     pkField: 'tipoDocId',          campos: ['descripcion'] },
  { id: 'cat-generos',               label: 'Géneros',                pkField: 'generoId',           campos: ['descripcion'] },
  { id: 'cat-estados-cita',          label: 'Estados de cita',        pkField: 'estadoCitaId',       campos: ['descripcion'] },
  { id: 'cat-prioridades',           label: 'Prioridades',            pkField: 'prioridadId',        campos: ['descripcion'] },
  { id: 'cat-estados-seguro',        label: 'Estados de seguro',      pkField: 'estadoSeguroId',     campos: ['descripcion'] },
  { id: 'cat-tipos-orden',           label: 'Tipos de orden',         pkField: 'tipoOrdenId',        campos: ['descripcion'] },
  { id: 'cat-tipos-detalle-orden',   label: 'Tipos de detalle orden', pkField: 'tipoDetalleId',      campos: ['descripcion'] },
  { id: 'cat-tipos-facturacion',     label: 'Tipos de facturación',   pkField: 'tipoFactId',         campos: ['descripcion'] },
  { id: 'cat-estados-especialidad',  label: 'Estados especialidad',   pkField: 'estadoEspId',        campos: ['descripcion'] },
  { id: 'config-facturacion',        label: 'Configuración facturación', pkField: 'configId',        campos: ['porcentajeCopago', 'tarifaBase', 'vigenciaDesde', 'vigenciaHasta'] },
  // Maestras (PK Integer)
  { id: 'especialidades',    label: 'Especialidades',     pkField: 'especialidadId', campos: ['nombre', 'descripcion', 'estadoEspId'] },
  { id: 'empleados',         label: 'Empleados',          pkField: 'empleadoId',     campos: ['cedula', 'nombreCompleto', 'cargo', 'correo', 'telefono'] },
  { id: 'usuarios',          label: 'Usuarios',           pkField: 'usuarioId',      campos: ['username', 'rolId'] },
  { id: 'medicos-perfil',    label: 'Perfiles médicos',   pkField: 'medicoId',       campos: ['empleadoId', 'especialidadId', 'numeroLicencia', 'turno'] },
  { id: 'enfermeros-perfil', label: 'Perfiles enfermeros', pkField: 'enfermeroId',   campos: ['empleadoId', 'turno'] },
];

// ── Estado global ─────────────────────────────────────────────────────────────
let currentRecurso = RECURSOS[0];
let currentUseCase = crudUseCaseFactory(currentRecurso.id);
let editingId      = null;

// ── DOM references ────────────────────────────────────────────────────────────
const tableHead   = document.getElementById('crud-table-head');
const tableBody   = document.getElementById('crud-table-body');
const formCrud    = document.getElementById('form-crud');
const formTitle   = document.getElementById('form-crud-title');
const btnGuardar  = document.getElementById('btn-guardar');
const btnCancelar = document.getElementById('btn-cancelar-edicion');
const bannerEl    = 'banner-soporte';

document.addEventListener('DOMContentLoaded', async () => {
  renderNavbar('Soporte de Información');
  renderSidebar();
  await seleccionarRecurso(RECURSOS[0]);
});

// ── Sidebar ───────────────────────────────────────────────────────────────────
function renderSidebar() {
  const catalogos = RECURSOS.slice(0, 11);
  const maestras  = RECURSOS.slice(11);
  const sidebar   = document.getElementById('sidebar-recursos');

  function buildItem(r) {
    const a = document.createElement('a');
    a.href = '#';
    a.className = 'list-group-item list-group-item-action list-group-item-sm py-1 px-2';
    a.dataset.rid = r.id;
    a.textContent = r.label;
    a.addEventListener('click', async (e) => {
      e.preventDefault();
      sidebar.querySelectorAll('a').forEach(x => x.classList.remove('active'));
      a.classList.add('active');
      await seleccionarRecurso(r);
    });
    return a;
  }

  const h1 = document.createElement('div');
  h1.className = 'sidebar-group-label';
  h1.textContent = 'Catálogos';
  sidebar.appendChild(h1);
  catalogos.forEach(r => sidebar.appendChild(buildItem(r)));

  const h2 = document.createElement('div');
  h2.className = 'sidebar-group-label mt-2';
  h2.textContent = 'Tablas maestras';
  sidebar.appendChild(h2);
  maestras.forEach(r => sidebar.appendChild(buildItem(r)));

  // Activar primero
  sidebar.querySelector('a')?.classList.add('active');
}

// ── Seleccionar recurso ───────────────────────────────────────────────────────
async function seleccionarRecurso(r) {
  currentRecurso = r;
  currentUseCase = crudUseCaseFactory(r.id);
  editingId      = null;
  formTitle.textContent = `Nuevo: ${r.label}`;
  btnGuardar.textContent = 'Crear';
  renderForm(r);
  await cargarTabla();
}

// ── Render form ───────────────────────────────────────────────────────────────
function renderForm(r) {
  const body = document.getElementById('form-fields');
  body.innerHTML = '';
  r.campos.forEach(campo => {
    const div   = document.createElement('div');
    div.className = 'mb-2';
    const label = document.createElement('label');
    label.className = 'form-label';
    label.textContent = campo;
    const input = document.createElement('input');
    input.type      = 'text';
    input.name      = campo;
    input.className = 'form-control form-control-sm';
    div.appendChild(label);
    div.appendChild(input);
    body.appendChild(div);
  });
}

// ── Cargar tabla ──────────────────────────────────────────────────────────────
async function cargarTabla() {
  tableBody.innerHTML = '<tr><td colspan="99" class="text-center text-muted py-3"><span class="spinner-border spinner-border-sm"></span> Cargando...</td></tr>';
  try {
    const items = await currentUseCase.listar();
    renderTabla(items);
  } catch (err) {
    showBanner(bannerEl, false, err.message);
    tableBody.innerHTML = '<tr><td colspan="99" class="text-danger text-center">Error al cargar</td></tr>';
  }
}

// ── Render tabla ──────────────────────────────────────────────────────────────
function renderTabla(items) {
  if (!items || items.length === 0) {
    tableBody.innerHTML = '<tr><td colspan="99" class="text-center text-muted">Sin registros</td></tr>';
    tableHead.innerHTML = '';
    return;
  }
  const keys = Object.keys(items[0]);
  // Head
  tableHead.innerHTML = '<tr>' + keys.map(k => `<th>${k}</th>`).join('') + '<th>Acciones</th></tr>';
  // Body
  tableBody.innerHTML = '';
  items.forEach(item => {
    const tr  = document.createElement('tr');
    keys.forEach(k => {
      const td = document.createElement('td');
      td.textContent = item[k] ?? '';
      tr.appendChild(td);
    });
    // Acciones
    const tdAcc = document.createElement('td');
    tdAcc.innerHTML = `
      <button class="btn btn-outline-primary btn-sm me-1 btn-editar" data-id="${item[currentRecurso.pkField]}">
        <i class="bi bi-pencil"></i>
      </button>
      <button class="btn btn-outline-danger btn-sm btn-borrar" data-id="${item[currentRecurso.pkField]}">
        <i class="bi bi-trash"></i>
      </button>
    `;
    tdAcc.querySelector('.btn-editar').addEventListener('click', () => iniciarEdicion(item));
    tdAcc.querySelector('.btn-borrar').addEventListener('click', () => confirmarBorrar(item[currentRecurso.pkField]));
    tr.appendChild(tdAcc);
    tableBody.appendChild(tr);
  });
}

// ── Guardar (crear/actualizar) ────────────────────────────────────────────────
formCrud?.addEventListener('submit', async (e) => {
  e.preventDefault();
  if (!formCrud.reportValidity()) return;
  btnGuardar.disabled = true;
  try {
    const data = Object.fromEntries(new FormData(formCrud));
    let res;
    if (editingId !== null) {
      res = await currentUseCase.actualizar(editingId, data);
    } else {
      res = await currentUseCase.crear(data);
    }
    showBanner(bannerEl, res?.exitoso ?? true, res?.mensaje ?? 'Operación exitosa', res?.idGenerado);
    cancelarEdicion();
    await cargarTabla();
  } catch (err) {
    showBanner(bannerEl, false, err.message);
  } finally {
    btnGuardar.disabled = false;
  }
});

// ── Editar ────────────────────────────────────────────────────────────────────
function iniciarEdicion(item) {
  editingId = item[currentRecurso.pkField];
  formTitle.textContent  = `Editar: ${currentRecurso.label} [${editingId}]`;
  btnGuardar.textContent = 'Actualizar';
  btnCancelar.classList.remove('d-none');
  fillForm(formCrud, item);
}

btnCancelar?.addEventListener('click', cancelarEdicion);

function cancelarEdicion() {
  editingId = null;
  formTitle.textContent  = `Nuevo: ${currentRecurso.label}`;
  btnGuardar.textContent = 'Crear';
  btnCancelar.classList.add('d-none');
  clearForm(formCrud);
}

// ── Borrar ────────────────────────────────────────────────────────────────────
function confirmarBorrar(id) {
  openConfirmModal(
    `¿Eliminar el registro con ID <strong>${id}</strong> de <em>${currentRecurso.label}</em>?`,
    async () => {
      try {
        await currentUseCase.eliminar(id);
        showBanner(bannerEl, true, 'Registro eliminado correctamente');
        await cargarTabla();
      } catch (err) {
        showBanner(bannerEl, false, err.message);
      }
    }
  );
}
