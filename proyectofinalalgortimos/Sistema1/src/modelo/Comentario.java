package modelo;

public class Comentario {

    private String idComentario;
    private String texto;
    private String autor;
    private String fecha;
    private String tipo;

    public Comentario(String idComentario, String texto, String autor,
                      String fecha, String tipo) {
        this.idComentario = idComentario;
        this.texto = texto;
        this.autor = autor;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getIdComentario() { return idComentario; }
    public String getTexto() { return texto; }
    public String getAutor() { return autor; }
    public String getFecha() { return fecha; }
    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        return idComentario + " | " + tipo + " | " + autor + " | " + fecha;
    }
}
