package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Validaciones {

    public static boolean validarCodigoDocumento(String codigo) {
        if (codigo == null) return false;
        return codigo.matches("DOC-\\d{4}");
    }

    public static boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) return false;
        return titulo.trim().length() >= Catalogos.MIN_TITULO && titulo.trim().length() <= Catalogos.MAX_TITULO;
    }

    public static boolean validarVersion(String version) {
        if (version == null) return false;
        return version.matches("v\\d+\\.\\d+");
    }

    public static boolean validarComentario(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        return texto.trim().length() >= Catalogos.MIN_COMENTARIO;
    }

    public static boolean validarDescripcion(String desc) {
        if (desc == null || desc.trim().isEmpty()) return false;
        return desc.trim().length() >= 10;
    }

    public static String obtenerFechaActual() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
