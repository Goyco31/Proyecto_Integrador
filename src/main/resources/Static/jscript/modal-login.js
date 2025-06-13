document.addEventListener('DOMContentLoaded', function() {
  // Abrir modal de login
  document.querySelectorAll('[data-modal="login-modal"]').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      document.getElementById('login-modal').style.display = 'flex';
      // Limpiar formulario y mensajes
      document.getElementById('loginForm').reset();
      const existingMessage = document.getElementById('login-message');
      if(existingMessage) existingMessage.remove();
    });
  });

  // Cerrar modales (genérico para todos)
  document.querySelectorAll('.close-modal').forEach(btn => {
    btn.addEventListener('click', () => {
      document.getElementById('login-modal').style.display = 'none';
      document.getElementById('2fa-modal').style.display = 'none';
    });
  });

  // Alternar a registro
  document.getElementById('abrir-registro')?.addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('login-modal').style.display = 'none';
    document.getElementById('register-modal').style.display = 'flex';
  });
});

document.getElementById("abrir-recuperar").addEventListener("click", function (e) {
    e.preventDefault();

    // Oculta el modal de login
    document.getElementById("login-modal").style.display = "none";

    // Muestra el modal de recuperar
    document.getElementById("recuperarConta").style.display = "flex";
  });