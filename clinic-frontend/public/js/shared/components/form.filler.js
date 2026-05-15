// form.filler.js — Rellena los campos de un <form> con un objeto de datos

/**
 * Para cada clave del objeto `data`, busca en `form` un elemento cuyo
 * `name` o `id` coincida y le asigna el valor correspondiente.
 *
 * Soporta: input, select, textarea, checkbox (boolean).
 *
 * @param {HTMLFormElement} form
 * @param {object}          data
 */
export function fillForm(form, data) {
  if (!form || !data) return;
  Object.entries(data).forEach(([key, value]) => {
    const el = form.querySelector(`[name="${key}"], #${CSS.escape(key)}`);
    if (!el) return;
    if (el.type === 'checkbox') {
      el.checked = Boolean(value);
    } else {
      el.value = value ?? '';
    }
  });
}

/**
 * Limpia todos los campos de un formulario.
 * @param {HTMLFormElement} form
 */
export function clearForm(form) {
  if (!form) return;
  form.reset();
}
