package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.integrador.spring.app.Modelo.EstadoTorneo;
import com.integrador.spring.app.Modelo.TipoTorneo;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Servicio.TorneoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class ControladorVistas {
    private final TorneoService torneoService;
    @GetMapping("/")
    public String inicio() {
        return "paginaPrincipal";
    }

    @GetMapping("/index")
    public String mostrarRegistro() {
        return "index";
    }

    /*@GetMapping("/paginaTorneo")
    public String paginatorneo() {
        return "paginaTorneo";
    }*/

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
    public String noticias(){
        return "/noticias";
    }
    
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        model.addAttribute("torneos", torneoService.listarTodosLosTorneos());
        return "torneos/lista";
    }

    @GetMapping("/torneos/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("torneo", new Torneo());
        model.addAttribute("tiposTorneo", TipoTorneo.values()); // Para select de tipo
        model.addAttribute("estadosTorneo", EstadoTorneo.values()); // Para select de estado
        return "torneos/formulario";
    }

    @GetMapping("/torneos/{id}")
    public String verDetalleTorneo(@PathVariable Integer id, Model model) {
        torneoService.obtenerTorneoPorId(id).ifPresent(torneo -> {
            model.addAttribute("torneo", torneo);
        });
        return "torneos/detalle";
    }

    @GetMapping("/paginaTorneo") // Manteniendo tu ruta existente
    public String paginaTorneo(Model model) {
        model.addAttribute("torneosDestacados", 
            torneoService.listarTorneosPorEstado(EstadoTorneo.EN_CURSO));
        return "paginaTorneo";
    }
}
