const torneosContainer = document.querySelector(".lista-torneos");

torneosContainer.addEventListener("click", (event) => {
  const card = event.target.closest(".carta-torneo");
  if (card) {
    const idTorneo = card.querySelector("#idTorneo").value;
    modalTorneo(idTorneo);
  }
});

async function modalTorneo(idTorneo) {
  const crearModalTorneo = document.createElement("div");
  crearModalTorneo.id = "modal-verTorneo";
  crearModalTorneo.classList.add("modal");

  let idEquipo = localStorage.getItem("idEquipo");
  let token = localStorage.getItem("authToken");
  fetch(`api/torneos/id/${idTorneo}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      let modal = `
            <div class="cerrar-detalle" id="cerrar-detalle">X</div>
            <div class="torneo-detalle">
                <div class="img-inspeccion-torneo">
                    <img src="'data:image/png;base64,' + ${data.bannerBase64}">
                    <img src="'data:image/png;base64, + ${data.juego.imgJuegoBase64}">
                </div>
                <div class="contenido-inspeccion-torneo">
                    <h3>${data.nombre}</h3>
                    <p>${data.descripcion}</p>
                    <P>${data.premio}</P>
                    <p>${data.cupos}</p>
                    <p>${data.formato}</p>
                    <p>${data.estado}</p>
                    <time>${data.fecha}</time>
                    <a onclick="downloadReglamento('${data.idTorneo}')">Reglamento del torneo</a>
                    <form method:"post" action="/api/torneos/registrarEquipoTorneo">
                    <input type="hidden" name="idTorneo" id="idTorneo" value="${data.idTorneo}">
                    <input type="hidden" name="idEquipo" id="idEquipo" value="${idEquipo}"> 
                        <button type="submit">Registrar Equipo</button>
                    </form>
                </div>
            </div>
            `;
      crearModalTorneo.innerHTML = modal;
    });

  document.body.appendChild(crearModalTorneo);

  const btnCerrarDetalle = document.getElementById("cerrar-detalle");
  btnCerrarDetalle.addEventListener("click", () => {
    crearModalTorneo.remove();
  });
}
