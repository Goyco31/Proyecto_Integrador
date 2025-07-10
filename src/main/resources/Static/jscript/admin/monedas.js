//lista todas las opciones de recarga
function listarMonedas() {
  const token = localStorage.getItem("authToken");
  //accede a la utl del controlador
  fetch("/api/comprarMonedas/lista", {
    headers: {
      //ingresa el token de verificacion
      Authorization: `Bearer ${token}`,
    },
  })
  //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la informacion
    .then((data) => {
      //añade la tabla al panel de admin
      let tabla = `
        <div class="tournament-actions">
            <button id="new-tournament-btn" class="btn-primary"  onclick="registrarOpcion()">
              Nueva Opcion de recarga
            </button>
            <form method="get" action="/ver/excel/opcionesRecarga">
              <button type="submit" class="btn-primary">Exportar un Excel</button>
            </form>
            <form method="get" action="/ver/excel/historialRecarga">
              <button type="submit" class="btn-primary">Exportar recargas realizadas</button>
            </form>
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
      //añade todas las opciones a la tabla
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

//registro de nuevas opciones
function registrarOpcion() {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  //modal para ingresar los nuevos datos
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

  //cierra el mdoal
  const btnCerrarModal = document.getElementById("cancel-tournament-modal");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });

  //al hacer click en el boton guardar
  const guardarRecompensa = document.getElementById("btnGuardarOpcion");
  guardarRecompensa.addEventListener("click", async (event) => {
    event.preventDefault();
    //extrae los valores de los datos ingresados en el modal
    const nombre = document.getElementById("tournament-name").value;
    const cantidad = parseInt(
      document.getElementById("tournament-cantidad").value
    );
    const precio = document.getElementById("tournament-precio").value;
    const imagen = document.getElementById("tournament-game-image").files[0];

    //almacena la info en los parametros del controlador
    const dataSave3 = new FormData();
    dataSave3.append("nombre", nombre);
    dataSave3.append("cantidad", cantidad);
    dataSave3.append("precio", precio);
    dataSave3.append("imagen", imagen);

    const token = localStorage.getItem("authToken");
    //accede a la url del controlador
    try {
      const res = await fetch("/api/comprarMonedas/registrar", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: dataSave3,
      });
      //verifica que funcione
      if (!res.ok) throw new Error();
      Swal.fire("Exito", "Opcion de recarga registrada", "success");
      listarMonedas();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo registrar la opcion de recarga", "error");
    }
  });
}

//actualizacion de opciones de recarga
function actualizarOpcion(idCompra) {
  const token = localStorage.getItem("authToken");
  //accede a la url del controlador con el id de la opcion
  fetch(`/api/comprarMonedas/id/${idCompra}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la info
    .then((data) => {
      //crea un contenedor con su clase e id
      const modalContainer = document.createElement("div");
      modalContainer.id = "tournament-modal";
      modalContainer.classList.add("modal");

      //modal para la actualizacion
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
      //cierra el modal
      const btnCerrarModal = document.getElementById("cancel-tournament-modal");
      btnCerrarModal.addEventListener("click", () => {
        modalContainer.remove();
      });

      //guarda la informacion
      const guardarRecompensa = document.getElementById("btnGuardarOpcion");
      guardarRecompensa.addEventListener("click", async (event) => {
        event.preventDefault();
        //extrae los valores ingresado en el modal
        const nombre = document.getElementById("tournament-name").value;
        const cantidad = parseInt(
          document.getElementById("tournament-cantidad").value
        );
        const precio = document.getElementById("tournament-precio").value;
        const imagen = document.getElementById("tournament-game-image")
          .files[0];

          //almacena los valores en los parametros del controlador
        const dataSave3 = new FormData();
        dataSave3.append("nombre", nombre);
        dataSave3.append("cantidad", cantidad);
        dataSave3.append("precio", precio);
        dataSave3.append("imagen", imagen);

        const token = localStorage.getItem("authToken");
        //accede a la url del controlador
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
          //veridica que todo salga bien
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

//eliminacion de opcion
function eliminarMonedas(CompraID) {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  //modal de confirmacion para la eliminacion
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
