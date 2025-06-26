document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('#actualizarContra form');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const nueva = form.querySelector('[name="contrasena"]').value.trim();
    const confirmar = form.querySelector('[name="confirmar_contrasena"]').value.trim();

    if (nueva.length < 6) {
      alert("La contraseña debe tener al menos 6 caracteres.");
      return;
    }

    if (nueva !== confirmar) {
      alert("Las contraseñas no coinciden.");
      return;
    }

    const correo = localStorage.getItem('correoRecuperacion');  // ✅ Nombre correcto
    if (!correo) {
      alert("No se encontró el correo de recuperación. Intenta nuevamente.");
      return;
    }

    try {
      const res = await fetch('/control/reset-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo, nueva })
      });

      const data = await res.json();

      if (!res.ok) throw new Error(data.message || "Error al actualizar la contraseña");

      alert("✅ Contraseña actualizada con éxito");
      form.reset();

      // ✅ Limpiar datos temporales
      localStorage.removeItem('correoRecuperacion');

      // ✅ Cerrar modal y abrir login
      document.getElementById("actualizarContra").style.display = "none";
      document.getElementById("login-modal").style.display = "flex";
    } catch (error) {
      console.error("Error al actualizar contraseña:", error);
      alert(error.message);
    }
  });
});
