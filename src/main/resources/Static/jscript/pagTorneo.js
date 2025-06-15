document.addEventListener('DOMContentLoaded', () => {

    // 1. Cargar datos de torneos
    // Intenta obtener los datos de torneos guardados en localStorage por la página de administración.
    const tournamentsDataRaw = localStorage.getItem('adminTournamentsData');
    // Si hay datos en localStorage, los parsea de JSON a un objeto JavaScript; si no, es un objeto vacío.
    const tournamentsFromLocalStorage = tournamentsDataRaw ? JSON.parse(tournamentsDataRaw) : {};

    // Combinar datos preestablecidos (hardcodeados) con los de localStorage.
    // Los datos de localStorage sobrescribirán los preestablecidos si tienen el mismo 'id'.
    const combinedTournamentsData = {
        // --- Datos de ejemplo preestablecidos ---
        "dota-julio-premium": {
            id: "dota-julio-premium",
            title: "Torneo DOTA 2 - Edición Premium Julio",
            type: "Premium", // Asegúrate de que 'type' esté presente para la insignia
            gameImage: "/Imagenes/dota-pro-banner.webp", // RUTA DE LA IMAGEN: Asegúrate que esta ruta sea correcta para tu proyecto (ej: src/main/resources/static/Imagenes/)
            gameIcon: "/Imagenes/dota2-icon.png",        // RUTA DEL ICONO: Asegúrate que esta ruta sea correcta
            date: "📅 20 Julio | 🕒 8:00 PM",
            prize: "🏆 $100 PEN + Puntos de canje en SK-Tienda",
            description: "¡La élite de Dota 2 se enfrenta! Participa en nuestro torneo premium y demuestra que eres el mejor. Estrategia, habilidad y trabajo en equipo te llevarán a la victoria.",
            slots: "16/32 equipos",
            format: "Eliminación Doble, Bo3",
            // Las rutas de PDF (ReglamentoenDoc) son correctas según tu estructura de carpetas mostrada
            rulesLink: "/ReglamentoenDoc/reglamentodeDota2.pdf",
            registerLink: "/inscripcion-dota.html",
            status: "activo"
        },
        "dota-agosto-pro": {
            id: "dota-agosto-pro",
            title: "DOTA 2 Pro Challenge - Agosto",
            type: "Pro",
            gameImage: "/Imagenes/dota-pro-banner.webp",
            gameIcon: "/Imagenes/dota2-icon.png",
            date: "📅 15 Agosto | 🕒 7:30 PM",
            prize: "🏆 $200 PEN + Puntos de canje en SK-Tienda",
            description: "Sube de nivel y compite contra profesionales. Grandes premios y la oportunidad de hacerte un nombre en la escena.",
            slots: "8/16 equipos",
            format: "Eliminación Directa, Bo5 Finales",
            rulesLink: "/ReglamentoenDoc/reglamentodeDota2.pdf",
            registerLink: "/inscripcion-dota.html",
            status: "activo"
        },
        "csgo-julio-open": {
            id: "csgo-julio-open",
            title: "CS:GO Open Series - Julio",
            type: "Open",
            gameImage: "/Imagenes/csgo-tournament-banner.png",
            gameIcon: "/Imagenes/csgo-icon.png",
            date: "📅 25 Julio | 🕒 9:00 PM",
            prize: "🏆 $250 PEN + Puntos de canje en SK-Tienda",
            description: "Demuestra tu puntería y tácticas en el CS:GO Open. Abierto a todos los niveles. ¡No te quedes fuera!",
            slots: "24/32 equipos",
            format: "Sistema Suizo + Playoffs",
            rulesLink: "/ReglamentoenDoc/reglamentodeCSGO2.pdf",
            registerLink: "/registro-torneo-csgo.html",
            status: "activo"
        },
        // Los datos de localStorage se expanden aquí, sobrescribiendo los IDs duplicados de arriba
        ...tournamentsFromLocalStorage
    };

    // 2. Obtener referencias a elementos del DOM
    // Usamos las clases que definimos en el HTML y CSS
    const tournamentsContainer = document.querySelector('.lista-torneos'); 
    const noActiveTournamentsMessage = document.getElementById('no-active-tournaments-message');
    const carouselModal = document.querySelector('.carousel-modal');
    const closeCarouselModalBtn = document.querySelector('.close-modal');
    const modalTournamentContentArea = document.querySelector('.modal-tournament-content-area');
    const overlay = document.querySelector('.overlay'); 

    // 3. Función para renderizar dinámicamente las tarjetas de torneos
    const renderTournamentCards = () => {
        if (!tournamentsContainer) {
            console.error("Error: El contenedor de torneos (.lista-torneos) no se encontró en el HTML. Asegúrate de que el elemento exista y su clase sea correcta.");
            return; // No podemos renderizar si no hay contenedor
        }

        tournamentsContainer.innerHTML = ''; // Limpiar cualquier tarjeta existente

        const activeTournaments = Object.values(combinedTournamentsData).filter(tournament => tournament.status === 'activo');

        if (activeTournaments.length === 0) {
            if (noActiveTournamentsMessage) {
                noActiveTournamentsMessage.style.display = 'block';
            }
            return;
        } else {
            if (noActiveTournamentsMessage) {
                noActiveTournamentsMessage.style.display = 'none';
            }
        }

        // Crear una tarjeta HTML para cada torneo activo
        activeTournaments.forEach(tournament => {
            const tournamentCard = document.createElement('div');
            // Usamos la clase 'carta-torneo' como en tu HTML y CSS
            tournamentCard.classList.add('carta-torneo');
            tournamentCard.dataset.tournamentId = tournament.id; // Almacena el ID del torneo

            // Normalizar el tipo de torneo para la clase CSS (ej: "Premium" -> "premium")
            const typeClass = tournament.type ? tournament.type.toLowerCase().replace(/\s/g, '') : 'default';

            // --- Estructura HTML para cada tarjeta ---
            tournamentCard.innerHTML = `
                <div class="img-torneo">
                    <img src="${tournament.gameImage}" alt="${tournament.title}" class="banner-torneo">
                    <span class="type-badge type-${typeClass}">
                        ${tournament.type ? tournament.type.toUpperCase() : ''}
                    </span>
                    <img src="${tournament.gameIcon}" alt="${tournament.title} Icon" class="juego-torneo">
                </div>
                <div class="contenido-torneo">
                    <h3>${tournament.title}</h3>
                    <p><i class="fas fa-calendar-alt"></i> ${tournament.date}</p>
                    <p><i class="fas fa-trophy"></i> ${tournament.prize}</p>
                    <p><i class="fas fa-users"></i> ${tournament.slots}</p>
                </div>
            `;
            tournamentsContainer.appendChild(tournamentCard); // Añadir la tarjeta al contenedor
        });
    };

    // --- Funcionalidad de Carrusel de Imágenes en las secciones de juegos destacados ---
    const initGameCarousels = () => {
        const gameCards = document.querySelectorAll('.game-card'); // Contenedores individuales de carrusel
        gameCards.forEach(card => {
            const carouselImages = card.querySelectorAll('.game-carousel img');
            let currentIndex = 0;

            if (carouselImages.length > 1) {
                // Asegurarse de que solo la primera imagen esté activa al inicio
                carouselImages.forEach((img, index) => {
                    if (index === 0) {
                        img.classList.add('active');
                    } else {
                        img.classList.remove('active');
                    }
                });

                // Establecer el intervalo para cambiar las imágenes
                setInterval(() => {
                    carouselImages[currentIndex].classList.remove('active');
                    currentIndex = (currentIndex + 1) % carouselImages.length;
                    carouselImages[currentIndex].classList.add('active');
                }, 3000); // Cambia cada 3 segundos
            }
        });
    };

    // --- Funcionalidad del Modal de Detalles del Torneo ---

    // Función para cargar el contenido del modal con los detalles de un torneo específico
    const loadModalContent = (tournamentId) => {
        const tournament = combinedTournamentsData[tournamentId]; // Obtener los datos del torneo por su ID
        if (tournament && modalTournamentContentArea) {
            // Rellenar el área de contenido del modal con la información del torneo
            modalTournamentContentArea.innerHTML = `
                <div class="modal-header">
                    <img src="${tournament.gameImage}" alt="${tournament.title} Banner" class="modal-banner-image">
                    <img src="${tournament.gameIcon}" alt="${tournament.title} Icon" class="modal-game-icon-lg">
                </div>
                <div class="modal-body">
                    <h2 class="modal-title">${tournament.title}</h2>
                    <p class="modal-description">${tournament.description}</p>
                    <div class="modal-details-grid">
                        <p><strong>Tipo:</strong> <span>${tournament.type || 'N/A'}</span></p>
                        <p><strong>Fecha:</strong> <span>${tournament.date}</span></p>
                        <p><strong>Premio:</strong> <span>${tournament.prize}</span></p>
                        <p><strong>Cupos:</strong> <span>${tournament.slots}</span></p>
                        <p><strong>Formato:</strong> <span>${tournament.format}</span></p>
                    </div>
                </div>
                <div class="modal-footer">
                    ${tournament.rulesLink ? `<a href="${tournament.rulesLink}" target="_blank" class="btn btn-secondary">Ver Reglamento</a>` : ''}
                    ${tournament.registerLink ? `<a href="${tournament.registerLink}" target="_blank" class="btn btn-primary">Inscribirse</a>` : ''}
                </div>
            `;
        } else if (modalTournamentContentArea) {
            // Mensaje si no se encuentran los detalles del torneo
            modalTournamentContentArea.innerHTML = '<p class="modal-error-message">Los detalles de este torneo no están disponibles.</p>';
        }
    };

    // Función para abrir el modal
    const openTheModal = () => {
        if (carouselModal) carouselModal.style.display = 'flex'; // Muestra el modal (CSS display: flex para centrar)
        if (overlay) overlay.style.display = 'block'; // Muestra el overlay
        document.body.style.overflow = 'hidden'; // Evita el scroll del cuerpo mientras el modal está abierto
    }

    // Función para cerrar el modal y limpiar su contenido
    const closeTheModal = () => {
        if (carouselModal) carouselModal.style.display = 'none'; // Oculta el modal
        if (overlay) overlay.style.display = 'none'; // Oculta el overlay
        document.body.style.overflow = 'auto'; // Restaura el scroll del cuerpo
        if (modalTournamentContentArea) {
            modalTournamentContentArea.innerHTML = ''; // Limpia el contenido del modal
        }
    };

    // Event listener para abrir el modal al hacer clic en cualquier tarjeta de torneo
    // Usamos delegación de eventos en el contenedor padre para eficiencia
    if (tournamentsContainer) {
        tournamentsContainer.addEventListener('click', (e) => {
            const card = e.target.closest('.carta-torneo'); // Encuentra la tarjeta de torneo más cercana al clic (usando la clase correcta)
            if (card) {
                const tournamentId = card.dataset.tournamentId; // Obtiene el ID del torneo de la tarjeta
                if (tournamentId) {
                    loadModalContent(tournamentId); // Carga el contenido en el modal
                    openTheModal(); // Abre el modal
                }
            }
        });
    }

    // Event listener para cerrar el modal al hacer clic en el botón de cerrar
    if (closeCarouselModalBtn) closeCarouselModalBtn.addEventListener('click', closeTheModal);

    // Event listener para cerrar el modal al hacer clic fuera del contenido del modal (en el overlay o el propio fondo del modal)
    if (overlay) {
        overlay.addEventListener('click', (e) => {
            // Solo cierra si el clic es directamente en el overlay y no en un elemento dentro del modal-content
            // O si el clic es directamente en el fondo del modal (si el modal mismo es el target)
            if (e.target === overlay || e.target === carouselModal) {
                closeTheModal();
            }
        });
    }

    // Asegura que también se cierre si se hace clic directamente en el fondo del modal (si no es el overlay)
    // Esto es un fallback, ya que el evento en 'overlay' ya debería capturar esto si el modal está contenido en el overlay.
    // Sin embargo, mantenerlo no hace daño.
    if (carouselModal) {
        carouselModal.addEventListener('click', (e) => {
            if (e.target === carouselModal) {
                closeTheModal();
            }
        });
    }


    // --- Inicialización al cargar la página ---
    renderTournamentCards(); // Renderiza los torneos activos al cargar la página
    initGameCarousels(); // Inicializa los carruseles de imágenes de los juegos destacados
});