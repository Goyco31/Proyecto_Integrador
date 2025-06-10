document.addEventListener('DOMContentLoaded', function() {
  const twoFAForm = document.getElementById('2faForm');
  if (!twoFAForm) return;

  twoFAForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // Mostrar estado de carga
    const submitButton = this.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    submitButton.disabled = true;
    
    const formData = {
      email: this.email.value,
      twoFactorCode: this.twoFactorCode.value
    };

    try {
      const response = await fetch('/verify-2fa', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'Código inválido');
      }

      // Si la verificación es exitosa
      if (data.token) {
        localStorage.setItem('authToken', data.token);
        window.location.href = data.redirectUrl || '/';
      } else {
        throw new Error('Autenticación incompleta');
      }
      
    } catch (error) {
      console.error('2FA error:', error);
      alert(error.message); // O mostrar en el modal como en login
    } finally {
      submitButton.innerHTML = originalText;
      submitButton.disabled = false;
    }
  });

  // Reenviar código
  document.getElementById('resend-code')?.addEventListener('click', async function(e) {
    e.preventDefault();
    
    const email = document.querySelector('#2faForm [name="email"]')?.value;
    if (!email) {
      alert('No hay email asociado');
      return;
    }

    try {
      const response = await fetch('/resend-2fa', {
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