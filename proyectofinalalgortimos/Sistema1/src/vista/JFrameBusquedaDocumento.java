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

public class JFrameBusquedaDocumento extends JFrame {

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
    private AprobacionServicio aprobacionServicio;
    private ReporteServicio reporteServicio;
    private JTabbedPane tabbedPane;

    public JFrameBusquedaDocumento(JFramePrincipal principal) {
        this.principal = principal;
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.aprobacionServicio = AprobacionServicio.getInstancia();
        this.reporteServicio = new ReporteServicio();

        setTitle("B\u00fasqueda y Reportes");
        setSize(950, 700);
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
        crearTabbedPane();
        crearPanelInferior();
    }

    private void crearPanelSuperior() {
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTop.setBackground(AZUL_OSCURO);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitulo = new JLabel("\ud83d\udd0d B\u00daSQUEDA Y REPORTES DOCUMENTALES");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);
        panelTop.add(lblTitulo);
        add(panelTop, BorderLayout.NORTH);
    }

    private void crearTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FUENTE_SUBTITULO);
        tabbedPane.setBackground(GRIS_FONDO);

        tabbedPane.addTab("B\u00fasqueda R\u00e1pida", crearPanelBusquedaAVL());
        tabbedPane.addTab("Reportes Documentales", crearPanelReportes());
        tabbedPane.addTab("Cola de Aprobaci\u00f3n (Reporte)", crearPanelColaReporte());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelBusquedaAVL() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(GRIS_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(GRIS_FONDO);

        JLabel lblCodigo = new JLabel("C\u00f3digo:");
        lblCodigo.setFont(FUENTE_NORMAL);
        panelBusqueda.add(lblCodigo);

        JTextField txfCodigoBuscar = new JTextField(12);
        txfCodigoBuscar.setFont(FUENTE_NORMAL);
        panelBusqueda.add(txfCodigoBuscar);

        JButton btnBuscar = crearBotonPrimario("Buscar Documento");
        panelBusqueda.add(btnBuscar);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(220);
        splitPane.setBackground(GRIS_FONDO);

        JPanel panelDetalle = new JPanel(new BorderLayout(5, 5));
        panelDetalle.setBackground(Color.WHITE);
        panelDetalle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel lblDetalleTitulo = new JLabel("Detalle del Documento");
        lblDetalleTitulo.setFont(FUENTE_SUBTITULO);
        lblDetalleTitulo.setForeground(AZUL_OSCURO);
        panelDetalle.add(lblDetalleTitulo, BorderLayout.NORTH);

        JPanel panelInfoDoc = new JPanel(new GridLayout(5, 2, 5, 3));
        panelInfoDoc.setBackground(Color.WHITE);

        JLabel lblCod = new JLabel(" ");
        JLabel lblTit = new JLabel(" ");
        JLabel lblAreaTipo = new JLabel(" ");
        JLabel lblResponsableVersion = new JLabel(" ");
        JLabel lblEstadoFecha = new JLabel(" ");
        JLabel lblVersionesPila = new JLabel(" ");
        JLabel lblComentariosCount = new JLabel(" ");

        lblCod.setFont(FUENTE_NORMAL);
        lblTit.setFont(FUENTE_NORMAL);
        lblAreaTipo.setFont(FUENTE_NORMAL);
        lblResponsableVersion.setFont(FUENTE_NORMAL);
        lblEstadoFecha.setFont(FUENTE_NORMAL);
        lblVersionesPila.setFont(FUENTE_NORMAL);
        lblComentariosCount.setFont(FUENTE_NORMAL);

        panelInfoDoc.add(lblCod);
        panelInfoDoc.add(lblTit);
        panelInfoDoc.add(lblAreaTipo);
        panelInfoDoc.add(lblResponsableVersion);
        panelInfoDoc.add(lblEstadoFecha);
        panelInfoDoc.add(lblVersionesPila);
        panelInfoDoc.add(lblComentariosCount);

        JPanel panelBotonesDoc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonesDoc.setBackground(Color.WHITE);

        JButton btnVerVersiones = crearBotonSecundario("Ver Versiones");
        JButton btnVerComentarios = crearBotonSecundario("Ver Comentarios");
        panelBotonesDoc.add(btnVerVersiones);
        panelBotonesDoc.add(btnVerComentarios);
        panelInfoDoc.add(panelBotonesDoc);

        panelDetalle.add(panelInfoDoc, BorderLayout.CENTER);

        JPanel panelRecorrido = new JPanel(new BorderLayout(5, 5));
        panelRecorrido.setBackground(GRIS_FONDO);

        JPanel panelRecControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRecControles.setBackground(GRIS_FONDO);

        JLabel lblRecorrido = new JLabel("Orden de B\u00fasqueda:");
        lblRecorrido.setFont(FUENTE_SUBTITULO);
        lblRecorrido.setForeground(AZUL_OSCURO);
        panelRecControles.add(lblRecorrido);

        JComboBox<String> cmbRecorrido = new JComboBox<>(new String[]{"Inorden", "Preorden", "Postorden"});
        cmbRecorrido.setFont(FUENTE_NORMAL);
        panelRecControles.add(cmbRecorrido);

        JButton btnMostrarRecorrido = crearBotonPrimario("Mostrar Recorrido");
        panelRecControles.add(btnMostrarRecorrido);
        panelRecorrido.add(panelRecControles, BorderLayout.NORTH);

        String[] colsRec = {"C\u00f3digo", "T\u00edtulo", "Estado"};
        DefaultTableModel modeloRecorrido = new DefaultTableModel(colsRec, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaRecorrido = new JTable(modeloRecorrido);
        configurarTabla(tablaRecorrido);
        panelRecorrido.add(new JScrollPane(tablaRecorrido), BorderLayout.CENTER);

        splitPane.setTopComponent(panelDetalle);
        splitPane.setBottomComponent(panelRecorrido);
        panel.add(splitPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String codigo = txfCodigoBuscar.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un c\u00f3digo",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Documento doc = documentoServicio.buscarPorCodigo(codigo);
            if (doc == null) {
                JOptionPane.showMessageDialog(this, "Documento no encontrado",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            lblCod.setText("C\u00f3digo: " + doc.getCodigo());
            lblTit.setText("T\u00edtulo: " + doc.getTitulo());
            lblAreaTipo.setText("\u00c1rea: " + doc.getArea() + " | Tipo: " + doc.getTipo());
            lblResponsableVersion.setText("Responsable: " + doc.getResponsable() + " | Versi\u00f3n: " + doc.getVersion());
            lblEstadoFecha.setText("Estado: " + doc.getEstado() + " | Fecha: " + doc.getFechaRegistro());
            lblVersionesPila.setText("Versiones en pila: " + doc.getHistorialVersiones().tamanio());
            lblComentariosCount.setText("Comentarios: " + doc.getComentarios().tamanio());

            btnVerVersiones.addActionListener(ev -> {
                JFrameVersiones frame = new JFrameVersiones(principal);
                frame.setVisible(true);
            });
            btnVerComentarios.addActionListener(ev -> {
                JFrameComentarios frame = new JFrameComentarios(principal);
                frame.setVisible(true);
            });
        });

        btnMostrarRecorrido.addActionListener(e -> {
            String tipo = (String) cmbRecorrido.getSelectedItem();
            Documento[] docs;
            if (tipo.equals("Inorden")) {
                docs = documentoServicio.obtenerTodosInorden();
            } else if (tipo.equals("Preorden")) {
                docs = documentoServicio.obtenerTodosPreorden();
            } else {
                docs = documentoServicio.obtenerTodosPostorden();
            }
            modeloRecorrido.setRowCount(0);
            for (int i = 0; i < docs.length; i++) {
                modeloRecorrido.addRow(new Object[]{
                    docs[i].getCodigo(), docs[i].getTitulo(), docs[i].getEstado()
                });
            }
        });

        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(GRIS_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBackground(GRIS_FONDO);

        JLabel lblFiltroEstado = new JLabel("Estado:");
        lblFiltroEstado.setFont(FUENTE_NORMAL);
        panelFiltros.add(lblFiltroEstado);

        String[] estadosConTodos = new String[Catalogos.ESTADOS.length + 1];
        estadosConTodos[0] = "Todos";
        for (int i = 0; i < Catalogos.ESTADOS.length; i++) estadosConTodos[i + 1] = Catalogos.ESTADOS[i];
        JComboBox<String> cmbEstado = new JComboBox<>(estadosConTodos);
        cmbEstado.setFont(FUENTE_NORMAL);
        panelFiltros.add(cmbEstado);

        JLabel lblFiltroArea = new JLabel("\u00c1rea:");
        lblFiltroArea.setFont(FUENTE_NORMAL);
        panelFiltros.add(lblFiltroArea);

        String[] areasConTodos = new String[Catalogos.AREAS.length + 1];
        areasConTodos[0] = "Todos";
        for (int i = 0; i < Catalogos.AREAS.length; i++) areasConTodos[i + 1] = Catalogos.AREAS[i];
        JComboBox<String> cmbArea = new JComboBox<>(areasConTodos);
        cmbArea.setFont(FUENTE_NORMAL);
        panelFiltros.add(cmbArea);

        JLabel lblFiltroTipo = new JLabel("Tipo:");
        lblFiltroTipo.setFont(FUENTE_NORMAL);
        panelFiltros.add(lblFiltroTipo);

        String[] tiposConTodos = new String[Catalogos.TIPOS.length + 1];
        tiposConTodos[0] = "Todos";
        for (int i = 0; i < Catalogos.TIPOS.length; i++) tiposConTodos[i + 1] = Catalogos.TIPOS[i];
        JComboBox<String> cmbTipo = new JComboBox<>(tiposConTodos);
        cmbTipo.setFont(FUENTE_NORMAL);
        panelFiltros.add(cmbTipo);

        JButton btnAplicar = crearBotonPrimario("Aplicar Filtros");
        panelFiltros.add(btnAplicar);

        JButton btnRestablecer = crearBotonSecundario("Restablecer Filtros");
        panelFiltros.add(btnRestablecer);

        panel.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = reporteServicio.getColumnasDocumento();
        DefaultTableModel modeloReportes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaReportes = new JTable(modeloReportes);
        configurarTabla(tablaReportes);
        panel.add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        JPanel panelEstadisticas = new JPanel(new GridLayout(2, 1));
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JLabel lblTotal = new JLabel("Total documentos: 0");
        lblTotal.setFont(FUENTE_SUBTITULO);
        lblTotal.setForeground(AZUL_OSCURO);

        JLabel lblDetalle = new JLabel("Borradores: 0 | En revisi\u00f3n: 0 | Publicados: 0 | Observados: 0 | Obsoletos: 0");
        lblDetalle.setFont(FUENTE_NORMAL);

        panelEstadisticas.add(lblTotal);
        panelEstadisticas.add(lblDetalle);

        JPanel panelInfReporte = new JPanel(new BorderLayout());
        panelInfReporte.setBackground(GRIS_FONDO);
        panelInfReporte.add(panelEstadisticas, BorderLayout.CENTER);

        JButton btnActualizar = crearBotonPrimario("Actualizar Reporte");
        panelInfReporte.add(btnActualizar, BorderLayout.EAST);

        panel.add(panelInfReporte, BorderLayout.SOUTH);

        Runnable cargarReporte = () -> {
            String estado = (String) cmbEstado.getSelectedItem();
            String area = (String) cmbArea.getSelectedItem();
            String tipo = (String) cmbTipo.getSelectedItem();

            Documento[] docs = documentoServicio.obtenerTodosInorden();

            int count = 0;
            for (int i = 0; i < docs.length; i++) {
                boolean pasa = true;
                if (!estado.equals("Todos") && !docs[i].getEstado().equals(estado)) pasa = false;
                if (!area.equals("Todos") && !docs[i].getArea().equals(area)) pasa = false;
                if (!tipo.equals("Todos") && !docs[i].getTipo().equals(tipo)) pasa = false;
                if (pasa) count++;
            }

            Documento[] filtrados = new Documento[count];
            int idx = 0;
            for (int i = 0; i < docs.length; i++) {
                boolean pasa = true;
                if (!estado.equals("Todos") && !docs[i].getEstado().equals(estado)) pasa = false;
                if (!area.equals("Todos") && !docs[i].getArea().equals(area)) pasa = false;
                if (!tipo.equals("Todos") && !docs[i].getTipo().equals(tipo)) pasa = false;
                if (pasa) {
                    filtrados[idx] = docs[i];
                    idx++;
                }
            }

            Object[][] datos = reporteServicio.getDocumentosParaTabla(filtrados);
            modeloReportes.setRowCount(0);
            for (int i = 0; i < datos.length; i++) {
                modeloReportes.addRow(datos[i]);
            }

            int borradores = 0, enRevision = 0, publicados = 0, observados = 0, obsoletos = 0;
            for (int i = 0; i < docs.length; i++) {
                String est = docs[i].getEstado();
                if (est.equals("Borrador")) borradores++;
                else if (est.equals("En revision")) enRevision++;
                else if (est.equals("Publicado")) publicados++;
                else if (est.equals("Observado")) observados++;
                else if (est.equals("Obsoleto")) obsoletos++;
            }
            lblTotal.setText("Total documentos: " + docs.length);
            lblDetalle.setText("Borradores: " + borradores + " | En revisi\u00f3n: " + enRevision +
                " | Publicados: " + publicados + " | Observados: " + observados + " | Obsoletos: " + obsoletos);
        };

        btnAplicar.addActionListener(e -> cargarReporte.run());
        btnActualizar.addActionListener(e -> cargarReporte.run());
        btnRestablecer.addActionListener(e -> {
            cmbEstado.setSelectedIndex(0);
            cmbArea.setSelectedIndex(0);
            cmbTipo.setSelectedIndex(0);
            cargarReporte.run();
        });

        SwingUtilities.invokeLater(cargarReporte::run);

        return panel;
    }

    private JPanel crearPanelColaReporte() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(GRIS_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = reporteServicio.getColumnasSolicitud();
        DefaultTableModel modeloCola = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaCola = new JTable(modeloCola);
        configurarTabla(tablaCola);
        panel.add(new JScrollPane(tablaCola), BorderLayout.CENTER);

        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInf.setBackground(Color.WHITE);
        panelInf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JLabel lblInfoCola = new JLabel("Total en cola: 0 | Alta prioridad: 0 | Media: 0 | Baja: 0");
        lblInfoCola.setFont(FUENTE_SUBTITULO);
        lblInfoCola.setForeground(AZUL_OSCURO);
        panelInf.add(lblInfoCola);

        JButton btnActualizar = crearBotonPrimario("Actualizar");
        panelInf.add(btnActualizar);

        panel.add(panelInf, BorderLayout.SOUTH);

        Runnable cargarCola = () -> {
            SolicitudAprobacion[] solicitudes = aprobacionServicio.obtenerTodasEnCola();
            Object[][] datos = reporteServicio.getSolicitudesParaTabla(solicitudes);
            modeloCola.setRowCount(0);
            for (int i = 0; i < datos.length; i++) {
                modeloCola.addRow(datos[i]);
            }

            int alta = 0, media = 0, baja = 0;
            for (int i = 0; i < solicitudes.length; i++) {
                String p = solicitudes[i].getPrioridad();
                if (p.equals("Alta")) alta++;
                else if (p.equals("Media")) media++;
                else if (p.equals("Baja")) baja++;
            }
            lblInfoCola.setText("Total en cola: " + solicitudes.length +
                " | Alta prioridad: " + alta + " | Media: " + media + " | Baja: " + baja);
        };

        btnActualizar.addActionListener(e -> cargarCola.run());
        SwingUtilities.invokeLater(cargarCola::run);

        return panel;
    }

    private void crearPanelInferior() {
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInf.setBackground(AZUL_OSCURO);
        panelInf.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton btnVolver = crearBotonSecundario("Volver al Men\u00fa");
        btnVolver.addActionListener(e -> dispose());
        panelInf.add(btnVolver);

        add(panelInf, BorderLayout.SOUTH);
    }

    public void seleccionarPestania(int indice) {
        if (tabbedPane != null && indice >= 0 && indice < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(indice);
        }
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
