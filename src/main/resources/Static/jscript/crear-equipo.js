// crear-equipo.js

console.log("JS cargado: crear-equipo.js");

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("createTeamForm");

  if (!form) return;

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    console.log("Formulario enviado");

    // Obtener el token desde localStorage
    const token = localStorage.getItem("authToken");

    if (!token) {
      alert("No se encontró el token. Inicia sesión primero.");
      return;
    }

    // Extraer datos del formulario
    const nombre = document.getElementById("teamName").value.trim();
    const descripcion = document.getElementById("teamDescriptionForm").value.trim();
    const region = document.getElementById("teamRegion").value;
    const logoUrl = document.getElementById("teamLogoUrl").value.trim(); // cambia nombre a logoUrl

    // Validación básica
    if (!nombre || !region) {
      alert("Por favor completa todos los campos obligatorios.");
      return;
    }

    try {
      const response = await fetch("/api/equipos/crear", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          nombre,
          logoUrl,
          region,
          descripcion
        })
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Error al crear equipo");
      }

      alert("¡Equipo creado exitosamente!");
      window.location.href = "/equipos";

    } catch (error) {
      console.error("Error al crear equipo:", error);
      alert("Error al crear equipo: " + error.message);
    }
  });
});
