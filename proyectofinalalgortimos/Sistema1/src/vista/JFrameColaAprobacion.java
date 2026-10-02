package vista;

import modelo.Documento;
import modelo.SolicitudAprobacion;
import servicio.AprobacionServicio;
import servicio.DocumentoServicio;
import servicio.ReporteServicio;
import util.Catalogos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameColaAprobacion extends JFrame {

    private static final Color AZUL_OSCURO = new Color(26, 39, 68);
    private static final Color AZUL_MEDIO = new Color(41, 82, 163);
    private static final Color GRIS_FONDO = new Color(245, 246, 250);
    private static final Color TEXTO_OSCURO = new Color(30, 30, 30);
    private static final Color VERDE_EXITO = new Color(39, 174, 96);
    private static final Color ROJO_ERROR = new Color(192, 57, 43);
    private static final Color AMARILLO_ADVERTENCIA = new Color(230, 126, 34);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    private JFramePrincipal principal;
    private DocumentoServicio documentoServicio;
    private AprobacionServicio aprobacionServicio;
    private ReporteServicio reporteServicio;

    private JTextField txfCodigoDocEnviar;
    private JLabel lblDocEnviarInfo;
    private JComboBox<String> cmbRevisor;
    private JComboBox<String> cmbPrioridad;
    private JTextField txfSolicitante;
    private JTable tablaCola;
    private DefaultTableModel modeloTabla;
    private JLabel lblSiguiente;
    private JTextArea txaComentarioRevisor;
    private JLabel lblContadorCola;
    private SolicitudAprobacion solicitudTomada;

    public JFrameColaAprobacion(JFramePrincipal principal) {
        this.principal = principal;
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.aprobacionServicio = AprobacionServicio.getInstancia();
        this.reporteServicio = new ReporteServicio();

        setTitle("Cola de Aprobaci\u00f3n");
        setSize(900, 650);
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

        cargarCola();
    }

    private void crearPanelSuperior() {
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(AZUL_OSCURO);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitulo = new JLabel("\u2705 COLA DE APROBACI\u00d3N");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);
        panelTop.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelEnviar = new JPanel(new GridBagLayout());
        panelEnviar.setBackground(AZUL_OSCURO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblCod = new JLabel("C\u00f3digo Doc:");
        lblCod.setForeground(Color.WHITE);
        lblCod.setFont(FUENTE_NORMAL);
        panelEnviar.add(lblCod, gbc);

        gbc.gridx = 1;
        txfCodigoDocEnviar = new JTextField(10);
        txfCodigoDocEnviar.setFont(FUENTE_NORMAL);
        panelEnviar.add(txfCodigoDocEnviar, gbc);

        gbc.gridx = 2;
        JButton btnBuscar = crearBotonPrimario("Buscar Documento");
        btnBuscar.addActionListener(e -> buscarDocumentoEnviar());
        panelEnviar.add(btnBuscar, gbc);

        gbc.gridx = 3;
        lblDocEnviarInfo = new JLabel("  ");
        lblDocEnviarInfo.setForeground(new Color(180, 190, 210));
        lblDocEnviarInfo.setFont(FUENTE_NORMAL);
        panelEnviar.add(lblDocEnviarInfo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblSol = new JLabel("Solicitante:");
        lblSol.setForeground(Color.WHITE);
        lblSol.setFont(FUENTE_NORMAL);
        panelEnviar.add(lblSol, gbc);

        gbc.gridx = 1;
        txfSolicitante = new JTextField(10);
        txfSolicitante.setFont(FUENTE_NORMAL);
        panelEnviar.add(txfSolicitante, gbc);

        gbc.gridx = 2;
        JLabel lblRev = new JLabel("Revisor:");
        lblRev.setForeground(Color.WHITE);
        lblRev.setFont(FUENTE_NORMAL);
        panelEnviar.add(lblRev, gbc);

        gbc.gridx = 3;
        cmbRevisor = new JComboBox<>(Catalogos.RESPONSABLES);
        cmbRevisor.setFont(FUENTE_NORMAL);
        panelEnviar.add(cmbRevisor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPri = new JLabel("Prioridad:");
        lblPri.setForeground(Color.WHITE);
        lblPri.setFont(FUENTE_NORMAL);
        panelEnviar.add(lblPri, gbc);

        gbc.gridx = 1;
        cmbPrioridad = new JComboBox<>(Catalogos.PRIORIDADES);
        cmbPrioridad.setFont(FUENTE_NORMAL);
        panelEnviar.add(cmbPrioridad, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        JButton btnEnviar = crearBotonPrimario("Enviar a Cola de Aprobaci\u00f3n");
        btnEnviar.addActionListener(e -> enviarAprobacion());
        panelEnviar.add(btnEnviar, gbc);
        gbc.gridwidth = 1;

        panelTop.add(panelEnviar, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(530);
        splitPane.setBackground(GRIS_FONDO);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBackground(GRIS_FONDO);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        JLabel lblCola = new JLabel("Cola de Aprobaci\u00f3n (por prioridad):");
        lblCola.setFont(FUENTE_SUBTITULO);
        lblCola.setForeground(AZUL_OSCURO);
        panelTabla.add(lblCola, BorderLayout.NORTH);

        String[] columnas = {"ID", "C\u00f3digo Doc", "T\u00edtulo", "Solicitante", "Revisor", "Prioridad", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCola = new JTable(modeloTabla);
        configurarTabla(tablaCola);
        panelTabla.add(new JScrollPane(tablaCola), BorderLayout.CENTER);

        JPanel panelDer = new JPanel();
        panelDer.setLayout(new BoxLayout(panelDer, BoxLayout.Y_AXIS));
        panelDer.setBackground(GRIS_FONDO);
        panelDer.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));

        JLabel lblAtender = new JLabel("Atender Solicitud");
        lblAtender.setFont(FUENTE_SUBTITULO);
        lblAtender.setForeground(AZUL_OSCURO);
        lblAtender.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDer.add(lblAtender);
        panelDer.add(Box.createVerticalStrut(8));

        lblSiguiente = new JLabel("<html><b>Siguiente en cola:</b><br/>Ninguna solicitud tomada</html>");
        lblSiguiente.setFont(FUENTE_NORMAL);
        lblSiguiente.setForeground(TEXTO_OSCURO);
        lblSiguiente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDer.add(lblSiguiente);
        panelDer.add(Box.createVerticalStrut(8));

        JLabel lblComent = new JLabel("Comentario del revisor:");
        lblComent.setFont(FUENTE_NORMAL);
        lblComent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDer.add(lblComent);

        txaComentarioRevisor = new JTextArea(4, 20);
        txaComentarioRevisor.setFont(FUENTE_NORMAL);
        txaComentarioRevisor.setLineWrap(true);
        txaComentarioRevisor.setWrapStyleWord(true);
        JScrollPane spComent = new JScrollPane(txaComentarioRevisor);
        spComent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDer.add(spComent);

        JLabel lblContador = new JLabel("Caracteres: 0");
        lblContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblContador.setForeground(new Color(120, 120, 120));
        lblContador.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDer.add(lblContador);

        txaComentarioRevisor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblContador.setText("Caracteres: " + txaComentarioRevisor.getText().trim().length());
            }
        });

        panelDer.add(Box.createVerticalStrut(10));

        JButton btnTomar = crearBotonPrimario("Tomar Siguiente Solicitud");
        btnTomar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTomar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnTomar.addActionListener(e -> tomarSolicitud());
        panelDer.add(btnTomar);
        panelDer.add(Box.createVerticalStrut(5));

        JButton btnAprobar = new JButton("Aprobar");
        btnAprobar.setFont(FUENTE_BOTON);
        btnAprobar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAprobar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAprobar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnAprobar.addActionListener(e -> aprobarSolicitud());
        panelDer.add(btnAprobar);
        panelDer.add(Box.createVerticalStrut(5));

        JButton btnObservar = new JButton("Observar");
        btnObservar.setFont(FUENTE_BOTON);
        btnObservar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnObservar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnObservar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnObservar.addActionListener(e -> observarSolicitud());
        panelDer.add(btnObservar);
        panelDer.add(Box.createVerticalStrut(5));

        JButton btnActualizar = crearBotonSecundario("Actualizar Cola");
        btnActualizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnActualizar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnActualizar.addActionListener(e -> cargarCola());
        panelDer.add(btnActualizar);

        splitPane.setLeftComponent(panelTabla);
        splitPane.setRightComponent(panelDer);
        add(splitPane, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInf.setBackground(AZUL_OSCURO);
        panelInf.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        lblContadorCola = new JLabel("Solicitudes en cola: 0");
        lblContadorCola.setFont(FUENTE_NORMAL);
        lblContadorCola.setForeground(Color.WHITE);
        panelInf.add(lblContadorCola);

        JButton btnVolver = crearBotonSecundario("Volver al Men\u00fa");
        btnVolver.addActionListener(e -> dispose());
        panelInf.add(btnVolver);

        add(panelInf, BorderLayout.SOUTH);
    }

    private void buscarDocumentoEnviar() {
        String codigo = txfCodigoDocEnviar.getText().trim();
        Documento doc = documentoServicio.buscarPorCodigo(codigo);
        if (doc == null || doc.isEliminadoLogico()) {
            JOptionPane.showMessageDialog(this, "Documento no encontrado",
                "Error", JOptionPane.ERROR_MESSAGE);
            lblDocEnviarInfo.setText("  ");
            return;
        }
        lblDocEnviarInfo.setText("  " + doc.getTitulo() + " | Estado: " + doc.getEstado());
    }

    private void enviarAprobacion() {
        String codigo = txfCodigoDocEnviar.getText().trim();
        String solicitante = txfSolicitante.getText().trim();
        String revisor = (String) cmbRevisor.getSelectedItem();
        String prioridad = (String) cmbPrioridad.getSelectedItem();

        if (codigo.isEmpty() || solicitante.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (aprobacionServicio.enviarAAprobacion(codigo, solicitante, revisor, prioridad)) {
            JOptionPane.showMessageDialog(this, "Solicitud enviada a la cola de aprobaci\u00f3n",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            cargarCola();
            principal.actualizarBarra();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo enviar. Verifique que el documento exista, no est\u00e9 eliminado y no tenga solicitud pendiente.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tomarSolicitud() {
        if (aprobacionServicio.colaVacia()) {
            JOptionPane.showMessageDialog(this, "La cola de aprobaci\u00f3n est\u00e1 vac\u00eda",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        solicitudTomada = aprobacionServicio.tomarSiguienteSolicitud();
        if (solicitudTomada != null) {
            lblSiguiente.setText("<html><b>Siguiente en cola:</b><br/>" +
                solicitudTomada.getIdSolicitud() + "<br/>" +
                "Doc: " + solicitudTomada.getCodigoDocumento() + "<br/>" +
                "T\u00edtulo: " + solicitudTomada.getTituloDocumento() + "<br/>" +
                "Prioridad: " + solicitudTomada.getPrioridad() + "<br/>" +
                "Revisor: " + solicitudTomada.getRevisor() + "</html>");
            cargarCola();
        }
    }

    private void aprobarSolicitud() {
        if (solicitudTomada == null) {
            JOptionPane.showMessageDialog(this, "Primero tome una solicitud de la cola",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String comentario = txaComentarioRevisor.getText().trim();
        if (comentario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un comentario de aprobaci\u00f3n",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "\u00bfDesea aprobar la solicitud " + solicitudTomada.getIdSolicitud() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (aprobacionServicio.aprobarSolicitud(solicitudTomada, comentario)) {
                JOptionPane.showMessageDialog(this, "Solicitud aprobada exitosamente",
                    "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
                solicitudTomada = null;
                lblSiguiente.setText("<html><b>Siguiente en cola:</b><br/>Ninguna solicitud tomada</html>");
                txaComentarioRevisor.setText("");
                cargarCola();
                principal.actualizarBarra();
            }
        }
    }

    private void observarSolicitud() {
        if (solicitudTomada == null) {
            JOptionPane.showMessageDialog(this, "Primero tome una solicitud de la cola",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String comentario = txaComentarioRevisor.getText().trim();
        if (comentario.length() < 15) {
            JOptionPane.showMessageDialog(this,
                "El comentario de observaci\u00f3n debe tener al menos 15 caracteres (actual: " + comentario.length() + ")",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (aprobacionServicio.observarSolicitud(solicitudTomada, comentario)) {
            JOptionPane.showMessageDialog(this, "Solicitud observada exitosamente",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            solicitudTomada = null;
            lblSiguiente.setText("<html><b>Siguiente en cola:</b><br/>Ninguna solicitud tomada</html>");
            txaComentarioRevisor.setText("");
            cargarCola();
            principal.actualizarBarra();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo observar la solicitud",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCola() {
        modeloTabla.setRowCount(0);
        SolicitudAprobacion[] solicitudes = aprobacionServicio.obtenerTodasEnCola();
        for (int i = 0; i < solicitudes.length; i++) {
            SolicitudAprobacion s = solicitudes[i];
            modeloTabla.addRow(new Object[]{
                s.getIdSolicitud(), s.getCodigoDocumento(), s.getTituloDocumento(),
                s.getSolicitante(), s.getRevisor(), s.getPrioridad(),
                s.getFechaSolicitud(), s.getEstado()
            });
        }
        lblContadorCola.setText("Solicitudes en cola: " + aprobacionServicio.tamaniosCola());
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
