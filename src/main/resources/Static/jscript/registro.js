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
