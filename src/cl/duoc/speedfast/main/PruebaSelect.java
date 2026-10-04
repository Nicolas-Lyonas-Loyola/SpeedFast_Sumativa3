package cl.duoc.speedfast.main;

import cl.duoc.speedfast.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PruebaSelect {

    public static void main(String[] args) {

        String sql = """
                SELECT id, nombre
                FROM repartidores
                ORDER BY id
                """;

        System.out.println("========================================");
        System.out.println(" PRUEBA SELECT - SPEEDFAST");
        System.out.println("========================================");

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            int contador = 0;

            while (rs.next()) {

                contador++;

                System.out.println(
                        rs.getInt("id")
                                + " | "
                                + rs.getString("nombre")
                );
            }

            System.out.println("----------------------------------------");
            System.out.println("Registros recuperados: " + contador);

        } catch (SQLException e) {

            System.out.println("SELECT: ERROR");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Codigo: " + e.getErrorCode());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("========================================");
    }
}