const hamburger = document.getElementById("hamburger");
    const mobileMenu = document.getElementById("mobileMenu");
    const overlay = document.getElementById("overlay");

    hamburger.addEventListener("click", () => {
      mobileMenu.classList.toggle("active");
      overlay.classList.toggle("active");

      // Desactivar o habilitar el scroll en el fondo
      if (mobileMenu.classList.contains("active")) {
        document.body.style.overflow = 'hidden';  // Desactiva el scroll
      } else {
        document.body.style.overflow = '';  // Reactiva el scroll
      }
    });

    document.querySelectorAll('#mobileMenu a').forEach(link => {
      link.addEventListener('click', () => {
        mobileMenu.classList.remove('active');
        overlay.classList.remove('active');
        document.body.style.overflow = '';  // Reactiva el scroll cuando se cierra el menú
      });
    });

    // Cerrar el menú y overlay si se hace clic fuera del menú (en el overlay)
    overlay.addEventListener('click', () => {
      mobileMenu.classList.remove('active');
      overlay.classList.remove('active');
      document.body.style.overflow = '';  // Reactiva el scroll cuando se cierra el menú
    });


    // Seleccionamos el encabezado
    const navbar = document.querySelector('.navbar');

    // Función para manejar el scroll
    function handleScroll() {
      if (window.scrollY > 0) { // Si se ha desplazado la página
        navbar.classList.add('fixed'); // Agrega la clase "fixed" al encabezado
      } else {
        navbar.classList.remove('fixed'); // Elimina la clase "fixed" si la página está en la parte superior
      }
    }

    // Escuchamos el evento de scroll
    window.addEventListener('scroll', handleScroll);




    function toggleInfo(id) {
      const info = document.getElementById(id + '-info');
      const card = document.getElementById(id + '-card');
      info.classList.toggle('active');
      card.classList.toggle('expanded');
    }





    // Abrir el modal correcto
    document.querySelectorAll('.read-more').forEach(link => {
      link.addEventListener('click', function (e) {
        e.preventDefault();
        const modalId = this.getAttribute('data-modal');
        const modal = document.getElementById(modalId);
        modal.style.display = "flex";
        document.body.style.overflow = "hidden"; // Bloquear scroll
      });
    });

    // Cerrar cualquier modal
    document.querySelectorAll('.modal .close').forEach(btn => {
      btn.addEventListener('click', function () {
        this.closest('.modal').style.display = "none";
        document.body.style.overflow = ""; // Restaurar scroll
      });
    });

    // Cerrar si se hace clic fuera del contenido
    window.addEventListener('click', function (e) {
      document.querySelectorAll('.modal').forEach(modal => {
        if (e.target === modal) {
          modal.style.display = "none";
          document.body.style.overflow = "";
        }
      });
    });

