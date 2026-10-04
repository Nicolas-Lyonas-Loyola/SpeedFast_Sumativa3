package cl.duoc.speedfast.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PanelRepartidores panelRepartidores =
                new PanelRepartidores();

        PanelPedidos panelPedidos =
                new PanelPedidos();

        PanelEntregas panelEntregas =
                new PanelEntregas();

        JTabbedPane pestanas =
                new JTabbedPane();

        pestanas.addTab(
                "Repartidores",
                panelRepartidores
        );

        pestanas.addTab(
                "Pedidos",
                panelPedidos
        );

        pestanas.addTab(
                "Entregas",
                panelEntregas
        );

        /*
         * Cada vez que se abre Entregas se vuelven
         * a consultar Pedido y Repartidor desde la BD.
         */
        pestanas.addChangeListener(e -> {

            if (pestanas.getSelectedComponent()
                    == panelEntregas) {

                panelEntregas.refrescarDatos();
            }
        });

        add(
                pestanas,
                BorderLayout.CENTER
        );
    }
}