package vista;

import modelo.Documento;
import servicio.DocumentoServicio;
import servicio.ReporteServicio;
import util.Catalogos;
import util.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameDocumento extends JFrame {

    private static final Color AZUL_OSCURO = new Color(26, 39, 68);
    private static final Color AZUL_MEDIO = new Color(41, 82, 163);
    private static final Color GRIS_FONDO = new Color(245, 246, 250);
    private static final Color TEXTO_OSCURO = new Color(30, 30, 30);
    private static final Color VERDE_EXITO = new Color(39, 174, 96);
    private static final Color ROJO_ERROR = new Color(192, 57, 43);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    private JFramePrincipal principal;
    private DocumentoServicio documentoServicio;
    private ReporteServicio reporteServicio;

    private JTextField txfCodigo;
    private JTextField txfTitulo;
    private JComboBox<String> cmbArea;
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbResponsable;
    private JTextField txfVersion;
    private JTextArea txaDescripcion;
    private JLabel lblEstado;
    private JLabel lblFecha;
    private JTable tablaDocumentos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbFiltroEstado;

    public JFrameDocumento(JFramePrincipal principal) {
        this.principal = principal;
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.reporteServicio = new ReporteServicio();

        setTitle("Gesti\u00f3n de Documentos");
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
        crearPanelIzquierdo();
        crearPanelDerecho();

        cargarTabla(documentoServicio.obtenerTodosInorden());
    }

    private void crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(AZUL_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("\ud83d\udcc4 GESTI\u00d3N DE DOCUMENTOS");
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(Color.WHITE);
        panel.add(lbl);
        add(panel, BorderLayout.NORTH);
    }

    private void crearPanelIzquierdo() {
        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setBackground(GRIS_FONDO);
        panelIzq.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        panelIzq.setPreferredSize(new Dimension(340, 0));

        JLabel lblFormTitulo = new JLabel("Formulario de Documento");
        lblFormTitulo.setFont(FUENTE_SUBTITULO);
        lblFormTitulo.setForeground(AZUL_OSCURO);
        lblFormTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblFormTitulo);
        panelIzq.add(Box.createVerticalStrut(10));

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
        formPanel.add(crearLabel("C\u00f3digo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfCodigo = new JTextField(12);
        txfCodigo.setFont(FUENTE_NORMAL);
        formPanel.add(txfCodigo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("T\u00edtulo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfTitulo = new JTextField(12);
        txfTitulo.setFont(FUENTE_NORMAL);
        formPanel.add(txfTitulo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("\u00c1rea:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbArea = new JComboBox<>(Catalogos.AREAS);
        cmbArea.setFont(FUENTE_NORMAL);
        formPanel.add(cmbArea, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbTipo = new JComboBox<>(Catalogos.TIPOS);
        cmbTipo.setFont(FUENTE_NORMAL);
        formPanel.add(cmbTipo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Responsable:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbResponsable = new JComboBox<>(Catalogos.RESPONSABLES);
        cmbResponsable.setFont(FUENTE_NORMAL);
        formPanel.add(cmbResponsable, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Versi\u00f3n:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txfVersion = new JTextField("v1.0", 12);
        txfVersion.setFont(FUENTE_NORMAL);
        formPanel.add(txfVersion, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Descripci\u00f3n:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txaDescripcion = new JTextArea(4, 12);
        txaDescripcion.setFont(FUENTE_NORMAL);
        txaDescripcion.setLineWrap(true);
        txaDescripcion.setWrapStyleWord(true);
        JScrollPane spDesc = new JScrollPane(txaDescripcion);
        formPanel.add(spDesc, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        lblEstado = new JLabel("Borrador");
        lblEstado.setFont(FUENTE_SUBTITULO);
        lblEstado.setForeground(AZUL_MEDIO);
        formPanel.add(lblEstado, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(crearLabel("Fecha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        lblFecha = new JLabel(Validaciones.obtenerFechaActual());
        lblFecha.setFont(FUENTE_NORMAL);
        formPanel.add(lblFecha, gbc);

        panelIzq.add(formPanel);
        panelIzq.add(Box.createVerticalStrut(10));

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 5, 5));
        panelBotones.setBackground(GRIS_FONDO);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnGenerar = crearBotonSecundario("Generar C\u00f3digo");
        btnGenerar.addActionListener(e -> txfCodigo.setText(documentoServicio.generarCodigo()));

        JButton btnGuardar = crearBotonPrimario("Guardar");
        btnGuardar.addActionListener(e -> guardarDocumento());

        JButton btnActualizar = crearBotonPrimario("Actualizar");
        btnActualizar.addActionListener(e -> actualizarDocumento());

        JButton btnEliminar = crearBotonPeligro("Eliminar (L\u00f3gico)");
        btnEliminar.addActionListener(e -> eliminarDocumento());

        JButton btnLimpiar = crearBotonSecundario("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JButton btnVolver = crearBotonSecundario("Volver al Men\u00fa");
        btnVolver.addActionListener(e -> dispose());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        panelIzq.add(panelBotones);

        add(panelIzq, BorderLayout.WEST);
    }

    private void crearPanelDerecho() {
        JPanel panelDer = new JPanel(new BorderLayout(5, 5));
        panelDer.setBackground(GRIS_FONDO);
        panelDer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(GRIS_FONDO);

        JLabel lblFiltro = new JLabel("Filtrar por estado:");
        lblFiltro.setFont(FUENTE_NORMAL);
        panelFiltro.add(lblFiltro);

        String[] opcionesFiltro = new String[Catalogos.ESTADOS.length + 1];
        opcionesFiltro[0] = "Todos";
        for (int i = 0; i < Catalogos.ESTADOS.length; i++) {
            opcionesFiltro[i + 1] = Catalogos.ESTADOS[i];
        }
        cmbFiltroEstado = new JComboBox<>(opcionesFiltro);
        cmbFiltroEstado.setFont(FUENTE_NORMAL);
        panelFiltro.add(cmbFiltroEstado);

        JButton btnFiltrar = crearBotonPrimario("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarTabla());
        panelFiltro.add(btnFiltrar);

        JButton btnActTabla = crearBotonSecundario("Actualizar Tabla");
        btnActTabla.addActionListener(e -> cargarTabla(documentoServicio.obtenerTodosInorden()));
        panelFiltro.add(btnActTabla);

        panelDer.add(panelFiltro, BorderLayout.NORTH);

        String[] columnas = {"C\u00f3digo", "T\u00edtulo", "\u00c1rea", "Tipo", "Versi\u00f3n", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaDocumentos = new JTable(modeloTabla);
        configurarTabla(tablaDocumentos);
        tablaDocumentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDocumentoSeleccionado();
        });

        JScrollPane sp = new JScrollPane(tablaDocumentos);
        panelDer.add(sp, BorderLayout.CENTER);

        add(panelDer, BorderLayout.CENTER);
    }

    private void cargarTabla(Documento[] documentos) {
        modeloTabla.setRowCount(0);
        if (documentos == null) return;
        for (int i = 0; i < documentos.length; i++) {
            Documento d = documentos[i];
            modeloTabla.addRow(new Object[]{
                d.getCodigo(), d.getTitulo(), d.getArea(),
                d.getTipo(), d.getVersion(), d.getEstado()
            });
        }
    }

    private void filtrarTabla() {
        String filtro = (String) cmbFiltroEstado.getSelectedItem();
        if (filtro.equals("Todos")) {
            cargarTabla(documentoServicio.obtenerTodosInorden());
        } else {
            cargarTabla(documentoServicio.filtrarPorEstado(filtro));
        }
    }

    private void cargarDocumentoSeleccionado() {
        int fila = tablaDocumentos.getSelectedRow();
        if (fila < 0) return;
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Documento doc = documentoServicio.buscarPorCodigo(codigo);
        if (doc == null) return;

        txfCodigo.setText(doc.getCodigo());
        txfTitulo.setText(doc.getTitulo());
        cmbArea.setSelectedItem(doc.getArea());
        cmbTipo.setSelectedItem(doc.getTipo());
        cmbResponsable.setSelectedItem(doc.getResponsable());
        txfVersion.setText(doc.getVersion());
        txaDescripcion.setText(doc.getDescripcion());
        lblEstado.setText(doc.getEstado());
        lblFecha.setText(doc.getFechaRegistro());
    }

    private void guardarDocumento() {
        String codigo = txfCodigo.getText().trim();
        String titulo = txfTitulo.getText().trim();
        String area = (String) cmbArea.getSelectedItem();
        String tipo = (String) cmbTipo.getSelectedItem();
        String responsable = (String) cmbResponsable.getSelectedItem();
        String version = txfVersion.getText().trim();
        String descripcion = txaDescripcion.getText().trim();

        if (!Validaciones.validarCodigoDocumento(codigo)) {
            JOptionPane.showMessageDialog(this, "El c\u00f3digo debe tener el formato DOC-0000",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Validaciones.validarTitulo(titulo)) {
            JOptionPane.showMessageDialog(this, "El t\u00edtulo debe tener entre 5 y 120 caracteres",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Validaciones.validarVersion(version)) {
            JOptionPane.showMessageDialog(this, "La versi\u00f3n debe tener el formato vX.Y (ej: v1.0)",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Validaciones.validarDescripcion(descripcion)) {
            JOptionPane.showMessageDialog(this, "La descripci\u00f3n debe tener al menos 10 caracteres",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cmbArea.getSelectedIndex() < 0 || cmbTipo.getSelectedIndex() < 0 || cmbResponsable.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar \u00e1rea, tipo y responsable",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Documento doc = new Documento(codigo, titulo, area, tipo, responsable,
            version, "Borrador", descripcion, Validaciones.obtenerFechaActual());

        if (documentoServicio.registrarDocumento(doc)) {
            JOptionPane.showMessageDialog(this, "Documento registrado exitosamente",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla(documentoServicio.obtenerTodosInorden());
            limpiarFormulario();
            principal.actualizarBarra();
        } else {
            JOptionPane.showMessageDialog(this, "El c\u00f3digo ya existe en el sistema",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDocumento() {
        int fila = tablaDocumentos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un documento de la tabla",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String codigo = txfCodigo.getText().trim();
        Documento doc = documentoServicio.buscarPorCodigo(codigo);
        if (doc == null) {
            JOptionPane.showMessageDialog(this, "Documento no encontrado",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (doc.getEstado().equals("Publicado")) {
            JOptionPane.showMessageDialog(this,
                "El documento est\u00e1 publicado. Para modificarlo, deber\u00e1 crear una nueva versi\u00f3n de trabajo.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (doc.isEliminadoLogico()) {
            JOptionPane.showMessageDialog(this, "El documento est\u00e1 eliminado",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = txfTitulo.getText().trim();
        String descripcion = txaDescripcion.getText().trim();

        if (!Validaciones.validarTitulo(titulo)) {
            JOptionPane.showMessageDialog(this, "El t\u00edtulo debe tener entre 5 y 120 caracteres",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Validaciones.validarDescripcion(descripcion)) {
            JOptionPane.showMessageDialog(this, "La descripci\u00f3n debe tener al menos 10 caracteres",
                "Error de Validaci\u00f3n", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String area = (String) cmbArea.getSelectedItem();
        String tipo = (String) cmbTipo.getSelectedItem();
        String responsable = (String) cmbResponsable.getSelectedItem();

        if (documentoServicio.actualizarDocumento(codigo, titulo, area, tipo, responsable, descripcion)) {
            JOptionPane.showMessageDialog(this, "Documento actualizado exitosamente",
                "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla(documentoServicio.obtenerTodosInorden());
            principal.actualizarBarra();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el documento",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDocumento() {
        int fila = tablaDocumentos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un documento de la tabla",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Documento doc = documentoServicio.buscarPorCodigo(codigo);
        if (doc == null) return;

        if (!doc.getEstado().equals("Obsoleto")) {
            JOptionPane.showMessageDialog(this,
                "Solo se pueden eliminar documentos en estado 'Obsoleto'",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "\u00bfEst\u00e1 seguro de eliminar l\u00f3gicamente el documento " + codigo + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (documentoServicio.eliminarLogico(codigo)) {
                JOptionPane.showMessageDialog(this, "Documento eliminado l\u00f3gicamente",
                    "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla(documentoServicio.obtenerTodosInorden());
                limpiarFormulario();
                principal.actualizarBarra();
            }
        }
    }

    private void limpiarFormulario() {
        txfCodigo.setText("");
        txfTitulo.setText("");
        cmbArea.setSelectedIndex(0);
        cmbTipo.setSelectedIndex(0);
        cmbResponsable.setSelectedIndex(0);
        txfVersion.setText("v1.0");
        txaDescripcion.setText("");
        lblEstado.setText("Borrador");
        lblFecha.setText(Validaciones.obtenerFechaActual());
        tablaDocumentos.clearSelection();
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

    private JButton crearBotonPeligro(String texto) {
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
