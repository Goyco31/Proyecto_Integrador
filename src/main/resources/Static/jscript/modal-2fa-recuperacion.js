document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('2fa-recuperacion-form');
  if (!form) return;

  form.addEventListener('submit', async function (e) {
    e.preventDefault();

    const code = form.querySelector('[name="twoFactorCode"]').value;
    const correo = localStorage.getItem('correoRecuperacion'); // ✅ Usar la misma clave

    if (!correo) {
      alert('No hay correo almacenado para validar el código.');
      return;
    }

    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    submitButton.disabled = true;

    try {
      const response = await fetch('/control/validate-reset-code', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ correo, code })
      });

      const text = await response.text();
      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        console.error('❌ Respuesta no JSON:', text);
        throw new Error('Respuesta inesperada del servidor');
      }

      if (!response.ok) {
        throw new Error(data.message || 'Código inválido o expirado');
      }

      // ✅ Código correcto: cerramos modal actual y abrimos el de cambiar contraseña
      document.getElementById('2fa-recuperacion-modal').style.display = 'none';
      document.getElementById('actualizarContra').style.display = 'flex';

    } catch (error) {
      console.error('Error al verificar código:', error);
      alert(error.message || 'Error inesperado al verificar código');
    } finally {
      submitButton.innerHTML = originalText;
      submitButton.disabled = false;
    }
  });

  // 📤 Reenviar código
  document.getElementById('resend-code')?.addEventListener('click', async function (e) {
    e.preventDefault();

    const correo = localStorage.getItem('correoRecuperacion');
    if (!correo) {
      alert('No hay correo disponible');
      return;
    }

    try {
      const response = await fetch('/control/forgot-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ correo })
      });

      const text = await response.text();
      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        console.warn('Reenvío no JSON:', text);
        alert('Código reenviado.');
        return;
      }

      alert(data.message || 'Código reenviado');
    } catch (error) {
      console.error('Error al reenviar código:', error);
      alert('No se pudo reenviar el código');
    }
  });
});
