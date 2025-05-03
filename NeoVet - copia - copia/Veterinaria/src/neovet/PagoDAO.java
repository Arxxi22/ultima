package neovet;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    
    private CitaDAO citaDAO;
    
    public PagoDAO() {
        this.citaDAO = new CitaDAO();
    }
    
    public int guardar(Pago pago) throws SQLException {
        String sql = "INSERT INTO Pagos (idCita, valor, fechaPago, formaPago, concepto, pagado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pago.getIdCita());
            stmt.setDouble(2, pago.getValor());
            
            // Convertir LocalDateTime a Timestamp
            Timestamp timestamp = Timestamp.valueOf(pago.getFechaPago());
            stmt.setTimestamp(3, timestamp);
            
            stmt.setString(4, pago.getFormaPago());
            stmt.setString(5, pago.getConcepto());
            stmt.setBoolean(6, pago.isPagado());
            
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
    
    public boolean actualizar(Pago pago) throws SQLException {
        String sql = "UPDATE Pagos SET idCita = ?, valor = ?, fechaPago = ?, formaPago = ?, concepto = ?, pagado = ? WHERE idPago = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pago.getIdCita());
            stmt.setDouble(2, pago.getValor());
            
            // Convertir LocalDateTime a Timestamp
            Timestamp timestamp = Timestamp.valueOf(pago.getFechaPago());
            stmt.setTimestamp(3, timestamp);
            
            stmt.setString(4, pago.getFormaPago());
            stmt.setString(5, pago.getConcepto());
            stmt.setBoolean(6, pago.isPagado());
            stmt.setInt(7, pago.getIdPago());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    public boolean eliminar(int idPago) throws SQLException {
        String sql = "DELETE FROM Pagos WHERE idPago = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPago);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    public Pago obtenerPorId(int idPago) throws SQLException {
        String sql = "SELECT * FROM Pagos WHERE idPago = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPago);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerPagoDeResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    public List<Pago> obtenerTodosPagos() throws SQLException {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM Pagos ORDER BY fechaPago DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pagos.add(extraerPagoDeResultSet(rs));
            }
        }
        
        return pagos;
    }
    
    public List<Pago> obtenerPagosPorCita(int idCita) throws SQLException {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM Pagos WHERE idCita = ? ORDER BY fechaPago DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(extraerPagoDeResultSet(rs));
                }
            }
        }
        
        return pagos;
    }
    
    private Pago extraerPagoDeResultSet(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("idPago"));
        pago.setIdCita(rs.getInt("idCita"));
        pago.setValor(rs.getDouble("valor"));
        
        // Convertir Timestamp a LocalDateTime
        Timestamp timestamp = rs.getTimestamp("fechaPago");
        if (timestamp != null) {
            pago.setFechaPago(timestamp.toLocalDateTime());
        }
        
        pago.setFormaPago(rs.getString("formaPago"));
        pago.setConcepto(rs.getString("concepto"));
        pago.setPagado(rs.getBoolean("pagado"));
        
        return pago;
    }
}
