package cl.duoc.speedfast.view;

import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.dao.impl.RepartidorDAOImpl;
import cl.duoc.speedfast.model.Repartidor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class PanelRepartidores extends JPanel {

    private final RepartidorDAO repartidorDAO;

    private final JTextField txtNombre;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaRepartidores;

    private int idSeleccionado = -1;

    public PanelRepartidores() {

        repartidorDAO = new RepartidorDAOImpl();

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // -------------------------------------------------
        // FORMULARIO
        // -------------------------------------------------

        JPanel panelFormulario =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelFormulario.add(
                new JLabel("Nombre:")
        );

        txtNombre =
                new JTextField(25);

        panelFormulario.add(txtNombre);

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // TABLA
        // -------------------------------------------------

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre"},
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tablaRepartidores =
                new JTable(modeloTabla);

        tablaRepartidores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(tablaRepartidores),
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // EVENTOS
        // -------------------------------------------------

        btnGuardar.addActionListener(
                e -> guardarRepartidor()
        );

        btnActualizar.addActionListener(
                e -> actualizarRepartidor()
        );

        btnEliminar.addActionListener(
                e -> eliminarRepartidor()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarSeleccion();
                    }
                });

        cargarTabla();
    }

    /**
     * Recupera todos los repartidores desde la BD
     * y actualiza el JTable.
     */
    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    /**
     * Registra un nuevo repartidor después
     * de validar el nombre.
     */
    private void guardarRepartidor() {

        String nombre =
                txtNombre.getText().trim();

        if (!validarNombre(nombre)) {
            return;
        }

        Repartidor repartidor =
                new Repartidor(nombre);

        boolean creado =
                repartidorDAO.create(repartidor);

        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza el repartidor seleccionado.
     */
    private void actualizarRepartidor() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor para actualizar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre =
                txtNombre.getText().trim();

        if (!validarNombre(nombre)) {
            return;
        }

        Repartidor repartidor =
                new Repartidor(
                        idSeleccionado,
                        nombre
                );

        boolean actualizado =
                repartidorDAO.update(repartidor);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el repartidor seleccionado.
     */
    private void eliminarRepartidor() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar el repartidor seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                repartidorDAO.delete(idSeleccionado);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el repartidor. "
                            + "Puede estar asociado a una entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga en el formulario el registro seleccionado.
     */
    private void cargarSeleccion() {

        int fila =
                tablaRepartidores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        txtNombre.setText(
                modeloTabla.getValueAt(
                        fila,
                        1
                ).toString()
        );
    }

    /**
     * Valida que el nombre sea obligatorio
     * y compatible con VARCHAR(100) de la BD.
     */
    private boolean validarNombre(String nombre) {

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre del repartidor es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return false;
        }

        if (nombre.length() > 100) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede superar 100 caracteres.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return false;
        }

        return true;
    }

    /**
     * Limpia la selección y prepara el formulario
     * para una nueva operación.
     */
    private void limpiarFormulario() {

        idSeleccionado = -1;

        txtNombre.setText("");

        tablaRepartidores.clearSelection();

        txtNombre.requestFocus();
    }
}