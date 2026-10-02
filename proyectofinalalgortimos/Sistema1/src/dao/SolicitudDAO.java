package dao;

import config.ConexionBD;
import modelo.SolicitudAprobacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDAO {

    public boolean insertar(SolicitudAprobacion sol) {
        String sql = "INSERT INTO SolicitudAprobacion (id_solicitud, codigo_documento, titulo_documento, solicitante, revisor, prioridad, fecha_solicitud, estado, comentario_revisor, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, sol.getIdSolicitud());
            ps.setString(2, sol.getCodigoDocumento());
            ps.setString(3, sol.getTituloDocumento());
            ps.setString(4, sol.getSolicitante());
            ps.setString(5, sol.getRevisor());
            ps.setString(6, sol.getPrioridad());
            ps.setString(7, sol.getFechaSolicitud());
            ps.setString(8, sol.getEstado());
            ps.setString(9, sol.getComentarioRevisor());
            ps.setString(10, sol.getVersion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando solicitud: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(SolicitudAprobacion sol) {
        String sql = "UPDATE SolicitudAprobacion SET estado=?, comentario_revisor=? WHERE id_solicitud=?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, sol.getEstado());
            ps.setString(2, sol.getComentarioRevisor());
            ps.setString(3, sol.getIdSolicitud());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando solicitud: " + e.getMessage());
            return false;
        }
    }

    public List<SolicitudAprobacion> obtenerPendientes() {
        List<SolicitudAprobacion> solicitudes = new ArrayList<>();
        // En un sistema real, podríamos ordenar aquí por prioridad o fecha. 
        // Dado que se insertan en la ColaAprobacion, la cola ya ordena por prioridad.
        String sql = "SELECT * FROM SolicitudAprobacion WHERE estado = 'Pendiente'";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                SolicitudAprobacion sol = new SolicitudAprobacion(
                    rs.getString("id_solicitud"),
                    rs.getString("codigo_documento"),
                    rs.getString("titulo_documento"),
                    rs.getString("solicitante"),
                    rs.getString("revisor"),
                    rs.getString("prioridad"),
                    rs.getString("fecha_solicitud"),
                    rs.getString("version")
                );
                sol.setEstado(rs.getString("estado"));
                sol.setComentarioRevisor(rs.getString("comentario_revisor"));
                solicitudes.add(sol);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo solicitudes: " + e.getMessage());
        }
        return solicitudes;
    }
}
