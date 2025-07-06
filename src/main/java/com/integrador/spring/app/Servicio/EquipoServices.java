package com.integrador.spring.app.Servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.EquipoRepo;
import com.integrador.spring.app.DTO.EquipoResponseDTO;
import com.integrador.spring.app.DTO.MiembroDTO;
import com.integrador.spring.app.Modelo.Equipo;
import com.integrador.spring.app.Modelo.User;

import jakarta.transaction.Transactional;

//servicios que se usaran para el controlador de los equipos
@Service
public class EquipoServices {

    @Autowired
    private EquipoRepo repo_equipo;

    public List<Equipo> listarEquipos() {
        return repo_equipo.findAll();
    }

    public Optional<Equipo> buscarId(int id) {
        return repo_equipo.findById(id);
    }

    public Optional<Equipo> buscarNombreEquipo(String nom_equipo) {
        return repo_equipo.findBfindByNombreEquipo(nom_equipo);
    }

    public Equipo actualizar(int id, Equipo equipo) {
        Equipo existe = repo_equipo.findById(id)
        .orElseThrow(() -> new RuntimeException("Equipo no entontrado"));
        return repo_equipo.save(existe);
    }

    public Equipo guardar(Equipo equipo){
        return repo_equipo.save(equipo);
    }

    public void eliminar(int id) {
        repo_equipo.deleteById(id);
    }

    public void eliminarNombre(String nom_equipo){
        repo_equipo.deleteByNombreEquipo(nom_equipo);
    }

    public boolean estaEnEquipo(User usuario) {
        return usuario.getEquipo() != null;
    }

    public boolean equipoLleno(Equipo equipo) {
        return equipo.getIntegrantes().size() >= 5;
    }

    public Equipo crearEquipo(String nombre, String logoUrl, String region, String descripcion, User creador) {
        if (buscarNombreEquipo(nombre).isPresent()) {
            throw new IllegalArgumentException("Ya existe un equipo con ese nombre.");
        }

        Equipo nuevo = new Equipo();
        nuevo.setNombreEquipo(nombre);
        nuevo.setLogoUrl(logoUrl);
        nuevo.setRegion(region);
        nuevo.setDescripcion(descripcion);
        nuevo.setFechaCreacion(LocalDate.now());
        nuevo.setLider(creador);
        nuevo.setIntegrantes(new ArrayList<>(List.of(creador)));

        creador.setEquipo(nuevo); // vínculo inverso

        return repo_equipo.save(nuevo);
    }

    @Transactional
    public Equipo unirseAEquipo(Equipo equipo, User usuario) {
        if (equipoLleno(equipo)) {
                throw new IllegalStateException("El equipo ya tiene 5 integrantes.");
            }
            if (usuario.getEquipo() != null) {
                throw new IllegalStateException("Ya perteneces a un equipo.");
            }

            equipo.getIntegrantes().add(usuario);
            usuario.setEquipo(equipo);

            return repo_equipo.save(equipo);
        }

    public EquipoResponseDTO mapearEquipoAResponse(Equipo equipo) {
        List<MiembroDTO> miembros = equipo.getIntegrantes() != null
            ? equipo.getIntegrantes().stream()
                .map(u -> new MiembroDTO(u.getNickname(), u.getFotoPerfil()))
                .collect(Collectors.toList())
            : new ArrayList<>();

        return EquipoResponseDTO.builder()
            .id(equipo.getIdEquipo())
            .nombre(equipo.getNombreEquipo())
            .logoUrl(equipo.getLogoUrl())
            .region(equipo.getRegion())
            .descripcion(equipo.getDescripcion())
            .liderNickname(equipo.getLider().getNickname())
            .cantidadMiembros(miembros.size())
            .integrantes(miembros)
            .build();
    }
}
