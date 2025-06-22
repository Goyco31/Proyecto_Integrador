package com.integrador.spring.app.Modelo;

// Importaciones necesarias para manejo de fechas, colecciones y seguridad
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

// Importa anotaciones de JPA para mapeo de entidades
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Anotaciones de Lombok para generar código automáticamente (getters, setters, etc.)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotaciones de Lombok para generar constructor vacío, constructor con todos los argumentos, getters, setters, etc.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Indica que esta clase es una entidad JPA y se mapeará a una tabla llamada
// "usuario"
@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "nickname" }) })
public class User implements UserDetails {
    @Id // Clave primaria
    @GeneratedValue // Valor generado automaticamente
    @Basic
    @Column(nullable = false)
    Integer id_usuario;
    String nombre;
    String apellido;
    String nickname;
    private String fotoPerfil;
    @Column(nullable = false)
    String correo;
    String contraseña;
    private Integer monedas;
    // Fecha de registro que se genera automáticamente al crear el registro
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    LocalDateTime fecha_registro;
    // Rol del usuario, representado como un Enum en forma de texto (STRING)
    @Enumerated(EnumType.STRING)
    role role;

    @Column(name = "is_2fa_enabled") // Exactamente como en la BD
    @Builder.Default
    private boolean is2faEnabled = false;

    @Column(name = "two_factor_code", length = 6)
    private String twoFactorCode;

    @Column(name = "two_factor_expiry")
    private LocalDateTime twoFactorExpiry;

    @OneToOne
    private InfoUsuario info;

    // Relacion uno a muchos con la tabla Recarga
    @OneToMany
    private List<Recarga> recarga;

    @OneToMany
    // @JsonManagedReference
    private List<CanjeRecompensa> canje;
    // Relacion uno a muchos con la tabla mensajes
    @OneToMany
    private List<Mensajes> mensajes;

    // Relacion muchos a uno con la tabla equipo
    @ManyToOne
    @JsonBackReference
    private Equipo equipo;

    public String getFotoPerfil() {
        return (fotoPerfil != null && !fotoPerfil.isBlank())
                ? "/Imagenes/perfil/" + fotoPerfil
                : "/Imagenes/perfil/default.png";
    }

    // Retorna una colección de autoridades (roles) del usuario para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // Los siguientes métodos indican el estado de la cuenta del usuario para
    // control de acceso
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.contraseña; // Retorna la contraseña
    }

    @Override
    public String getUsername() {
        return this.nickname; // Retorna el nombre de usuario
    }

    public Integer getId() {
        return id_usuario;
    }

}
