package cl.duoc.speedfast.main;

import cl.duoc.speedfast.dao.EntregaDAO;
import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.dao.impl.EntregaDAOImpl;
import cl.duoc.speedfast.dao.impl.PedidoDAOImpl;
import cl.duoc.speedfast.dao.impl.RepartidorDAOImpl;
import cl.duoc.speedfast.model.Entrega;
import cl.duoc.speedfast.model.EstadoPedido;
import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.model.Repartidor;
import cl.duoc.speedfast.model.TipoPedido;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PruebaEntregaDAO {

    public static void main(String[] args) {

        RepartidorDAO repartidorDAO = new RepartidorDAOImpl();
        PedidoDAO pedidoDAO = new PedidoDAOImpl();
        EntregaDAO entregaDAO = new EntregaDAOImpl();

        System.out.println("========================================");
        System.out.println(" PRUEBA CRUD ENTREGA DAO - SPEEDFAST");
        System.out.println("========================================");

        // -------------------------------------------------
        // PREPARACION: crear registros relacionados
        // -------------------------------------------------

        Repartidor repartidorPrueba =
                new Repartidor("Repartidor Prueba Entrega");

        boolean repartidorCreado =
                repartidorDAO.create(repartidorPrueba);

        Pedido pedidoPrueba = new Pedido(
                "Av. SpeedFast 500",
                TipoPedido.ENCOMIENDA,
                EstadoPedido.PENDIENTE
        );

        boolean pedidoCreado =
                pedidoDAO.create(pedidoPrueba);

        System.out.println("\n--- DATOS RELACIONADOS ---");
        System.out.println(
                "Repartidor creado: "
                        + repartidorCreado
                        + " | ID: "
                        + repartidorPrueba.getId()
        );

        System.out.println(
                "Pedido creado: "
                        + pedidoCreado
                        + " | ID: "
                        + pedidoPrueba.getId()
        );

        // -------------------------------------------------
        // CREATE ENTREGA
        // -------------------------------------------------

        Entrega entregaPrueba = new Entrega(
                pedidoPrueba.getId(),
                repartidorPrueba.getId(),
                LocalDate.of(2026, 10, 4),
                LocalTime.of(12, 30)
        );

        boolean creada =
                entregaDAO.create(entregaPrueba);

        System.out.println("\n--- CREATE ---");
        System.out.println("Creada: " + creada);
        System.out.println(
                "ID generado: " + entregaPrueba.getId()
        );

        // -------------------------------------------------
        // READ
        // -------------------------------------------------

        System.out.println("\n--- READ ALL ---");

        List<Entrega> entregas =
                entregaDAO.readAll();

        for (Entrega entrega : entregas) {

            System.out.println(
                    entrega.getId()
                            + " | Pedido: "
                            + entrega.getIdPedido()
                            + " | Repartidor: "
                            + entrega.getIdRepartidor()
                            + " | Fecha: "
                            + entrega.getFecha()
                            + " | Hora: "
                            + entrega.getHora()
            );
        }

        // -------------------------------------------------
        // UPDATE
        // -------------------------------------------------

        System.out.println("\n--- UPDATE ---");

        entregaPrueba.setFecha(
                LocalDate.of(2026, 10, 5)
        );

        entregaPrueba.setHora(
                LocalTime.of(15, 45)
        );

        boolean actualizada =
                entregaDAO.update(entregaPrueba);

        System.out.println(
                "Actualizada: " + actualizada
        );

        entregas = entregaDAO.readAll();

        for (Entrega entrega : entregas) {

            if (entrega.getId()
                    == entregaPrueba.getId()) {

                System.out.println(
                        "Registro actualizado: "
                                + entrega.getId()
                                + " | Pedido: "
                                + entrega.getIdPedido()
                                + " | Repartidor: "
                                + entrega.getIdRepartidor()
                                + " | Fecha: "
                                + entrega.getFecha()
                                + " | Hora: "
                                + entrega.getHora()
                );
            }
        }

        // -------------------------------------------------
        // DELETE ENTREGA
        // -------------------------------------------------

        System.out.println("\n--- DELETE ---");

        boolean eliminada =
                entregaDAO.delete(entregaPrueba.getId());

        System.out.println(
                "Eliminada: " + eliminada
        );

        // -------------------------------------------------
        // VERIFICACION FINAL
        // -------------------------------------------------

        System.out.println("\n--- VERIFICACION FINAL ---");

        boolean existeEntrega =
                entregaDAO.readAll()
                        .stream()
                        .anyMatch(
                                entrega ->
                                        entrega.getId()
                                                == entregaPrueba.getId()
                        );

        System.out.println(
                "La entrega de prueba sigue en la BD: "
                        + existeEntrega
        );

        // -------------------------------------------------
        // LIMPIEZA DE DATOS TEMPORALES
        // -------------------------------------------------

        boolean pedidoEliminado =
                pedidoDAO.delete(pedidoPrueba.getId());

        boolean repartidorEliminado =
                repartidorDAO.delete(
                        repartidorPrueba.getId()
                );

        System.out.println("\n--- LIMPIEZA ---");
        System.out.println(
                "Pedido temporal eliminado: "
                        + pedidoEliminado
        );

        System.out.println(
                "Repartidor temporal eliminado: "
                        + repartidorEliminado
        );

        System.out.println("========================================");
    }
}