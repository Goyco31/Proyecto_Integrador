document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('loginForm');
  if (!loginForm) return;

  loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    // Mostrar estado de carga
    const submitButton = this.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    submitButton.disabled = true;

    const formData = {
      nickname: this.nickname.value,
      contraseña: this.contraseña.value
    };

    try {
      const response = await fetch('http://localhost:8080/control/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      const data = await response.json();

      // Verificar si la respuesta requiere 2FA
      if (data.requires2FA || data.twoFactorRequired) {
        document.getElementById('login-modal').style.display = 'none';
        document.getElementById('2fa-modal').style.display = 'flex';

        // Asegurarse de que el campo email exista en el formulario 2FA
        const emailInput = document.querySelector('#2faForm [name="email"]');
        if (emailInput) {
          emailInput.value = data.email || formData.nickname;
        }
        return;
      }

      // Si no requiere 2FA pero tiene token
      if (data.token) {
        localStorage.setItem('authToken', data.token);
        window.location.href = data.redirectUrl || '/';
        return;
      }

      // Cerrar modal de login si existe
      const modalLogin = document.getElementById('login-modal');
      if (modalLogin) modalLogin.style.display = 'none';

      // Abrir modal de verificación 2FA
      const modal2FA = document.getElementById('2fa-modal');
      if (modal2FA) modal2FA.style.display = 'flex';

      // Si no cumple con ninguno de los casos anteriores
      throw new Error(data.message || 'Respuesta inesperada del servidor');

    } catch (error) {
      console.error('Login error:', error);

      // Mostrar mensaje de error debajo del formulario
      const errorDiv = document.createElement('div');
      errorDiv.id = 'login-message';
      errorDiv.className = 'error-message';
      errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${error.message}`;

      const existingMsg = document.getElementById('login-message');
      if (existingMsg) existingMsg.remove();

      loginForm.parentNode.insertBefore(errorDiv, loginForm.nextSibling);

    } finally {
      submitButton.innerHTML = originalText;
      submitButton.disabled = false;
    }
  });
});