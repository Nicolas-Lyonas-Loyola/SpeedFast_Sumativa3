package cl.duoc.speedfast.dao.impl;

import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.model.EstadoPedido;
import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.model.TipoPedido;
import cl.duoc.speedfast.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public boolean create(Pedido pedido) {

        String sql = """
                INSERT INTO pedidos(direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo().name());
            ps.setString(3, pedido.getEstado().name());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        pedido.setId(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al registrar pedido: " + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Pedido> readAll() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
                SELECT id, direccion, tipo, estado
                FROM pedidos
                ORDER BY id
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        TipoPedido.valueOf(
                                rs.getString("tipo")
                        ),
                        EstadoPedido.valueOf(
                                rs.getString("estado")
                        )
                );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al listar pedidos: " + e.getMessage()
            );
        }

        return pedidos;
    }

    @Override
    public boolean update(Pedido pedido) {

        String sql = """
                UPDATE pedidos
                SET direccion = ?,
                    tipo = ?,
                    estado = ?
                WHERE id = ?
                """;

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo().name());
            ps.setString(3, pedido.getEstado().name());
            ps.setInt(4, pedido.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al actualizar pedido: " + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        String sql = """
                DELETE FROM pedidos
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
                    "Error al eliminar pedido: " + e.getMessage()
            );
        }

        return false;
    }
}