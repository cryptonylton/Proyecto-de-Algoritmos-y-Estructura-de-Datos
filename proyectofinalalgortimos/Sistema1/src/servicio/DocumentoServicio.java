package servicio;

import dao.ComentarioDAO;
import dao.DocumentoDAO;
import dao.VersionDAO;
import estructuras.ArbolAVL;
import modelo.Comentario;
import modelo.Documento;
import modelo.VersionDocumento;
import util.Validaciones;

import java.util.List;

public class DocumentoServicio {

    private static DocumentoServicio instancia;
    private ArbolAVL arbolDocumentos;
    private int contadorDocumentos;
    private int contadorComentarios;
    
    private DocumentoDAO documentoDAO;
    private ComentarioDAO comentarioDAO;
    private VersionDAO versionDAO;

    private DocumentoServicio() {
        this.arbolDocumentos = new ArbolAVL();
        this.contadorDocumentos = 0;
        this.contadorComentarios = 0;
        
        this.documentoDAO = new DocumentoDAO();
        this.comentarioDAO = new ComentarioDAO();
        this.versionDAO = new VersionDAO();
        
        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        List<Documento> docs = documentoDAO.obtenerTodos();
        for (Documento doc : docs) {
            // Cargar versiones
            List<VersionDocumento> versiones = versionDAO.obtenerPorDocumento(doc.getCodigo());
            for (VersionDocumento v : versiones) {
                doc.getHistorialVersiones().apilar(v);
            }
            // Cargar comentarios
            List<Comentario> comentarios = comentarioDAO.obtenerPorDocumento(doc.getCodigo());
            for (Comentario c : comentarios) {
                doc.getComentarios().agregar(c);
                // Actualizar contador de comentarios
                try {
                    int numCom = Integer.parseInt(c.getIdComentario().split("-")[1]);
                    if (numCom > contadorComentarios) contadorComentarios = numCom;
                } catch (Exception e) {}
            }
            
            arbolDocumentos.insertar(doc);
            
            // Actualizar contador de documentos
            try {
                int numDoc = Integer.parseInt(doc.getCodigo().split("-")[1]);
                if (numDoc > contadorDocumentos) contadorDocumentos = numDoc;
            } catch (Exception e) {}
        }
    }

    public static DocumentoServicio getInstancia() {
        if (instancia == null) {
            instancia = new DocumentoServicio();
        }
        return instancia;
    }

    public String generarCodigo() {
        contadorDocumentos++;
        return String.format("DOC-%04d", contadorDocumentos);
    }

    public String generarCodigoComentario() {
        contadorComentarios++;
        return String.format("COM-%04d", contadorComentarios);
    }

    public String generarCodigoSolicitud() {
        return AprobacionServicio.getInstancia().generarCodigoSolicitudInterno();
    }

    public boolean registrarDocumento(Documento doc) {
        if (documentoDAO.insertar(doc)) {
            return arbolDocumentos.insertar(doc);
        }
        return false;
    }

    public Documento buscarPorCodigo(String codigo) {
        return arbolDocumentos.buscar(codigo);
    }

    public Documento[] obtenerTodosInorden() {
        return arbolDocumentos.recorridoInorden();
    }

    public Documento[] obtenerTodosPreorden() {
        return arbolDocumentos.recorridoPreorden();
    }

    public Documento[] obtenerTodosPostorden() {
        return arbolDocumentos.recorridoPostorden();
    }

    public boolean crearNuevaVersion(String codigoDoc, String nuevaVersion,
                                     String descripcion, String autor, String motivo) {
        Documento doc = buscarPorCodigo(codigoDoc);
        if (doc == null || doc.isEliminadoLogico()) return false;
        if (!doc.getEstado().equals("Borrador") && !doc.getEstado().equals("Observado")) return false;
        if (!Validaciones.validarVersion(nuevaVersion)) return false;

        VersionDocumento versionAnterior = new VersionDocumento(
            doc.getVersion(), doc.getDescripcion(), autor,
            Validaciones.obtenerFechaActual(), motivo
        );
        
        String versionOriginal = doc.getVersion();
        String descOriginal = doc.getDescripcion();
        
        doc.setVersion(nuevaVersion);
        doc.setDescripcion(descripcion);

        if (versionDAO.insertar(codigoDoc, versionAnterior) && documentoDAO.actualizar(doc)) {
            doc.getHistorialVersiones().apilar(versionAnterior);
            return true;
        } else {
            // Rollback en memoria si falla BD
            doc.setVersion(versionOriginal);
            doc.setDescripcion(descOriginal);
            return false;
        }
    }

