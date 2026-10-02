package modelo;

public class SolicitudAprobacion {

    private String idSolicitud;
    private String codigoDocumento;
    private String tituloDocumento;
    private String solicitante;
    private String revisor;
    private String prioridad;
    private String fechaSolicitud;
    private String estado;
    private String comentarioRevisor;
    private String version;

    public SolicitudAprobacion(String idSolicitud, String codigoDocumento,
                               String tituloDocumento, String solicitante,
                               String revisor, String prioridad,
                               String fechaSolicitud, String version) {
        this.idSolicitud = idSolicitud;
        this.codigoDocumento = codigoDocumento;
        this.tituloDocumento = tituloDocumento;
        this.solicitante = solicitante;
        this.revisor = revisor;
        this.prioridad = prioridad;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = "Pendiente";
        this.comentarioRevisor = "";
        this.version = version;
    }

    public String getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(String idSolicitud) { this.idSolicitud = idSolicitud; }

    public String getCodigoDocumento() { return codigoDocumento; }
    public void setCodigoDocumento(String codigoDocumento) { this.codigoDocumento = codigoDocumento; }

    public String getTituloDocumento() { return tituloDocumento; }
    public void setTituloDocumento(String tituloDocumento) { this.tituloDocumento = tituloDocumento; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public String getRevisor() { return revisor; }
    public void setRevisor(String revisor) { this.revisor = revisor; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(String fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getComentarioRevisor() { return comentarioRevisor; }
    public void setComentarioRevisor(String comentarioRevisor) { this.comentarioRevisor = comentarioRevisor; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    @Override
    public String toString() {
        return idSolicitud + " | " + codigoDocumento + " | " + prioridad + " | " + estado;
    }
}
