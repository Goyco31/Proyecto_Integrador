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
            rulesLink: "/pdfDota2",
            registerLink: "/torneoinscrito",
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
            rulesLink: "/pdfDota2",
            registerLink: "/torneoinscrito",
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
            rulesLink: "/pdfCSGO2",
            registerLink: "/torneoinscrito",
            status: "activo"
        },
        // Los datos de localStorage se expanden aquí, sobrescribiendo los IDs duplicados de arriba
        ...tournamentsFromLocalStorage
    };

    // 2. Obtener referencias a elementos del DOM
    const tournamentsContainer = document.getElementById('tournaments-cards-container');
    const noActiveTournamentsMessage = document.getElementById('no-active-tournaments-message');
    const carouselModal = document.querySelector('.carousel-modal');
    const closeCarouselModalBtn = document.querySelector('.close-modal');
    const modalTournamentContentArea = document.querySelector('.modal-tournament-content-area');
    const overlay = document.getElementById('overlay'); // El overlay oscuro

    // 3. Función para renderizar dinámicamente las tarjetas de torneos
    const renderTournamentCards = () => {
        if (!tournamentsContainer) {
            console.error("Error: El contenedor de torneos (#tournaments-cards-container) no se encontró en el HTML. Asegúrate de que el elemento exista y su ID sea correcto.");
            return; // No podemos renderizar si no hay contenedor
        }

        tournamentsContainer.innerHTML = ''; // Limpiar cualquier tarjeta existente

        const activeTournaments = Object.values(combinedTournamentsData).filter(tournament => tournament.status === 'activo');

        if (activeTournaments.length === 0) {
            if (noActiveTournamentsMessage) {
                noActiveTournamentsMessage.style.display = 'block';
                // Si el contenedor está vacío y no hay mensaje, aquí se vería vacío.
                // Es crucial que 'noActiveTournamentsMessage' exista y tenga estilos para ser visible.
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
            tournamentCard.classList.add('tournament-card');
            tournamentCard.dataset.tournamentId = tournament.id; // Almacena el ID del torneo

            // --- Estructura HTML para cada tarjeta ---
            // Este HTML es generado por JS y necesita CSS para verse bien.
            tournamentCard.innerHTML = `
                <div class="card-image-wrapper">
                    <img src="${tournament.gameImage}" alt="${tournament.title}" class="card-game-banner">
                    <span class="type-badge type-${tournament.type ? tournament.type.toLowerCase().replace(/\s/g, '') : 'default'}">
                        ${tournament.type ? tournament.type.toUpperCase() : ''}
                    </span>
                    <img src="${tournament.gameIcon}" alt="${tournament.title} Icon" class="card-game-icon">
                </div>
                <div class="card-info">
                    <h3 class="card-title">${tournament.title}</h3>
                    <p class="card-schedule"><i class="fas fa-calendar-alt"></i> ${tournament.date}</p>
                    <p class="card-prize"><i class="fas fa-trophy"></i> ${tournament.prize}</p>
                    <p class="card-slots"><i class="fas fa-users"></i> ${tournament.slots}</p>
                </div>
            `;
            tournamentsContainer.appendChild(tournamentCard); // Añadir la tarjeta al contenedor
        });
    };

    // --- Funcionalidad de Carrusel de Imágenes en las secciones de juegos destacados ---
    // (Asumo que esta parte es para un carrusel de imágenes genérico en la página, no directamente de torneos)
    const initGameCarousels = () => {
        const gameCards = document.querySelectorAll('.game-card'); // Contenedores individuales de carrusel
        gameCards.forEach(card => {
            const carouselImages = card.querySelectorAll('.game-carousel img');
            let currentIndex = 0;

            if (carouselImages.length > 1) {
                carouselImages.forEach((img, index) => {
                    if (index === 0) {
                        img.classList.add('active');
                    } else {
                        img.classList.remove('active');
                    }
                });

                setInterval(() => {
                    carouselImages[currentIndex].classList.remove('active');
                    currentIndex = (currentIndex + 1) % carouselImages.length;
                    carouselImages[currentIndex].classList.add('active');
                }, 3000);
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

    // Event listener para abrir el modal al hacer clic en cualquier tarjeta de torneo
    // Usamos delegación de eventos en el contenedor padre para eficiencia
    if (tournamentsContainer) {
        tournamentsContainer.addEventListener('click', (e) => {
            const card = e.target.closest('.tournament-card'); // Encuentra la tarjeta de torneo más cercana al clic
            if (card) {
                const tournamentId = card.dataset.tournamentId; // Obtiene el ID del torneo de la tarjeta
                if (tournamentId) {
                    loadModalContent(tournamentId); // Carga el contenido en el modal
                    if (carouselModal) carouselModal.style.display = 'flex'; // Muestra el modal
                    if (overlay) overlay.style.display = 'block'; // Muestra el overlay
                    document.body.style.overflow = 'hidden'; // Evita el scroll del cuerpo mientras el modal está abierto
                }
            }
        });
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

    // Event listener para cerrar el modal al hacer clic en el botón de cerrar
    if (closeCarouselModalBtn) closeCarouselModalBtn.addEventListener('click', closeTheModal);

    // Event listener para cerrar el modal al hacer clic fuera del contenido del modal (en el overlay)
    // Se verifica si el evento fue en el overlay o en el modal mismo (su fondo)
    if (overlay) {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay || e.target === carouselModal) {
                closeTheModal();
            }
        });
    }
    // Asegura que también se cierre si se hace clic directamente en el fondo del modal (si no es el overlay)
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