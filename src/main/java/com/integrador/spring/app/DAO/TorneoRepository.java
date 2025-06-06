package com.integrador.spring.app.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integrador.spring.app.Modelo.EstadoTorneo;
import com.integrador.spring.app.Modelo.ModoJuego;
import com.integrador.spring.app.Modelo.TipoTorneo;
import com.integrador.spring.app.Modelo.Torneo;

public interface TorneoRepository extends JpaRepository<Torneo, Integer> {
    List<Torneo> findByTipo(TipoTorneo tipo);
    List<Torneo> findByEstado(EstadoTorneo estado);
    List<Torneo> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);

    // Nuevos métodos para ModoJuego
    List<Torneo> findByModoJuego(ModoJuego modoJuego);
    List<Torneo> findByModoJuegoAndEstado(ModoJuego modoJuego, EstadoTorneo estado);
}