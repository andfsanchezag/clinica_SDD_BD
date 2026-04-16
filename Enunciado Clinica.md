Aquí tienes la transcripción del documento a formato **Markdown (MD)**, organizada y limpia para que puedas usarla directamente:

---

# Clínica_IPS_2024

## FORMATO DE LA EVALUACIÓN DE SEGUIMIENTO O TRABAJOS INDEPENDIENTES

* **Código:** FO-DOC-12
* **Versión:** 02
* **Fecha de aprobación:** Julio 26 de 2022
* **Páginas:** 1 - 9
* **Estado:** COPIA CONTROLADA

---

# Actividad

## Funcionamiento de la Aplicación de Gestión de Información de una Clínica

---

## Descripción

La aplicación de gestión de información de la clínica tiene como objetivo principal facilitar el manejo eficiente de los datos relacionados con los pacientes y el personal.

Está diseñada para múltiples roles:

* Personal administrativo
* Soporte de información
* Enfermeras
* Médicos
* Recursos humanos

---

# Descripción de Roles

---

## 1. Recursos Humanos

Responsables de:

* Crear y eliminar usuarios
* Administrar accesos y permisos
* Gestionar información del personal

### Datos del empleado

| Campo               | Descripción                             |
| ------------------- | --------------------------------------- |
| Nombre completo     | Texto                                   |
| Número de cédula    | Único en el sistema                     |
| Correo electrónico  | Debe ser válido                         |
| Número de teléfono  | 1 a 10 dígitos                          |
| Fecha de nacimiento | Formato DD/MM/YYYY (máx. 150 años)      |
| Dirección           | Máximo 30 caracteres                    |
| Rol                 | Médico, Enfermera, Administrativo, etc. |

⚠️ **Restricción:**
NO pueden visualizar información de pacientes, medicamentos ni procedimientos.

---

## 2. Personal Administrativo

Funciones:

* Registrar pacientes
* Programar citas
* Gestionar facturación y seguros

### Credenciales

| Campo      | Regla                                                           |
| ---------- | --------------------------------------------------------------- |
| Usuario    | Único, máx. 15 caracteres, solo letras y números                |
| Contraseña | 1 mayúscula, 1 número, 1 carácter especial, mínimo 8 caracteres |

### Datos del paciente

| Campo               | Descripción               |
| ------------------- | ------------------------- |
| Identificación      | Única (cédula)            |
| Nombre completo     | Texto                     |
| Fecha de nacimiento | Máx. 150 años             |
| Género              | Masculino, femenino, otro |
| Dirección           | Texto                     |
| Teléfono            | 10 dígitos                |
| Correo              | Opcional                  |

---

### Información adicional

#### Contacto de emergencia (solo uno)

* Nombre completo
* Relación
* Teléfono (10 dígitos)

#### Seguro médico (solo uno)

* Compañía
* Número de póliza
* Estado (booleano)
* Vigencia (dd/mm/yyyy)

---

### Facturación

Debe incluir:

* Nombre, edad y cédula del paciente
* Médico tratante
* Compañía de seguros
* Número de póliza
* Días de vigencia
* Fecha de finalización

---

### Reglas de facturación

* Si hay medicamentos → incluir nombre, costo y dosis
* Si hay procedimientos → incluir nombre
* Si hay ayudas diagnósticas → incluir examen

#### Cobros

* Póliza activa → copago de **$50.000**, resto aseguradora
* Si supera **$1.000.000 en copagos al año** → no paga más copago
* Sin póliza → paga el 100%

---

## 3. Soporte de Información

Responsables de:

* Integridad de datos
* Inventarios (medicamentos, procedimientos, ayudas diagnósticas)
* Soporte técnico

---

## 4. Enfermeras

Funciones:

* Registrar signos vitales:

  * Presión arterial
  * Temperatura
  * Pulso
  * Oxígeno

* Registrar:

  * Medicamentos administrados
  * Procedimientos realizados
  * Observaciones

---

## 5. Médicos

Responsables de:

* Historia clínica
* Diagnósticos
* Tratamientos

### Historia clínica (NoSQL)

* Clave principal: **Cédula del paciente**
* Subclave: **Fecha**

#### Campos

* Fecha
* Cédula del médico
* Motivo consulta
* Sintomatología
* Diagnóstico

---

## Órdenes médicas

---

### Medicamentos

| Campo           | Descripción      |
| --------------- | ---------------- |
| Número de orden | Máx. 6 dígitos   |
| ID medicamento  | Desde inventario |
| Dosis           | Texto            |
| Duración        | Texto            |
| Ítem            | Número           |

---

### Procedimientos

| Campo                 | Descripción      |
| --------------------- | ---------------- |
| Número de orden       | Máx. 6 dígitos   |
| ID procedimiento      | Desde inventario |
| Cantidad              | Número           |
| Frecuencia            | Texto            |
| Requiere especialista | Booleano         |
| ID especialidad       | Desde inventario |
| Ítem                  | Número           |

---

### Ayudas diagnósticas

| Campo                 | Descripción      |
| --------------------- | ---------------- |
| Número de orden       | Máx. 6 dígitos   |
| ID examen             | Desde inventario |
| Cantidad              | Número           |
| Requiere especialista | Booleano         |
| ID especialidad       | Desde inventario |
| Ítem                  | Número           |

---

## Reglas de negocio

* No se puede mezclar ayuda diagnóstica con medicamentos o procedimientos
* Tras resultados → nueva orden con diagnóstico
* Órdenes son únicas
* Ítems no se repiten dentro de una orden
* Múltiples elementos → misma orden

---

## Almacenamiento

### Órdenes (SQL)

* Número de orden
* Cédula paciente
* Cédula médico
* Fecha

---

## Tablas especializadas

### Medicamentos

* Número de orden
* Ítem
* Nombre
* Dosis
* Duración
* Costo

---

### Procedimientos

* Número de orden
* Ítem
* Nombre
* Frecuencia
* Costo
* Requiere especialista
* ID especialidad

---

### Ayudas diagnósticas

* Número de orden
* Ítem
* Nombre
* Cantidad
* Costo
* Requiere especialista
* ID especialidad

---

## Reglas adicionales

* Ítems comienzan desde 1
* Relación orden–ítem es única
* Puede haber mezcla de medicamentos y procedimientos
* Hospitalización = procedimiento
* Incluir intervenciones de enfermería

---

## Nota final

**¡Éxitos!**

---

Si quieres, en el siguiente paso puedo:

* Convertir esto en **modelo de dominio (DDD)**
* Sacar **entidades, value objects y agregados**
* O diseñarte directamente la **base de datos (SQL + NoSQL)** 🚀
