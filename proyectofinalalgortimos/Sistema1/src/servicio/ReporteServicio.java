package servicio;

import modelo.Comentario;
import modelo.Documento;
import modelo.SolicitudAprobacion;
import modelo.VersionDocumento;

public class ReporteServicio {

    private DocumentoServicio documentoServicio;
    private AprobacionServicio aprobacionServicio;

    public ReporteServicio() {
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.aprobacionServicio = AprobacionServicio.getInstancia();
    }

    public Object[][] getDocumentosParaTabla(Documento[] docs) {
        if (docs == null) return new Object[0][0];
        Object[][] datos = new Object[docs.length][8];
        for (int i = 0; i < docs.length; i++) {
            datos[i][0] = docs[i].getCodigo();
            datos[i][1] = docs[i].getTitulo();
            datos[i][2] = docs[i].getArea();
            datos[i][3] = docs[i].getTipo();
            datos[i][4] = docs[i].getResponsable();
            datos[i][5] = docs[i].getVersion();
            datos[i][6] = docs[i].getEstado();
            datos[i][7] = docs[i].getFechaRegistro();
        }
        return datos;
    }

    public Object[][] getSolicitudesParaTabla(SolicitudAprobacion[] solicitudes) {
        if (solicitudes == null) return new Object[0][0];
        Object[][] datos = new Object[solicitudes.length][8];
        for (int i = 0; i < solicitudes.length; i++) {
            datos[i][0] = solicitudes[i].getIdSolicitud();
            datos[i][1] = solicitudes[i].getCodigoDocumento();
            datos[i][2] = solicitudes[i].getTituloDocumento();
            datos[i][3] = solicitudes[i].getSolicitante();
            datos[i][4] = solicitudes[i].getRevisor();
            datos[i][5] = solicitudes[i].getPrioridad();
            datos[i][6] = solicitudes[i].getFechaSolicitud();
            datos[i][7] = solicitudes[i].getEstado();
        }
        return datos;
    }

    public Object[][] getVersionesParaTabla(VersionDocumento[] versiones) {
        if (versiones == null) return new Object[0][0];
        Object[][] datos = new Object[versiones.length][5];
        for (int i = 0; i < versiones.length; i++) {
            datos[i][0] = versiones[i].getVersion();
            datos[i][1] = versiones[i].getAutor();
            datos[i][2] = versiones[i].getFecha();
            datos[i][3] = versiones[i].getMotivoCambio();
            datos[i][4] = versiones[i].getDescripcion();
        }
        return datos;
    }

    public Object[][] getComentariosParaTabla(Comentario[] comentarios) {
        if (comentarios == null) return new Object[0][0];
        Object[][] datos = new Object[comentarios.length][5];
        for (int i = 0; i < comentarios.length; i++) {
            datos[i][0] = comentarios[i].getIdComentario();
            datos[i][1] = comentarios[i].getTexto();
            datos[i][2] = comentarios[i].getAutor();
            datos[i][3] = comentarios[i].getFecha();
            datos[i][4] = comentarios[i].getTipo();
        }
        return datos;
    }

    public String[] getColumnasDocumento() {
        return new String[]{"Codigo", "Titulo", "Area", "Tipo", "Responsable", "Version", "Estado", "Fecha"};
    }

    public String[] getColumnasSolicitud() {
        return new String[]{"ID", "Codigo Doc", "Titulo", "Solicitante", "Revisor", "Prioridad", "Fecha", "Estado"};
    }

    public String[] getColumnasVersion() {
        return new String[]{"Version", "Autor", "Fecha", "Motivo", "Descripcion"};
    }

    public String[] getColumnasComentario() {
        return new String[]{"ID", "Texto", "Autor", "Fecha", "Tipo"};
    }
}
