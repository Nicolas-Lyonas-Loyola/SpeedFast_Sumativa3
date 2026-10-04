package cl.duoc.speedfast.view;

import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.dao.impl.PedidoDAOImpl;
import cl.duoc.speedfast.model.EstadoPedido;
import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.model.TipoPedido;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class PanelPedidos extends JPanel {

    private final PedidoDAO pedidoDAO;

    private final JTextField txtDireccion;
    private final JComboBox<TipoPedido> cmbTipo;
    private final JComboBox<EstadoPedido> cmbEstado;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaPedidos;

    private int idSeleccionado = -1;

    public PanelPedidos() {

        pedidoDAO = new PedidoDAOImpl();

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
        // PANEL SUPERIOR
        // -------------------------------------------------

        JPanel panelSuperior =
                new JPanel(new BorderLayout());

        JPanel panelCampos =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelCampos.add(
                new JLabel("Dirección:")
        );

        txtDireccion =
                new JTextField(22);

        panelCampos.add(txtDireccion);

        panelCampos.add(
                new JLabel("Tipo:")
        );

        cmbTipo =
                new JComboBox<>(TipoPedido.values());

        panelCampos.add(cmbTipo);

        panelCampos.add(
                new JLabel("Estado:")
        );

        cmbEstado =
                new JComboBox<>(EstadoPedido.values());

        panelCampos.add(cmbEstado);

        JPanel panelBotones =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(
                panelCampos,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                panelBotones,
                BorderLayout.CENTER
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // TABLA
        // -------------------------------------------------

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Dirección",
                        "Tipo",
                        "Estado"
                },
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

        tablaPedidos =
                new JTable(modeloTabla);

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(tablaPedidos),
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // EVENTOS
        // -------------------------------------------------

        btnGuardar.addActionListener(
                e -> guardarPedido()
        );

        btnActualizar.addActionListener(
                e -> actualizarPedido()
        );

        btnEliminar.addActionListener(
                e -> eliminarPedido()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarSeleccion();
                    }
                });

        cargarTabla();
    }

    /**
     * Recupera los pedidos almacenados en MySQL
     * y actualiza el JTable.
     */
    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }
    }

    /**
     * Registra un pedido después de validar
     * los datos obligatorios.
     */
    private void guardarPedido() {

        String direccion =
                txtDireccion.getText().trim();

        if (!validarDireccion(direccion)) {
            return;
        }

        TipoPedido tipo =
                (TipoPedido) cmbTipo.getSelectedItem();

        EstadoPedido estado =
                (EstadoPedido) cmbEstado.getSelectedItem();

        Pedido pedido = new Pedido(
                direccion,
                tipo,
                estado
        );

        boolean creado =
                pedidoDAO.create(pedido);

        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza el pedido seleccionado.
     */
    private void actualizarPedido() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido para actualizar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String direccion =
                txtDireccion.getText().trim();

        if (!validarDireccion(direccion)) {
            return;
        }

        TipoPedido tipo =
                (TipoPedido) cmbTipo.getSelectedItem();

        EstadoPedido estado =
                (EstadoPedido) cmbEstado.getSelectedItem();

        Pedido pedido = new Pedido(
                idSeleccionado,
                direccion,
                tipo,
                estado
        );

        boolean actualizado =
                pedidoDAO.update(pedido);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el pedido seleccionado.
     */
    private void eliminarPedido() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar el pedido seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                pedidoDAO.delete(idSeleccionado);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el pedido. "
                            + "Puede estar asociado a una entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga en el formulario los datos
     * de la fila seleccionada.
     */
    private void cargarSeleccion() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        txtDireccion.setText(
                modeloTabla.getValueAt(
                        fila,
                        1
                ).toString()
        );

        cmbTipo.setSelectedItem(
                modeloTabla.getValueAt(
                        fila,
                        2
                )
        );

        cmbEstado.setSelectedItem(
                modeloTabla.getValueAt(
                        fila,
                        3
                )
        );
    }

    /**
     * Valida que la dirección sea obligatoria
     * y compatible con VARCHAR(100) de la BD.
     */
    private boolean validarDireccion(
            String direccion
    ) {

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección del pedido es obligatoria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtDireccion.requestFocus();

            return false;
        }

        if (direccion.length() > 100) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección no puede superar 100 caracteres.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtDireccion.requestFocus();

            return false;
        }

        return true;
    }

    /**
     * Limpia el formulario y la selección actual.
     */
    private void limpiarFormulario() {

        idSeleccionado = -1;

        txtDireccion.setText("");

        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);

        tablaPedidos.clearSelection();

        txtDireccion.requestFocus();
    }
}