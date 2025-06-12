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
        // Validación: tamaño máximo 5MB
        const maxSize = 5 * 1024 * 1024; // 5MB
        if (file.size > maxSize) {
            alert("La imagen es demasiado grande (máximo 5MB)");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        const token = localStorage.getItem("authToken"); // <-- ⚠️ Asegúrate de tener el token

        try {
            const res = await fetch("/api/usuarios/upload-foto", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                },
                body: formData
            });
            const filename = await res.text();
            if (res.ok) {
                // Mostrar imagen y guardar nombre para backend
                document.getElementById("profile-picture").src = "/uploads/perfil/" + filename;
                document.getElementById("profile-picture").dataset.filename = filename;
            } else {
                alert("Error al subir imagen: " + filename);
            }
        } catch (err) {
            console.error("Error subiendo imagen", err);
        }
    });
});