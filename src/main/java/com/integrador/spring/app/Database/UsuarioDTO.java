package com.integrador.spring.app.Database;

import java.time.LocalDateTime;
import java.util.List;

import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Modelo.role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Integer id_usuario;
    private String nombre;
    private String apellido;
    private String nickname;
    private String correo;
    private String contraseña;
    private LocalDateTime fecha_registro;
    private role role;

    private InfoUsuarioDTO info;
    // Relacion uno a uno con la tabla Monedas
    private MonedasDTO monedas;
    // Relacion uno a muchos con la tabla Recarga
    private List<RecargaDTO> recarga;
    // Relacion uno a muchos con la tabla mensajes
    private List<MensajesDTO> mensajes;
    // Relacion muchos a uno con la tabla equipo
    private EquipoDTO equipo;

    public UsuarioDTO(User user) {
        this.id_usuario = user.getId_usuario();
        this.nombre = user.getNombre();
        this.apellido = user.getApellido();
        this.nickname = user.getNickname();
        this.correo = user.getCorreo();
        this.contraseña = user.getContraseña();
        this.fecha_registro = user.getFecha_registro();
        this.info = new InfoUsuarioDTO(user.getInfo());
        this.equipo = new EquipoDTO(user.getEquipo());
        this.role = getRole();
        this.monedas = new MonedasDTO(user.getMonedas());
        this.recarga = user.getRecarga().stream().map(RecargaDTO::new).toList();
        this.mensajes = user.getMensajes().stream().map(MensajesDTO::new).toList();
    }
}
