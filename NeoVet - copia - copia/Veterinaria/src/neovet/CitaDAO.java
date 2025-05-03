package neovet;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    
    private MascotaDAO mascotaDAO;
    private VeterinarioDAO veterinarioDAO;
    
    public CitaDAO() {
        this.mascotaDAO = new MascotaDAO();
        this.veterinarioDAO = new VeterinarioDAO();
    }
    
    public int guardar(Cita cita) throws SQLException {
        String sql = "INSERT INTO Citas (idMascota, idVeterinario, motivo, sintomas, medicamentos, fechaHora, estado, valorServicio, descripcionServicio) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, cita.getIdMascota());
            stmt.setInt(2, cita.getIdVeterinario());
            stmt.setString(3, cita.getMotivo());
            stmt.setString(4, cita.getSintomas());
            stmt.setString(5, cita.getMedicamentos());
            
            // En SQLite guardamos LocalDateTime como TEXT
            if (cita.getFechaHora() != null) {
                stmt.setString(6, cita.getFechaHora().toString());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            
            stmt.setString(7, cita.getEstado());
            stmt.setDouble(8, cita.getValorServicio());
            stmt.setString(9, cita.getDescripcionServicio());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
            return -1;
        }
    }
    
    public Cita obtenerPorId(int idCita) throws SQLException {
        String sql = "SELECT c.*, m.nombre as nombreMascota FROM Citas c " +
                    "LEFT JOIN Mascotas m ON c.idMascota = m.idMascota " +
                    "WHERE c.idCita = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerCitaDeLaDB(rs);
                }
            }
            
            return null;
        }
    }
    
    public List<Cita> obtenerTodos() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, m.nombre as nombreMascota FROM Citas c " +
                    "LEFT JOIN Mascotas m ON c.idMascota = m.idMascota " +
                    "ORDER BY c.fechaHora DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                citas.add(extraerCitaDeLaDB(rs));
            }
        }
        
        return citas;
    }
    
    public List<Cita> obtenerCitasSinPago() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, m.nombre as nombreMascota FROM Citas c " +
                    "LEFT JOIN Mascotas m ON c.idMascota = m.idMascota " +
                    "WHERE c.idCita NOT IN (SELECT idCita FROM Pagos) " +
                    "ORDER BY c.fechaHora DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                citas.add(extraerCitaDeLaDB(rs));
            }
        }
        
        return citas;
    }
    
    private Cita extraerCitaDeLaDB(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        
        cita.setIdCita(rs.getInt("idCita"));
        cita.setIdMascota(rs.getInt("idMascota"));
        cita.setIdVeterinario(rs.getInt("idVeterinario"));
        cita.setMotivo(rs.getString("motivo"));
        cita.setSintomas(rs.getString("sintomas"));
        cita.setMedicamentos(rs.getString("medicamentos"));
        
        // Convertir texto a LocalDateTime en SQLite
        String fechaHoraStr = rs.getString("fechaHora");
        if (fechaHoraStr != null && !fechaHoraStr.isEmpty()) {
            cita.setFechaHora(LocalDateTime.parse(fechaHoraStr));
        }
        
        cita.setEstado(rs.getString("estado"));
        cita.setValorServicio(rs.getDouble("valorServicio"));
        cita.setDescripcionServicio(rs.getString("descripcionServicio"));
        
        // Establecer nombre de mascota para mostrar en tablas
        try {
            cita.setNombreMascota(rs.getString("nombreMascota"));
        } catch (SQLException e) {
            // Si no existe la columna, simplemente continúa
        }
        
        return cita;
    }
    
    public boolean actualizar(Cita cita) throws SQLException {
        String sql = "UPDATE Citas SET idMascota = ?, idVeterinario = ?, motivo = ?, sintomas = ?, " +
                    "medicamentos = ?, fechaHora = ?, estado = ?, valorServicio = ?, descripcionServicio = ? " +
                    "WHERE idCita = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cita.getIdMascota());
            stmt.setInt(2, cita.getIdVeterinario());
            stmt.setString(3, cita.getMotivo());
            stmt.setString(4, cita.getSintomas());
            stmt.setString(5, cita.getMedicamentos());
            
            // En SQLite guardamos LocalDateTime como TEXT
            if (cita.getFechaHora() != null) {
                stmt.setString(6, cita.getFechaHora().toString());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            
            stmt.setString(7, cita.getEstado());
            stmt.setDouble(8, cita.getValorServicio());
            stmt.setString(9, cita.getDescripcionServicio());
            stmt.setInt(10, cita.getIdCita());
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
    
    public void actualizarEstadoCita(Cita cita) throws SQLException {
        String sql = "UPDATE Citas SET estado = ? WHERE idCita = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cita.getEstado());
            stmt.setInt(2, cita.getIdCita());
            
            stmt.executeUpdate();
        }
    }
    
    public boolean eliminar(int idCita) throws SQLException {
        String sql = "DELETE FROM Citas WHERE idCita = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCita);
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
}
