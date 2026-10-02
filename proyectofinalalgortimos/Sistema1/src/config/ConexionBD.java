package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=GestionDocumental;user=sa;password=Huancasiqui997;encrypt=true;trustServerCertificate=true;";

    private ConexionBD() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexion = DriverManager.getConnection(url);
            System.out.println("Conexión a BD establecida exitosamente.");
        } catch (ClassNotFoundException e) {
            System.err.println(
                    "Error: Driver JDBC de SQL Server no encontrado. Asegurate de añadir mssql-jdbc.jar al build path.");
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
            System.err.println(
                    "Asegúrate que las claves esten bien.");
        }
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.err.println("Error re-estableciendo conexión: " + e.getMessage());
        }
        return conexion;
    }
}
