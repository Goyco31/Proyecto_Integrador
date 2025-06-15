package com.integrador.spring.app.Controlador;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Servicio.RecompensaServices;
import com.integrador.spring.app.Servicio.TorneoServices;

@Controller
public class ControladorVistas {

    @Autowired
    private TorneoServices service_torneo;

    @Autowired
    private RecompensaServices services_recompensa;

    @GetMapping("/")
    public String inicio() {
        return "paginaPrincipal";
    }

    @GetMapping("/paginaTorneo")
    public String paginatorneo(Model model) {
        List<Torneo> lista = service_torneo.listarTorneo();

        for (Torneo torneo : lista) {
            if (torneo.getBanner() != null) {
                byte[] bannerBytes = torneo.getBanner();
                String bannerBase64 = Base64.getEncoder().encodeToString(bannerBytes);
                torneo.setBannerBase64(bannerBase64);
            }
            if (torneo.getJuego() != null && torneo.getJuego().getImgJuego() != null) {
                byte[] imgJuegoBytes = torneo.getJuego().getImgJuego();
                String imgJuegoBase64 = Base64.getEncoder().encodeToString(imgJuegoBytes);
                torneo.getJuego().setImgJuegoBase64(imgJuegoBase64);
            }
        }

        model.addAttribute("torneos", lista);
        return "paginaTorneo";
    }

    @GetMapping("/explorar")
    public String explorar() {
        return "Explorar";
    }

    @GetMapping("/clasificacion")
    public String clasificacion() {
        return "Clasificacion";
    }

    @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }

    @GetMapping("/noticias")
    public String noticias() {
        return "noticias";
    }

    @GetMapping("/canjes")
    public String recompensas(Model model) {
        List<Recompensa> lista = services_recompensa.listarTodas();
        for(Recompensa recompensa: lista){
            if (recompensa.getImgRecompensa() != null) {
            byte[] imgRecompensaByte = recompensa.getImgRecompensa();
            String imgRecompensaBase64 = Base64.getEncoder().encodeToString(imgRecompensaByte);
            recompensa.setImgRecompensaBase64(imgRecompensaBase64);                
            }

        }
        model.addAttribute("recompensas", lista);
        return "canjes";
    }

    @GetMapping("/administrador")
    @PreAuthorize("hasRole('ADMIN')")
    public String vistaAdmin() {
        return "administrador"; // nombre de tu template
    }
}
