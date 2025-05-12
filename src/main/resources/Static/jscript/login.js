document.addEventListener('DOMContentLoaded', function() {
  // Abrir modal
  document.querySelectorAll('[data-modal="login-modal"]').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      document.getElementById('login-modal').style.display = 'flex';
      // Limpiar mensajes previos al abrir el modal
      const existingMessage = document.getElementById('login-message');
      if(existingMessage) {
        existingMessage.remove();
      }
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
  });

  // Manejo del formulario
  document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // Mostrar loader o estado de carga
    const submitButton = this.querySelector('button[type="submit"]');
    const originalButtonText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
    submitButton.disabled = true;
    
    // 1. Obtener los datos del formulario
    const formData = {
      nickname: this.querySelector('[name="nickname"]').value,
      contraseña: this.querySelector('[name="contraseña"]').value
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
      
      // 4. Si la autenticación fue exitosa
      if (data.token) {
        localStorage.setItem('authToken', data.token);
        
        // Mostrar mensaje de éxito en lugar de redirigir
        const form = document.getElementById('loginForm');
        
        // Limpiar mensajes previos
        const existingMessage = document.getElementById('login-message');
        if(existingMessage) {
          existingMessage.remove();
        }
        
        // Crear y mostrar mensaje de éxito
        const successMessage = document.createElement('div');
        successMessage.id = 'login-message';
        successMessage.className = 'success-message';
        successMessage.innerHTML = `
          <i class="fas fa-check-circle"></i> Inicio de sesión exitoso
          <p>Bienvenido, ${data.nickname || 'usuario'}!</p>
        `;
        
        // Insertar después del formulario
        form.parentNode.insertBefore(successMessage, form.nextSibling);
        
        // Opcional: cerrar el modal después de 3 segundos
        setTimeout(() => {
          document.getElementById('login-modal').style.display = 'none';
        }, 3000);
        
        // Limpiar el formulario
        form.reset();
      } else {
        throw new Error('No se recibió token de autenticación');
      }
      
    } catch (error) {
      console.error('Error en el login:', error);
      
      // Mostrar mensaje de error en el modal
      const form = document.getElementById('loginForm');
      const existingMessage = document.getElementById('login-message');
      
      if(existingMessage) {
        existingMessage.remove();
      }
      
      const errorMessage = document.createElement('div');
      errorMessage.id = 'login-message';
      errorMessage.className = 'error-message';
      errorMessage.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${error.message}`;
      
      form.parentNode.insertBefore(errorMessage, form.nextSibling);
      
    } finally {
      // Restaurar el botón a su estado original
      submitButton.innerHTML = originalButtonText;
      submitButton.disabled = false;
    }
  });
});