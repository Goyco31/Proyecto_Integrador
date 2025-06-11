document.addEventListener("DOMContentLoaded", function () {
  const token = localStorage.getItem("authToken");
  if (!token) return;

  fetch("/api/usuarios/me", {
    headers: {
      Authorization: "Bearer " + token
    }
  })
  .then(response => {
    if (!response.ok) throw new Error("No autenticado");
    return response.json();
  })
  .then(user => {
    // Mostrar el usuario logueado
    document.getElementById("user-logged").style.display = "flex";
    document.getElementById("login-buttons").style.display = "none";
    document.getElementById("user-nickname").innerText = user.nickname;
    document.getElementById("user-avatar").src = user.fotoPerfil;
  })
  .catch(err => {
    console.log("Usuario no autenticado o token inválido");
    // Opcional: esconder panel usuario si se estaba mostrando
    document.getElementById("user-logged").style.display = "none";
    document.getElementById("login-buttons").style.display = "flex";
  });
});
