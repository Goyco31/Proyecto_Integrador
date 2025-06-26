document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem("authToken");
    const editProfileBtn = document.getElementById('edit-profile-btn');
    const saveProfileBtn = document.getElementById('save-profile-btn');
    const profilePictureUpload = document.getElementById('profile-picture-upload');
    const changePictureBtn = document.getElementById('change-picture-btn');
    const profilePicture = document.getElementById('profile-picture');
    const profileDisplayName = document.getElementById('profile-display-name');

    const nameInput = document.getElementById('name');
    const nicknameInput = document.getElementById('nickname');
    const lastnameInput = document.getElementById('apellido');
    const emailInput = document.getElementById('email');

    // Cargar datos del usuario
    if (token) {
        fetch("/api/usuarios/me", {
            headers: { "Authorization": "Bearer " + token }
        })
        .then(res => res.json())
        .then(user => {
            nameInput.value = user.nombre || "";
            lastnameInput.value = user.apellido || "";
            nicknameInput.value = user.nickname || "";
            emailInput.value = user.correo || "";
            profilePicture.src = "/imagenes/perfil/" + (user.fotoPerfil || "default.png");
            profileDisplayName.textContent = `${user.nombre} (${user.nickname})`;
        })
        .catch(err => console.error("No se pudo cargar el perfil", err));
    }

    function toggleEditMode(enable) {
        nameInput.disabled = !enable;
        lastnameInput.disabled = !enable;
        nicknameInput.disabled = !enable;
        emailInput.disabled = !enable;

        saveProfileBtn.style.display = enable ? 'inline-block' : 'none';
        editProfileBtn.style.display = enable ? 'none' : 'inline-block';
    }

    editProfileBtn.addEventListener('click', () => {
        toggleEditMode(true);
    });

    saveProfileBtn.addEventListener('click', () => {
        const updatedUser = {
            nombre: nameInput.value,
            apellido: lastnameInput.value,
            nickname: nicknameInput.value,
            correo: emailInput.value,
            fotoPerfil: document.getElementById("profile-picture").dataset.filename || null
        };

        fetch('/api/usuarios/me', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(updatedUser)
        })
        .then(res => {
            if (!res.ok) throw new Error("Error al actualizar");
            return res.json();
        })
        .then(data => {
            alert("Perfil actualizado correctamente");
            toggleEditMode(false);
            profileDisplayName.textContent = `${data.nombre} (${data.nickname})`;
        })
        .catch(err => {
            alert("Hubo un error al actualizar el perfil");
            console.error(err);
        });
    });

    changePictureBtn.addEventListener('click', () => {
        profilePictureUpload.click();
    });

    profilePictureUpload.addEventListener('change', async (event) => {
        const file = event.target.files[0];
        if (!file) return;

        // Validaciones
        const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
        const maxSize = 5 * 1024 * 1024; // 5MB

        if (!validTypes.includes(file.type)) {
            alert('Solo se permiten imágenes JPEG, PNG o GIF');
            return;
        }

        if (file.size > maxSize) {
            alert('La imagen no debe exceder 5MB');
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            // Mostrar loader
            changePictureBtn.disabled = true;
            changePictureBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Subiendo...';

            const res = await fetch("/api/usuarios/upload-foto", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("authToken")
                },
                body: formData
            });

            if (!res.ok) {
                const error = await res.text();
                throw new Error(error || "Error al subir imagen");
            }

            const filename = await res.text();
            
            // Actualizar imagen en el frontend
            profilePicture.src = `/uploads/perfil/${filename}?t=${Date.now()}`; // Cache busting
            profilePicture.dataset.filename = filename;
            
            alert("Foto de perfil actualizada con éxito");
        } catch (err) {
            console.error("Error subiendo imagen:", err);
            alert(err.message || "Error al subir la imagen");
        } finally {
            changePictureBtn.disabled = false;
            changePictureBtn.innerHTML = 'Cambiar Imagen';
            profilePictureUpload.value = ''; // Resetear input
        }
    });
});