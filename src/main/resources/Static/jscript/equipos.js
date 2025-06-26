document.addEventListener("DOMContentLoaded", () => {
  // Elementos del DOM
  const grid = document.getElementById("teamGrid");
  const inspectModal = document.getElementById("inspectModal");
  const joinModal = document.getElementById("joinConfirmModal");
  const token = localStorage.getItem("authToken");

  // Debug inicial
  console.log("Elementos cargados:", {
    grid,
    inspectModal,
    joinModal,
    tokenExists: !!token
  });

  // Verificar que los modales existen
  if (!inspectModal || !joinModal) {
    console.error("Modales no encontrados:", {
      inspectModal: !!inspectModal,
      joinModal: !!joinModal
    });
    return;
  }

  let equipoSeleccionado = null;

  // ======================
  // FUNCIONES PARA MODALES
  // ======================

  /**
   * Muestra un modal y bloquea el scroll del body
   * @param {HTMLElement} modal - Elemento modal a mostrar
   */
  function showModal(modal) {
    if (!modal) {
      console.error("Intento de mostrar modal no existente");
      return;
    }

    console.log(`Mostrando modal: ${modal.id}`);
    
    // Aplicar clases para mostrar
    document.body.classList.add("modal-open");
    modal.classList.add("active");
    
    // Debug: Verificar estilos aplicados
    console.log("Estado del modal:", {
      display: window.getComputedStyle(modal).display,
      opacity: window.getComputedStyle(modal).opacity,
      visibility: window.getComputedStyle(modal).visibility
    });
  }

  /**
   * Oculta un modal y restaura el scroll del body
   * @param {HTMLElement} modal - Elemento modal a ocultar
   */
  function hideModal(modal) {
    if (!modal) return;
    
    document.body.classList.remove("modal-open");
    modal.classList.remove("active");
  }

  // ======================
  // MANEJADORES DE EVENTOS
  // ======================

  // Delegación de eventos para los botones
  grid.addEventListener("click", async (e) => {
    const target = e.target.closest(".inspect-btn, .join-btn");
    if (!target) return;

    e.preventDefault();
    e.stopPropagation();

    // Inspeccionar equipo
    if (target.classList.contains("inspect-btn")) {
      const id = target.dataset.id;
      console.log("Inspeccionar equipo ID:", id);
      
      try {
        // Mostrar loader (opcional)
        target.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Cargando...';
        
        const response = await fetch(`/api/equipos/id/${id}`, {
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        });

        if (!response.ok) {
          throw new Error(`Error ${response.status}: ${response.statusText}`);
        }

        const equipo = await response.json();
        console.log("Datos del equipo recibidos:", equipo);

        // Actualizar contenido del modal
        document.getElementById("inspectTeamName").textContent = equipo.nombre || "Equipo";
        document.getElementById("teamRegion").textContent = `Región: ${equipo.region || "N/A"}`;
        document.getElementById("teamDescription").textContent = equipo.descripcion || "Sin descripción";
        document.getElementById("membersCount").textContent = equipo.integrantes?.length || 0;

        // Renderizar miembros
        const miembrosList = document.getElementById("teamMembersList");
        miembrosList.innerHTML = equipo.integrantes?.map(miembro => `
          <div class="miembro">
            <img src="${miembro.fotoPerfil || '/imagenes/default-avatar.png'}" 
                 alt="${miembro.nickname}" 
                 class="miembro-avatar"
                 onerror="this.src='/imagenes/default-avatar.png'">
            <span>${miembro.nickname}</span>
          </div>
        `).join("") || "<p>No hay miembros aún</p>";

        equipoSeleccionado = id;
        showModal(inspectModal);
      } catch (error) {
        console.error("Error al cargar equipo:", error);
        alert(`Error: ${error.message}`);
      } finally {
        // Restaurar botón
        if (target.classList.contains("inspect-btn")) {
          target.textContent = "Inspeccionar";
        }
      }
    }

    // Unirse a equipo
    if (target.classList.contains("join-btn")) {
      const id = target.dataset.id;
      const nombre = target.dataset.nombre;
      console.log("Unirse al equipo:", { id, nombre });

      equipoSeleccionado = id;
      document.getElementById("confirmTeamName").textContent = nombre;
      showModal(joinModal);
    }
  });

  // Confirmar unión al equipo
  document.querySelector(".confirm-join-btn").addEventListener("click", async () => {
    if (!equipoSeleccionado) {
      alert("No se ha seleccionado ningún equipo");
      return;
    }

    const btn = document.querySelector(".confirm-join-btn");
    const originalText = btn.innerHTML;
    
    try {
      // Mostrar loader
      btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Uniendo...';
      btn.disabled = true;

      const response = await fetch(`/api/equipos/unirse/${equipoSeleccionado}`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Error ${response.status}`);
      }

      const resultado = await response.json();
      alert(resultado.message || "¡Te has unido al equipo exitosamente!");
      hideModal(joinModal);
      
      // Recargar después de 1 segundo para que se vea el mensaje
      setTimeout(() => location.reload(), 1000);
    } catch (error) {
      console.error("Error al unirse al equipo:", error);
      alert(error.message || "Ocurrió un error al unirse al equipo");
    } finally {
      btn.innerHTML = originalText;
      btn.disabled = false;
    }
  });

  // Cerrar modales
  document.querySelectorAll(".close-button, .cancel-join-btn").forEach(btn => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      hideModal(inspectModal);
      hideModal(joinModal);
    });
  });

  // Cerrar al hacer clic fuera del modal
  window.addEventListener("click", (e) => {
    if (e.target === inspectModal) hideModal(inspectModal);
    if (e.target === joinModal) hideModal(joinModal);
  });

  // ======================
  // FUNCIONES AUXILIARES
  // ======================

  /**
   * Carga los equipos desde la API
   */
  async function cargarEquipos() {
    try {
      console.log("Cargando equipos...");
      
      const response = await fetch("/api/equipos", {
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!response.ok) {
        throw new Error(`Error ${response.status}: ${response.statusText}`);
      }

      const equipos = await response.json();
      console.log("Equipos recibidos:", equipos);
      
      renderizarEquipos(equipos);
    } catch (error) {
      console.error("Error al cargar equipos:", error);
      grid.innerHTML = `
        <div class="error-message">
          <i class="fas fa-exclamation-triangle"></i>
          <p>${error.message || "Error al cargar equipos"}</p>
          <button onclick="location.reload()">Reintentar</button>
        </div>
      `;
    }
  }

  /**
   * Renderiza los equipos en el grid
   * @param {Array} equipos - Lista de equipos a renderizar
   */
  function renderizarEquipos(equipos) {
    if (!equipos || equipos.length === 0) {
      grid.innerHTML = `
        <div class="no-teams">
          <i class="fas fa-users-slash"></i>
          <p>No hay equipos disponibles</p>
        </div>
      `;
      return;
    }

    grid.innerHTML = equipos.map(equipo => `
      <div class="team-card">
        <img src="${equipo.logoUrl || '/imagenes/default-logo.png'}" 
             class="team-logo" 
             alt="${equipo.nombre}"
             onerror="this.src='/imagenes/default-logo.png'">
        <h3 class="team-name">${equipo.nombre}</h3>
        <p class="team-members">${equipo.cantidadMiembros || 0}/5 Miembros</p>
        <div class="team-card-actions">
          <button class="btn inspect-btn" data-id="${equipo.id}">
            <i class="fas fa-search"></i> Inspeccionar
          </button>
          <button class="btn join-btn" data-id="${equipo.id}" data-nombre="${equipo.nombre}">
            <i class="fas fa-user-plus"></i> Unir
          </button>
        </div>
      </div>
    `).join("");
  }

  // ======================
  // HERRAMIENTAS DE DEBUG
  // ======================

  // Exponer funciones para pruebas
  window.debugModales = {
    mostrarInspect: () => showModal(inspectModal),
    mostrarJoin: () => showModal(joinModal),
    ocultarInspect: () => hideModal(inspectModal),
    ocultarJoin: () => hideModal(joinModal),
    verificarModales: () => ({
      inspectModal: {
        existe: !!inspectModal,
        visible: inspectModal?.classList.contains("active")
      },
      joinModal: {
        existe: !!joinModal,
        visible: joinModal?.classList.contains("active")
      }
    })
  };

  console.log("Debug modales disponible: window.debugModales");

  // ======================
  // INICIALIZACIÓN
  // ======================

  // Cargar equipos iniciales
  cargarEquipos();
});