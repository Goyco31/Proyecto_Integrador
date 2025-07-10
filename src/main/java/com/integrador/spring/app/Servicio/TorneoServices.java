package com.integrador.spring.app.Servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.spring.app.DAO.JuegoRepo;
import com.integrador.spring.app.DAO.TorneoRepo;
import com.integrador.spring.app.DTO.EquipoResponseDTO;
import com.integrador.spring.app.Modelo.Equipo;
import com.integrador.spring.app.Modelo.Inscripciones;
import com.integrador.spring.app.Modelo.Juego;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.Torneo.EstadoTorneo;
import com.integrador.spring.app.Modelo.Torneo.Tipo;
import com.integrador.spring.app.Servicio.InscripcionesServices;

@Service
public class TorneoServices {

    //inyeccion de repositorios
    @Autowired
    private TorneoRepo repo_torneo;

    @Autowired
    private JuegoRepo repo_juego;
    @Autowired
    private EquipoServices equipoServices;

    @Autowired
    private InscripcionesServices inscripcionesServices;

    //lista todos los torneos
    public List<Torneo> listarTorneo() {
        return repo_torneo.findAll();
    }

    //busca torneo por su id
    public Optional<Torneo> buscarId(Integer id) {
        return repo_torneo.findById(id);
    }

    //busca torneo por su nombre
    public Optional<Torneo> buscarTorneoNombre(String nombre) {
        return repo_torneo.findByNombre(nombre);
    }

    //crea un nuevo torneo
    public Torneo añadirTorneo(String nombre, String descripcion, Tipo tipo, MultipartFile banner, LocalDate fecha,
            Integer premio, Integer cupos, String formato, MultipartFile docReglamento, EstadoTorneo estado,
            Juego juego)
            throws java.io.IOException {

        Torneo nuevo = new Torneo();
        nuevo.setNombre(nombre);
        nuevo.setDescripcion(descripcion);
        nuevo.setTipo(tipo);
        nuevo.setBanner(banner.getBytes());
        System.out.println("Banner Content Type: " + banner.getContentType());
        nuevo.setFecha(fecha);
        nuevo.setPremio(premio);
        nuevo.setCupos(cupos);
        nuevo.setFormato(formato);
        nuevo.setDocReglamento(docReglamento.getBytes());
        nuevo.setEstado(estado);

        Optional<Juego> existeJuego = repo_juego.findById(juego.getIdJuego());
        if (existeJuego.isPresent()) {
            nuevo.setJuego(juego);
        }

        return repo_torneo.save(nuevo);
    }

    //actualiza el torneo
    public Torneo actualizarTorneo(Integer id, String nombre, String descripcion, Tipo tipo,
            MultipartFile banner,
            LocalDate fecha,
            Integer premio, Integer cupos, String formato, MultipartFile docReglamento, EstadoTorneo estado,
            Juego juego)
            throws java.io.IOException {

        Optional<Torneo> existe = repo_torneo.findById(id);

        //valida que el torneo exista
        if (existe.isPresent()) {
            Torneo actualizar = existe.get();
            //valida si es que una campo no es llenado tenga la informacion anterior
            if (nombre != null) {
                actualizar.setNombre(nombre);
            }
            if (descripcion != null) {
                actualizar.setDescripcion(descripcion);
            }
            if (tipo != null) {
                actualizar.setTipo(tipo);
            }
            if (banner != null) {
                actualizar.setBanner(banner.getBytes());
            }

            if (fecha != null) {
                actualizar.setFecha(fecha);
            }

            if (premio != null) {
                actualizar.setPremio(premio);
            }

            if (cupos != null) {
                actualizar.setCupos(cupos);
            }

            if (formato != null) {
                actualizar.setFormato(formato);
            }

            if (docReglamento != null) {
                actualizar.setDocReglamento(docReglamento.getBytes());
            }

            if (estado != null) {
                actualizar.setEstado(estado);
            }

            Optional<Juego> existeJuego = repo_juego.findById(juego.getIdJuego());
            if (existeJuego.isPresent()) {
                actualizar.setJuego(juego);
            }

            return repo_torneo.save(actualizar);
        } else {
            return null;
        }
    }

    //guardar informacion
    public Torneo guardar(Torneo torneo) {
        return repo_torneo.save(torneo);
    }

    //elimina por su id
    public void eliminar(Integer id) {
        repo_torneo.deleteById(id);
    }

    //elimina por su nombre
    public void eliminarTorneoNombre(String nombre) {
        repo_torneo.deleteByNombre(nombre);
    }

    

    public ResponseEntity<String> registrarEquipoEnTorneo(Integer idEquipo, Integer idTorneo) {
        Optional<Torneo> exiteTorneo = repo_torneo.findById(idTorneo);
        Optional<Equipo> equipoOptional = equipoServices.buscarId(idEquipo);

        if (!exiteTorneo.isPresent() || !equipoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equipo o torneo no encontrado");
        }

        Equipo equipo = equipoOptional.get();
        Torneo torneo = exiteTorneo.get();

        if (equipo.getIntegrantes().size() != 5) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El equipo no cumple con los integrantes necesarios, se necesitan 5");
        }

        Inscripciones inscripcion = new Inscripciones();
        inscripcion.setEquipo(equipo);
        inscripcion.setTorneo(torneo);

        if (torneo.getCupos() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CUpos completados");
        }

        torneo.setCupos(torneo.getCupos() - 1);
        repo_torneo.save(torneo);

        inscripcionesServices.guardar(inscripcion);
        return ResponseEntity.ok().body("Equipo registrado correctamente");
    }
}
