//lista todas las recompensas
function listarRecompensas() {
  const token = localStorage.getItem("authToken");
  //accede a la url del controlador
  fetch("/api/recompensas/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la informacion
    .then((data) => {
      //tabla que aparecera para el admin
      let tabla = `
        <div class="tournament-actions">
            <button id="new-tournament-btn" class="btn-primary"  onclick="registrarRecompensas()">
              Nueva Recompensa
            </button>
            <form method="get" action="/ver/excel/recompensas">
              <button type="submit" class="btn-primary">Exportar un Excel</button>
            </form>
        </div>

        <table class="tournaments-table">
                          <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Descripcion</th>
                                <th>Costo</th>
                                <th>Disponible</th>
                                <th>Cantidad</th>
                                <th>Imagen</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      //ingresa la info de todas las opciones de recompensa
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.idRecompensa}</td>
                                <td>${l.nombre}</td>
                                <td>${l.descripcion}</td>
                                <td>${l.costo} monedas</td>
                                <td>${l.disponible}</td>
                                <td>${l.cantidad}</td>
                                <td>
                                    <img src="data:image/png;base64,${l.imgRecompensaBase64}" alt="${l.nombre}" style="width: 100px; height: 100px;">
                                </td>
                                </td>
                                <td>
                                    <button class="btn-secondary edit-btn" data-id="${l.idRecompensa}" onclick="actualizarRecompensa(${l.idRecompensa})">Editar</button>
                                    <button class="btn-danger delete-btn" data-recompensa-id="${l.idRecompensa}" onclick="eliminarRecompensa(${l.idRecompensa})">Eliminar</button>
                                </td>
                        </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;
    });
}

//actualizar ua recompensa
function actualizarRecompensa(id) {
  const token = localStorage.getItem("authToken");
  //accede a la url del controlador
  fetch(`/api/recompensas/id/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
  //convierte la respuest a formato JSON
    .then((r) => r.json())
    //mapea la info
    .then((data) => {
      //crea un contenedor con su clase e id
      const modalContainer = document.createElement("div");
      modalContainer.id = "tournament-modal";
      modalContainer.classList.add("modal");

      //modal para actualizar la recompensa elegida
      modalContainer.innerHTML = `
        <div class="modal-content">
          <span class="close">&times;</span>
          <h2>Actualizar Recompensa</h2>
          <form id="tournament-form" class="admin-form">
            <div class="form-group">
              <label for="tournament-name">Nombre:</label>
              <input type="text" id="tournament-name" value="${data.nombre}" required />
            </div>
            <div class="form-group">
              <label for="tournament-description">Descripcion:</label>
              <textarea id="tournament-description" rows="3" required>${data.descripcion}</textarea>
            </div>
            <div class="form-group">
              <label for="tournament-costo">Costo:</label>
              <input
                type="number"
                id="tournament-costo"
                value="${data.costo}"
                required
              />
            </div>
            <div class="form-group">
              <label for="tournament-cantidad">Cantidad:</label>
              <input
                type="number"
                id="tournament-cantidad"
                value="${data.cantidad}"
                required
              />
            </div>
            <div class="form-group">
              <label for="tournament-game-image">Imagen de la recompensa:</label>
              <input type="file" id="tournament-game-image" />
              <img src="data:image/png;base64,${data.imgRecompensaBase64}" alt="${data.nombre}" style="width: 100px; height: 100px;">
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

      //cierra el modal
      const btnCerrarModal = document.getElementById("cancel-tournament-modal");
      btnCerrarModal.addEventListener("click", () => {
        modalContainer.remove();
      });

      //guarda la nueva informacion
      const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
      guardarRecompensa.addEventListener("click", async (event) => {
        event.preventDefault();
        //extrae los valores de la nueva informacion
        const nombre = document.getElementById("tournament-name").value;
        const descripcion = document.getElementById(
          "tournament-description"
        ).value;
        const costo = parseInt(
          document.getElementById("tournament-costo").value
        );
        const cantidad = parseInt(
          document.getElementById("tournament-cantidad").value
        );
        const imagen = document.getElementById("tournament-game-image")
          .files[0];
        let disponible = cantidad > 0;

        //guarda los valores en los parametros del controlador
        const dataSave = new FormData();
        dataSave.append("nombre", nombre);
        dataSave.append("descripcion", descripcion);
        dataSave.append("costo", costo);
        dataSave.append("cantidad", cantidad);
        dataSave.append("disponible", disponible);
        dataSave.append("imagen", imagen);

        const token = localStorage.getItem("authToken");
        //accede ala url del controlador
        try {
          const res = await fetch(`/api/recompensas/actualizar/id/${id}`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: dataSave,
          });
          //verifica que todo salga bien
          if (!res.ok) throw new Error();
          Swal.fire("Exito", "Recompensa actualizada", "success");
          listarRecompensas();
          modalContainer.remove();
        } catch (error) {
          Swal.fire("Error", errorMessage, "error");
        }
      });
    });
}

//registro de recompensas
function registrarRecompensas() {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  //datos para acceder a las url
  const modalTitle = "Registrar Recompensa";
  const fetchUrl = "/api/recompensas/registrar";
  const fetchMethod = "POST";
  const successMessage = "Recompensa registrado";
  const errorMessage = "No se pudo registrar la recompensa";

  //modal para registrar una nueva recompensa
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
          <label for="tournament-description">Descripcion:</label>
          <textarea id="tournament-description" rows="3" required></textarea>
        </div>
        <div class="form-group">
          <label for="tournament-costo">Costo:</label>
          <input
            type="number"
            id="tournament-costo"
            required
          />
        </div>
        <div class="form-group">
          <label for="tournament-cantidad">Cantidad:</label>
          <input
            type="number"
            id="tournament-cantidad"
            required
          />
        </div>
        <div class="form-group">
          <label for="tournament-game-image">Imagen de la recompensa:</label>
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

  //cerrar el modal
  const btnCerrarModal = document.getElementById("cancel-tournament-modal");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });

  //guarda la informacion
  const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
  guardarRecompensa.addEventListener("click", async (event) => {
    event.preventDefault();
    //extrae los valores ingresados
    const nombre = document.getElementById("tournament-name").value;
    const descripcion = document.getElementById("tournament-description").value;
    const costo = parseInt(document.getElementById("tournament-costo").value);
    const cantidad = parseInt(
      document.getElementById("tournament-cantidad").value
    );
    let disponible;
    //si la cantidad es mator a 0 la recompensa estara disponible
    if (cantidad > 0) {
      disponible = true;
    } else {
      disponible = false;
    }
    const imagen = document.getElementById("tournament-game-image").files[0];

    //guarda los valores en los parametros del controlador
    const dataSave = new FormData();
    dataSave.append("nombre", nombre);
    dataSave.append("descripcion", descripcion);
    dataSave.append("costo", costo);
    dataSave.append("cantidad", cantidad);
    dataSave.append("disponible", disponible);
    dataSave.append("imagen", imagen);

    const token = localStorage.getItem("authToken");
    try {
      //accede a la url del controlador
      const res = await fetch(fetchUrl, {
        method: fetchMethod,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: dataSave,
      });
      //verifica que todo funcione
      if (!res.ok) throw new Error();
      Swal.fire("Exito", successMessage, "success");
      listarRecompensas();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", errorMessage, "error");
    }
  });
}

//elimina la recompensa
function eliminarRecompensa(idRecompensa) {
  //crea un contenedor con sus clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  //modal de confirmacion
  modalContainer.innerHTML = `
        <div class="modal-content small-modal">
          <p>¿Seguro que desea eliminar esta recompensa?😕</p>
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
    console.log("Token:", token); // Add this line
    try {
      const res = await fetch(`/api/recompensas/eliminar/id/${idRecompensa}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Recompensa eliminada", "success");
      listarRecompensas();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar la recompensa", "error");
    }
  });
}
