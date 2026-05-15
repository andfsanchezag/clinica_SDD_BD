# Clínica IPS — Sistema de Gestión Clínica

Sistema web de gestión clínica con autenticación JWT por rol. Arquitectura de tres capas: base de datos MySQL con lógica en stored procedures, API REST Spring Boot y frontend Vanilla JS + Bootstrap 5.

---

## Requisitos previos

| Herramienta | Versión mínima | Verificar |
|---|---|---|
| **Java JDK** | 17 | `java -version` |
| **Maven** | 3.9+ | `mvn -version` |
| **Node.js** | 18+ | `node -v` |
| **npm** | 9+ | `npm -v` |
| **MySQL Server** | 8.0+ | `mysql --version` |

---

## 1. Base de Datos

### Crear el esquema completo (primera vez)

Ejecutar el script de migración orquestador desde **dentro** de la carpeta `clinic_bd/`:

```bash
cd clinic_bd
mysql -u root -p --delimiter=";" < migrate.sql
```

> El script `migrate.sql` ejecuta en orden todos los archivos `00_schema.sql` → `10_jwt_auth_migration.sql`.
> **`00_schema.sql` hace `DROP DATABASE IF EXISTS clinica_ips`**, por lo que elimina y recrea la base de datos completa.

### Configuración de conexión

Los parámetros de conexión del backend se encuentran en [`clinic/src/main/resources/application.properties`](clinic/src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_ips
spring.datasource.username=root
spring.datasource.password=pass
```

Ajusta `username` y `password` según tu instalación local de MySQL.

---

## 2. Backend — API REST (Spring Boot)

```bash
cd clinic
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows (CMD)
.\mvnw.cmd spring-boot:run      # Windows (PowerShell)
```

O compilar y ejecutar el JAR:

```bash
cd clinic
./mvnw clean package -DskipTests
java -jar target/clinic-0.0.1-SNAPSHOT.jar
```

La API queda disponible en **`http://localhost:8083`**.

> **Puerto:** 8083  
> **Auth:** Bearer JWT (TTL 24 h)

---

## 3. Frontend — Servidor Estático (Node.js / Express)

```bash
cd clinic-frontend
npm install          # Solo la primera vez
npm start            # Inicia el servidor en el puerto 3000
```

La aplicación queda disponible en **`http://localhost:3000`**.

> En desarrollo puedes usar `npm run dev` (nodemon) para recarga automática al guardar.

---

## 4. Orden de arranque

```
1. MySQL (debe estar corriendo en :3306)
2. Backend Spring Boot  → :8083
3. Frontend Node.js     → :3000
```

Abrir el navegador en `http://localhost:3000` → redirige automáticamente a la pantalla de login.

---

## 5. Usuarios del sistema

Todos los usuarios usan la contraseña **`123456`**.

| Usuario | Contraseña | Rol en el sistema | Nombre completo | Panel |
|---|---|---|---|---|
| `medico` | `123456` | Médico | Carlos Alberto Rios | `/pages/doctor.html` |
| `enfermeria` | `123456` | Enfermería | Laura Gomez Mendez | `/pages/enfermera.html` |
| `administrativo` | `123456` | Administrativo | Jorge Hernandez Cano | `/pages/administrativo.html` |
| `recursos_humanos` | `123456` | Recursos Humanos | Maria Lucia Bedoya | `/pages/rrhh.html` |
| `soporte_informacion` | `123456` | Soporte de Información | Andres Felipe Vargas | `/pages/soporte.html` |

> Las contraseñas se almacenan como hash BCrypt (rounds = 10). Las credenciales de prueba fueron cargadas por [`clinic_bd/10_jwt_auth_migration.sql`](clinic_bd/10_jwt_auth_migration.sql).

---

## 6. Funcionalidades por rol

### Médico (`medico`)
- Abrir encuentro clínico a partir de una cita programada
- Cerrar encuentro con diagnóstico, tratamiento y observaciones
- Registrar órdenes médicas (medicamentos, procedimientos, ayudas diagnósticas)
- Agregar ítems de detalle a las órdenes

### Enfermería (`enfermeria`)
- Registrar signos vitales (presión, temperatura, pulso, oxígeno) por encuentro
- Registrar administración de medicamentos al paciente

### Administrativo (`administrativo`)
- Registrar pacientes, contactos de emergencia y seguros médicos
- Programar, reprogramar y cancelar citas
- Emitir facturas y registrar pagos

### Recursos Humanos (`recursos_humanos`)
- Registrar empleados (médicos, enfermeros, administrativos, etc.)
- Crear usuarios operativos del sistema (genera hash BCrypt del password)

### Soporte de Información (`soporte_informacion`)
- Gestión de catálogos: roles, tipos de documento, géneros, especialidades
- Catálogos de inventario: medicamentos, procedimientos, ayudas diagnósticas

---

## 7. Estructura del repositorio

```
clinica_SDD_BD/
├── clinic/                  # Backend Spring Boot (Java 17)
│   ├── src/main/java/app/
│   │   ├── application/     # DTOs de request/response
│   │   ├── domain/          # Entidades JPA y servicios de dominio
│   │   └── infrastructure/  # Controllers REST, JwtService, SecurityConfig
│   └── pom.xml
│
├── clinic-frontend/         # Frontend Vanilla JS (Node.js/Express)
│   ├── public/
│   │   ├── pages/           # HTMLs por rol (login, doctor, enfermera, etc.)
│   │   └── js/              # Módulos ES2022 por capa (domain/application/infra/presentation)
│   ├── server.js            # Express estático puerto 3000
│   └── package.json
│
├── clinic_bd/               # Scripts SQL en orden de ejecución
│   ├── migrate.sql          # Orquestador — ejecutar este
│   ├── 00_schema.sql        # Crea la BD (hace DROP DATABASE)
│   ├── 01_catalogos.sql     # Tablas catálogo
│   ├── 02_tablas_maestras.sql
│   ├── 03_tablas_transaccionales.sql
│   ├── 04_triggers.sql
│   ├── 05_procedimientos.sql  # Toda la lógica de negocio
│   ├── 06_vistas.sql
│   ├── 07_auditoria.sql
│   ├── 08_seguridad.sql
│   ├── 09_datos_semilla.sql   # Datos de prueba
│   └── 10_jwt_auth_migration.sql  # Usuarios BCrypt para pruebas
│
└── SDD/                     # Documentación de diseño del sistema
    ├── c4_architecture.md   # Modelo C4 con diagramas Mermaid
    └── *.md                 # Otros documentos de diseño
```

---

## 8. Arquitectura

La documentación completa de arquitectura (modelo C4 con diagramas de contexto, contenedores, componentes y secuencias) está en [`SDD/c4_architecture.md`](SDD/c4_architecture.md).

**Resumen:**
- **Base de datos:** MySQL 8.x — toda la lógica de negocio en stored procedures con parámetros `OUT o_codigo / o_mensaje / o_id_generado`
- **Backend:** Spring Boot 4.x, Java 17, Spring Security + JWT (JJWT 0.12), Bean Validation, JDBC vía `EntityManager`
- **Frontend:** Vanilla JS ES2022 (módulos nativos), Bootstrap 5.3, sin bundler, arquitectura por capas (domain → application → infrastructure → presentation)
