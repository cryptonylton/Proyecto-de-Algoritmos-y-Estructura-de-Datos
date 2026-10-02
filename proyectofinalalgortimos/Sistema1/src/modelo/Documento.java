package modelo;

import estructuras.PilaVersiones;
import estructuras.ListaComentarios;

public class Documento {

    private String codigo;
    private String titulo;
    private String area;
    private String tipo;
    private String responsable;
    private String version;
    private String estado;
    private String descripcion;
    private String fechaRegistro;
    private boolean eliminadoLogico;
    private PilaVersiones historialVersiones;
    private ListaComentarios comentarios;

    public Documento(String codigo, String titulo, String area, String tipo,
                     String responsable, String version, String estado,
                     String descripcion, String fechaRegistro) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.area = area;
        this.tipo = tipo;
        this.responsable = responsable;
        this.version = version;
        this.estado = "Borrador";
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
        this.eliminadoLogico = false;
        this.historialVersiones = new PilaVersiones();
        this.comentarios = new ListaComentarios();
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isEliminadoLogico() { return eliminadoLogico; }
    public void setEliminadoLogico(boolean eliminadoLogico) { this.eliminadoLogico = eliminadoLogico; }

    public PilaVersiones getHistorialVersiones() { return historialVersiones; }

    public ListaComentarios getComentarios() { return comentarios; }

    @Override
    public String toString() {
        return codigo + " | " + titulo + " | " + estado + " | " + version;
    }
}
