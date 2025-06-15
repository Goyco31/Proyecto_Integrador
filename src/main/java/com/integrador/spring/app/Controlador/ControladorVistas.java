package com.integrador.spring.app.Controlador;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ControladorVistas {
    @GetMapping("/")
    public String inicio() {
        return "paginaPrincipal";
    }

    @GetMapping("/paginaTorneo")
    public String paginatorneo() {
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
    public String noticias(){
        return "noticias";
    } 
    @GetMapping("/equipos")
    public String equipos(){
        return "equipos";
    }

    @GetMapping("/administrador")
    @PreAuthorize("hasRole('ADMIN')")
    public String vistaAdmin() {
        return "administrador"; // nombre de tu template
    }
    
    @GetMapping("/canjes")
    public String mostrarRecompensas() {
      return "canjes";
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
}
