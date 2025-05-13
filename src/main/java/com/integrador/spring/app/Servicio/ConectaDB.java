package com.integrador.spring.app.Servicio;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

// Clase responsable de establecer la conexión con la base de datos MySQL
public class ConectaDB {

     // Método estático que retorna un objeto Connection si la conexión fue exitosa
    public static Connection getConnetion(){
        Connection cnx = null;

        // URL de conexión a la base de datos 'skilltourney' en localhost
        String url = "jdbc:mysql://localhost:3306/skilltourney?useTimeZone=true&" + "serverTimeZone=UTC&autoReconnect=true";
        
        String user = "root"; // Usuario de la base de datos
        
        String clave = ""; // Contraseña del usuario
        
        String Driver = "com.mysql.cj.jdbc.Driver"; // Driver JDBC de MySQL
        
        try{

            // Carga el driver JDBC en tiempo de ejecución
            Class.forName(Driver);  

            // Establece la conexión con la base de datos
            cnx = DriverManager.getConnection(url,user,clave);
        }catch(ClassNotFoundException ex){

            // Manejo de error si no se encuentra el driver JDBC
        }catch(SQLException ex){
            
        }
        
        return cnx;
    }
    
     // Método main para probar la conexión a la base de datos
    public static void main(String[]args) throws SQLException{
        Connection cnx = ConectaDB.getConnetion();
        
        System.out.println(""+cnx.getCatalog());
    }
}
