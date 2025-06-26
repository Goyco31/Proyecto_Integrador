package com.integrador.spring.app.DTO;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
// Importaciones de Lombok para generar automáticamente código repetitivo
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

// Anotaciones de Lombok
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    // Nombre del nuevo usuario
    String nombre;
    // Apellido del nuevo usuario
    String apellido;
    // Nombre de usuario único (nickname), usado para iniciar sesión
    String nickname;
    // Correo electrónico del usuario (también puede usarse como identificador o para notificaciones)
    String correo;
    // Contraseña del usuario, en texto plano al ser enviada
    String contraseña;

    // Validación centralizada
    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre), "El nombre no puede estar vacío");
        Preconditions.checkArgument(StringUtils.isNotBlank(apellido), "El apellido no puede estar vacío");
        Preconditions.checkArgument(StringUtils.isNotBlank(nickname), "El nickname no puede estar vacío");
        Preconditions.checkArgument(EmailValidator.getInstance().isValid(correo), "Correo electrónico inválido");
        Preconditions.checkArgument(StringUtils.length(contraseña) >= 8, "La contraseña debe tener al menos 8 caracteres");
    }

    // Normalización de datos
    public void normalize() {
        this.nombre = StringUtils.normalizeSpace(nombre);
        this.apellido = StringUtils.normalizeSpace(apellido);
        this.nickname = StringUtils.normalizeSpace(nickname);
        this.correo = StringUtils.normalizeSpace(correo).toLowerCase();
    }
}
