package cl.duoc.speedfast.view;

import cl.duoc.speedfast.dao.EntregaDAO;
import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.dao.impl.EntregaDAOImpl;
import cl.duoc.speedfast.dao.impl.PedidoDAOImpl;
import cl.duoc.speedfast.dao.impl.RepartidorDAOImpl;
import cl.duoc.speedfast.model.Entrega;
import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.model.Repartidor;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PanelEntregas extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd");

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;

    private final JComboBox<Pedido> cmbPedido;
    private final JComboBox<Repartidor> cmbRepartidor;
    private final JTextField txtFecha;
    private final JTextField txtHora;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaEntregas;

    private List<Entrega> entregasActuales =
            new ArrayList<>();

    private int idSeleccionado = -1;

    public PanelEntregas() {

        entregaDAO = new EntregaDAOImpl();
        pedidoDAO = new PedidoDAOImpl();
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
        // PANEL SUPERIOR
        // -------------------------------------------------

        JPanel panelSuperior =
                new JPanel(new BorderLayout());

        JPanel panelCampos =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelCampos.add(
                new JLabel("Pedido:")
        );

        cmbPedido =
                new JComboBox<>();

        panelCampos.add(cmbPedido);

        panelCampos.add(
                new JLabel("Repartidor:")
        );

        cmbRepartidor =
                new JComboBox<>();

        panelCampos.add(cmbRepartidor);

        panelCampos.add(
                new JLabel("Fecha:")
        );

        txtFecha =
                new JTextField(10);

        panelCampos.add(txtFecha);

        panelCampos.add(
                new JLabel("Hora:")
        );

        txtHora =
                new JTextField(6);

        panelCampos.add(txtHora);

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

        JButton btnFiltrarPedido =
                new JButton("Filtrar por Pedido");

        JButton btnFiltrarRepartidor =
                new JButton("Filtrar por Repartidor");

        JButton btnMostrarTodas =
                new JButton("Mostrar todas");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnFiltrarPedido);
        panelBotones.add(btnFiltrarRepartidor);
        panelBotones.add(btnMostrarTodas);

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
                        "Pedido",
                        "Repartidor",
                        "Fecha",
                        "Hora"
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

        tablaEntregas =
                new JTable(modeloTabla);

        tablaEntregas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(tablaEntregas),
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // EVENTOS
        // -------------------------------------------------

        btnGuardar.addActionListener(
                e -> guardarEntrega()
        );

        btnActualizar.addActionListener(
                e -> actualizarEntrega()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        btnFiltrarPedido.addActionListener(
                e -> filtrarPorPedido()
        );

        btnFiltrarRepartidor.addActionListener(
                e -> filtrarPorRepartidor()
        );

        btnMostrarTodas.addActionListener(
                e -> cargarTabla()
        );

        tablaEntregas
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarSeleccion();
                    }
                });

        refrescarDatos();
    }


    /**
     * Refresca la información relacionada desde MySQL.
     * Se utiliza también al volver a la pestaña Entregas.
     */
    public void refrescarDatos() {

        cargarCombos();
        cargarTabla();
        limpiarFormulario();
    }

    /**
     * Carga los pedidos y repartidores existentes
     * directamente desde la base de datos.
     */
    private void cargarCombos() {

        cmbPedido.removeAllItems();
        cmbRepartidor.removeAllItems();

        for (Pedido pedido : pedidoDAO.readAll()) {
            cmbPedido.addItem(pedido);
        }

        for (Repartidor repartidor
                : repartidorDAO.readAll()) {

            cmbRepartidor.addItem(repartidor);
        }
    }

    /**
     * Recupera todas las entregas desde la BD.
     */
    private void cargarTabla() {

        mostrarEntregas(
                entregaDAO.readAll()
        );
    }

    /**
     * Muestra en el JTable la lista de entregas recibida.
     */
    private void mostrarEntregas(
            List<Entrega> entregas
    ) {

        modeloTabla.setRowCount(0);

        entregasActuales =
                new ArrayList<>(entregas);

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Entrega entrega : entregasActuales) {

            Pedido pedido =
                    buscarPedido(
                            entrega.getIdPedido(),
                            pedidos
                    );

            Repartidor repartidor =
                    buscarRepartidor(
                            entrega.getIdRepartidor(),
                            repartidores
                    );

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            pedido != null
                                    ? pedido
                                    : "Pedido #"
                                    + entrega.getIdPedido(),
                            repartidor != null
                                    ? repartidor
                                    : "Repartidor #"
                                    + entrega.getIdRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    /**
     * Registra una entrega después de validar
     * entidades relacionadas, fecha y hora.
     */
    private void guardarEntrega() {

        Pedido pedido =
                (Pedido) cmbPedido.getSelectedItem();

        Repartidor repartidor =
                (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes registrar al menos un pedido "
                            + "antes de crear una entrega.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes registrar al menos un repartidor "
                            + "antes de crear una entrega.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate fecha =
                obtenerFecha();

        if (fecha == null) {
            return;
        }

        LocalTime hora =
                obtenerHora();

        if (hora == null) {
            return;
        }

        Entrega entrega = new Entrega(
                pedido.getId(),
                repartidor.getId(),
                fecha,
                hora
        );

        boolean creada =
                entregaDAO.create(entrega);

        if (creada) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega registrada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza la entrega seleccionada.
     */
    private void actualizarEntrega() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega para actualizar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Pedido pedido =
                (Pedido) cmbPedido.getSelectedItem();

        Repartidor repartidor =
                (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido y repartidor son obligatorios.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate fecha =
                obtenerFecha();

        if (fecha == null) {
            return;
        }

        LocalTime hora =
                obtenerHora();

        if (hora == null) {
            return;
        }

        Entrega entrega = new Entrega(
                idSeleccionado,
                pedido.getId(),
                repartidor.getId(),
                fecha,
                hora
        );

        boolean actualizada =
                entregaDAO.update(entrega);

        if (actualizada) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega actualizada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina la entrega seleccionada.
     */
    private void eliminarEntrega() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar la entrega seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminada =
                entregaDAO.delete(idSeleccionado);

        if (eliminada) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga en el formulario la entrega
     * seleccionada en la tabla.
     */
    private void cargarSeleccion() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1
                || fila >= entregasActuales.size()) {

            return;
        }

        Entrega entrega =
                entregasActuales.get(fila);

        idSeleccionado =
                entrega.getId();

        seleccionarPedidoPorId(
                entrega.getIdPedido()
        );

        seleccionarRepartidorPorId(
                entrega.getIdRepartidor()
        );

        txtFecha.setText(
                entrega.getFecha()
                        .format(FORMATO_FECHA)
        );

        txtHora.setText(
                entrega.getHora()
                        .format(FORMATO_HORA)
        );
    }

    /**
     * Valida y convierte la fecha ingresada.
     */
    private LocalDate obtenerFecha() {

        String texto =
                txtFecha.getText().trim();

        if (texto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha es obligatoria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        try {

            return LocalDate.parse(
                    texto,
                    FORMATO_FECHA
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha inválida. Usa el formato yyyy-MM-dd.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtFecha.requestFocus();

            return null;
        }
    }

    /**
     * Valida y convierte la hora ingresada.
     */
    private LocalTime obtenerHora() {

        String texto =
                txtHora.getText().trim();

        if (texto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La hora es obligatoria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        try {

            return LocalTime.parse(
                    texto,
                    FORMATO_HORA
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hora inválida. Usa el formato HH:mm.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtHora.requestFocus();

            return null;
        }
    }
    /**
     * Muestra solo las entregas asociadas
     * al pedido seleccionado.
     */
    private void filtrarPorPedido() {

        Pedido pedido =
                (Pedido) cmbPedido.getSelectedItem();

        if (pedido == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido para filtrar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<Entrega> filtradas =
                entregaDAO.readAll()
                        .stream()
                        .filter(
                                entrega ->
                                        entrega.getIdPedido()
                                                == pedido.getId()
                        )
                        .toList();

        mostrarEntregas(filtradas);
    }

    /**
     * Muestra solo las entregas asociadas
     * al repartidor seleccionado.
     */
    private void filtrarPorRepartidor() {

        Repartidor repartidor =
                (Repartidor) cmbRepartidor.getSelectedItem();

        if (repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor para filtrar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<Entrega> filtradas =
                entregaDAO.readAll()
                        .stream()
                        .filter(
                                entrega ->
                                        entrega.getIdRepartidor()
                                                == repartidor.getId()
                        )
                        .toList();

        mostrarEntregas(filtradas);
    }
    private Pedido buscarPedido(
            int id,
            List<Pedido> pedidos
    ) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }

    private Repartidor buscarRepartidor(
            int id,
            List<Repartidor> repartidores
    ) {

        for (Repartidor repartidor
                : repartidores) {

            if (repartidor.getId() == id) {
                return repartidor;
            }
        }

        return null;
    }

    private void seleccionarPedidoPorId(
            int id
    ) {

        for (int i = 0;
             i < cmbPedido.getItemCount();
             i++) {

            Pedido pedido =
                    cmbPedido.getItemAt(i);

            if (pedido.getId() == id) {

                cmbPedido.setSelectedIndex(i);

                return;
            }
        }
    }

    private void seleccionarRepartidorPorId(
            int id
    ) {

        for (int i = 0;
             i < cmbRepartidor.getItemCount();
             i++) {

            Repartidor repartidor =
                    cmbRepartidor.getItemAt(i);

            if (repartidor.getId() == id) {

                cmbRepartidor.setSelectedIndex(i);

                return;
            }
        }
    }

    /**
     * Limpia selección y deja fecha/hora
     * preparadas para un nuevo registro.
     */
    private void limpiarFormulario() {

        idSeleccionado = -1;

        tablaEntregas.clearSelection();

        if (cmbPedido.getItemCount() > 0) {
            cmbPedido.setSelectedIndex(0);
        }

        if (cmbRepartidor.getItemCount() > 0) {
            cmbRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText(
                LocalDate.now()
                        .format(FORMATO_FECHA)
        );

        txtHora.setText(
                LocalTime.now()
                        .format(FORMATO_HORA)
        );
    }
}