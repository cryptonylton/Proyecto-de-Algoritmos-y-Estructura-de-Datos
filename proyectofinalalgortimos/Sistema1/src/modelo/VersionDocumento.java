package modelo;

public class VersionDocumento {

    private String version;
    private String descripcion;
    private String autor;
    private String fecha;
    private String motivoCambio;

    public VersionDocumento(String version, String descripcion, String autor,
                            String fecha, String motivoCambio) {
        this.version = version;
        this.descripcion = descripcion;
        this.autor = autor;
        this.fecha = fecha;
        this.motivoCambio = motivoCambio;
    }

    public String getVersion() { return version; }
    public String getDescripcion() { return descripcion; }
    public String getAutor() { return autor; }
    public String getFecha() { return fecha; }
    public String getMotivoCambio() { return motivoCambio; }

    @Override
    public String toString() {
        return version + " | " + autor + " | " + fecha + " | " + motivoCambio;
    }
}
