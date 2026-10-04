package cl.duoc.speedfast.main;

import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.dao.impl.RepartidorDAOImpl;
import cl.duoc.speedfast.model.Repartidor;

import java.util.List;

public class PruebaRepartidorDAO {

    public static void main(String[] args) {

        RepartidorDAO repartidorDAO = new RepartidorDAOImpl();

        System.out.println("========================================");
        System.out.println(" PRUEBA CRUD REPARTIDOR DAO - SPEEDFAST");
        System.out.println("========================================");

        // CREATE
        Repartidor repartidorPrueba =
                new Repartidor("Repartidor Prueba DAO");

        boolean creado = repartidorDAO.create(repartidorPrueba);

        System.out.println("\n--- CREATE ---");
        System.out.println("Creado: " + creado);
        System.out.println("ID generado: " + repartidorPrueba.getId());
        System.out.println("Nombre: " + repartidorPrueba.getNombre());

        // READ
        System.out.println("\n--- READ ALL ---");

        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {
            System.out.println(repartidor);
        }

        // UPDATE
        System.out.println("\n--- UPDATE ---");

        repartidorPrueba.setNombre("Repartidor Actualizado DAO");

        boolean actualizado =
                repartidorDAO.update(repartidorPrueba);

        System.out.println("Actualizado: " + actualizado);

        // Comprobamos nuevamente desde MySQL
        repartidores = repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {
            if (repartidor.getId() == repartidorPrueba.getId()) {
                System.out.println(
                        "Registro actualizado: " + repartidor
                );
            }
        }

        // DELETE
        System.out.println("\n--- DELETE ---");

        boolean eliminado =
                repartidorDAO.delete(repartidorPrueba.getId());

        System.out.println("Eliminado: " + eliminado);

        // Verificación final
        System.out.println("\n--- VERIFICACION FINAL ---");

        boolean existe =
                repartidorDAO.readAll()
                        .stream()
                        .anyMatch(
                                repartidor ->
                                        repartidor.getId()
                                                == repartidorPrueba.getId()
                        );

        System.out.println(
                "El repartidor de prueba sigue en la BD: " + existe
        );

        System.out.println("========================================");
    }
}