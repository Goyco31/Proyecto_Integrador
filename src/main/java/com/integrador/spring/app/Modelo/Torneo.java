package com.integrador.spring.app.Modelo;

import java.time.LocalDate;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

//Mapeo con la tabla Torneo de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Torneo {

    //Id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTorneo;

    private String nombre;
    @Lob
    private String descripcion;

    private Tipo tipo;

    @Lob
    private byte[] banner;

    private String bannerBase64;

    public String getBannerBase64() {
        if (banner != null) {
            return Base64.getEncoder().encodeToString(banner);
        }
        return null;
    }

    public void setBannerBase64(String bannerBase64) {
        this.bannerBase64 = bannerBase64;
    }

    // private byte[] imgJuego;

    private LocalDate fecha;
    private Integer premio;
    private Integer cupos;
    private String formato;

    @Lob
    @Column(name = "reglamento", columnDefinition = "LONGBLOB")
    private byte[] docReglamento;

    public String getDocReglamentoBase64() {
        if (docReglamento != null) {
            return Base64.getEncoder().encodeToString(docReglamento);
        }
        return null;
    }

    public void setDocReglamento(byte[] docReglamento) {
        this.docReglamento = docReglamento;
    }

    private EstadoTorneo estado;

    @OneToMany //(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Inscripciones> inscripciones;

    public static enum Tipo {
        Premium, Pro, Open;
    }

    public static enum EstadoTorneo{
        Activo, Finalizado, Cancelado;
    }
    @ManyToOne
    @JsonBackReference
    private Juego juego;
}
