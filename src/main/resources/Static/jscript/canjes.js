async function canjearRecompensa(idRecompensa) {
      const token = localStorage.getItem("authToken");
      const idUser = localStorage.getItem("idUser");

      try {
        const res = await fetch(`/api/canje/usuario/${idUser}/recompensa/${idRecompensa}`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!res.ok) throw new Error();
        Swal.fire("Exito", "Se canjeo correctamente tu recompensa\nRevisa tu correo!", "success");
      } catch (error) {
        Swal.fire("Error", "No se pudo canjear la recompensa\nYa no esta disponible", "error");
      }
    }

    const redeemButtons = document.querySelectorAll(".redeem-button");
    redeemButtons.forEach(button => {
      button.addEventListener("click", function() {
        const rewardCard = this.closest(".reward-card");
        const idRecompensa = rewardCard.querySelector("input[name='idRecompensa']").value;
        canjearRecompensa(idRecompensa);
      });
    });