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
            
            <div class="torneo-detalle">
            <div class="cerrar-detalle" id="cerrar-detalle">X</div>
                <div class="img-inspeccion-torneo">
                    ${data.bannerBase64
          ? `<img src="data:image/png;base64,${data.bannerBase64}">`
          : ""
        }
                    ${data.juego && data.juego.imgJuegoBase64
          ? `<img src="data:image/png;base64,${data.juego.imgJuegoBase64}">`
          : ""
        }
                </div>
                <div class="contenido-inspeccion-torneo">
                    <h3>${data.nombre}</h3>
                    <p class="descripcion-torneo">${data.descripcion}</p>
                    <P class="premio-torneo">${data.premio}</P>
                    <p class="cupos-torneo">${data.cupos}</p>
                    <p class="formato-torneo">${data.formato}</p>
                    <p class="estado-torneo" data-estado="${data.estado}">${data.estado}</p>
                    <time>${data.fecha}</time>
                    <a onclick="downloadReglamento('${data.idTorneo
        }')">Reglamento del torneo</a>`;

      if (token) {
        fetch(`/api/usuarios/me`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
          .then((res) => res.json())
          .then((userData) => {
            console.log("userData.equipo:", userData.equipo);
            if (userData.equipo) {
              const idEquipo = userData.equipo.idEquipo;

              console.log("idEquipo", idEquipo);
              console.log("idTorneo", parseInt(idTorneo));
              modal += `
                          <div class="registro-contenedor">
                            <input type="hidden" name="idTorneo" id="idTorneo" value="${idTorneo}">
                            <input type="hidden" name="idEquipo" id="idEquipo" value="${idEquipo}">
                            <button class="btn-registro-equipo" id="btn-registro-equipo">Registrar Equipo</button>
                          </div>
                          `;
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

            const btnRegistrar = crearModalTorneo.querySelector(
              ".btn-registro-equipo"
            );
            if (btnRegistrar) {
              btnRegistrar.addEventListener("click", function () {
                const carta = this.closest(".registro-contenedor");
                const idTorneo = carta.querySelector(
                  "input[name='idTorneo']"
                ).value;
                const idEquipo = carta.querySelector(
                  "input[name='idEquipo']"
                ).value;
                registroEquipo(idEquipo, idTorneo);
              });
            }

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

async function registroEquipo(idEquipo, idTorneo) {
  const token = localStorage.getItem("authToken");
  try {
    const res = await fetch(
      `/api/torneos/registrarEquipo/${idEquipo}/Torneo/${idTorneo}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    if (!res.ok) throw new Error();
    Swal.fire("Equipo Registrado", "Tu equipo se registró en el torneo ¡Prepárate para la batalla!", "success")
      .then(result => {
        if(result.isConfirmed){
          window.location.href ="/paginaTorneo"    
        }
      });
  } catch (error) {
    Swal.fire(
      "Error",
      "Tu equipo no cumple con los integrantes necesarios",
      "error"
    );
  }
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
