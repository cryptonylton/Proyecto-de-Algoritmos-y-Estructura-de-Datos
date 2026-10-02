package vista;

import servicio.AprobacionServicio;
import servicio.DocumentoServicio;

import javax.swing.*;
import java.awt.*;


public class JFramePrincipal extends JFrame {

    private static final Color AZUL_OSCURO = new Color(26, 39, 68);
    private static final Color AZUL_MEDIO = new Color(41, 82, 163);
    private static final Color GRIS_FONDO = new Color(245, 246, 250);
    private static final Color TEXTO_OSCURO = new Color(30, 30, 30);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    private DocumentoServicio documentoServicio;
    private AprobacionServicio aprobacionServicio;
    private JLabel lblEstado;

    public JFramePrincipal() {
        documentoServicio = DocumentoServicio.getInstancia();
        aprobacionServicio = AprobacionServicio.getInstancia();

        setTitle("Sistema de Gestion Documental v1.0");
        setSize(800, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearMenuBar();
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();
    }

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(AZUL_OSCURO);

        JMenu menuSistema = new JMenu("Sistema");
        menuSistema.setForeground(Color.WHITE);
        menuSistema.setFont(FUENTE_NORMAL);

        JMenuItem itemInicio = new JMenuItem("Inicio");
        itemInicio.setFont(FUENTE_NORMAL);
        itemInicio.addActionListener(e -> toFront());
        menuSistema.add(itemInicio);
        menuSistema.addSeparator();

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setFont(FUENTE_NORMAL);
        itemSalir.addActionListener(e -> System.exit(0));
        menuSistema.add(itemSalir);

        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setForeground(Color.WHITE);
        menuAyuda.setFont(FUENTE_NORMAL);

        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.setFont(FUENTE_NORMAL);
        itemAcerca.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Sistema de Gesti\u00f3n Documental v1.0\n" +
                "Proyecto Acad\u00e9mico\n" +
                "Curso: Algoritmos y Estructuras de Datos\n" +
                "Java 21 + Java Swing",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);

        menuBar.add(menuSistema);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);
    }

    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(AZUL_OSCURO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTI\u00d3N DOCUMENTAL");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Algoritmos y Estructuras de Datos");
        lblSubtitulo.setFont(FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(new Color(180, 190, 210));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(5));
        panelSuperior.add(lblSubtitulo);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new GridLayout(2, 3, 15, 15));
        panelCentral.setBackground(GRIS_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JButton btnDocumentos = crearBotonMenu("\ud83d\udcc4", "Documentos");
        btnDocumentos.addActionListener(e -> {
            JFrameDocumento frame = new JFrameDocumento(this);
            frame.setVisible(true);
        });

        JButton btnVersiones = crearBotonMenu("\ud83d\udccb", "Versiones");
        btnVersiones.addActionListener(e -> {
            JFrameVersiones frame = new JFrameVersiones(this);
            frame.setVisible(true);
        });

        JButton btnAprobaciones = crearBotonMenu("\u2705", "Aprobaciones");
        btnAprobaciones.addActionListener(e -> {
            JFrameColaAprobacion frame = new JFrameColaAprobacion(this);
            frame.setVisible(true);
        });

        JButton btnComentarios = crearBotonMenu("\ud83d\udcac", "Comentarios");
        btnComentarios.addActionListener(e -> {
            JFrameComentarios frame = new JFrameComentarios(this);
            frame.setVisible(true);
        });

        JButton btnBusqueda = crearBotonMenu("\ud83d\udd0d", "B\u00fasqueda y Reportes");
        btnBusqueda.addActionListener(e -> {
            JFrameBusquedaDocumento frame = new JFrameBusquedaDocumento(this);
            frame.setVisible(true);
        });

        panelCentral.add(btnDocumentos);
        panelCentral.add(btnVersiones);
        panelCentral.add(btnAprobaciones);
        panelCentral.add(btnComentarios);
        panelCentral.add(btnBusqueda);
        panelCentral.add(new JLabel());

        add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String icono, String texto) {
        JButton boton = new JButton("<html><center><span style='font-size:28px;'>" + icono +
            "</span><br/><span style='font-size:12px;'>" + texto + "</span></center></html>");
        boton.setPreferredSize(new Dimension(150, 100));
        boton.setMinimumSize(new Dimension(150, 100));
        boton.setFont(FUENTE_BOTON);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.setBackground(AZUL_OSCURO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        lblEstado = new JLabel();
        lblEstado.setFont(FUENTE_NORMAL);
        lblEstado.setForeground(Color.WHITE);
        actualizarBarra();

        panelInferior.add(lblEstado);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public void actualizarBarra() {
        int docs = documentoServicio.getArbolDocumentos().tamanio();
        int cola = aprobacionServicio.tamaniosCola();
        lblEstado.setText("Documentos en sistema: " + docs + " | Cola de aprobaci\u00f3n: " + cola + " solicitudes");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            new JFramePrincipal().setVisible(true);
        });
    }
}
