package cl.duoc.speedfast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/speedfast_db";

    private static final String USUARIO = "root";

    private static final String PASSWORD =
            System.getenv("MYSQL_PASSWORD");

    private ConexionDB() {
    }

    public static Connection conectar() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "No se encontró la variable de entorno MYSQL_PASSWORD."
            );
        }

        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}