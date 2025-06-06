document.addEventListener('DOMContentLoaded', () => {
    const editProfileBtn = document.getElementById('edit-profile-btn');
    const saveProfileBtn = document.getElementById('save-profile-btn');
    const profilePictureUpload = document.getElementById('profile-picture-upload');
    const changePictureBtn = document.getElementById('change-picture-btn'); // Este es el botón del ícono de la cámara
    const profilePicture = document.getElementById('profile-picture');
    const profileDisplayName = document.getElementById('profile-display-name');

    const inputs = document.querySelectorAll('.profile-details input');

    let isEditing = false;

    // Function to toggle edit mode
    function toggleEditMode() {
        isEditing = !isEditing;

        inputs.forEach(input => {
            input.disabled = !isEditing;
        });

        if (isEditing) {
            editProfileBtn.style.display = 'none';
            saveProfileBtn.style.display = 'inline-block';
        } else {
            editProfileBtn.style.display = 'inline-block';
            saveProfileBtn.style.display = 'none';
        }
    }

    // Event listener for Edit Profile button
    editProfileBtn.addEventListener('click', toggleEditMode);


    // **ESTA ES LA PARTE QUE ACTIVA EL INPUT AL HACER CLIC EN EL BOTÓN DE LA CÁMARA**
    changePictureBtn.addEventListener('click', () => {
        profilePictureUpload.click(); // Simula un clic en el input de tipo 'file' oculto
    });

    // **ESTA PARTE MANEJA LA CARGA DE LA IMAGEN SELECCIONADA**
    profilePictureUpload.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                profilePicture.src = e.target.result; // Establece la nueva imagen como fuente
            };
            reader.readAsDataURL(file); // Lee el archivo como una URL de datos
        }
    });

    // Initial state: inputs are disabled
    inputs.forEach(input => {
        input.disabled = true;
    });

    // Set initial display name
    const initialName = document.getElementById('name').value;
    const initialNickname = document.getElementById('nickname').value;
    profileDisplayName.textContent = `${initialName} (${initialNickname})`;
});