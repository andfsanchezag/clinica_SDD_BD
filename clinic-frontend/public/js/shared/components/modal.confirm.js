// modal.confirm.js — Modal Bootstrap de confirmación antes de DELETE

/**
 * Abre un modal de confirmación y ejecuta onConfirm si el usuario acepta.
 * Requiere que exista en el HTML el modal con id="modal-confirm".
 * Si no existe, lo inyecta automáticamente.
 *
 * @param {string}   message    Texto descriptivo de la acción a confirmar
 * @param {Function} onConfirm  Callback async ejecutado al confirmar
 */
export function openConfirmModal(message, onConfirm) {
  // Inyectar modal si aún no existe en el DOM
  if (!document.getElementById('modal-confirm')) {
    document.body.insertAdjacentHTML('beforeend', `
      <div class="modal fade" id="modal-confirm" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header bg-danger text-white">
              <h6 class="modal-title"><i class="bi bi-exclamation-triangle me-2"></i>Confirmar eliminación</h6>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="modal-confirm-body"></div>
            <div class="modal-footer">
              <button class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancelar</button>
              <button class="btn btn-danger btn-sm" id="modal-confirm-ok">Eliminar</button>
            </div>
          </div>
        </div>
      </div>`);
  }

  document.getElementById('modal-confirm-body').textContent = message;

  const modal = new bootstrap.Modal(document.getElementById('modal-confirm'));

  const btnOk = document.getElementById('modal-confirm-ok');
  // Clonar para eliminar listeners previos
  const newBtn = btnOk.cloneNode(true);
  btnOk.replaceWith(newBtn);

  newBtn.addEventListener('click', async () => {
    modal.hide();
    await onConfirm();
  });

  modal.show();
}
