<<<<<<< HEAD
=======
// Define los datos de los torneos globalmente o antes de DOMContentLoaded
const tournamentsData = {
    "dota-julio-premium": {
        title: "Torneo DOTA 2 - Edición Premium Julio",
        gameImage: "/Imagenes/dota-pro-banner.webp", 
        gameIcon: "/Imagenes/dota2-icon.png",        
        date: "📅 20 Julio | 🕒 8:00 PM",
        prize: "🏆 $100 PEN + Puntos de canjeto en SK-Tienda",
        description: "¡La élite de Dota 2 se enfrenta! Participa en nuestro torneo premium y demuestra que eres el mejor. Estrategia, habilidad y trabajo en equipo te llevarán a la victoria.",
        slots: "16/32 equipos",
        format: "Eliminación Doble, Bo3",
        rulesLink: "/ReglamentoenDoc/reglamentodeDota2.pdf",
        registerLink: "/inscripcion-dota.html"
    },
    "dota-agosto-pro": {
        title: "DOTA 2 Pro Challenge - Agosto",
        gameImage: "/Imagenes/dota-pro-banner.webp",   
        gameIcon: "/Imagenes/dota2-icon.png",
        date: "📅 15 Agosto | 🕒 7:30 PM",
        prize: "🏆 $200 PEN + Puntos de canjeto en SK-Tienda",
        description: "Sube de nivel y compite contra profesionales. Grandes premios y la oportunidad de hacerte un nombre en la escena.",
        slots: "8/16 equipos",
        format: "Eliminación Directa, Bo5 Finales",
        rulesLink: "/ReglamentoenDoc/reglamentodeDota2.pdf",
        registerLink: "/inscripcion-dota.html",
    },
    "csgo-julio-open": {
        title: "CS:GO Open Series - Julio",
        gameImage: "/Imagenes/csgo-tournament-banner.png", 
        gameIcon: "/Imagenes/csgo-icon.png",          
        date: "📅 25 Julio | 🕒 9:00 PM",
        prize: "🏆 $250 PEN + Puntos de canjeto en SK-Tienda",
        description: "Demuestra tu puntería y tácticas en el CS:GO Open. Abierto a todos los niveles. ¡No te quedes fuera!",
        slots: "24/32 equipos",
        format: "Sistema Suizo + Playoffs",
        rulesLink: "/ReglamentoenDoc/reglamentodeCSGO2.pdf",
        registerLink: "/registro-torneo-csgo.html",
    }
};

