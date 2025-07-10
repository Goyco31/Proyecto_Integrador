//funcion para mostrar los detalles del torneo
function modalTorneo(idTorneo) {
  //crea un contenedor con su clase e id
  const crearModalTorneo = document.createElement("div");
  crearModalTorneo.id = "modal-verTorneo";
  crearModalTorneo.classList.add("modal");

  let token = localStorage.getItem("authToken");
  let idUser = localStorage.getItem("idUser");
  console.log("idUser:", idUser);

  //hace una peticion get al controlador buscando el torneo seleccionado
  fetch(`api/torneos/id/${idTorneo}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
  //convierte la respuesta a formato JSON
    .then((r) => r.json())
    //mapea la info
    .then((data) => {
      console.log("Tournament data:", data);
      //modal para los detalles del torneo
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
                    <p >Quedan <span>${data.cupos}</span> cupos disponibles</p>
                    <time>${data.fecha}</time>
                    <a onclick="downloadReglamento('${data.idTorneo
        }')">Reglamento del torneo</a>`;

      if (token) {
        //hace una peticion get al controlador de usuarios
        fetch(`/api/usuarios/me`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        //convierte la respuesta a formato JSON
          .then((res) => res.json())
          //mapea la info
          .then((userData) => {
            console.log("userData.equipo:", userData.equipo);
            if (userData.equipo) {
              const idEquipo = userData.equipo.idEquipo;

              console.log("idEquipo", idEquipo);
              console.log("idTorneo", parseInt(idTorneo));
              //agre un boton de registro de equipos si es que el usuario tiene un equipo
              modal += `
                          <div class="registro-contenedor">
                            <input type="hidden" name="idTorneo" id="idTorneo" value="${idTorneo}">
                            <input type="hidden" name="idEquipo" id="idEquipo" value="${idEquipo}">
                            <button class="btn-registro-equipo" id="btn-registro-equipo">Registrar Equipo</button>
                          </div>
                          `;
            } else {
              //si el usuario no tiene un equipo le saldra un mensaje
              modal += `<p>No estás en un equipo. Debes crear un equipo para registrarte en este torneo.</p>`;
            }
            modal += `
                    </div>
                </div>
                `;
            crearModalTorneo.innerHTML = modal;

            //abre el modal
            document.body.appendChild(crearModalTorneo);
            crearModalTorneo.style.display = "block";
            crearModalTorneo.style.backgroundColor = "white";

            //rastrea el boton de registrar equipo con su info
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
                //ingresa los parametros a la funcion registroEquipo
                registroEquipo(idEquipo, idTorneo);
              });
            }
            //cierra el modal
            const btnCerrarDetalle = document.getElementById("cerrar-detalle");
            btnCerrarDetalle.addEventListener("click", () => { 
              crearModalTorneo.remove();
            });
          })
          .catch((error) => {
            console.error("Error:", error);
          });
      } else {
        modal += `
                    </div>
                </div>
                `;
        crearModalTorneo.innerHTML = modal;

        //abre el modal cn 
        document.body.appendChild(crearModalTorneo);
        crearModalTorneo.style.display = "block";
        crearModalTorneo.style.backgroundColor = "white";

        //cierra el modal
        const btnCerrarDetalle = document.getElementById("cerrar-detalle");
        btnCerrarDetalle.addEventListener("click", () => {
          crearModalTorneo.remove();
        });
      }
    });
}

//registro de un equipo a un torneo
async function registroEquipo(idEquipo, idTorneo) {
  const token = localStorage.getItem("authToken");
  try {
    //hace una peticion POST a la url del contrlador
    const res = await fetch(
      `/api/torneos/registrarEquipo/${idEquipo}/Torneo/${idTorneo}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    //verifica que todo salga bien
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
      "Registro no se pudo concretar",
      "error"
    );
  }
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