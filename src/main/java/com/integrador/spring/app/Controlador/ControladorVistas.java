package com.integrador.spring.app.Controlador;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.ComprarMonedasServices;
import com.integrador.spring.app.Servicio.RecompensaServices;
import com.integrador.spring.app.Servicio.TorneoServices;
import com.integrador.spring.app.Servicio.UsuarioServices;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorVistas {

    @Autowired
    private TorneoServices service_torneo;

    @Autowired
    private RecompensaServices services_recompensa;

    @Autowired
    private ComprarMonedasServices services_compra;

    @Autowired
    private UsuarioServices services_user;

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

        User usuario = new User();
        Optional<User> user = services_user.buscarId(usuario.getId());
        model.addAttribute("user", user);
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
        for (Recompensa recompensa : lista) {
            if (recompensa.getImgRecompensa() != null) {
                byte[] imgRecompensaByte = recompensa.getImgRecompensa();
                String imgRecompensaBase64 = Base64.getEncoder().encodeToString(imgRecompensaByte);
                recompensa.setImgRecompensaBase64(imgRecompensaBase64);
            }

        }
        model.addAttribute("recompensas", lista);
        return "canjes";
    }

    @GetMapping("/ComprarMonedas")
    public String ComprarMonedas(Model model) {
        List<ComprarMonedas> lista = services_compra.listar();

        for (ComprarMonedas opcion : lista) {
            if (opcion.getImgMoneda() != null) {
                byte[] imgMonedasBytes = opcion.getImgMoneda();
                String imgMonedasBase64 = Base64.getEncoder().encodeToString(imgMonedasBytes);
                opcion.setImgimgMonedaBase64(imgMonedasBase64);
            }
        }
        model.addAttribute("opcionesRecarga", lista);
        return "ComprarMonedas";
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/equipos")
    public String equipos() {
        return "equipos";
    }

    @GetMapping("/administrador")
    @PreAuthorize("hasRole('ADMIN')")
    public String vistaAdmin() {
        return "administrador"; // nombre de tu template
    }

    @GetMapping("/crearequipo")
    public String crearequipo() {
        return "crearequipo";
    }

    @GetMapping("/torneoinscrito")
    public String torneoinscrito() {
        return "torneoinscrito";
    }

    @GetMapping("/cuadrotorneo")
    public String cuadrotorneo() {
        return "cuadrotorneo";
    }

    @GetMapping("/pdfDota2")
    public ResponseEntity<byte[]> descargarPDFDota2() throws IOException {
        // Suponiendo que el PDF está en src/main/resources/static/pdf/cuadro.pdf
        ClassPathResource pdfFile = new ClassPathResource("Static/ReglamentoenDoc/ReglamentodeDota2.pdf");

        byte[] contenido = Files.readAllBytes(pdfFile.getFile().toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ReglamentodeDota2.pdf");

        return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
    }

    @GetMapping("/pdfCSGO2")
    public ResponseEntity<byte[]> descargarPDFCSGO2() throws IOException {
        // Suponiendo que el PDF está en src/main/resources/static/pdf/cuadro.pdf
        ClassPathResource pdfFile2 = new ClassPathResource("Static/ReglamentoenDoc/reglamentodeCSGO2.pdf");

        byte[] contenido2 = Files.readAllBytes(pdfFile2.getFile().toPath());

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_PDF);
        headers2.setContentDispositionFormData("attachment", "ReglamentodeCSGO2.pdf");

        return new ResponseEntity<>(contenido2, headers2, HttpStatus.OK);
    }

    @GetMapping("/pagoCancel")
    public String pagoCancel() {
        return "pagoCancel";
    }

    @GetMapping("/pagoError")
    public String pagoError() {
        return "pagoError";
    }

    @GetMapping("/pagoExito")
    public String pagoExito() {
        return "pagoExito";
    }

    
}
