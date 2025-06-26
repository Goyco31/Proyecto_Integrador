function listarUsuarios() {
  const token = localStorage.getItem("authToken");
  fetch("/api/usuarios/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      let tabla = `
      <div class="tournament-actions">
            <form method="get" action="/ver/excel/usuarios">
              <button type="submit" class="btn-primary">Exportar un Excel</button>
            </form>
        </div>
                      <table class="tournaments-table">
                          <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Nickname</th>
                                <th>Correo</th>
                                <th>Fecha de registro</th>
                                <th>Monedas</th>
                                <th>Rol</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.id_usuario}</td>
                                <td>${l.nombre}</td>
                                <td>${l.apellido}</td>
                                <td>${l.nickname}</td>
                                <td>${l.correo}</td>
                                <td>${l.fecha_registro}</td>
                                <td>${l.monedas}</td>

                                <td>${l.role}</td>
                                <td><button  class="btn-danger delete-btn" data-user-id="${l.id_usuario}">Eliminar</button></td>
                        </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;

      // Add event listeners to the delete buttons
      const deleteButtons = document.querySelectorAll(".delete-btn");
      deleteButtons.forEach((button) => {
        button.addEventListener("click", openDeleteModal);
      });
    });
}

function openDeleteModal(event) {
  const userId = event.target.dataset.userId;
  // Set the user ID in the modal's data attribute
  document.getElementById("confirm-modal").dataset.userId = userId;
  document.getElementById("confirm-modal").style.display = "flex";
}

function confirmDeleteUser() {
  const userId = document.getElementById("confirm-modal").dataset.userId;
  // Call the delete API endpoint with the user ID
  deleteUser(userId);
  closeDeleteModal();
}

function closeDeleteModal() {
  document.getElementById("confirm-modal").style.display = "none";
}

async function deleteUser(userId) {
  const token = localStorage.getItem("authToken");
  try {
    const response = await fetch(`/api/usuarios/id/${userId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      // User deleted successfully, refresh the user list
      listarUsuarios();
    } else {
      console.error("Error deleting user:", response.status);
      alert("Error deleting user");
    }
  } catch (error) {
    console.error("Error deleting user:", error);
    alert("Error deleting user");
  }
}
