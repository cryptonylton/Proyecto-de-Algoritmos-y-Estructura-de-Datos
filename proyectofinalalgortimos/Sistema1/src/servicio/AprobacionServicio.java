package servicio;

import dao.SolicitudDAO;
import estructuras.ColaAprobacion;
import modelo.Comentario;
import modelo.Documento;
import modelo.SolicitudAprobacion;
import util.Validaciones;

import java.util.List;

public class AprobacionServicio {

    private static AprobacionServicio instancia;
    private ColaAprobacion colaAprobacion;
    private DocumentoServicio documentoServicio;
    private int contadorSolicitudes;
    
    private SolicitudDAO solicitudDAO;

    private AprobacionServicio() {
        this.colaAprobacion = new ColaAprobacion();
        this.documentoServicio = DocumentoServicio.getInstancia();
        this.contadorSolicitudes = 0;
        
        this.solicitudDAO = new SolicitudDAO();
        
        cargarDatosDesdeBD();
    }
    
    private void cargarDatosDesdeBD() {
        List<SolicitudAprobacion> pendientes = solicitudDAO.obtenerPendientes();
        for (SolicitudAprobacion sol : pendientes) {
            colaAprobacion.encolar(sol);
            try {
                int numSol = Integer.parseInt(sol.getIdSolicitud().split("-")[1]);
                if (numSol > contadorSolicitudes) contadorSolicitudes = numSol;
            } catch (Exception e) {}
        }
    }

    public static AprobacionServicio getInstancia() {
        if (instancia == null) {
            instancia = new AprobacionServicio();
        }
        return instancia;
    }

    String generarCodigoSolicitudInterno() {
        contadorSolicitudes++;
        return String.format("SOL-%04d", contadorSolicitudes);
    }

    public String generarCodigoSolicitud() {
        return generarCodigoSolicitudInterno();
    }

    public boolean enviarAAprobacion(String codigoDoc, String solicitante,
                                     String revisor, String prioridad) {
        Documento doc = documentoServicio.buscarPorCodigo(codigoDoc);
        if (doc == null || doc.isEliminadoLogico()) return false;
        if (colaAprobacion.existeSolicitudPendiente(codigoDoc)) return false;

        String idSolicitud = generarCodigoSolicitud();
        SolicitudAprobacion solicitud = new SolicitudAprobacion(
            idSolicitud, codigoDoc, doc.getTitulo(), solicitante,
            revisor, prioridad, Validaciones.obtenerFechaActual(), doc.getVersion()
        );
        
        if (solicitudDAO.insertar(solicitud)) {
            colaAprobacion.encolar(solicitud);
            documentoServicio.cambiarEstado(codigoDoc, "En revision");
            return true;
        }
        return false;
    }

    public SolicitudAprobacion tomarSiguienteSolicitud() {
        return colaAprobacion.desencolar();
    }

    public SolicitudAprobacion verSiguiente() {
        return colaAprobacion.verFrente();
    }

    public boolean aprobarSolicitud(SolicitudAprobacion solicitud, String comentario) {
        if (solicitud == null) return false;
        if (comentario == null || comentario.trim().isEmpty()) return false;

        solicitud.setEstado("Aprobada");
        solicitud.setComentarioRevisor(comentario);
        
        if (solicitudDAO.actualizar(solicitud)) {
            documentoServicio.cambiarEstado(solicitud.getCodigoDocumento(), "Publicado");

            Comentario com = new Comentario(
                documentoServicio.generarCodigoComentario(),
                comentario,
                solicitud.getRevisor(),
                Validaciones.obtenerFechaActual(),
                "Aprobacion"
            );
            documentoServicio.agregarComentario(solicitud.getCodigoDocumento(), com);
            return true;
        } else {
            solicitud.setEstado("Pendiente");
            solicitud.setComentarioRevisor("");
            return false;
        }
    }

    public boolean observarSolicitud(SolicitudAprobacion solicitud, String comentario) {
        if (solicitud == null) return false;
        if (comentario == null || comentario.trim().length() < 15) return false;

        solicitud.setEstado("Observada");
        solicitud.setComentarioRevisor(comentario);
        
        if (solicitudDAO.actualizar(solicitud)) {
            documentoServicio.cambiarEstado(solicitud.getCodigoDocumento(), "Observado");

            Comentario com = new Comentario(
                documentoServicio.generarCodigoComentario(),
                comentario,
                solicitud.getRevisor(),
                Validaciones.obtenerFechaActual(),
                "Observacion"
            );
            documentoServicio.agregarComentario(solicitud.getCodigoDocumento(), com);
            return true;
        } else {
            solicitud.setEstado("Pendiente");
            solicitud.setComentarioRevisor("");
            return false;
        }
    }

    public SolicitudAprobacion[] obtenerTodasEnCola() {
        return colaAprobacion.obtenerTodos();
    }

    public boolean colaVacia() {
        return colaAprobacion.estaVacia();
    }

    public int tamaniosCola() {
        return colaAprobacion.tamanio();
    }

    public int getContadorSolicitudes() { return contadorSolicitudes; }
    public void setContadorSolicitudes(int contadorSolicitudes) { this.contadorSolicitudes = contadorSolicitudes; }
}
