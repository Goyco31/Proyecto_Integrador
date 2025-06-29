function listarJuegos() {
  const token = localStorage.getItem("authToken");
  fetch("/api/juegos/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      let tabla = `
      <div class="tournament-actions">
            <button id="new-tournament-btn" class="btn-primary"  onclick="registrarJuego()">
              Nuevo juego
            </button>
            <form method="get" action="/ver/excel/juegos">
              <button type="submit" class="btn-primary">Exportar un Excel</button>
            </form>
        </div>
      
      <table class="tournaments-table">
                          <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Genero</th>
                                <th>Imagen del juego</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.idJuego}</td>
                                <td>${l.nombreJuego}</td>
                                
                                <td>${l.generoJuego}</td>
                                <td>
                                    <img src="data:image/png;base64,${l.imgJuegoBase64}" alt="${l.nombreJuego}" style="width: 100px; height: 100px;">
                                </td>
                                <td>
                                  <button class="btn-secondary edit-btn" data-id="${l.idJuego}" onclick="actualizarJuego(${l.idJuego})">Editar</button>
                                  <button class="btn-danger delete-btn" data-juego-id="${l.idJuego}" onclick="eliminarJuego(${l.idJuego})">Eliminar</button></td>
                        </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;
    });
}

function registrarJuego() {
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  modalContainer.innerHTML = `
    <div class="modal-content">
      <span class="close">&times;</span>
      <h2>Registrar Recompensa</h2>
      <form id="tournament-form" class="admin-form">
        <div class="form-group">
          <label for="tournament-name">Nombre:</label>
          <input type="text" id="tournament-name" required />
        </div>
        <div class="form-group">
          <label for="tournament-costo">Genero:</label>
          <input
            type="text"
            id="tournament-genero"
            required
          />
        </div>
        <div class="form-group">
          <label for="tournament-game-image">Imagen del juego:</label>
          <input type="file" id="tournament-game-image" required />
        </div>
        <div class="form-actions">
            <button type="submit" class="btn-primary" id="btnGuardarRecompensa">Guardar</button>
            <button
              type="button"
              class="btn-cancel"
              id="cancel-tournament-modal"
            >
              Cancelar
            </button>
        </div>
      </form>
    </div>
  `;
  document.body.appendChild(modalContainer);

  const btnCerrarModal = document.getElementById("cancel-tournament-modal");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });

  const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
  guardarRecompensa.addEventListener("click", async (event) => {
    event.preventDefault();
    const nombre = document.getElementById("tournament-name").value;
    const genero = document.getElementById("tournament-genero").value;
    const imagen = document.getElementById("tournament-game-image").files[0];

    const dataSave2 = new FormData();
    dataSave2.append("nombre", nombre);
    dataSave2.append("genero", genero);
    dataSave2.append("imagen", imagen);

    const token = localStorage.getItem("authToken");
    try {
      const res = await fetch("/api/juegos/registrar", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: dataSave2,
      });
      if (!res.ok) throw new Error();
      Swal.fire("Exito", "juego registrado", "success");
      listarJuegos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "no se pudo registrar el juego", "error");
    }
  });
}

function actualizarJuego(idJuego) {
  const token = localStorage.getItem("authToken");
  fetch(`/api/juegos/id/${idJuego}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      const modalContainer = document.createElement("div");
      modalContainer.id = "tournament-modal";
      modalContainer.classList.add("modal");

      modalContainer.innerHTML = `
        <div class="modal-content">
      <span class="close">&times;</span>
      <h2>Registrar Recompensa</h2>
      <form id="tournament-form" class="admin-form">
        <div class="form-group">
          <label for="tournament-name">Nombre:</label>
          <input type="text" id="tournament-name" required value="${data.nombreJuego}"/>
        </div>
        <div class="form-group">
          <label for="tournament-costo">Genero:</label>
          <input
            type="text"
            id="tournament-genero"
            required value="${data.generoJuego}"
          />
        </div>
        <div class="form-group">
          <label for="tournament-game-image">Imagen del juego:</label>
          <input type="file" id="tournament-game-image" required value="${data.imgJuegoBase64}"/>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn-primary" id="btnGuardarRecompensa">Guardar</button>
            <button
              type="button"
              class="btn-cancel"
              id="cancel-tournament-modal"
            >
              Cancelar
            </button>
        </div>
      </form>
    </div>
      `;

      document.body.appendChild(modalContainer);

      const btnCerrarModal = document.getElementById("cancel-tournament-modal");
      btnCerrarModal.addEventListener("click", () => {
        modalContainer.remove();
      });

      const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
      guardarRecompensa.addEventListener("click", async (event) => {
        event.preventDefault();
        const nombre = document.getElementById("tournament-name").value;
        const genero = document.getElementById("tournament-genero").value;
        const imagen = document.getElementById("tournament-game-image")
          .files[0];

        const dataSave2 = new FormData();
        dataSave2.append("nombre", nombre);
        dataSave2.append("genero", genero);
        dataSave2.append("imagen", imagen);

        const token = localStorage.getItem("authToken");
        try {
          const res = await fetch(`/api/juegos/actualizar/id/${idJuego}`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: dataSave2,
          });
          if (!res.ok) throw new Error();
          Swal.fire("Exito", "juego actualizado", "success");
          listarJuegos();
          modalContainer.remove();
        } catch (error) {
          Swal.fire("Error", "no se pudo actualizar el juego", "error");
        }
      });
    });
}

function eliminarJuego(idJuego) {
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  modalContainer.innerHTML = `
        <div class="modal-content small-modal">
          <p>¿Seguro que desea eliminar este juego?😕</p>
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
    try {
      const res = await fetch(`/api/juegos/eliminar/id/${idJuego}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Juego eliminado", "success");
      listarJuegos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar el Jueog", "error");
    }
  });
}
