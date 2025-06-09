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

document.addEventListener('DOMContentLoaded', () => {
    // Carrusel automático para secciones de juegos
    const initGameCarousels = () => {
        document.querySelectorAll('.game-carousel').forEach(carousel => {
            const images = carousel.querySelectorAll('img');
            if (images.length === 0) return; // No hacer nada si no hay imágenes
            let currentIndex = 0;
            images[currentIndex].classList.add('active'); // Activar la primera imagen

            setInterval(() => {
                images[currentIndex].classList.remove('active');
                currentIndex = (currentIndex + 1) % images.length;
                images[currentIndex].classList.add('active');
            }, 3000);
        });
    };
    // Control del modal
    const tournamentCards = document.querySelectorAll('.tournament-card');
    const modal = document.querySelector('.carousel-modal'); // El overlay oscuro
    const closeModalButton = document.querySelector('.close-modal');
    // El área DENTRO de .modal-content donde se cargarán los detalles del torneo
    const modalContentOutputArea = modal.querySelector('.modal-tournament-content-area'); 
    const loadModalContent = (tournamentId) => {
        const tournament = tournamentsData[tournamentId];
        if (modalContentOutputArea) { 
            if (tournament) {
                modalContentOutputArea.innerHTML = `
                    <div class="tournament-modal-details">
                        <img src="${tournament.gameImage}" alt="Banner del Torneo: ${tournament.title}" class="tournament-modal-banner">
                        <div class="tournament-modal-header">
                            <img src="${tournament.gameIcon}" alt="Icono de ${tournament.game}" class="tournament-modal-game-icon">
                            <h2 class="tournament-modal-title">${tournament.title}</h2>
                        </div>
                        <div class="tournament-modal-info">
                            <p><strong>Fecha:</strong> ${tournament.date}</p>
                            <p><strong>Premio:</strong> ${tournament.prize}</p>
                            <p><strong>Formato:</strong> ${tournament.format}</p>
                            <p><strong>Cupos:</strong> ${tournament.slots}</p>
                        </div>
                        <p class="tournament-modal-description">${tournament.description}</p>
                        <div class="tournament-modal-actions">
                            <a href="${tournament.rulesLink}" class="modal-button rules-button" target="_blank">Ver Reglamento</a>
                            <a href="${tournament.registerLink}" class="modal-button register-button">Inscribirse</a>
                        </div>
                    </div>
                `;
            } else {
                modalContentOutputArea.innerHTML = '<p style="color:white; text-align:center; padding: 20px;">Los detalles de este torneo no están disponibles.</p>';
            }
        } else {
            console.error("El área de contenido del modal (.modal-tournament-content-area) no se encontró.");
        }
    };
    if (tournamentCards.length > 0 && modal && closeModalButton && modalContentOutputArea) {
        tournamentCards.forEach(card => {
            card.addEventListener('click', () => {
                const tournamentId = card.dataset.tournamentId;
                if (tournamentId) {
                    loadModalContent(tournamentId);
                    modal.style.display = 'block';
                    document.body.style.overflow = 'hidden'; // Evita el scroll del body
                }
            });
        });
        const closeTheModal = () => {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto'; // Restaura el scroll del body
            if (modalContentOutputArea) {
                modalContentOutputArea.innerHTML = ''; // Limpia el contenido del modal al cerrar
            }
        };
        closeModalButton.onclick = closeTheModal;
        window.onclick = (e) => {
            if (e.target === modal) { // Si se hace clic en el overlay oscuro
                closeTheModal();
            }
        };
    } else {
        console.warn();
    }
    initGameCarousels();
});