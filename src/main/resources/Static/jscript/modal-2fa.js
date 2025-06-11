document.addEventListener('DOMContentLoaded', function () {
  const twoFAForm = document.getElementById('2faForm');
  if (!twoFAForm) return;

  twoFAForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const submitButton = this.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    submitButton.disabled = true;

    const formData = {
      twoFactorCode: this.querySelector('[name="twoFactorCode"]')?.value || '',
      tempToken: localStorage.getItem('tempToken') || ''
    };

    try {
      const response = await fetch('/control/validate-2fa', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      const text = await response.text();
      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        console.error('❌ JSON inválido:', text);
        throw new Error('Respuesta inesperada del servidor');
      }

      if (!response.ok) {
        throw new Error(data.message || 'Código inválido');
      }

      if (data.token) {
        localStorage.setItem('authToken', data.token);
        localStorage.removeItem('tempToken');
        window.location.href = '/';
      } else {
        throw new Error('Autenticación incompleta');
      }
    } catch (error) {
      console.error('2FA error:', error);
      alert(error.message);
    } finally {
      submitButton.innerHTML = originalText;
      submitButton.disabled = false;
    }
  });

  // Reenviar código
  document.getElementById('resend-code')?.addEventListener('click', async function (e) {
    e.preventDefault();

    const email = document.querySelector('#2faForm [name="email"]')?.value;
    if (!email) {
      alert('No hay email asociado');
      return;
    }

    try {
      const response = await fetch('/control/resend-2fa', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      });

      const data = await response.json();
      alert(data.message || 'Código reenviado');
    } catch (error) {
      console.error('Resend error:', error);
      alert('Error al reenviar código');
    }
  });
});
