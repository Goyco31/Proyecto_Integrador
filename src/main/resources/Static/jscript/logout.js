document.addEventListener('DOMContentLoaded', function () {
  const logoutButton = document.getElementById('logout-btn');

  if (logoutButton) {
    logoutButton.addEventListener('click', function () {
      // 1. Eliminar el token
      localStorage.removeItem('authToken');
      localStorage.removeItem('tempToken');
      localStorage.removeItem("idUser");

      // 2. Redirigir a la página de inicio
      window.location.href = '/';
    });
  }
});
