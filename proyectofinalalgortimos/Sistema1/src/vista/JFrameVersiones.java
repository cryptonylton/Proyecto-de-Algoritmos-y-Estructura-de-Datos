package vista;

import modelo.Documento;
import modelo.VersionDocumento;
import servicio.DocumentoServicio;
import servicio.ReporteServicio;
import util.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameVersiones extends JFrame {

    private static final Color AZUL_OSCURO = new Color(26, 39, 68);
    private static final Color AZUL_MEDIO = new Color(41, 82, 163);
    private static final Color GRIS_FONDO = new Color(245, 246, 250);
    private static final Color TEXTO_OSCURO = new Color(30, 30, 30);
    private static final Color ROJO_ERROR = new Color(192, 57, 43);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    private JFramePrincipal principal;
    private DocumentoServicio documentoServicio;
    private ReporteServicio reporteServicio;

    private JTextField txfCodigoBuscar;
    private JLabel lblDocInfo;
    private JTextField txfNuevaVersion;
    private JTextArea txaDescripcionNueva;
    private JTextField txfAutor;
    private JTextArea txaMotivo;
    private JTable tablaVersiones;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstadoPila;
    private Documento documentoActual;

    public JFrameVersiones(JFramePrincipal principal) {
        this.principal = principal;
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.reporteServicio = new ReporteServicio();

        setTitle("Gesti\u00f3n de Versiones");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(GRIS_FONDO);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                principal.actualizarBarra();
            }
        });

        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();
    }

    private void crearPanelSuperior() {
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(AZUL_OSCURO);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitulo = new JLabel("\ud83d\udccb GESTI\u00d3N DE VERSIONES");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);
        panelTop.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(AZUL_OSCURO);

        JLabel lblCodigo = new JLabel("C\u00f3digo de Documento:");
        lblCodigo.setFont(FUENTE_NORMAL);
        lblCodigo.setForeground(Color.WHITE);
        panelBusqueda.add(lblCodigo);

        txfCodigoBuscar = new JTextField(12);
        txfCodigoBuscar.setFont(FUENTE_NORMAL);
        panelBusqueda.add(txfCodigoBuscar);

        JButton btnBuscar = crearBotonPrimario("Buscar");
        btnBuscar.addActionListener(e -> buscarDocumento());
        panelBusqueda.add(btnBuscar);

        lblDocInfo = new JLabel("   Busque un documento para ver sus versiones");
        lblDocInfo.setFont(FUENTE_NORMAL);
        lblDocInfo.setForeground(new Color(180, 190, 210));
        panelBusqueda.add(lblDocInfo);

        panelTop.add(panelBusqueda, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBackground(GRIS_FONDO);

        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setBackground(GRIS_FONDO);
        panelIzq.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JLabel lblCrear = new JLabel("Crear Nueva Versi\u00f3n");
        lblCrear.setFont(FUENTE_SUBTITULO);
        lblCrear.setForeground(AZUL_OSCURO);
        lblCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblCrear);
        panelIzq.add(Box.createVerticalStrut(8));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Nueva Versi\u00f3n:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfNuevaVersion = new JTextField(12);
        txfNuevaVersion.setFont(FUENTE_NORMAL);
        formPanel.add(txfNuevaVersion, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Descripci\u00f3n:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txaDescripcionNueva = new JTextArea(3, 12);
        txaDescripcionNueva.setFont(FUENTE_NORMAL);
        txaDescripcionNueva.setLineWrap(true);
        txaDescripcionNueva.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txaDescripcionNueva), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfAutor = new JTextField(12);
        txfAutor.setFont(FUENTE_NORMAL);
        formPanel.add(txfAutor, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txaMotivo = new JTextArea(2, 12);
        txaMotivo.setFont(FUENTE_NORMAL);
        txaMotivo.setLineWrap(true);
        txaMotivo.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txaMotivo), gbc);

        panelIzq.add(formPanel);
        panelIzq.add(Box.createVerticalStrut(10));

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setBackground(GRIS_FONDO);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCrear = crearBotonPrimario("Crear Versi\u00f3n");
        btnCrear.addActionListener(e -> crearVersion());
        panelBotones.add(btnCrear);

        JButton btnRestaurar = crearBotonSecundario("Restaurar Versi\u00f3n Anterior");
        btnRestaurar.addActionListener(e -> restaurarVersion());
        panelBotones.add(btnRestaurar);

        panelIzq.add(panelBotones);

        JPanel panelDer = new JPanel(new BorderLayout(5, 5));
        panelDer.setBackground(GRIS_FONDO);
        panelDer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        JLabel lblHistorial = new JLabel("Historial de Versiones");
        lblHistorial.setFont(FUENTE_SUBTITULO);
        lblHistorial.setForeground(AZUL_OSCURO);
        panelDer.add(lblHistorial, BorderLayout.NORTH);

        String[] columnas = {"Versi\u00f3n", "Autor", "Fecha", "Motivo", "Descripci\u00f3n"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaVersiones = new JTable(modeloTabla);
        configurarTabla(tablaVersiones);
        panelDer.add(new JScrollPane(tablaVersiones), BorderLayout.CENTER);

        JButton btnVerHistorial = crearBotonPrimario("Ver Historial");
        btnVerHistorial.addActionListener(e -> cargarHistorial());
        panelDer.add(btnVerHistorial, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelIzq);
        splitPane.setRightComponent(panelDer);
        add(splitPane, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInf.setBackground(AZUL_OSCURO);
        panelInf.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        lblEstadoPila = new JLabel("Versiones en pila: 0");
        lblEstadoPila.setFont(FUENTE_NORMAL);
        lblEstadoPila.setForeground(Color.WHITE);
        panelInf.add(lblEstadoPila);

        JButton btnVolver = crearBotonSecundario("Volver al Men\u00fa");
        btnVolver.addActionListener(e -> dispose());
        panelInf.add(btnVolver);

        add(panelInf, BorderLayout.SOUTH);
    }

    private void buscarDocumento() {
        String codigo = txfCodigoBuscar.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un c\u00f3digo de documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        documentoActual = documentoServicio.buscarPorCodigo(codigo);
        if (documentoActual == null || documentoActual.isEliminadoLogico()) {
            JOptionPane.showMessageDialog(this, "Documento no encontrado",
                "Error", JOptionPane.ERROR_MESSAGE);
            documentoActual = null;
            lblDocInfo.setText("   Documento no encontrado");
            return;
        }
        lblDocInfo.setText("   " + documentoActual.getCodigo() + " | " +
            documentoActual.getTitulo() + " | " + documentoActual.getEstado() +
            " | " + documentoActual.getVersion());
        lblDocInfo.setForeground(Color.WHITE);
        cargarHistorial();
    }

    private void crearVersion() {
        if (documentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!documentoActual.getEstado().equals("Borrador") && !documentoActual.getEstado().equals("Observado")) {
            JOptionPane.showMessageDialog(this,
                "El documento est\u00e1 Publicado. Debe crear un documento nuevo.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevaVersion = txfNuevaVersion.getText().trim();
        String descripcion = txaDescripcionNueva.getText().trim();
        String autor = txfAutor.getText().trim();
        String motivo = txaMotivo.getText().trim();

        if (!Validaciones.validarVersion(nuevaVersion)) {
            JOptionPane.showMessageDialog(this, "La versi\u00f3n debe tener formato vX.Y",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Validaciones.validarDescripcion(descripcion)) {
            JOptionPane.showMessageDialog(this, "La descripci\u00f3n debe tener al menos 10 caracteres",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el autor",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el motivo del cambio",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (documentoServicio.crearNuevaVersion(documentoActual.getCodigo(),
                nuevaVersion, descripcion, autor, motivo)) {
            JOptionPane.showMessageDialog(this, "Nueva versi\u00f3n creada exitosamente",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            cargarHistorial();
            buscarDocumento();
            txfNuevaVersion.setText("");
            txaDescripcionNueva.setText("");
            txfAutor.setText("");
            txaMotivo.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo crear la versi\u00f3n",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restaurarVersion() {
        if (documentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (documentoActual.getEstado().equals("Publicado")) {
            JOptionPane.showMessageDialog(this,
                "No se puede restaurar versiones de un documento Publicado",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (documentoActual.getHistorialVersiones().estaVacia()) {
            JOptionPane.showMessageDialog(this,
                "No hay versiones anteriores en la pila",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "\u00bfDesea restaurar la versi\u00f3n anterior del documento?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (documentoServicio.restaurarVersionAnterior(documentoActual.getCodigo())) {
                JOptionPane.showMessageDialog(this, "Versi\u00f3n restaurada exitosamente",
                    "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
                buscarDocumento();
                cargarHistorial();
            }
        }
    }

    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        if (documentoActual == null) return;

        VersionDocumento[] versiones = documentoActual.getHistorialVersiones().obtenerTodos();
        for (int i = 0; i < versiones.length; i++) {
            VersionDocumento v = versiones[i];
            String desc = v.getDescripcion();
            if (desc.length() > 30) desc = desc.substring(0, 30) + "...";
            modeloTabla.addRow(new Object[]{
                v.getVersion(), v.getAutor(), v.getFecha(), v.getMotivoCambio(), desc
            });
        }
        lblEstadoPila.setText("Versiones en pila: " + documentoActual.getHistorialVersiones().tamanio());
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_NORMAL);
        lbl.setForeground(TEXTO_OSCURO);
        return lbl;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void configurarTabla(JTable tabla) {
        tabla.setRowHeight(25);
        tabla.setFont(FUENTE_NORMAL);
        tabla.setSelectionBackground(AZUL_MEDIO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(230, 233, 240));

        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(AZUL_OSCURO);
                c.setForeground(java.awt.Color.WHITE);
                c.setFont(FUENTE_SUBTITULO);
                return c;
            }
        });

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 243, 248));
                }
                return c;
            }
        });
    }
}
