package cl.duoc.speedfast.main;

import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.dao.impl.PedidoDAOImpl;
import cl.duoc.speedfast.model.EstadoPedido;
import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.model.TipoPedido;

import java.util.List;

public class PruebaPedidoDAO {

    public static void main(String[] args) {

        PedidoDAO pedidoDAO = new PedidoDAOImpl();

        System.out.println("========================================");
        System.out.println(" PRUEBA CRUD PEDIDO DAO - SPEEDFAST");
        System.out.println("========================================");

        // CREATE
        Pedido pedidoPrueba = new Pedido(
                "Av. Alameda 123",
                TipoPedido.COMIDA,
                EstadoPedido.PENDIENTE
        );

        boolean creado = pedidoDAO.create(pedidoPrueba);

        System.out.println("\n--- CREATE ---");
        System.out.println("Creado: " + creado);
        System.out.println("ID generado: " + pedidoPrueba.getId());
        System.out.println("Direccion: " + pedidoPrueba.getDireccion());
        System.out.println("Tipo: " + pedidoPrueba.getTipo());
        System.out.println("Estado: " + pedidoPrueba.getEstado());

        // READ
        System.out.println("\n--- READ ALL ---");

        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {
            System.out.println(
                    pedido.getId()
                            + " | "
                            + pedido.getDireccion()
                            + " | "
                            + pedido.getTipo()
                            + " | "
                            + pedido.getEstado()
            );
        }

        // UPDATE
        System.out.println("\n--- UPDATE ---");

        pedidoPrueba.setDireccion("Av. Providencia 456");
        pedidoPrueba.setTipo(TipoPedido.EXPRESS);
        pedidoPrueba.setEstado(EstadoPedido.EN_REPARTO);

        boolean actualizado =
                pedidoDAO.update(pedidoPrueba);

        System.out.println("Actualizado: " + actualizado);

        pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == pedidoPrueba.getId()) {

                System.out.println(
                        "Registro actualizado: "
                                + pedido.getId()
                                + " | "
                                + pedido.getDireccion()
                                + " | "
                                + pedido.getTipo()
                                + " | "
                                + pedido.getEstado()
                );
            }
        }

        // DELETE
        System.out.println("\n--- DELETE ---");

        boolean eliminado =
                pedidoDAO.delete(pedidoPrueba.getId());

        System.out.println("Eliminado: " + eliminado);

        // VERIFICACION FINAL
        System.out.println("\n--- VERIFICACION FINAL ---");

        boolean existe =
                pedidoDAO.readAll()
                        .stream()
                        .anyMatch(
                                pedido ->
                                        pedido.getId()
                                                == pedidoPrueba.getId()
                        );

        System.out.println(
                "El pedido de prueba sigue en la BD: " + existe
        );

        System.out.println("========================================");
    }
}