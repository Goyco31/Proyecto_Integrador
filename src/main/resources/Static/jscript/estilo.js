// main.js
document.addEventListener('DOMContentLoaded', () => {
    // Carrusel automático para secciones de juegos
    const initGameCarousels = () => {
        document.querySelectorAll('.game-carousel').forEach(carousel => {
            const images = carousel.querySelectorAll('img');
            let currentIndex = 0;
            
            setInterval(() => {
                images[currentIndex].classList.remove('active');
                currentIndex = (currentIndex + 1) % images.length;
                images[currentIndex].classList.add('active');
            }, 3000);
        });
    };

    // Control del modal
    const tournamentCards = document.querySelectorAll('.tournament-card');
    const modal = document.querySelector('.carousel-modal');
    const closeModal = document.querySelector('.close-modal');

    tournamentCards.forEach(card => {
        card.addEventListener('click', () => {
            const carouselType = card.dataset.carousel;
            loadModalContent(carouselType);
            modal.style.display = 'block';
        });
    });

    closeModal.onclick = () => modal.style.display = 'none';
    window.onclick = (e) => e.target === modal ? modal.style.display = 'none' : null;

    const loadModalContent = (type) => {
        // Lógica para cargar contenido específico según el torneo
    };

    initGameCarousels();
});
