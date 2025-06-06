package com.integrador.spring.app.Servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.Controlador.TorneoRequest;
import com.integrador.spring.app.DAO.TorneoRepository;
import com.integrador.spring.app.Modelo.EstadoTorneo;
import com.integrador.spring.app.Modelo.ModoJuego;
import com.integrador.spring.app.Modelo.TipoTorneo;
import com.integrador.spring.app.Modelo.Torneo;

import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    // CREATE
    public Torneo crearTorneo(TorneoRequest request, String creador) {
        if (request.getTipo() == TipoTorneo.PAGA && request.getPrecioInscripcion() == null) {
            throw new IllegalArgumentException("Los torneos pagos requieren precio");
        }

        Torneo torneo = Torneo.builder()
            .nombre_torneo(request.getNombreTorneo())
            .modoJuego(request.getModoJuego())
            .tipo(request.getTipo())
            .precio_inscripcion(request.getTipo() == TipoTorneo.GRATIS ? 0.0 : request.getPrecioInscripcion())
            .descripcion(request.getDescripcion())
            .fechaInicio(request.getFechaInicio())
            .fechaFin(request.getFechaFin())
            .premio(request.getPremio())
            .estado(EstadoTorneo.PROGRAMADO)
            .creador(creador)
            .build();

        return torneoRepository.save(torneo);
    }

    // READ ALL
    public List<Torneo> listarTodosLosTorneos() {
        return torneoRepository.findAll();
    }

    // READ BY ID
    public Optional<Torneo> obtenerTorneoPorId(Integer id) {
        return torneoRepository.findById(id);
    }

    // UPDATE
    public Torneo actualizarTorneo(Integer id, Torneo torneoActualizado) {
        return torneoRepository.findById(id)
                .map(torneo -> {
                    torneo.setNombre_torneo(torneoActualizado.getNombre_torneo());
                    torneo.setDescripcion(torneoActualizado.getDescripcion());
                    torneo.setTipo(torneoActualizado.getTipo());
                    torneo.setPrecio_inscripcion(torneoActualizado.getPrecio_inscripcion());
                    torneo.setPremio(torneoActualizado.getPremio());
                    torneo.setModoJuego(torneoActualizado.getModoJuego());
                    torneo.setFechaInicio(torneoActualizado.getFechaInicio());
                    torneo.setFechaFin(torneoActualizado.getFechaFin());
                    torneo.setEstado(torneoActualizado.getEstado());
                    return torneoRepository.save(torneo);
                })
                .orElseGet(() -> {
                    torneoActualizado.setId_torneo(id);
                    return torneoRepository.save(torneoActualizado);
                });
    }

    // DELETE
    public void eliminarTorneo(Integer id) {
        torneoRepository.deleteById(id);
    }

    // Métodos adicionales
    public List<Torneo> listarTorneosPorTipo(TipoTorneo tipo) {
        return torneoRepository.findByTipo(tipo);
    }
    
    public List<Torneo> listarTorneosPorEstado(EstadoTorneo estado) {
        return torneoRepository.findByEstado(estado);
    }

    // Nuevos métodos específicos para ModoJuego
    public List<Torneo> listarTorneosVirtuales() {
        return torneoRepository.findByModoJuego(ModoJuego.VIRTUAL);
    }

    public List<Torneo> listarTorneosPresencialesActivos() {
        return torneoRepository.findByModoJuegoAndEstado(
            ModoJuego.PRESENCIAL, 
            EstadoTorneo.EN_CURSO
        );
    }

    // Método para verificar modo de juego
    public String obtenerDetallesModoJuego(Integer torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
            .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
        
        return switch (torneo.getModoJuego()) {
            case VIRTUAL -> "Este torneo se juega en línea";
            case PRESENCIAL -> "Ubicación física requerida";
        };
    }
}