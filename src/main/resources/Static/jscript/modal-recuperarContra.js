document.addEventListener('DOMContentLoaded', function () {
  const form = document.querySelector('#recuperarConta form');
  if (!form) return;

  form.addEventListener('submit', function (e) {
    e.preventDefault();

    const correo = document.getElementById('correo-recuperar').value;

    fetch('/control/forgot-password', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ correo })
    })
    .then(async res => {
      const data = await res.text();
      if (!res.ok) throw new Error(data || 'Error inesperado');

      // ✅ Guarda el correo para usarlo después
      localStorage.setItem('correoRecuperacion', correo);

      alert('✅ Código enviado a tu correo');

      // ✅ Muestra el modal correcto para verificar el código
      document.getElementById('recuperarConta').style.display = 'none';
      document.getElementById('2fa-recuperacion-modal').style.display = 'flex';
    })
    .catch(err => {
      alert(err.message);
      console.error('Error en solicitud de recuperación', err);
    });
  });
});
