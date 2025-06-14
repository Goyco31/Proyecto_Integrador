document.addEventListener('DOMContentLoaded', () => {
    // Este código se ejecuta una vez que todo el contenido HTML del documento
    // ha sido completamente cargado y parseado. Es una buena práctica para
    // asegurar que todos los elementos HTML a los que intentamos acceder
    // ya existan en el DOM.

    // --- Obtención de Referencias a Elementos del DOM ---
    // Aquí se obtienen referencias a los elementos HTML que necesitaremos
    // manipular en nuestro JavaScript. Se usan sus IDs o clases para encontrarlos.

    const inspectModal = document.getElementById('inspectModal');
    // Referencia al modal de "Inspeccionar Equipo".
    const joinConfirmModal = document.getElementById('joinConfirmModal');
    // Referencia al modal de "Confirmar Unirse".
    const closeButtons = document.querySelectorAll('.close-button');
    // Referencia a todos los botones de cerrar (la 'x') dentro de los modales.
    // Aunque inicialmente puede ser una lista vacía si los modales no están en el DOM,
    // se re-obtienen cuando se adjuntan los event listeners.

    // Elementos dentro del modal de Inspeccionar que mostrarán la información del equipo
    const inspectTeamName = document.getElementById('inspectTeamName');
    // Título del nombre del equipo en el modal de inspección.
    const membersCount = document.getElementById('membersCount');
    // Contador de miembros (ej. "3/5").
    const teamMembersList = document.getElementById('teamMembersList');
    // Contenedor donde se listarán los avatares y nombres de los miembros.
    const teamWins = document.getElementById('teamWins');
    // Campo para mostrar las victorias del equipo.
    const teamLosses = document.getElementById('teamLosses');
    // Campo para mostrar las derrotas del equipo.
    const teamKDRatio = document.getElementById('teamKDRatio');
    // Campo para mostrar el ratio K/D (Kills/Deaths).
    const teamMatches = document.getElementById('teamMatches');
    // Campo para mostrar la cantidad de partidas jugadas.
    const teamRank = document.getElementById('teamRank');
    // Campo para mostrar el rango o clasificación del equipo.
    const teamRegion = document.getElementById('teamRegion');
    // Campo para mostrar la región del equipo.
    const teamDescription = document.getElementById('teamDescription');
    // Campo para mostrar la descripción del equipo.

    // Elementos dentro del modal de Confirmar Unirse
    const confirmTeamName = document.getElementById('confirmTeamName');
    // Campo para mostrar el nombre del equipo en el modal de confirmación.
    const confirmJoinBtn = document.querySelector('.confirm-join-btn');
    // Botón para confirmar la unión al equipo.
    const cancelJoinBtn = document.querySelector('.cancel-join-btn');
    // Botón para cancelar la unión.

    // Contenedor principal donde se insertarán las tarjetas de equipo dinámicamente
    const teamGrid = document.getElementById('teamGrid');
    // Referencia al div con ID 'teamGrid' en el HTML, que inicialmente está vacío.

    // --- Datos de Ejemplo de Equipos ---
    // Este objeto simula una base de datos o una respuesta de una API con la información de los equipos.
    // En una aplicación real, estos datos se cargarían desde un servidor.
    // Se ha ajustado para un máximo de 5 miembros por equipo.
    const teamsData = {
        team1: { // 'team1' es un ID único para este equipo, usado para referenciarlo.
            id: 'team1', // ID también dentro del objeto para fácil acceso.
            name: "EquipoAlfa",
            logo: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt4gWnnn9Bsaix0-sutpVqLyJVOA6Njg2GVYXClmL_FnypHUwaEp8fVDlgbgNzZQbc5eE&usqp=CAU", // URL del logo.
            members: [ // Array de objetos, cada uno representa un miembro.
                { name: "Player1", avatar: "https://thumbs.dreamstime.com/b/pegatina-avatar-de-gamer-con-atuendo-fresco-y-accesorios-contorno-estilo-juego-vector-fondo-blanco-descubra-nuestro-%C3%BAltimo-355999038.jpg" },
                { name: "Player2", avatar: "https://png.pngtree.com/png-vector/20240317/ourmid/pngtree-white-hooded-red-face-gaming-avatar-vector-mascot-png-image_11993064.png" },
                { name: "Player3", avatar: "https://img.freepik.com/fotos-premium/avatar-de-logotipo-de-jogo-para-um-canal-de-streaming-de-jogos-ou-blogueiro_327483-16438.jpg" },
                { name: "Player4", avatar: "https://img.freepik.com/vector-premium/diseno-logotipo-mascota-shinobi-esport_1189562-853.jpg" },
                { name: "Player5", avatar: "https://img.freepik.com/vector-premium/plantilla-logotipo-juego-mascota-grim-reaper_92741-735.jpg" }
            ],
            wins: 12,
            losses: 3,
            kdRatio: 4.0,
            matches: 15,
            rank: "Global Elite",
            region: "LATAM",
            description: "Somos un equipo competitivo con enfoque en el juego en equipo y la mejora constante. Buscamos jugadores dedicados y con buena comunicación. ¡Únete a la familia Alfa!"
        },
        team2: {
            id: 'team2',
            name: "GamingGods",
            logo: "https://pbs.twimg.com/profile_images/1358912925054951424/i8Ig8AUQ_400x400.jpg",
            members: [
                { name: "GamerX", avatar: "https://img.freepik.com/fotos-premium/avatar-logotipo-juego-canal-transmision-juegos-o-blogger_327483-16546.jpg" },
                { name: "NoobSlayer", avatar: "https://img.freepik.com/fotos-premium/avatar-logotipo-juego-canal-transmision-juegos-o-blogger_272306-1508.jpg" },
                { name: "ProPlayer", avatar: "https://img.freepik.com/fotos-premium/avatar-logotipo-juego-canal-transmision-juegos-o-blogger_327483-16500.jpg" }
            ],
            wins: 5,
            losses: 1,
            kdRatio: 5.0,
            matches: 6,
            rank: "Master Guardian",
            region: "Europe",
            description: "Un equipo casual pero serio, nos divertimos mientras competimos. Buscamos jugadores activos y con ganas de mejorar."
        },
        team3: {
            id: 'team3',
            name: "CyberNinjas",
            logo: "https://img.freepik.com/premium-vector/cyber-ninja-esport-logo-mascot-design_122297-209.jpg",
            members: [
                { name: "Shadow", avatar: "https://img.freepik.com/fotos-premium/avatar-logotipo-juego-canal-transmision-juegos-o-blogger_327483-16167.jpg" },
                { name: "Blade", avatar: "https://ih1.redbubble.net/image.5068742496.8109/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg" }
            ],
            wins: 8,
            losses: 2,
            kdRatio: 4.0,
            matches: 10,
            rank: "Diamond",
            region: "North America",
            description: "Sigilosos y letales. Los ninjas del ciberespacio dominan el campo de batalla."
        }
        // Puedes añadir más equipos aquí en este objeto 'teamsData'.
    };

    let currentTeamId = null;
    // Variable para almacenar el ID del equipo con el que se está interactuando
    // (por ejemplo, el equipo que se está inspeccionando o al que se intenta unir).

    // --- Funciones para Manipular Modales ---

    function openModal(modal) {
        // Añade la clase 'active' al modal, lo que lo hace visible (CSS).
        modal.classList.add('active');
        // Evita que la página principal se desplace cuando el modal está abierto.
        document.body.style.overflow = 'hidden';
    }

    function closeModal(modal) {
        // Remueve la clase 'active' del modal, ocultándolo.
        modal.classList.remove('active');
        // Restaura el desplazamiento de la página principal.
        document.body.style.overflow = '';
    }

    // --- Función para Renderizar las Tarjetas de Equipo Dinámicamente ---

    function renderTeams() {
        let teamsHtml = ''; // Variable para construir el HTML de todas las tarjetas.
        // Itera sobre cada equipo en el objeto 'teamsData'.
        for (const teamId in teamsData) {
            const team = teamsData[teamId];
            // Genera el HTML para la previsualización de miembros (solo los primeros 3 avatares).
            const membersPreviewHtml = team.members.slice(0, 3).map(member => `
                <img src="${member.avatar}" alt="${member.name}" class="member-avatar">
            `).join(''); // Une los elementos del array en una sola cadena HTML.

            // Construye la plantilla HTML para una sola tarjeta de equipo.
            // Se usan backticks (`) para las template literals, lo que permite
            // insertar variables directamente con ${}.
            teamsHtml += `
                <div class="team-card">
                    <div class="team-header-card">
                        <img src="${team.logo}" alt="Team Logo" class="team-logo">
                        <div class="team-info">
                            <span class="team-name">${team.name}</span>
                            <div class="team-members-preview">
                                ${membersPreviewHtml}
                            </div>
                        </div>
                    </div>
                    <div class="team-stats">
                        <span class="stat-item">
                            <span class="stat-value">${team.members.length}</span><br>
                            /5 Miembros </span>
                        <span class="stat-item"><span class="stat-value">${team.wins}</span><br>Victorias</span>
                        <span class="stat-item"><span class="stat-value">${team.losses}</span><br>Derrotas</span>
                    </div>
                    <div class="team-actions">
                        <button class="btn inspect-btn" data-team-id="${team.id}">Inspeccionar</button>
                        <button class="btn join-btn" data-team-id="${team.id}">Unir</button>
                    </div>
                </div>
            `;
        }
        // Inserta todo el HTML generado dentro del 'teamGrid' en el DOM.
        teamGrid.innerHTML = teamsHtml;

        // Después de que todas las tarjetas se han insertado en el DOM,
        // se adjuntan los event listeners a los botones recién creados.
        attachEventListeners();
    }

    // --- Función para Adjuntar Event Listeners ---
    // Esta función es crucial porque los botones se crean dinámicamente.
    // Necesitamos adjuntar los eventos *después* de que los elementos existan en la página.
    function attachEventListeners() {
        // Obtiene todas las referencias a los botones de "Inspeccionar" y "Unir"
        // que ahora existen en el DOM.
        const inspectButtons = document.querySelectorAll('.inspect-btn');
        const joinButtons = document.querySelectorAll('.join-btn');
        // También re-obtenemos los botones de cerrar de los modales, por si acaso
        // se recargara alguna parte de la interfaz.
        const closeButtonsArray = document.querySelectorAll('.close-button');

        // Adjunta los event listeners a los botones de "Inspeccionar".
        inspectButtons.forEach(button => {
            // Se remueve el listener primero para evitar duplicados si la función
            // 'attachEventListeners' se llama varias veces (ej. al refrescar la lista).
            button.removeEventListener('click', handleInspectClick);
            button.addEventListener('click', handleInspectClick);
        });

        // Adjunta los event listeners a los botones de "Unir".
        joinButtons.forEach(button => {
            button.removeEventListener('click', handleJoinClick);
            button.addEventListener('click', handleJoinClick);
        });

        // Adjunta los event listeners a los botones de cerrar de los modales.
        closeButtonsArray.forEach(button => {
            button.removeEventListener('click', handleCloseClick);
            button.addEventListener('click', handleCloseClick);
        });

        // Asegura que el listener para el botón "Unirme a este equipo" dentro del modal
        // de inspeccionar solo se adjunte una vez.
        const joinFromInspectButton = document.querySelector('.join-from-inspect');
        if (joinFromInspectButton) { // Verifica que el botón exista.
            joinFromInspectButton.removeEventListener('click', handleJoinFromInspectClick);
            joinFromInspectButton.addEventListener('click', handleJoinFromInspectClick);
        }

        // Adjunta los listeners a los botones de Confirmar/Cancelar en el modal de unión.
        if (confirmJoinBtn) {
            confirmJoinBtn.removeEventListener('click', handleConfirmJoinClick);
            confirmJoinBtn.addEventListener('click', handleConfirmJoinClick);
        }
        if (cancelJoinBtn) {
            cancelJoinBtn.removeEventListener('click', handleCancelJoinClick);
            cancelJoinBtn.addEventListener('click', handleCancelJoinClick);
        }
    }

    // --- Funciones de Manejo de Eventos (Event Handlers) ---
    // Estas funciones son llamadas cuando un evento (ej. clic) ocurre en un elemento.

    function handleInspectClick() {
        // 'this' se refiere al botón que fue clickeado.
        currentTeamId = this.dataset.teamId; // Obtiene el ID del equipo desde el atributo 'data-team-id'.
        const team = teamsData[currentTeamId]; // Busca los datos completos del equipo usando su ID.

        if (team) { // Si se encuentran los datos del equipo:
            // Rellena los campos del modal de inspección con la información del equipo.
            inspectTeamName.textContent = team.name;
            membersCount.textContent = `${team.members.length}/5`; // Cantidad de miembros actual y el máximo.
            teamWins.textContent = team.wins;
            teamLosses.textContent = team.losses;
            teamKDRatio.textContent = team.kdRatio;
            teamMatches.textContent = team.matches;
            teamRank.textContent = team.rank;
            teamRegion.textContent = team.region;
            teamDescription.textContent = team.description;

            // Limpia la lista de miembros anterior antes de añadir los nuevos.
            teamMembersList.innerHTML = '';
            // Itera sobre los miembros del equipo y crea una tarjeta para cada uno.
            team.members.forEach(member => {
                const memberCard = document.createElement('div'); // Crea un div nuevo.
                memberCard.classList.add('member-card'); // Le añade la clase CSS.
                // Rellena el HTML interno de la tarjeta de miembro.
                memberCard.innerHTML = `
                    <img src="${member.avatar}" alt="${member.name}">
                    <span>${member.name}</span>
                `;
                teamMembersList.appendChild(memberCard); // Añade la tarjeta de miembro al contenedor.
            });
            openModal(inspectModal); // Abre el modal de inspección.
        }
    }

    function handleJoinClick() {
        // Similar a 'handleInspectClick', obtiene el ID del equipo.
        currentTeamId = this.dataset.teamId;
        const team = teamsData[currentTeamId];
        if (team) {
            confirmTeamName.textContent = team.name; // Muestra el nombre del equipo en el modal de confirmación.
            openModal(joinConfirmModal); // Abre el modal de confirmación para unirse.
        }
    }

    function handleJoinFromInspectClick() {
        closeModal(inspectModal); // Cierra el modal de inspección primero.
        const team = teamsData[currentTeamId]; // Obtiene los datos del equipo actualmente inspeccionado.
        if (team) {
            confirmTeamName.textContent = team.name; // Muestra el nombre en el modal de confirmación.
            openModal(joinConfirmModal); // Abre el modal de confirmación.
        }
    }

    function handleCloseClick(event) {
        // Encuentra el modal padre más cercano al botón de cerrar clickeado.
        const modalToClose = event.target.closest('.modal');
        if (modalToClose) {
            closeModal(modalToClose); // Cierra ese modal.
        }
    }

    function handleConfirmJoinClick() {
        // Simula la acción de unirse al equipo.
        alert(`Te has unido a ${teamsData[currentTeamId].name}! (Esta es una simulación)`);
        closeModal(joinConfirmModal); // Cierra el modal de confirmación.
        // Aquí iría la lógica real para unirse al equipo (ej. una petición a un servidor).
    }

    function handleCancelJoinClick() {
        closeModal(joinConfirmModal); // Simplemente cierra el modal de confirmación.
    }

    // --- Cierre de Modales al Hacer Clic Fuera ---
    // Escucha clics en cualquier parte de la ventana.
    window.addEventListener('click', (event) => {
        // Si el clic fue directamente en el fondo oscuro del modal de inspeccionar, ciérralo.
        if (event.target === inspectModal) {
            closeModal(inspectModal);
        }
        // Si el clic fue directamente en el fondo oscuro del modal de confirmación, ciérralo.
        if (event.target === joinConfirmModal) {
            closeModal(joinConfirmModal);
        }
    });

    // --- Ejecución Inicial ---
    // Llama a la función para renderizar los equipos cuando el script se carga.
    // Esto asegura que la lista de equipos se muestre apenas la página esté lista.
    renderTeams();
});