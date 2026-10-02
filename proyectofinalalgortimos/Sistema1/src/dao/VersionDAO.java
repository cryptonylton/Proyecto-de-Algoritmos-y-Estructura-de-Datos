package dao;

import config.ConexionBD;
import modelo.VersionDocumento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionDAO {

    public boolean insertar(String codigoDocumento, VersionDocumento version) {
        String sql = "INSERT INTO VersionDocumento (codigo_documento, version, descripcion, autor, fecha, motivo_cambio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, codigoDocumento);
            ps.setString(2, version.getVersion());
            ps.setString(3, version.getDescripcion());
            ps.setString(4, version.getAutor());
            ps.setString(5, version.getFecha());
            ps.setString(6, version.getMotivoCambio());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando version: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarUltima(String codigoDocumento, String version) {
        String sql = "DELETE FROM VersionDocumento WHERE codigo_documento = ? AND version = ?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, codigoDocumento);
            ps.setString(2, version);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando version: " + e.getMessage());
            return false;
        }
    }

    public List<VersionDocumento> obtenerPorDocumento(String codigoDocumento) {
        List<VersionDocumento> versiones = new ArrayList<>();
        // Ordenamos por id_version ASC para que al insertarlas en la pila (push), 
        // la más reciente quede en la cima.
        String sql = "SELECT * FROM VersionDocumento WHERE codigo_documento = ? ORDER BY id_version ASC";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, codigoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VersionDocumento v = new VersionDocumento(
                        rs.getString("version"),
                        rs.getString("descripcion"),
                        rs.getString("autor"),
                        rs.getString("fecha"),
                        rs.getString("motivo_cambio")
                    );
                    versiones.add(v);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo versiones: " + e.getMessage());
        }
        return versiones;
    }
}
