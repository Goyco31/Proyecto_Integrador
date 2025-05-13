document.addEventListener('DOMContentLoaded', function() {
    // Manejar el envío del formulario de registro
    document.getElementById('registroForm').addEventListener('submit', async function(e) {
        e.preventDefault(); // evita el comportamiento por defecto del formulario

        const form = e.target;
        const data = {
            nombre: form.nombre.value,
            apellido: form.apellido.value,
            nickname: form.nickname.value,
            correo: form.correo.value,
            contraseña: form.contrasena.value
        };

        const response = await fetch('/control/registro', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.json();
            alert("Registro exitoso: " + result.token); 
            
        } else {
            alert("Error en el registro.");
        }
    });

    // Cerrar modal al hacer clic en la X
    const closeModalBtn = document.querySelector('#register-modal .close-modal');
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', function() {
            document.getElementById('register-modal').style.display = 'none';
        });
    }

    // Alternar al modal de login cuando se hace clic en "Conéctate"
    const abrirLogin = document.getElementById('abrir-login');
    if (abrirLogin) {
        abrirLogin.addEventListener('click', function(e) {
            e.preventDefault();
            // Cierra el modal de registro
            document.getElementById('register-modal').style.display = 'none';
            // Abre el modal de login
            document.getElementById('login-modal').style.display = 'flex';
        });
    }
});