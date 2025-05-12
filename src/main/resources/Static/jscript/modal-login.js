document.addEventListener('DOMContentLoaded', function() {
  // Alternar entre login y registro
  document.getElementById('abrir-registro')?.addEventListener('click', function(e) {
    e.preventDefault();
    document.getElementById('login-modal').style.display = 'none';
    document.getElementById('register-modal').style.display = 'flex';
  });

  // Manejo del formulario de login
  document.getElementById('loginForm')?.addEventListener('submit', function(e) {
    e.preventDefault();
    
    const formData = {
      username: this.username.value,
      password: this.password.value,
      remember: this.remember.checked
    };

    // Aquí iría tu lógica de autenticación
    console.log('Datos de login:', formData);
    
    // Ejemplo de envío con fetch:
    fetch('/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
      if(data.success) {
        window.location.reload();
      } else {
        alert(data.message || 'Error al iniciar sesión');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al conectar con el servidor');
    });
  });
});