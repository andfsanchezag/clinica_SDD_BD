// server.js — Clinic Frontend
// Servidor estático Express que sirve public/ en el puerto 3000
const express = require('express');
const path    = require('path');

const app  = express();
const PORT = process.env.PORT || 3000;

// Servir todos los archivos estáticos de public/
app.use(express.static(path.join(__dirname, 'public')));

// Cualquier ruta no reconocida → index.html (que redirige a login)
app.get('*', (_req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Clinic Frontend → http://localhost:${PORT}`);
});
