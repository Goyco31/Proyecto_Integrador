package com.integrador.spring.app.Servicio;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConectaDB {
    public static Connection getConnetion(){
        Connection cnx = null;

        String url = "jdbc:mysql://localhost:3306/skilltourney?useTimeZone=true&" + "serverTimeZone=UTC&autoReconnect=true";
        
        String user = "root";
        
        String clave = "";
        
        String Driver = "com.mysql.cj.jdbc.Driver";
        
        try{
            Class.forName(Driver);
            cnx = DriverManager.getConnection(url,user,clave);
        }catch(ClassNotFoundException ex){
            
        }catch(SQLException ex){
            
        }
        
        return cnx;
    }
    
    public static void main(String[]args) throws SQLException{
        Connection cnx = ConectaDB.getConnetion();
        
        System.out.println(""+cnx.getCatalog());
    }
}
