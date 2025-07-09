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
      console.log("🧪 Usuario:", data);

      // Mostrar panel usuario y ocultar botones login
      document.getElementById("user-logged").style.display = "flex";
      document.getElementById("login-buttons").style.display = "none";

      // Nickname
      document.getElementById("user-nickname").innerText = data.nickname;
      document.getElementById("monedas").innerHTML = data.monedas;
      // Imagen
      const avatar = data.fotoPerfil && data.fotoPerfil.trim() !== ""
        ? data.fotoPerfil
        : "/imagenes/perfil/default.png";
      document.getElementById("user-avatar").src = avatar;

      // 🔐 Mostrar enlace a administrador SOLO si es ADMIN
      if (data.role === "ADMIN") {
        document.getElementById("admin-link").style.display = "inline-block";
      }

    })
    .catch(err => {
      console.log("Usuario no autenticado o token inválido");
      document.getElementById("user-logged").style.display = "none";
      document.getElementById("login-buttons").style.display = "flex";
    });
});