>>>>>>> c6d3fad5c8a307e3e02d98aa76e715295c596e8c
document.addEventListener('DOMContentLoaded', () => {

    // 1. Cargar datos de torneos
    // Intenta obtener los datos de torneos guardados en localStorage por la página de administración.
    // 'adminTournamentsData' debe ser la misma clave utilizada en administrador.js
    const tournamentsDataRaw = localStorage.getItem('adminTournamentsData');
    // Si hay datos en localStorage, los parsea de JSON a un objeto JavaScript; si no, es un objeto vacío.
    const tournamentsFromLocalStorage = tournamentsDataRaw ? JSON.parse(tournamentsDataRaw) : {};

    // Combinar datos preestablecidos (hardcodeados) con los de localStorage.
    // Los datos de localStorage sobrescribirán los preestablecidos si tienen el mismo 'id'.
    // Esto asegura que los torneos creados/editados en admin persistan.
    const combinedTournamentsData = {
        // --- Datos de ejemplo preestablecidos ---
        // Estos torneos aparecerán si localStorage está vacío o si no hay un torneo con el mismo ID en localStorage.
        // Si no quieres torneos preestablecidos, puedes eliminar esta sección.
        "dota-julio-premium": {
            id: "dota-julio-premium",
            title: "Torneo DOTA 2 - Edición Premium Julio",
            gameImage: "/Imagenes/dota-pro-banner.webp", // Ruta de la imagen del banner del juego para el modal
            gameIcon: "/Imagenes/dota2-icon.png",       // Ruta del icono del juego para el modal
            date: "📅 20 Julio | 🕒 8:00 PM",
            prize: "🏆 $100 PEN + Puntos de canje en SK-Tienda",
            description: "¡La élite de Dota 2 se enfrenta! Participa en nuestro torneo premium y demuestra que eres el mejor. Estrategia, habilidad y trabajo en equipo te llevarán a la victoria.",
            slots: "16/32 equipos",
            format: "Eliminación Doble, Bo3",
            // Asegúrate de que estas rutas de PDF existan en tu proyecto (ej: src/main/resources/static/Reglamentos_PDF/)
            rulesLink: "/ReglamentoenDoc/reglamentodeDota2.pdf",
            registerLink: "/inscripcion-dota.html", // Enlace de inscripción (ejemplo)
            status: "activo", // Estado del torneo (debe coincidir con la lógica del administrador)
            type: "Premium" // Tipo de torneo (usado para la insignia visual)
        },
        "dota-agosto-pro": {
            id: "dota-agosto-pro",
            title: "DOTA 2 Pro Challenge - Agosto",
            gameImage: "/Imagenes/dota-pro-banner.webp",
            gameIcon: "/Imagenes/dota2-icon.png",
            date: "📅 15 Agosto | 🕒 7:30 PM",
            prize: "🏆 $200 PEN + Puntos de canje en SK-Tienda",
            description: "Sube de nivel y compite contra profesionales. Grandes premios y la oportunidad de hacerte un nombre en la escena.",
            slots: "8/16 equipos",
            format: "Eliminación Directa, Bo5 Finales",
            rulesLink: "/reglamentodeDota2.pdf",
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
            prize: "🏆 $250 PEN + Puntos de canje en SK-Tienda",
            description: "Demuestra tu puntería y tácticas en el CS:GO Open. Abierto a todos los niveles. ¡No te quedes fuera!",
            slots: "24/32 equipos",
            format: "Sistema Suizo + Playoffs",
            rulesLink: "/reglamentodeDota2.pdf",
            registerLink: "/registro-torneo-csgo.html",
            status: "activo",
            type: "Open"
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
            console.error("No se encontró el contenedor de torneos (#tournaments-cards-container).");
            return;
        }

        tournamentsContainer.innerHTML = ''; // Limpiar cualquier tarjeta existente

        // Convertir el objeto de torneos a un array de sus valores para poder filtrar y mapear.
        // Solo mostramos torneos con 'status: "activo"'.
        const activeTournaments = Object.values(combinedTournamentsData).filter(tournament => tournament.status === 'activo');

        // Mostrar un mensaje si no hay torneos activos
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
            tournamentCard.classList.add('tournament-card');
            // Almacena el ID del torneo en un atributo de datos para fácil acceso al hacer clic
            tournamentCard.dataset.tournamentId = tournament.id;

            // Determina la clase CSS para la insignia de tipo (ej. "premium", "pro", "open")
            let badgeClass = '';
            if (tournament.type) {
                badgeClass = tournament.type.toLowerCase().replace(/\s/g, ''); // Elimina espacios si los hay
            }

            // Inyectar el HTML de la tarjeta con los datos del torneo
            tournamentCard.innerHTML = `
                <div class="card-header">
                    <h3>${tournament.title}</h3>
                    <span class="type-badge ${badgeClass}">${tournament.type ? tournament.type.toUpperCase() : ''}</span>
                </div>
                <div class="card-content">
                    <p class="schedule">${tournament.date}</p>
                    <p class="prize">${tournament.prize}</p>
                </div>
            `;
            tournamentsContainer.appendChild(tournamentCard); // Añadir la tarjeta al contenedor
        });
    };

    // --- Funcionalidad de Carrusel de Imágenes en las secciones de juegos destacados ---
    const initGameCarousels = () => {
        const gameCards = document.querySelectorAll('.game-card');
        gameCards.forEach(card => {
            const carouselImages = card.querySelectorAll('.game-carousel img');
            let currentIndex = 0;

            if (carouselImages.length > 1) {
                // Asegurarse de que solo la primera imagen sea 'active' al inicio
                carouselImages.forEach((img, index) => {
                    if (index === 0) {
                        img.classList.add('active');
                    } else {
                        img.classList.remove('active');
                    }
                });

                // Cambia la imagen activa cada 3 segundos
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
                <div class="modal-banner" style="background-image: url(${tournament.gameImage});">
                    <img src="${tournament.gameIcon}" alt="${tournament.title} Icon" class="modal-game-icon">
                </div>
                <h2>${tournament.title}</h2>
                <p class="modal-description">${tournament.description}</p>
                <div class="modal-details">
                    <p><strong>Tipo:</strong> ${tournament.type || 'N/A'}</p>
                    <p><strong>Fecha:</strong> ${tournament.date}</p>
                    <p><strong>Premio:</strong> ${tournament.prize}</p>
                    <p><strong>Cupos:</strong> ${tournament.slots}</p>
                    <p><strong>Formato:</strong> ${tournament.format}</p>
                </div>
                <div class="modal-actions">
                    <a href="${tournament.rulesLink}" target="_blank" class="btn-secondary">Ver Reglamento</a>
                    <a href="${tournament.registerLink}" target="_blank" class="btn-primary">Inscribirse</a>
                </div>
            `;
        } else if (modalTournamentContentArea) {
             // Mensaje si no se encuentran los detalles del torneo
             modalTournamentContentArea.innerHTML = '<p style="color:white; text-align:center; padding: 20px;">Los detalles de este torneo no están disponibles.</p>';
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