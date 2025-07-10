//listar torneos
function listarTorneos() {
  const token = localStorage.getItem("authToken");
  //accede a la url del controlador
  fetch("/api/torneos/lista", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
  //conviert la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la info
    .then((data) => {
      //tabla para la info que aparecera en la vista del admin
      let tabla = `
        <div class="tournament-actions">
            <button id="new-tournament-btn" class="btn-primary"  onclick="registrarTorneo()">
              Nuevo Torneo
            </button>
            <form method="get" action="/ver/excel/torneos">
              <button type="submit" class="btn-primary">Exportar un Excel</button>
            </form>
        </div>
                   <table class="tournaments-table">
                          <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Descripcion</th>
                                <th>Tipo</th>
                                <th>Fecha Inicio</th>
                                <th>Premio</th>
                                <th>Cupos</th>
                                <th>Formato</th>
                                <th>Reglamento</th>
                                <th>Estado</th>
                                <th>Juego</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>`;
      //ingresa la informacion del torneo a la tabla
      data.forEach((l) => {
        tabla += `<tbody class="tournaments-table-body">
                          <td>${l.idTorneo}</td>
                                <td>${l.nombre}</td>
                                <td>${l.descripcion}</td>
                                <td>${l.tipo}</td>
                                <td>${l.fecha}</td>
                                <td>${l.premio}</td>
                                <td>${l.cupos}</td>
                                <td>${l.formato}</td>
                                <td>
                                  <a href="#" onclick="downloadReglamento('${l.idTorneo}')">Descargar</a>
                                </td>
                                <td>${l.estado}</td>
                                <td>${l.juego}</td>
                                <td>
                                  <button class="btn-secondary edit-btn" data-id="${l.idTorneo}" onclick="actualizarTorneo(${l.idTorneo})">Editar</button>
                                  <button class="btn-danger delete-btn" data-torneo-id="${l.idTorneo}" onclick="eliminarTorneo(${l.idTorneo})">Eliminar</button>
                                </td>
                              </tbody>`;
      });
      tabla += `</table>`;
      document.getElementById("contenedor-tablas").innerHTML = tabla;
    });
}

//descarga el documento de reglamento
function downloadReglamento(torneoId) {
  const token = localStorage.getItem("authToken");
  //accede a la url del controlador
  fetch(`/api/torneos/downloadReglamento/${torneoId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })//convierte la respuesta a un String
    .then((response) => response.text())
    .then((base64Data) => {
      // decodifica el string a binario 
      const byteCharacters = atob(base64Data);
      const byteArrays = [];

      // Divide los caracteres en fragmentos de 512 bytes para convertirlos a bytes
      for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);

        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          // Convierte cada caracter a su código ASCII
          byteNumbers[i] = slice.charCodeAt(i);
        }

        // Crea un arreglo de bytes y lo agrega a la lista
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }

      const blob = new Blob(byteArrays, { type: "application/pdf" });

      //crea un link para descargar
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = `reglamento_${torneoId}.pdf`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    })
    .catch((error) => {
      console.error("Error downloading reglamento:", error);
    });
}
//registro de un nuevo torneo
function registrarTorneo() {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "tournament-modal";
  modalContainer.classList.add("modal");

  //datos para acceder a las url del controlador
  const modalTitle = "Registrar torneo";
  const fetchUrl = "/api/torneos/registrar";
  const fetchMethod = "POST";
  const successMessage = "Torneo registrado";
  const errorMessage = "No se pudo registrar el torneo";

  //crea el modal para registar torneos
  modalContainer.innerHTML = `
    <div class="modal-content">
      <span class="close">&times;</span>
      <h2>${modalTitle}</h2>
      <form id="tournament-form" class="admin-form">
        <div class="form-group">
          <label for="tournament-name">Nombre:</label>
          <input type="text" id="tournament-name" required />
        </div>
        <div class="form-group">
          <label for="tournament-description">Descripción:</label>
          <textarea id="tournament-description" rows="3" required></textarea>
        </div>
        <div class="form-group">
          <label for="tournament-type">Tipo:</label>
          <select name="" id="tournament-type">
            <option value="Premium">Premium</option>
            <option value="Pro">Pro</option>
            <option value="Open">Open</option>
          </select>
        </div>
        <div class="form-group">
          <label for="tournament-game-image">Imagen del Juego (Banner):</label>
          <input type="file" id="tournament-game-image" required />
        </div>
        <div class="form-group">
          <label for="tournament-date">Fecha:</label>
          <input type="date" name="" id="tournament-date" />
        </div>
        <div class="form-group">
          <label for="tournament-prize">Premio:</label>
          <input
            type="number"
            id="tournament-prize"
            required
            placeholder="Ej: 🏆 00 monedas"
          />
        </div>
        <div class="form-group">
          <label for="tournament-slots">Cupos:</label>
          <input
            type="number"
            id="tournament-slots"
            required
            placeholder="Ej: 00/00 equipos"
          />
        </div>
        <div class="form-group">
          <label for="tournament-format">Formato:</label>
          <input
            type="text"
            id="tournament-format"
            required
            placeholder="Ej: Eliminación Doble, Bo3"
          />
        </div>
        <div class="form-group">
          <label for="tournament-rules-link">Archivo de Reglamento:</label>
          <input type="file" id="tournament-rules-link" required />
        </div>
        <div class="form-group">
          <label for="tournament-status">Estado:</label>
          <select id="tournament-status">
            <option value="Activo">Activo</option>
            <option value="Finalizado">Finalizado</option>
            <option value="Cancelado">Cancelado</option>
          </select>
        </div>
        <div class="form-group">
          <label for="tournament-juego">ID del juego</label>
          <input type="number" id="tournament-juego">
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary" id="guardarTorneoBtn">Guardar</button>
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
  const closeBtn = document.getElementById("cancel-tournament-modal");
  closeBtn.addEventListener("click", () => {
    modalContainer.remove();
  });

  //guarda la informacion 
  const guardarTorneoBtn = modalContainer.querySelector("#guardarTorneoBtn");
  guardarTorneoBtn.addEventListener("click", async (event) => {
    event.preventDefault();
    //extrae los valores ingresado al modal
    const nombre = document.getElementById("tournament-name").value;
    const descripcion = document.getElementById("tournament-description").value;
    const tipo = document.getElementById("tournament-type").value;
    const banner = document.getElementById("tournament-game-image").files[0];
    const fecha = document.getElementById("tournament-date").value;
    const premio = parseFloat(
      document.getElementById("tournament-prize").value
    );
    const cupos = parseFloat(document.getElementById("tournament-slots").value);
    const formato = document.getElementById("tournament-format").value;
    const docReglamento = document.getElementById("tournament-rules-link")
      .files[0];
    const estado = document.getElementById("tournament-status").value;
    const juego = document.getElementById("tournament-juego").value;

    //guarda los datos en los parametros del controlador
    const formData = new FormData();
    formData.append("nombre", nombre);
    formData.append("descripcion", descripcion);
    formData.append("tipo", tipo);
    formData.append("banner", banner);
    formData.append("fecha", fecha);
    formData.append("premio", premio);
    formData.append("cupos", cupos);
    formData.append("formato", formato);
    formData.append("docReglamento", docReglamento);
    formData.append("estado", estado);
    formData.append("juego", juego);

    const token = localStorage.getItem("authToken");
    try {
      //accede a la url del navegador con una peticion Post
      const res = await fetch(fetchUrl, {
        method: fetchMethod,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });
      //verifica que todo este bien
      if (!res.ok) throw new Error();
      Swal.fire("Éxito", successMessage, "success");
      listarTorneos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", errorMessage, "error");
    }
  });
}

//actualiza la info d eun torneo
function actualizarTorneo(torneoId) {
  const token = localStorage.getItem("authToken");
  const modalTitle = "Actualizar torneo";
  //accede a la url del contrlador
  fetch(`/api/torneos/id/${torneoId}`, {
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

      //mosal para actualizar la info del torneo
      modalContainer.innerHTML = `
          <div class="modal-content">
            <span class="close">&times;</span>
            <h2>${modalTitle}</h2>
            <form id="tournament-form" class="admin-form">
              
              <div class="form-group">
                <label for="tournament-name">Nombre:</label>
                <input type="text" id="tournament-name" required value="${
                  data.nombre
                }"/>
              </div>
              <div class="form-group">
                <label for="tournament-description">Descripción:</label>
                <textarea id="tournament-description" rows="3" required>${
                  data.descripcion
                }</textarea>
              </div>
              <div class="form-group">
                <label for="tournament-type">Tipo:</label>
                <select name="" id="tournament-type">
                  <option value="Premium" ${
                    data.tipo === "Premium" ? "selected" : ""
                  }>Premium</option>
                  <option value="Pro" ${
                    data.tipo === "Pro" ? "selected" : ""
                  }>Pro</option>
                  <option value="Open" ${
                    data.tipo === "Open" ? "selected" : ""
                  }>Open</option>
                </select>
              </div>
              <div class="form-group">
                <label for="tournament-game-image">Imagen del Juego (Banner):</label>
                <input type="file" id="tournament-game-image"/>
              </div>
              <div class="form-group">
                <label for="tournament-date">Fecha:</label>
                <input type="date" name="" id="tournament-date" value="${
                  data.fecha
                }"/>
              </div>
              <div class="form-group">
                <label for="tournament-prize">Premio:</label>
                <input
                  type="number"
                  id="tournament-prize"
                  required
                  placeholder="Ej: 🏆 00 monedas" value="${data.premio}"
                />
              </div>
              <div class="form-group">
                <label for="tournament-slots">Cupos:</label>
                <input
                  type="number"
                  id="tournament-slots"
                  required
                  placeholder="Ej: 00/00 equipos" value="${data.cupos}"
                />
              </div>
              <div class="form-group">
                <label for="tournament-format">Formato:</label>
                <input
                  type="text" id="tournament-format"
                  required
                  placeholder="Ej: Eliminación Doble, Bo3" value="${
                    data.formato
                  }"
                />
              </div>
              <div class="form-group">
                <label for="tournament-rules-link">Archivo de Reglamento:</label>
                <input type="file" id="tournament-rules-link"/>
              </div>
              <div class="form-group">
                <label for="tournament-status">Estado:</label>
                <select id="tournament-status">
                  <option value="Activo" ${
                    data.estado === "Activo" ? "selected" : ""
                  }>Activo</option>
                  <option value="Finalizado" ${
                    data.estado === "Finalizado" ? "selected" : ""
                  }>Finalizado</option>
                  <option value="Cancelado" ${
                    data.estado === "Cancelado" ? "selected" : ""
                  }>Cancelado</option>
                </select>
              </div>
              <div class="form-group">
                <label for="tournament-juego">ID del juego</label>
                <input type="text" id="tournament-juego" value="${data.juego}"/>
              </div>
              <div class="form-actions">
                  <button type="submit" class="btn-primary" id="guardarTorneoBtn">Guardar</button>
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
      const closeBtn = document.getElementById("cancel-tournament-modal");
      closeBtn.addEventListener("click", () => {
        modalContainer.remove();
      });

      //guarda la nueva info
      const guardarTorneoBtn =
        modalContainer.querySelector("#guardarTorneoBtn");

      guardarTorneoBtn.addEventListener("click", async (event) => {
        event.preventDefault();
        //extrae los valores de los nuevos datos ingresados
        const nombre = document.getElementById("tournament-name").value;
        const descripcion = document.getElementById(
          "tournament-description"
        ).value;
        const tipo = document.getElementById("tournament-type").value;
        const banner = document.getElementById("tournament-game-image")
          .files[0];
        const fecha = document.getElementById("tournament-date").value;
        const premio = parseFloat(
          document.getElementById("tournament-prize").value
        );
        const cupos = parseFloat(
          document.getElementById("tournament-slots").value
        );
        const formato = document.getElementById("tournament-format").value;
        const docReglamento = document.getElementById("tournament-rules-link")
          .files[0];
        const estado = document.getElementById("tournament-status").value;
        const juego = document.getElementById("tournament-juego").value;

        //guarda los valores a los parametros del controlador
        const formData = new FormData();
        formData.append("nombre", nombre);
        formData.append("descripcion", descripcion);
        formData.append("tipo", tipo);
        formData.append("banner", banner);
        formData.append("fecha", fecha);
        formData.append("premio", premio);
        formData.append("cupos", cupos);
        formData.append("formato", formato);
        formData.append("docReglamento", docReglamento);
        formData.append("estado", estado);
        formData.append("juego", juego);

        const token = localStorage.getItem("authToken");
        try {
          //hace una peticion put al controlador
          const res = await fetch(`/api/torneos/actualizar/id/${torneoId}`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: formData,
          });
          //verifica que todo salga bien
          if (!res.ok) throw new Error();
          Swal.fire("Éxito", "Torneo actualizado", "success");
          listarTorneos();
          modalContainer.remove();
        } catch (error) {
          Swal.fire("Error", "No se pudo actualizar", "error");
        }
      });
    });
}


//elimina el torneo
function eliminarTorneo(idTorneo) {
  //crea un contenedor con su clase e id
  const modalContainer = document.createElement("div");
  modalContainer.id = "confirm-modal";
  modalContainer.classList.add("modal");

  //modal de confirmacion
  modalContainer.innerHTML = `
        <div class="modal-content small-modal">
          <p>¿Seguro que desea eliminar este torneo?😕</p>
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

  // Botón Cancelar
  const btnCerrarModal = modalContainer.querySelector("#cancel-delete-btn");
  btnCerrarModal.addEventListener("click", () => {
    modalContainer.remove();
  });

  // Botón Eliminar
  const btnConfirmar = modalContainer.querySelector("#confirm-delete-btn");
  btnConfirmar.onclick = async function (event) {
    event.preventDefault();
    const token = localStorage.getItem("authToken");

    try {
      //hece la peticion delete al controlador
      const res = await fetch(`/api/torneos/eliminar/id/${idTorneo}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      //verifica que todo salga bien
      if (!res.ok) throw new Error();
      Swal.fire("Éxito", "Torneo eliminado", "success");
      listarTorneos();
      modalContainer.remove();
    } catch (error) {
      Swal.fire("Error", "No se pudo eliminar el torneo", "error");
    }
  };
  
}
