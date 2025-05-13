// Agrega un evento al formulario con id 'registroForm' cuando se envía
document.getElementById('registroForm').addEventListener('submit', async function(e) {
    e.preventDefault(); // evita el comportamiento por defecto del formulario

    const form = e.target;
    // Crea un objeto con los valores del formulario
    const data = {
        nombre: form.nombre.value,
        apellido: form.apellido.value,
        nickname: form.nickname.value,
        correo: form.correo.value,
        contraseña: form.contrasena.value
    };
    // Envía los datos al servidor mediante una petición POST
    const response = await fetch('/control/registro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // Indica que se está enviando JSON
        },
        body: JSON.stringify(data) // Convierte el objeto 'data' a JSON

    });

    // Verifica si la respuesta del servidor fue exitosa
    if (response.ok) {
        const result = await response.json();
        alert("Registro exitoso: " + result.token); 
        
    } else {
        alert("Error en el registro."); // Muestra un mensaje si algo salió mal
    }
});
