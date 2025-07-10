//funcion para listar usuarios
function listarJuegos() {
  //token de verificacion
  const token = localStorage.getItem("authToken");
  //busca al ruta
  fetch("/api/juegos/lista", {
    headers: {
      //ingresa el token de verificacion
      Authorization: `Bearer ${token}`,
    },
  })
  //convierte la respuesta al formato JSON
    .then((r) => r.json())
    //mapea la informacion
    .then((data) => {
      //insersion html dinamica para el listado de juegos
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
      //ingresa los datos de juegos a la tabla
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

//registro de un nuevo juego
function registrarJuego() {
  //crea un nuevo contenedor y le agrega su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  //modal para un nuevo registro
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
  //cierra el modal al hacer click en el boton cancelar
  const btnCerrarModal = document.getElementById("cancel-tournament-modal");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });
  //guarda la informacion del nuevo juego
  const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
  guardarRecompensa.addEventListener("click", async (event) => {
    event.preventDefault();
    //extrae el valor de los datos ingresados
    const nombre = document.getElementById("tournament-name").value;
    const genero = document.getElementById("tournament-genero").value;
    const imagen = document.getElementById("tournament-game-image").files[0];

    //guarda la info en los @RequestParam del controlador
    const dataSave2 = new FormData();
    dataSave2.append("nombre", nombre);
    dataSave2.append("genero", genero);
    dataSave2.append("imagen", imagen);

    const token = localStorage.getItem("authToken");
    try {
      //acceso a la url de registro
      const res = await fetch("/api/juegos/registrar", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: dataSave2,
      });
      //verifica si hay un error
      if (!res.ok) throw new Error();
      //mensaje de confirmacion
      Swal.fire("Exito", "juego registrado", "success");
      //muestra el nuevo juego en la tabla del admin
      listarJuegos();
      //remueve el modal de registro
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "no se pudo registrar el juego", "error");
    }
  });
}
//actualizar el juego
function actualizarJuego(idJuego) {
  const token = localStorage.getItem("authToken");
  //busca la url para actualizar el juego
  fetch(`/api/juegos/id/${idJuego}`, {
    //verfica el token
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
  //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la informacion
    .then((data) => {
      //crea un modal con su clase e id
      const modalContainer = document.createElement("div");
      modalContainer.id = "tournament-modal";
      modalContainer.classList.add("modal");

      //modal de actualizacion con los datos existentes
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

      //cierra el modal
      const btnCerrarModal = document.getElementById("cancel-tournament-modal");
      btnCerrarModal.addEventListener("click", () => {
        modalContainer.remove();
      });

      //guarda la nueva informacion
      const guardarRecompensa = document.getElementById("btnGuardarRecompensa");
      guardarRecompensa.addEventListener("click", async (event) => {
        event.preventDefault();
        const nombre = document.getElementById("tournament-name").value;
        const genero = document.getElementById("tournament-genero").value;
        const imagen = document.getElementById("tournament-game-image")
          .files[0];

          //almacena la informacion el los parametros del controlador
        const dataSave2 = new FormData();
        dataSave2.append("nombre", nombre);
        dataSave2.append("genero", genero);
        dataSave2.append("imagen", imagen);

        const token = localStorage.getItem("authToken");
        //acceso a la url que actualiza el juego
        try {
          const res = await fetch(`/api/juegos/actualizar/id/${idJuego}`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: dataSave2,
          });
          //verifica que todo salio bien
          if (!res.ok) throw new Error();
          //mensaje de confirmacion
          Swal.fire("Exito", "juego actualizado", "success");
          //muestra la nueva info en la tabla del admin
          listarJuegos();
          //cierra el modal
          modalContainer.remove();
        } catch (error) {
          Swal.fire("Error", "no se pudo actualizar el juego", "error");
        }
      });
    });
}

//elimina el juego
function eliminarJuego(idJuego) {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  //modal para confirma la eliminacion
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
    //acceso a la url del controlador
    try {
      const res = await fetch(`/api/juegos/eliminar/id/${idJuego}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      //verifica que todo salio bien
      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Juego eliminado", "success");
      listarJuegos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar el Jueog", "error");
    }
  });
}
