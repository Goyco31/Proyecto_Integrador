document.addEventListener("DOMContentLoaded", function () {
  const token = localStorage.getItem("authToken");
  if (!token) return;

  fetch("/api/usuarios/me", {
    headers: {
      Authorization: "Bearer " + token
    }
  })
    .then(async res => {
    if (!res.ok) throw new Error("No autenticado");

    const data = await res.json();
    console.log("🧪 Usuario:", data); // <-- Aquí ves si viene `fotoPerfil`

    // Mostrar panel usuario y ocultar botones login
    document.getElementById("user-logged").style.display = "flex";
    document.getElementById("login-buttons").style.display = "none";

    // Cargar nickname
    document.getElementById("user-nickname").innerText = data.nickname;

    // Cargar imagen o fallback
    const avatar = data.fotoPerfil && data.fotoPerfil.trim() !== ""
      ? data.fotoPerfil
      : "/imagenes/perfil/default.png";

    document.getElementById("user-avatar").src = avatar;
  })
  .catch(err => {
    console.log("Usuario no autenticado o token inválido");
    // Opcional: esconder panel usuario si se estaba mostrando
    document.getElementById("user-logged").style.display = "none";
    document.getElementById("login-buttons").style.display = "flex";
  });
});
