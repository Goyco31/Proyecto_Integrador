package com.integrador.spring.app.DAO;

// Importación de la entidad User que será manipulada por este DAO
import com.integrador.spring.app.Modelo.User;

// Importación de Optional para el manejo seguro de valores que pueden ser nulos
import java.util.Optional;

// Importación de JpaRepository, que proporciona métodos CRUD automáticos y más
import org.springframework.data.jpa.repository.JpaRepository;

// Extiende JpaRepository, lo que le da acceso a operaciones básicas de base de datos
public interface UserDAO extends JpaRepository<User,Integer>{
    // Método personalizado para buscar un usuario por su nickname
    Optional<User> findByNickname(String nickname);
    public boolean existsByCorreo(String correo);
    public void deleteByNickname(String nickname);
    
}