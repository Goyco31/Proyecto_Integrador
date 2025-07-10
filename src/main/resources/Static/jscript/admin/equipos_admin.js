function listarEquipos() {
  const token = localStorage.getItem("authToken");
  fetch("/api/equipos", {
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
                                <th>Nombre del Equipo</th>
                                <th>Logo</th>
                                <th>Region</th>
                                <th>Descripcion</th>
                                <th>Lider</th>
                                <th>Cantidad de miembros</th>
                                <th>Integrantes</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.id}</td>
                                <td>${l.nombre}</td>
                                <td>${l.logoUrl}</td>
                                <td>${l.region}</td>
                                <td>${l.descripcion}</td>
                                <td>${l.liderNickname}</td>
                                <td>${l.cantidadMiembros}</td>
                                <td>${l.integrantes.nickname}</td>
                                <td>
                                    <button  class="btn-danger delete-btn" data-equipo-id="${l.id}">Eliminar</button></td>
                        </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;

     
    });
}




//elimina el juego
function eliminarEquipo(idEquipo) {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  //modal para confirma la eliminacion
  modalContainer.innerHTML = `
        <div class="modal-content small-modal">
          <p>¿Seguro que desea eliminar este equipo?😕</p>
          <div class="form-actions">
            <button
              id="confirm-delete-btn"
              class="btn-danger"
              
            >
              Eliminar
            </button>
            <button
              id="cancel-delete-btn"
              class="btn-secondary"
            >
              Cancelar
            </button>
          </div>
        </div>`;

  document.body.appendChild(modalContainer);

  // Botón Cancelar (buscar dentro del modal)
  const btnCerrarModal = modalContainer.querySelector("#cancel-delete-btn");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });

  // Botón Eliminar
  const btnConfirmar = modalContainer.querySelector("#confirm-delete-btn");
  btnConfirmar.addEventListener("click", async function (event) {
    event.preventDefault();
    const token = localStorage.getItem("authToken");
    //acceso a la url del controlador
    try {
      const res = await fetch(`/api/equipos/id/${idEquipo}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      //verifica que todo salio bien
      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Equipo eliminado", "success");
      listarJuegos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar el equipo", "error");
    }
  });
}