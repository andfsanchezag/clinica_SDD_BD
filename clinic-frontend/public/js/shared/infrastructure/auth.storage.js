// auth.storage.js — CRUD sobre localStorage para la sesión de clínica

export const AuthStorage = {

  save({ token, rol, usuario, usuarioId }) {
    localStorage.setItem('clinic_token',   token);
    localStorage.setItem('clinic_rol',     rol);
    localStorage.setItem('clinic_usuario', usuario);
    if (usuarioId != null) localStorage.setItem('clinic_usuario_id', usuarioId);
  },

  saveUserId(id) {
    localStorage.setItem('clinic_usuario_id', id);
  },

  getToken()   { return localStorage.getItem('clinic_token'); },
  getRol()     { return localStorage.getItem('clinic_rol'); },
  getUsuario() { return localStorage.getItem('clinic_usuario'); },
  getUserId()  { return Number(localStorage.getItem('clinic_usuario_id')); },

  clear() {
    ['clinic_token', 'clinic_rol', 'clinic_usuario', 'clinic_usuario_id']
      .forEach(k => localStorage.removeItem(k));
  },

  isTokenExpired() {
    const token = this.getToken();
    if (!token) return true;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return Date.now() >= payload.exp * 1000;
    } catch {
      return true;
    }
  },
};
