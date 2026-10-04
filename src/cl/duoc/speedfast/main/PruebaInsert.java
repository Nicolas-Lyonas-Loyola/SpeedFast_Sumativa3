package cl.duoc.speedfast.main;

import cl.duoc.speedfast.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaInsert {

    public static void main(String[] args) {

        String sql = """
                INSERT INTO repartidores(nombre)
                VALUES (?)
                """;

        System.out.println("========================================");
        System.out.println(" PRUEBA INSERT - SPEEDFAST");
        System.out.println("========================================");

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(1, "Camila");

            int filas = ps.executeUpdate();

            System.out.println("Filas insertadas: " + filas);

            try (ResultSet claves = ps.getGeneratedKeys()) {

                if (claves.next()) {
                    System.out.println(
                            "ID generado por MySQL: "
                                    + claves.getInt(1)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("INSERT: ERROR");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Codigo: " + e.getErrorCode());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("========================================");
    }
}
