// result.banner.js — Alerta Bootstrap de éxito o error (SpResultadoResponse)

/**
 * Muestra una alerta en el contenedor indicado.
 * @param {string}  containerId  ID del elemento donde insertar la alerta
 * @param {boolean} success      true = verde, false = roja
 * @param {string}  message      Texto del mensaje
 * @param {number|null} idGenerado  ID generado (opcional)
 */
export function showBanner(containerId, success, message, idGenerado = null) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const type = success ? 'success' : 'danger';
  const icon = success ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill';
  const idText = (success && idGenerado) ? ` <small class="text-muted">(ID: ${idGenerado})</small>` : '';

  container.innerHTML = `
    <div class="alert alert-${type} alert-dismissible fade show result-banner d-flex align-items-center gap-2" role="alert">
      <i class="bi ${icon}"></i>
      <span>${message}${idText}</span>
      <button type="button" class="btn-close ms-auto" data-bs-dismiss="alert"></button>
    </div>`;

  // Auto-desaparecer después de 6 s en caso de éxito
  if (success) {
    setTimeout(() => {
      container.querySelector('.alert')?.remove();
    }, 6000);
  }
}

/**
 * Limpia el contenedor de banner.
 * @param {string} containerId
 */
export function clearBanner(containerId) {
  const container = document.getElementById(containerId);
  if (container) container.innerHTML = '';
}
