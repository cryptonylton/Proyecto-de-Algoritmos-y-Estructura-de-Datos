package dao;

import config.ConexionBD;
import modelo.Documento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {

    public boolean insertar(Documento doc) {
        String sql = "INSERT INTO Documento (codigo, titulo, area, tipo, responsable, version_actual, estado, descripcion, fecha_registro, eliminado_logico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, doc.getCodigo());
            ps.setString(2, doc.getTitulo());
            ps.setString(3, doc.getArea());
            ps.setString(4, doc.getTipo());
            ps.setString(5, doc.getResponsable());
            ps.setString(6, doc.getVersion());
            ps.setString(7, doc.getEstado());
            ps.setString(8, doc.getDescripcion());
            ps.setString(9, doc.getFechaRegistro());
            ps.setBoolean(10, doc.isEliminadoLogico());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando documento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Documento doc) {
        String sql = "UPDATE Documento SET titulo=?, area=?, tipo=?, responsable=?, version_actual=?, estado=?, descripcion=?, eliminado_logico=? WHERE codigo=?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, doc.getTitulo());
            ps.setString(2, doc.getArea());
            ps.setString(3, doc.getTipo());
            ps.setString(4, doc.getResponsable());
            ps.setString(5, doc.getVersion());
            ps.setString(6, doc.getEstado());
            ps.setString(7, doc.getDescripcion());
            ps.setBoolean(8, doc.isEliminadoLogico());
            ps.setString(9, doc.getCodigo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando documento: " + e.getMessage());
            return false;
        }
    }

    public List<Documento> obtenerTodos() {
        List<Documento> documentos = new ArrayList<>();
        String sql = "SELECT * FROM Documento";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Documento doc = new Documento(
                    rs.getString("codigo"),
                    rs.getString("titulo"),
                    rs.getString("area"),
                    rs.getString("tipo"),
                    rs.getString("responsable"),
                    rs.getString("version_actual"),
                    rs.getString("estado"),
                    rs.getString("descripcion"),
                    rs.getString("fecha_registro")
                );
                doc.setEliminadoLogico(rs.getBoolean("eliminado_logico"));
                documentos.add(doc);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo documentos: " + e.getMessage());
        }
        return documentos;
    }
}