    public boolean restaurarVersionAnterior(String codigoDoc) {
        Documento doc = buscarPorCodigo(codigoDoc);
        if (doc == null || doc.isEliminadoLogico()) return false;
        if (doc.getEstado().equals("Publicado")) return false;
        if (doc.getHistorialVersiones().estaVacia()) return false;

        VersionDocumento versionAnterior = doc.getHistorialVersiones().verCima();
        String versionActual = doc.getVersion();
        String descActual = doc.getDescripcion();
        String versionRestaurada = versionAnterior.getVersion();

        doc.setVersion(versionAnterior.getVersion());
        doc.setDescripcion(versionAnterior.getDescripcion());

        Comentario comentario = new Comentario(
            generarCodigoComentario(),
            "Version restaurada desde " + versionRestaurada + " por [sistema]",
            "[sistema]",
            Validaciones.obtenerFechaActual(),
            "Restauracion"
        );

        if (versionDAO.eliminarUltima(codigoDoc, versionRestaurada) && 
            documentoDAO.actualizar(doc) && 
            comentarioDAO.insertar(codigoDoc, comentario)) {
            
            doc.getHistorialVersiones().desapilar();
            doc.getComentarios().agregar(comentario);
            return true;
        } else {
            // Rollback en memoria
            doc.setVersion(versionActual);
            doc.setDescripcion(descActual);
            return false;
        }
    }

    public boolean eliminarLogico(String codigoDoc) {
        Documento doc = buscarPorCodigo(codigoDoc);
        if (doc == null) return false;
        if (!doc.getEstado().equals("Obsoleto")) return false;
        if (doc.isEliminadoLogico()) return false;
        
        doc.setEliminadoLogico(true);
        if (documentoDAO.actualizar(doc)) {
            return true;
        } else {
            doc.setEliminadoLogico(false);
            return false;
        }
    }

    public boolean actualizarDocumento(String codigo, String nuevoTitulo,
                                       String nuevaArea, String nuevoTipo,
                                       String nuevoResponsable, String nuevaDescripcion) {
        Documento doc = buscarPorCodigo(codigo);
        if (doc == null || doc.isEliminadoLogico()) return false;
        if (doc.getEstado().equals("Publicado")) return false;

        String oldTitulo = doc.getTitulo();
        String oldArea = doc.getArea();
        String oldTipo = doc.getTipo();
        String oldResp = doc.getResponsable();
        String oldDesc = doc.getDescripcion();

        doc.setTitulo(nuevoTitulo);
        doc.setArea(nuevaArea);
        doc.setTipo(nuevoTipo);
        doc.setResponsable(nuevoResponsable);
        doc.setDescripcion(nuevaDescripcion);
        
        if (documentoDAO.actualizar(doc)) {
            return true;
        } else {
            doc.setTitulo(oldTitulo);
            doc.setArea(oldArea);
            doc.setTipo(oldTipo);
            doc.setResponsable(oldResp);
            doc.setDescripcion(oldDesc);
            return false;
        }
    }

    public boolean agregarComentario(String codigoDoc, Comentario comentario) {
        Documento doc = buscarPorCodigo(codigoDoc);
        if (doc == null || doc.isEliminadoLogico()) return false;
        
        if (comentarioDAO.insertar(codigoDoc, comentario)) {
            doc.getComentarios().agregar(comentario);
            return true;
        }
        return false;
    }

    public Documento[] filtrarPorEstado(String estado) {
        Documento[] todos = obtenerTodosInorden();
        int count = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getEstado().equals(estado)) count++;
        }
        Documento[] resultado = new Documento[count];
        int idx = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getEstado().equals(estado)) {
                resultado[idx] = todos[i];
                idx++;
            }
        }
        return resultado;
    }

    public Documento[] filtrarPorArea(String area) {
        Documento[] todos = obtenerTodosInorden();
        int count = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getArea().equals(area)) count++;
        }
        Documento[] resultado = new Documento[count];
        int idx = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getArea().equals(area)) {
                resultado[idx] = todos[i];
                idx++;
            }
        }
        return resultado;
    }

    public Documento[] filtrarPorTipo(String tipo) {
        Documento[] todos = obtenerTodosInorden();
        int count = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getTipo().equals(tipo)) count++;
        }
        Documento[] resultado = new Documento[count];
        int idx = 0;
        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getTipo().equals(tipo)) {
                resultado[idx] = todos[i];
                idx++;
            }
        }
        return resultado;
    }

    public boolean cambiarEstado(String codigoDoc, String nuevoEstado) {
        Documento doc = buscarPorCodigo(codigoDoc);
        if (doc == null || doc.isEliminadoLogico()) return false;
        
        String oldEstado = doc.getEstado();
        doc.setEstado(nuevoEstado);
        
        if (documentoDAO.actualizar(doc)) {
            return true;
        } else {
            doc.setEstado(oldEstado);
            return false;
        }
    }

    public int getContadorDocumentos() { return contadorDocumentos; }
    public void setContadorDocumentos(int contadorDocumentos) { this.contadorDocumentos = contadorDocumentos; }

    public int getContadorComentarios() { return contadorComentarios; }
    public void setContadorComentarios(int contadorComentarios) { this.contadorComentarios = contadorComentarios; }

    public ArbolAVL getArbolDocumentos() { return arbolDocumentos; }
}
