package cl.duoc.speedfast.dao.impl;

import cl.duoc.speedfast.dao.EntregaDAO;
import cl.duoc.speedfast.model.Entrega;
import cl.duoc.speedfast.util.ConexionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public boolean create(Entrega entrega) {

        String sql = """
                INSERT INTO entregas(
                    id_pedido,
                    id_repartidor,
                    fecha,
                    hora
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );
            ps.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet claves = ps.getGeneratedKeys()) {

                    if (claves.next()) {
                        entrega.setId(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al registrar entrega: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Entrega> readAll() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_pedido,
                    id_repartidor,
                    fecha,
                    hora
                FROM entregas
                ORDER BY id
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar entregas: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    @Override
    public boolean update(Entrega entrega) {

        String sql = """
                UPDATE entregas
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
                WHERE id = ?
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );
            ps.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );
            ps.setInt(5, entrega.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar entrega: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        String sql = """
                DELETE FROM entregas
                WHERE id = ?
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al eliminar entrega: "
                            + e.getMessage()
            );
        }

        return false;
    }
}