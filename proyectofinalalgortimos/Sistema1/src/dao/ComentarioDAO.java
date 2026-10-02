package dao;

import config.ConexionBD;
import modelo.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    public boolean insertar(String codigoDocumento, Comentario comentario) {
        String sql = "INSERT INTO Comentario (id_comentario, codigo_documento, texto, autor, fecha, tipo_comentario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, comentario.getIdComentario());
            ps.setString(2, codigoDocumento);
            ps.setString(3, comentario.getTexto());
            ps.setString(4, comentario.getAutor());
            ps.setString(5, comentario.getFecha());
            ps.setString(6, comentario.getTipo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando comentario: " + e.getMessage());
            return false;
        }
    }

    public List<Comentario> obtenerPorDocumento(String codigoDocumento) {
        List<Comentario> comentarios = new ArrayList<>();
        String sql = "SELECT * FROM Comentario WHERE codigo_documento = ? ORDER BY id_comentario ASC";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, codigoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comentario c = new Comentario(
                        rs.getString("id_comentario"),
                        rs.getString("texto"),
                        rs.getString("autor"),
                        rs.getString("fecha"),
                        rs.getString("tipo_comentario")
                    );
                    comentarios.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo comentarios: " + e.getMessage());
        }
        return comentarios;
    }
}
