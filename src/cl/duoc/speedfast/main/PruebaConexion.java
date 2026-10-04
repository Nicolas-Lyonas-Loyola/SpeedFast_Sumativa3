package cl.duoc.speedfast.main;

import cl.duoc.speedfast.util.ConexionDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" PRUEBA DE CONEXION JDBC - SPEEDFAST");
        System.out.println("========================================");

        try (Connection conexion = ConexionDB.conectar()) {

            DatabaseMetaData meta = conexion.getMetaData();

            System.out.println("Conexion a MySQL: OK");
            System.out.println("Base de datos: " + conexion.getCatalog());
            System.out.println("MySQL Server: " + meta.getDatabaseProductVersion());
            System.out.println("Driver JDBC: " + meta.getDriverName());
            System.out.println("Version Driver: " + meta.getDriverVersion());
            System.out.println("Java: " + System.getProperty("java.version"));

        } catch (SQLException e) {

            System.out.println("Conexion a MySQL: ERROR");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Codigo: " + e.getErrorCode());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("========================================");
    }
}