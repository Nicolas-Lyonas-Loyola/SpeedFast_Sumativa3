package cl.duoc.speedfast.main;

import cl.duoc.speedfast.view.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal();

            ventana.setVisible(true);
        });
    }

}