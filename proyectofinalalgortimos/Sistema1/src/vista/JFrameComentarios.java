package vista;

import modelo.Comentario;
import modelo.Documento;
import servicio.DocumentoServicio;
import servicio.ReporteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameComentarios extends JFrame {

    private static final Color AZUL_OSCURO = new Color(26, 39, 68);
    private static final Color AZUL_MEDIO = new Color(41, 82, 163);
    private static final Color GRIS_FONDO = new Color(245, 246, 250);
    private static final Color TEXTO_OSCURO = new Color(30, 30, 30);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    private JFramePrincipal principal;
    private DocumentoServicio documentoServicio;
    private ReporteServicio reporteServicio;

    private JTextField txfCodigoBuscar;
    private JLabel lblDocInfo;
    private JTextArea txaTexto;
    private JTextField txfAutorComentario;
    private JComboBox<String> cmbTipoComentario;
    private JTable tablaComentarios;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalComentarios;
    private Documento documentoActual;

    public JFrameComentarios(JFramePrincipal principal) {
        this.principal = principal;
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.reporteServicio = new ReporteServicio();

        setTitle("Gesti\u00f3n de Comentarios");
        setSize(800, 550);
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

        JLabel lblTitulo = new JLabel("\ud83d\udcac GESTI\u00d3N DE COMENTARIOS");
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

        lblDocInfo = new JLabel("   Busque un documento");
        lblDocInfo.setFont(FUENTE_NORMAL);
        lblDocInfo.setForeground(new Color(180, 190, 210));
        panelBusqueda.add(lblDocInfo);

        panelTop.add(panelBusqueda, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(320);
        splitPane.setBackground(GRIS_FONDO);

        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setBackground(GRIS_FONDO);
        panelIzq.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JLabel lblAgregar = new JLabel("Agregar Comentario");
        lblAgregar.setFont(FUENTE_SUBTITULO);
        lblAgregar.setForeground(AZUL_OSCURO);
        lblAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblAgregar);
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
        formPanel.add(crearLabel("Texto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txaTexto = new JTextArea(4, 15);
        txaTexto.setFont(FUENTE_NORMAL);
        txaTexto.setLineWrap(true);
        txaTexto.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txaTexto), gbc);

        row++;
        gbc.gridx = 1; gbc.gridy = row;
        JLabel lblContador = new JLabel("Caracteres: 0");
        lblContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblContador.setForeground(new Color(120, 120, 120));
        formPanel.add(lblContador, gbc);

        txaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblContador.setText("Caracteres: " + txaTexto.getText().trim().length());
            }
        });

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfAutorComentario = new JTextField(15);
        txfAutorComentario.setFont(FUENTE_NORMAL);
        formPanel.add(txfAutorComentario, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbTipoComentario = new JComboBox<>(new String[]{
            "Observacion", "Actualizacion", "Aprobacion", "Restauracion"});
        cmbTipoComentario.setFont(FUENTE_NORMAL);
        formPanel.add(cmbTipoComentario, gbc);

        panelIzq.add(formPanel);
        panelIzq.add(Box.createVerticalStrut(10));

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setBackground(GRIS_FONDO);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnAgregar = crearBotonPrimario("Agregar Comentario");
        btnAgregar.addActionListener(e -> agregarComentario());
        panelBotones.add(btnAgregar);

        JButton btnLimpiar = crearBotonSecundario("Limpiar");
        btnLimpiar.addActionListener(e -> {
            txaTexto.setText("");
            txfAutorComentario.setText("");
            cmbTipoComentario.setSelectedIndex(0);
        });
        panelBotones.add(btnLimpiar);

        panelIzq.add(panelBotones);

        JPanel panelDer = new JPanel(new BorderLayout(5, 5));
        panelDer.setBackground(GRIS_FONDO);
        panelDer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        JLabel lblLista = new JLabel("Comentarios del Documento:");
        lblLista.setFont(FUENTE_SUBTITULO);
        lblLista.setForeground(AZUL_OSCURO);
        panelDer.add(lblLista, BorderLayout.NORTH);

        String[] columnas = {"ID", "Tipo", "Autor", "Fecha", "Texto"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row1, int column) { return false; }
        };
        tablaComentarios = new JTable(modeloTabla);
        configurarTabla(tablaComentarios);
        panelDer.add(new JScrollPane(tablaComentarios), BorderLayout.CENTER);

        JButton btnVerComentarios = crearBotonPrimario("Ver Comentarios");
        btnVerComentarios.addActionListener(e -> cargarComentarios());
        panelDer.add(btnVerComentarios, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelIzq);
        splitPane.setRightComponent(panelDer);
        add(splitPane, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInf.setBackground(AZUL_OSCURO);
        panelInf.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        lblTotalComentarios = new JLabel("Total de comentarios: 0");
        lblTotalComentarios.setFont(FUENTE_NORMAL);
        lblTotalComentarios.setForeground(Color.WHITE);
        panelInf.add(lblTotalComentarios);

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
            documentoActual.getTitulo() + " | " + documentoActual.getEstado());
        lblDocInfo.setForeground(Color.WHITE);
        cargarComentarios();
    }

    private void agregarComentario() {
        if (documentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String texto = txaTexto.getText().trim();
        String autor = txfAutorComentario.getText().trim();
        String tipo = (String) cmbTipoComentario.getSelectedItem();

        if (texto.length() < 15) {
            JOptionPane.showMessageDialog(this,
                "El comentario debe tener al menos 15 caracteres (actual: " + texto.length() + ")",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el autor del comentario",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idComentario = documentoServicio.generarCodigoComentario();
        Comentario comentario = new Comentario(idComentario, texto, autor,
            util.Validaciones.obtenerFechaActual(), tipo);

        if (documentoServicio.agregarComentario(documentoActual.getCodigo(), comentario)) {
            JOptionPane.showMessageDialog(this, "Comentario agregado exitosamente",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            cargarComentarios();
            txaTexto.setText("");
            txfAutorComentario.setText("");
            cmbTipoComentario.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar el comentario",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarComentarios() {
        modeloTabla.setRowCount(0);
        if (documentoActual == null) return;

        Comentario[] comentarios = documentoActual.getComentarios().obtenerTodos();
        for (int i = 0; i < comentarios.length; i++) {
            Comentario c = comentarios[i];
            String texto = c.getTexto();
            if (texto.length() > 40) texto = texto.substring(0, 40) + "...";
            modeloTabla.addRow(new Object[]{
                c.getIdComentario(), c.getTipo(), c.getAutor(), c.getFecha(), texto
            });
        }
        lblTotalComentarios.setText("Total de comentarios: " + documentoActual.getComentarios().tamanio());
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
