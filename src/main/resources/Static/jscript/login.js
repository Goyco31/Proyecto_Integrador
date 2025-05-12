document.addEventListener('DOMContentLoaded', function() {
  // Abrir modal
  document.querySelectorAll('[data-modal="login-modal"]').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      document.getElementById('login-modal').style.display = 'flex';
    });
  });

  // Cerrar modal
  document.getElementById('login-modal').querySelector('.close-modal').addEventListener('click', () => {
    document.getElementById('login-modal').style.display = 'none';
  });

  // Alternar a registro
  document.getElementById('abrir-registro').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('login-modal').style.display = 'none';
    // Aquí puedes agregar lógica para mostrar el modal de registro
  });

  // Manejo del formulario
  document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // 1. Obtener los datos del formulario
    const formData = {
      nickname: this.querySelector('[name="nickname"]').value, // Asegúrate que el input tenga name="nickname"
      contraseña: this.querySelector('[name="contraseña"]').value // Asegúrate que el input tenga name="contraseña"
    };
    
    try {
      // 2. Enviar la petición al backend
      const response = await fetch('http://localhost:8080/control/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      // 3. Manejar la respuesta
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error en el servidor');
      }

      const data = await response.json();
      
      // 4. Guardar el token JWT si la autenticación fue exitosa
      if (data.token) {
        localStorage.setItem('authToken', data.token);
        // Redirigir al usuario o cerrar el modal
        window.location.href = '/dashboard.html'; // Ajusta esta ruta
      } else {
        throw new Error('No se recibió token de autenticación');
      }
      
    } catch (error) {
      console.error('Error en el login:', error);
      alert('Error al iniciar sesión: ' + error.message);
      // También puedes mostrar el error en el modal en lugar de usar alert
    }
  });
});