// crud.usecase.factory.js — Fábrica de casos de uso CRUD genéricos

import { soporteApi }  from '../infrastructure/soporte.api.js';
import { RecursoEntity } from '../domain/recurso.entity.js';

/**
 * @param {string} recurso  - nombre del recurso (ej: 'cat-roles')
 * @returns {{ listar, buscar, crear, actualizar, eliminar }}
 */
export function crudUseCaseFactory(recurso) {
  return {
    listar:     ()        => soporteApi.getAll(recurso),
    buscar:     (id)      => soporteApi.getById(recurso, id),
    crear:      (data)    => soporteApi.create(recurso, new RecursoEntity(data)),
    actualizar: (id, data) => soporteApi.update(recurso, id, new RecursoEntity(data)),
    eliminar:   (id)      => soporteApi.remove(recurso, id),
  };
}
