document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('loginForm');
  if (!loginForm) return;

  loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const submitButton = this.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    submitButton.disabled = true;

    const formData = {
      nickname: this.nickname.value,
      contraseña: this.contraseña.value
    };

    try {
      const response = await fetch('/control/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      const data = await response.json();

      // Si requiere verificación en dos pasos
      if (data.requires2fa || data.requires2FA) {
        // Guardar tempToken
        localStorage.setItem('tempToken', data.tempToken);
        localStorage.setItem('idUser', data.idUser);
        //localStorage.setItem('idEquipo', data.idEquipo);
        // Mostrar modal de 2FA
        document.getElementById('login-modal').style.display = 'none';
        document.getElementById('2fa-modal').style.display = 'flex';

        return;
      }

      // Si autenticación directa sin 2FA
      if (data.token) {
        localStorage.setItem('authToken', data.token);
        localStorage.setItem('idUser', data.idUser);
        //localStorage.setItem('idEquipo', data.idEquipo);
        window.location.href = '/';
        return;
      }

      throw new Error(data.message || 'Respuesta inesperada del servidor');

    } catch (error) {
      console.error('Login error:', error);
      alert(error.message);
    } finally {
      submitButton.innerHTML = originalText;
      submitButton.disabled = false;
    }
  });
});
