document.addEventListener('DOMContentLoaded', () => {
    let tournamentsData = JSON.parse(localStorage.getItem('adminTournamentsData')) || {
        // Datos de ejemplo preestablecidos si no hay datos guardados en localStorage
        "dota-julio-premium": {
            id: "dota-julio-premium", // ID único para el torneo
            title: "Torneo DOTA 2 - Edición Premium Julio",
            gameImage: "/Imagenes/dota-pro-banner.webp",
            gameIcon: "/Imagenes/dota2-icon.png",
            date: "📅 20 Julio | 🕒 8:00 PM",
            prize: "🏆 $100 PEN + Puntos de canjeto en SK-Tienda",
            description: "¡La élite de Dota 2 se enfrenta! Participa en nuestro torneo premium y demuestra que eres el mejor. Estrategia, habilidad y trabajo en equipo te llevarán a la victoria.",
            slots: "16/32 equipos",
            format: "Eliminación Doble, Bo3",
            rulesLink: "/Reglamentos_PDF/reglamento-dota-julio.pdf",
            registerLink: "/inscripcion-dota.html",
            status: "activo", // Agregamos el estado para la administración (nuevo campo)
            type: "Premium" // Agregamos el tipo para la administración (nuevo campo)
        },
        "dota-agosto-pro": {
            id: "dota-agosto-pro",
            title: "DOTA 2 Pro Challenge - Agosto",
            gameImage: "/Imagenes/dota-pro-banner.webp",
            gameIcon: "/Imagenes/dota2-icon.png",
            date: "📅 15 Agosto | 🕒 7:30 PM",
            prize: "🏆 $200 PEN + Puntos de canjeto en SK-Tienda",
            description: "Sube de nivel y compite contra profesionales. Grandes premios y la oportunidad de hacerte un nombre en la escena.",
            slots: "8/16 equipos",
            format: "Eliminación Directa, Bo5 Finales",
            rulesLink: "/Reglamentos_PDF/reglamento-dota-agosto.pdf",
            registerLink: "/inscripcion-dota.html",
            status: "activo",
            type: "Pro"
        },
        "csgo-julio-open": {
            id: "csgo-julio-open",
            title: "CS:GO Open Series - Julio",
            gameImage: "/Imagenes/csgo-tournament-banner.png",
            gameIcon: "/Imagenes/csgo-icon.png",
            date: "📅 25 Julio | 🕒 9:00 PM",
            prize: "🏆 $250 PEN + Puntos de canjeto en SK-Tienda",
            description: "Demuestra tu puntería y tácticas en el CS:GO Open. Abierto a todos los niveles. ¡No te quedes fuera!",
            slots: "24/32 equipos",
            format: "Sistema Suizo + Playoffs",
            rulesLink: "/Reglamentos_PDF/reglamento-csgo-julio.pdf",
            registerLink: "/registro-torneo-csgo.html",
            status: "activo",
            type: "Open"
        }
    };

    // Función auxiliar para guardar el objeto tournamentsData en localStorage
    const saveTournamentsData = () => {
        localStorage.setItem('adminTournamentsData', JSON.stringify(tournamentsData));
    };

    // === ELEMENTOS DEL DOM ===
    // Se obtienen referencias a los elementos HTML con los que se interactuará.
    const adminSidebar = document.querySelector('.admin-sidebar');
    const adminSections = document.querySelectorAll('.admin-section'); // Todas las secciones de contenido
    const showTournamentsBtn = document.getElementById('show-tournaments');
    const showDbConfigBtn = document.getElementById('show-db-config');

    const tournamentsSection = document.getElementById('tournaments-section');
    const dbConfigSection = document.getElementById('db-config-section');

    const tournamentsTableBody = document.getElementById('tournaments-table-body');
    const noTournamentsMessage = document.getElementById('no-tournaments-message');
    const newTournamentBtn = document.getElementById('new-tournament-btn');
    const exportBtn = document.getElementById('export-btn');
    const importBtn = document.getElementById('import-btn');

    const tournamentModal = document.getElementById('tournament-modal');
    const modalTitle = document.getElementById('modal-title');
    const tournamentForm = document.getElementById('tournament-form');
    const closeButtons = document.querySelectorAll('.close-button'); // Nodelist para todos los botones de cerrar modales
    const cancelTournamentModalBtn = document.getElementById('cancel-tournament-modal');

    // Campos del formulario de torneo
    const tournamentIdInput = document.getElementById('tournament-id'); // Campo oculto para el ID
    const tournamentNameInput = document.getElementById('tournament-name');
    const tournamentDescriptionInput = document.getElementById('tournament-description');
    const tournamentTypeInput = document.getElementById('tournament-type');
    const tournamentGameImageInput = document.getElementById('tournament-game-image');
    const tournamentGameIconInput = document.getElementById('tournament-game-icon');
    const tournamentDateInput = document.getElementById('tournament-date');
    const tournamentPrizeInput = document.getElementById('tournament-prize');
    const tournamentSlotsInput = document.getElementById('tournament-slots');
    const tournamentFormatInput = document.getElementById('tournament-format');
    const tournamentRulesLinkInput = document.getElementById('tournament-rules-link');
    const tournamentRegisterLinkInput = document.getElementById('tournament-register-link');
    const tournamentStatusSelect = document.getElementById('tournament-status'); // Selector de estado

    const confirmModal = document.getElementById('confirm-modal');
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const cancelDeleteBtn = document.getElementById('cancel-delete-btn');
    let tournamentToDeleteId = null; // Variable para almacenar el ID del torneo a eliminar

    // Elementos del formulario de configuración de BD
    const dbConfigForm = document.getElementById('db-config-form');
    const connectDbBtn = document.getElementById('connect-db-btn');
    const downloadDbBtn = document.getElementById('download-db-btn');
    const cancelDbConfigBtn = document.getElementById('cancel-db-config');

    // === FUNCIONES DE INTERFAZ ===

    // Función para mostrar una sección específica y ocultar las demás
    const showSection = (sectionToShow) => {
        adminSections.forEach(section => {
            section.classList.remove('active'); // Oculta todas las secciones
        });
        sectionToShow.classList.add('active'); // Muestra la sección deseada
    };

    // Función para renderizar (dibujar) la tabla de torneos con los datos actuales
    const renderTournamentsTable = () => {
        tournamentsTableBody.innerHTML = ''; // Limpiar contenido actual de la tabla

        const tournamentsArray = Object.values(tournamentsData); // Convierte el objeto de torneos a un array

        // Muestra u oculta el mensaje "No hay torneos"
        if (tournamentsArray.length === 0) {
            noTournamentsMessage.style.display = 'block';
            return;
        } else {
            noTournamentsMessage.style.display = 'none';
        }

        // Itera sobre cada torneo y crea una fila en la tabla
        tournamentsArray.forEach(tournament => {
            const row = tournamentsTableBody.insertRow();
            row.dataset.tournamentId = tournament.id; // Almacena el ID del torneo en el atributo data-id de la fila

            // Rellena la fila con los datos del torneo
            row.innerHTML = `
                <td>${tournament.title}</td>
                <td>${tournament.type}</td>
                <td>N/A</td> <td>${tournament.prize}</td>
                <td>${tournament.format}</td>
                <td>${tournament.date.split('|')[0].trim()}</td> <td>N/A</td> <td><span class="status-badge status-${tournament.status}">${tournament.status.toUpperCase()}</span></td>
                <td class="actions-column">
                    <button class="btn-secondary edit-btn" data-id="${tournament.id}">Editar</button>
                    <button class="btn-danger delete-btn" data-id="${tournament.id}">Eliminar</button>
                </td>
            `;
        });
    };

    // Función para abrir el modal de torneo, ya sea para crear uno nuevo o editar uno existente
    const openTournamentModal = (tournament = null) => {
        tournamentForm.reset(); // Limpia todos los campos del formulario al abrir el modal

        if (tournament) {
            // Si se pasa un objeto torneo, se está editando
            modalTitle.textContent = 'Editar Torneo';
            tournamentIdInput.value = tournament.id;
            tournamentNameInput.value = tournament.title;
            tournamentDescriptionInput.value = tournament.description;
            tournamentTypeInput.value = tournament.type;
            tournamentGameImageInput.value = tournament.gameImage;
            tournamentGameIconInput.value = tournament.gameIcon;
            tournamentDateInput.value = tournament.date;
            tournamentPrizeInput.value = tournament.prize;
            tournamentSlotsInput.value = tournament.slots;
            tournamentFormatInput.value = tournament.format;
            tournamentRulesLinkInput.value = tournament.rulesLink;
            tournamentRegisterLinkInput.value = tournament.registerLink;
            tournamentStatusSelect.value = tournament.status; // Rellena el selector de estado
        } else {
            // Si no se pasa un objeto torneo, se está creando uno nuevo
            modalTitle.textContent = 'Nuevo Torneo';
            tournamentIdInput.value = ''; // Asegura que el campo ID esté vacío para un nuevo torneo
        }
        tournamentModal.style.display = 'flex'; // Muestra el modal (con display flex para centrar)
    };

    // Función para cerrar cualquier modal que esté abierto
    const closeAllModals = () => {
        tournamentModal.style.display = 'none';
        confirmModal.style.display = 'none';
    };

    // === MANEJADORES DE EVENTOS ===

    // Eventos para la navegación de la barra lateral
    showTournamentsBtn.addEventListener('click', (e) => {
        e.preventDefault(); // Evita el comportamiento predeterminado del enlace
        showSection(tournamentsSection); // Muestra la sección de torneos
        renderTournamentsTable(); // Vuelve a cargar la tabla para reflejar cualquier cambio
    });

    showDbConfigBtn.addEventListener('click', (e) => {
        e.preventDefault();
        showSection(dbConfigSection); // Muestra la sección de configuración de BD
    });

    // Evento para el botón "Nuevo Torneo"
    newTournamentBtn.addEventListener('click', () => openTournamentModal());

    // Eventos para los botones "EXPORTAR" e "IMPORTAR" (simulados)
    exportBtn.addEventListener('click', () => {
        alert('Funcionalidad de EXPORTAR aún no implementada en el frontend. Necesita un backend.');
        // En un backend real, aquí se haría una solicitud HTTP para descargar los datos.
        // fetch('/api/tournaments/export').then(response => response.blob()).then(blob => { ... });
    });

    importBtn.addEventListener('click', () => {
        alert('Funcionalidad de IMPORTAR aún no implementada en el frontend. Necesita un backend.');
        // En un backend real, aquí se abriría un input de tipo file para que el usuario suba un archivo.
        // const input = document.createElement('input'); input.type = 'file'; input.click();
    });

    // Delegación de eventos para los botones de Editar y Eliminar dentro de la tabla
    // Esto es eficiente porque un solo event listener maneja los clics en todos los botones de la tabla,
    // incluso los que se añaden dinámicamente.
    tournamentsTableBody.addEventListener('click', (e) => {
        if (e.target.classList.contains('edit-btn')) {
            const id = e.target.dataset.id; // Obtiene el ID del torneo del atributo data-id del botón
            const tournament = tournamentsData[id];
            if (tournament) {
                openTournamentModal(tournament); // Abre el modal en modo edición
            }
        } else if (e.target.classList.contains('delete-btn')) {
            tournamentToDeleteId = e.target.dataset.id; // Guarda el ID para la confirmación
            confirmModal.style.display = 'flex'; // Muestra el modal de confirmación
        }
    });

    // Manejo del envío del formulario de torneo (Guardar/Actualizar)
    tournamentForm.addEventListener('submit', (e) => {
        e.preventDefault(); // Previene el envío del formulario por defecto (recarga de página)

        // Genera un ID único si el torneo es nuevo, o usa el ID existente si se está editando
        const id = tournamentIdInput.value || `tournament-${Date.now()}`;
        const newTournament = {
            id: id,
            title: tournamentNameInput.value,
            description: tournamentDescriptionInput.value,
            type: tournamentTypeInput.value,
            gameImage: tournamentGameImageInput.value,
            gameIcon: tournamentGameIconInput.value,
            date: tournamentDateInput.value,
            prize: tournamentPrizeInput.value,
            slots: tournamentSlotsInput.value,
            format: tournamentFormatInput.value,
            rulesLink: tournamentRulesLinkInput.value,
            registerLink: tournamentRegisterLinkInput.value,
            status: tournamentStatusSelect.value // Guarda el estado del torneo
        };

        if (tournamentsData[id]) {
            // Si el ID ya existe, se está editando un torneo
            tournamentsData[id] = newTournament;
            alert('Torneo actualizado exitosamente!');
        } else {
            // Si el ID no existe, se está creando un nuevo torneo
            tournamentsData[id] = newTournament;
            alert('Torneo agregado exitosamente!');
        }
        saveTournamentsData(); // Guarda los datos actualizados en localStorage
        renderTournamentsTable(); // Vuelve a dibujar la tabla para mostrar los cambios
        closeAllModals(); // Cierra el modal
    });

    // Eventos para cerrar los modales (botón 'x' y botón "Cancelar" del formulario de torneo)
    closeButtons.forEach(button => {
        button.addEventListener('click', closeAllModals);
    });
    cancelTournamentModalBtn.addEventListener('click', closeAllModals);

    // Manejo de la confirmación de eliminación
    confirmDeleteBtn.addEventListener('click', () => {
        if (tournamentToDeleteId && tournamentsData[tournamentToDeleteId]) {
            delete tournamentsData[tournamentToDeleteId]; // Elimina el torneo del objeto de datos
            saveTournamentsData(); // Guarda los cambios
            renderTournamentsTable(); // Actualiza la tabla
            alert('Torneo eliminado exitosamente!');
        }
        closeAllModals(); // Cierra el modal de confirmación
        tournamentToDeleteId = null; // Resetea el ID temporal
    });
    cancelDeleteBtn.addEventListener('click', closeAllModals); // Cierra el modal de confirmación

    // Manejo de la configuración de BD (funcionalidad simulada)
    connectDbBtn.addEventListener('click', () => {
        // En un entorno real, aquí iría una solicitud HTTP al backend para intentar la conexión
        alert('Intentando conectar a la Base de Datos... (Funcionalidad simulada en el frontend)');
    });

    downloadDbBtn.addEventListener('click', () => {
        // En un entorno real, aquí iría una solicitud HTTP al backend para iniciar la descarga
        alert('Iniciando descarga de la Base de Datos... (Funcionalidad simulada en el frontend)');
    });

    cancelDbConfigBtn.addEventListener('click', (e) => {
        e.preventDefault(); // Evita el envío del formulario si es un botón de tipo submit
        alert('Configuración de BD cancelada.');
        showSection(tournamentsSection); // Vuelve a la vista de torneos
    });

    // === INICIALIZACIÓN ===
    renderTournamentsTable(); // Carga la tabla de torneos al iniciar la página
    showSection(tournamentsSection); // Asegura que la sección de torneos sea la primera en mostrarse
});