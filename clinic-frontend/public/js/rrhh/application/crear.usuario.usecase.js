import { rrhhApi }       from '../infrastructure/rrhh.api.js';
import { UsuarioEntity } from '../domain/usuario.entity.js';
import { AuthStorage }   from '../../shared/infrastructure/auth.storage.js';

export async function crearUsuarioUseCase({ empleadoId, username, password, confirmPassword }) {
  if (!password || password.length < 8) {
    throw new Error('La contraseña debe tener al menos 8 caracteres');
  }
  if (password !== confirmPassword) {
    throw new Error('Las contraseñas no coinciden');
  }
  // Hash client-side using bcryptjs loaded via CDN as window.dcodeIO?.bcrypt
  const bcrypt = window.dcodeIO?.bcrypt ?? window.bcrypt;
  if (!bcrypt) throw new Error('bcryptjs no está disponible');
  const contrasenaHash = bcrypt.hashSync(password, 10);

  const entity = new UsuarioEntity({
    empleadoId,
    codigoUsuario:   username,
    contrasenaHash,
    usuarioOperador: AuthStorage.getUserId(),
  });
  return await rrhhApi.crearUsuario(entity);
}
