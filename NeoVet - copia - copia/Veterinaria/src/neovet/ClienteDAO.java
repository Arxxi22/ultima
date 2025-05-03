package neovet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    public int guardar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (nombre, telefono, email, direccion, rfc) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getTelefono());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getRfc());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
            return -1;
        }
    }
    
    public List<Cliente> obtenerTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = extraerClienteDeLaDB(rs);
                clientes.add(cliente);
            }
        }
        
        return clientes;
    }
    
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE nombre LIKE ? ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = extraerClienteDeLaDB(rs);
                    clientes.add(cliente);
                }
            }
        }
        
        return clientes;
    }
    
    private Cliente extraerClienteDeLaDB(ResultSet rs) throws SQLException {
        int idCliente = rs.getInt("idCliente");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono");
        String email = rs.getString("email");
        String direccion = rs.getString("direccion");
        String rfc = rs.getString("rfc");
        
        return new Cliente(idCliente, nombre, telefono, email, direccion, rfc);
    }
    
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET nombre = ?, telefono = ?, email = ?, direccion = ?, rfc = ? WHERE idCliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getTelefono());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getRfc());
            stmt.setInt(6, cliente.getIdCliente());
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
    
    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM Clientes WHERE idCliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
}
