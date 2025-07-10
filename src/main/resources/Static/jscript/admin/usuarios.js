//lista todos los usuarios
function listarUsuarios() {
  const token = localStorage.getItem("authToken");
  //hace una peticion get al controlador
  fetch("/api/usuarios/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la info
    .then((data) => {
      //inserta la tabla a la vista del admin
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
      //ingresa al info de todos los usuarios
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

      // boton para eliminar el usuario
      const deleteButtons = document.querySelectorAll(".delete-btn");
      deleteButtons.forEach((button) => {
        button.addEventListener("click", openDeleteModal);
      });
    });
}

function openDeleteModal(event) {
  const userId = event.target.dataset.userId;
  //establece el id del usuario
  document.getElementById("confirm-modal").dataset.userId = userId;
  document.getElementById("confirm-modal").style.display = "flex";
}

function confirmDeleteUser() {
  const userId = document.getElementById("confirm-modal").dataset.userId;
  //llama a la url del controlador haciendo una peticion delete
  deleteUser(userId);
  closeDeleteModal();
}
//cierra el modal de eliminacion
function closeDeleteModal() {
  document.getElementById("confirm-modal").style.display = "none";
}

//elimar usuario
async function deleteUser(userId) {
  const token = localStorage.getItem("authToken");
  try {
    //hace una peticion delete del controlador
    const response = await fetch(`/api/usuarios/id/${userId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      //muestra los usuarios en la vista del admin
      listarUsuarios();
    } else {
      console.error("Error al eliminar el usuario:", response.status);
      alert("Error al eliminar el usuario");
    }
  } catch (error) {
    console.error("Error al eliminar el usuario:", error);
    alert("Error al eliminar el usuario");
  }
}
