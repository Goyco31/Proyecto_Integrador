function modalTorneo(idTorneo) {
  const crearModalTorneo = document.createElement("div");
  crearModalTorneo.id = "modal-verTorneo";
  crearModalTorneo.classList.add("modal");

  //let idEquipo = localStorage.getItem("idEquipo");
  let token = localStorage.getItem("authToken");
  let idUser = localStorage.getItem("idUser");
  console.log("idUser:", idUser);

  fetch(`api/torneos/id/${idTorneo}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      console.log("Tournament data:", data);
      let modal = `
            <div class="cerrar-detalle" id="cerrar-detalle">X</div>
            <div class="torneo-detalle">
                <div class="img-inspeccion-torneo">
                    ${
                      data.bannerBase64
                        ? `<img src="data:image/png;base64,${data.bannerBase64}">`
                        : ""
                    }
                    ${
                      data.juego && data.juego.imgJuegoBase64
                        ? `<img src="data:image/png;base64,${data.juego.imgJuegoBase64}">`
                        : ""
                    }
                </div>
                <div class="contenido-inspeccion-torneo">
                    <h3>${data.nombre}</h3>
                    <p>${data.descripcion}</p>
                    <P>${data.premio}</P>
                    <p>${data.cupos}</p>
                    <p>${data.formato}</p>
                    <p>${data.estado}</p>
                    <time>${data.fecha}</time>
                    <a onclick="downloadReglamento('${
                      data.idTorneo
                    }')">Reglamento del torneo</a>`;

      if (idUser) {
        fetch(`/api/usuarios/id/${idUser}?timestamp=${new Date().getTime()}`, {
          
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
          .then((res) => res.json())
          .then((userData) => {
            console.log("userData.equipo:", userData.equipo);
            if (userData.equipo) {
              const idEquipo = userData.equipo.idEquipo;
              modal += `
                          <form method:"post" action="/api/torneos/registrarEquipoTorneo">
                          <input type="hidden" name="idTorneo" id="idTorneo" value="${data.idTorneo}">
                          <input type="hidden" name="idEquipo" id="idEquipo" value="${idEquipo}"> 
                              <button type="submit">Registrar Equipo</button>
                          </form>`;
            } else {
              modal += `<p>No estás en un equipo. Debes crear un equipo para registrarte en este torneo.</p>`;
            }
            modal += `
                    </div>
                </div>
                `;
            crearModalTorneo.innerHTML = modal;

            document.body.appendChild(crearModalTorneo);
            crearModalTorneo.style.display = "block";
            crearModalTorneo.style.backgroundColor = "white";

            const btnCerrarDetalle = document.getElementById("cerrar-detalle");
            btnCerrarDetalle.addEventListener("click", () => {
              crearModalTorneo.remove();
            });
          })
          .catch((error) => {
            console.error("Error fetching equipo data:", error);
            // Handle the error appropriately, e.g., display an error message to the user
          });
      } else {
        modal += `
                    </div>
                </div>
                `;
        crearModalTorneo.innerHTML = modal;

        document.body.appendChild(crearModalTorneo);
        crearModalTorneo.style.display = "block";
        crearModalTorneo.style.backgroundColor = "white";

        const btnCerrarDetalle = document.getElementById("cerrar-detalle");
        btnCerrarDetalle.addEventListener("click", () => {
          crearModalTorneo.remove();
        });
      }
    });
}

function downloadReglamento(torneoId) {
  const token = localStorage.getItem("authToken");
  fetch(`/api/torneos/downloadReglamento/${torneoId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => response.text())
    .then((base64Data) => {
      // Convert Base64 data to a file
      const byteCharacters = atob(base64Data);
      const byteArrays = [];

      for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);

        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }

        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }

      const blob = new Blob(byteArrays, { type: "application/pdf" });

      // Create a download link
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
