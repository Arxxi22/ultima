package neovet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class ConexionBD {
    
    // Ruta de tu base de datos SQLite
    private static final String URL = "jdbc:sqlite:neovet.db";
    
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Cargar el driver SQLite
            Class.forName("org.sqlite.JDBC");
            
            // Obtener la conexión
            Connection conn = DriverManager.getConnection(URL);
            
            // Habilitar llaves foráneas
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            
            // Inicializar tablas si es necesario
            inicializarBaseDeDatos(conn);
            
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver SQLite", e);
        }
    }
    
    private static void inicializarBaseDeDatos(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Tabla Clientes
            stmt.execute("CREATE TABLE IF NOT EXISTS Clientes (" +
                "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "telefono TEXT, " +
                "email TEXT, " +
                "direccion TEXT, " +
                "rfc TEXT)");
            
            // Tabla Veterinarios
            stmt.execute("CREATE TABLE IF NOT EXISTS Veterinarios (" +
                "idVeterinario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "especialidad TEXT, " +
                "cedulaProfesional TEXT, " +
                "telefono TEXT, " +
                "email TEXT, " +
                "activo BOOLEAN DEFAULT 1)");
            
            // Tabla Mascotas
            stmt.execute("CREATE TABLE IF NOT EXISTS Mascotas (" +
                "idMascota INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idCliente INTEGER, " +
                "nombre TEXT, " +
                "especie TEXT, " +
                "raza TEXT, " +
                "color TEXT, " +
                "fechaNacimiento TEXT, " +
                "sexo TEXT, " +
                "peso REAL, " +
                "FOREIGN KEY(idCliente) REFERENCES Clientes(idCliente))");
            
            // Tabla Citas
            stmt.execute("CREATE TABLE IF NOT EXISTS Citas (" +
                "idCita INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idMascota INTEGER, " +
                "idVeterinario INTEGER, " +
                "motivo TEXT, " +
                "sintomas TEXT, " +
                "medicamentos TEXT, " +
                "fechaHora TEXT, " +
                "estado TEXT, " +
                "valorServicio REAL, " +
                "descripcionServicio TEXT, " +
                "FOREIGN KEY(idMascota) REFERENCES Mascotas(idMascota), " +
                "FOREIGN KEY(idVeterinario) REFERENCES Veterinarios(idVeterinario))");
            
            // Tabla Pagos
            stmt.execute("CREATE TABLE IF NOT EXISTS Pagos (" +
                "idPago INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idCita INTEGER, " +
                "valor REAL, " +
                "fechaPago TEXT, " +
                "formaPago TEXT, " +
                "concepto TEXT, " +
                "pagado BOOLEAN, " +
                "FOREIGN KEY(idCita) REFERENCES Citas(idCita))");
        }
    }
    
    // Método para cerrar conexión
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void inicializarBaseDatos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inicializarBaseDatos'");
    }
}
