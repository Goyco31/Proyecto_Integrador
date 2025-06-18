function listarMonedas() {
  const token = localStorage.getItem("authToken");
  fetch("/api/comprarMonedas/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      let tabla = `
        <div class="tournament-actions">
            <button id="new-tournament-btn" class="btn-primary"  onclick="registrarOpcion()">
              Nueva Opcion de recarga
            </button>
        </div>

        <table class="tournaments-table">
                          <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Cantidad</th>
                                <th>Precio de compra</th>
                                <th>Imagen</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.idCompra}</td>
                                <td>${l.nombre}</td>
                                <td>${l.cantidad}</td>
                                <td>S/. ${l.precioCompra}</td>
                                <td>
                                    <img src="data:image/png;base64,${l.imgimgMonedaBase64}" alt="${l.nombre}" style="width: 100px; height: 100px;">
                                </td>
                                <td>
                                    <button class="btn-secondary edit-btn" data-id="${l.idCompra}" onclick="actualizarOpcion(${l.idCompra})">Editar</button>
                                    <button class="btn-danger delete-btn" data-juego-id="${l.idCompra}" onclick="eliminarMonedas(${l.idCompra})">Eliminar</button>
                                </td>
                        </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;
    });
}

function registrarOpcion() {
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  modalContainer.innerHTML = `<div class="modal-content">
          <span class="close">&times;</span>
          <h2>Registrar Opciones de recarga</h2>
          <form id="tournament-form" class="admin-form">
            <div class="form-group">
              <label for="tournament-name">Nombre:</label>
              <input type="text" id="tournament-name" required />
            </div>
            <div class="form-group">
              <label for="tournament-cantidad">Cantidad:</label>
              <input type="text" id="tournament-cantidad" required />
            </div>
            <div class="form-group">
              <label for="tournament-precio">Precio de compra:</label>
              <input
                type="number"
                id="tournament-precio"
                required
              />
            </div>
            <div class="form-group">
              <label for="tournament-game-image">Imagen de la recompensa:</label>
              <input type="file" id="tournament-game-image" required />
            </div>
            <div class="form-actions">
              <button type="submit" class="btn-primary" id="btnGuardarOpcion">Guardar</button>
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

  const guardarRecompensa = document.getElementById("btnGuardarOpcion");
  guardarRecompensa.addEventListener("click", async (event) => {
    event.preventDefault();
    const nombre = document.getElementById("tournament-name").value;
    const cantidad = parseInt(
      document.getElementById("tournament-cantidad").value
    );
    const precio = document.getElementById("tournament-precio").value;
    const imagen = document.getElementById("tournament-game-image").files[0];

    const dataSave3 = new FormData();
    dataSave3.append("nombre", nombre);
    dataSave3.append("cantidad", cantidad);
    dataSave3.append("precio", precio);
    dataSave3.append("imagen", imagen);

    const token = localStorage.getItem("authToken");
    try {
      const res = await fetch("/api/comprarMonedas/registrar", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: dataSave3,
      });
      if (!res.ok) throw new Error();
      Swal.fire("Exito", "Opcion de recarga registrada", "success");
      listarMonedas();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo registrar la opcion de recarga", "error");
    }
  });
}

function actualizarOpcion(idCompra) {
  const token = localStorage.getItem("authToken");
  fetch(`/api/comprarMonedas/id/${idCompra}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      const modalContainer = document.createElement("div");
      modalContainer.id = "tournament-modal";
      modalContainer.classList.add("modal");

      modalContainer.innerHTML = `<div class="modal-content">
          <span class="close">&times;</span>
          <h2>Actualizar Opciones de recarga</h2>
          <form id="tournament-form" class="admin-form">
            <div class="form-group">
              <label for="tournament-name">Nombre:</label>
              <input type="text" id="tournament-name" value="${data.nombre}" required />
            </div>
            <div class="form-group">
              <label for="tournament-cantidad">Cantidad:</label>
              <input type="text" id="tournament-cantidad" value="${data.cantidad}" required />
            </div>
            <div class="form-group">
              <label for="tournament-precio">Precio de compra:</label>
              <input
                type="number"
                id="tournament-precio"
                value="${data.precioCompra}"
                required
              />
            </div>
            <div class="form-group">
              <label for="tournament-game-image">Imagen de la recompensa:</label>
              <input type="file" id="tournament-game-image" />
              <img src="data:image/png;base64,${data.imgimgMonedaBase64}" alt="${data.imgimgMonedaBase64}" style="width: 100px; height: 100px;">
            </div>
            <div class="form-actions">
              <button type="submit" class="btn-primary" id="btnGuardarOpcion">Guardar</button>
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

      const guardarRecompensa = document.getElementById("btnGuardarOpcion");
      guardarRecompensa.addEventListener("click", async (event) => {
        event.preventDefault();
        const nombre = document.getElementById("tournament-name").value;
        const cantidad = parseInt(
          document.getElementById("tournament-cantidad").value
        );
        const precio = document.getElementById("tournament-precio").value;
        const imagen = document.getElementById("tournament-game-image")
          .files[0];

        const dataSave3 = new FormData();
        dataSave3.append("nombre", nombre);
        dataSave3.append("cantidad", cantidad);
        dataSave3.append("precio", precio);
        dataSave3.append("imagen", imagen);

        const token = localStorage.getItem("authToken");

        try {
          const res = await fetch(
            `/api/comprarMonedas/actualizar/id/${idCompra}`,
            {
              method: "PUT",
              headers: {
                Authorization: `Bearer ${token}`,
              },
              body: dataSave3,
            }
          );
          if (!res.ok) throw new Error();
          Swal.fire("Exito", "Opcion de recarga actualizada", "success");
          listarMonedas();
          modalContainer.remove();
        } catch (error) {
          Swal.fire(
            "Error",
            "No se pudo actualizar la opcion de recarga",
            "error"
          );
        }
      });
    });
}

function eliminarMonedas(CompraID) {
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  modalContainer.innerHTML = `
        <div class="modal-content small-modal">
          <p>¿Seguro que desea eliminar esta Opcion?😕</p>
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
      const res = await fetch(`/api/comprarMonedas/eliminar/id/${CompraID}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Opcion eliminado", "success");
      listarMonedas();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar la opcion", "error");
    }
  });
}
