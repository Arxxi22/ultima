package neovet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Callback;

public class VeterinarioDAO {
    
    public int guardar(Veterinario veterinario) throws SQLException {
        String sql = "INSERT INTO Veterinarios (nombre, especialidad, cedulaProfesional, telefono, email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, veterinario.getNombre());
            stmt.setString(2, veterinario.getEspecialidad());
            stmt.setString(3, veterinario.getCedulaProfesional());
            stmt.setString(4, veterinario.getTelefono());
            stmt.setString(5, veterinario.getEmail());
            
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
    
    public List<Veterinario> obtenerTodos() throws SQLException {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = "SELECT * FROM Veterinarios ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Veterinario veterinario = extraerVeterinarioDeLaDB(rs);
                veterinarios.add(veterinario);
            }
        }
        
        return veterinarios;
    }
    
    private Veterinario extraerVeterinarioDeLaDB(ResultSet rs) throws SQLException {
        int idVeterinario = rs.getInt("idVeterinario");
        String nombre = rs.getString("nombre");
        String especialidad = rs.getString("especialidad");
        String cedulaProfesional = rs.getString("cedulaProfesional");
        String telefono = rs.getString("telefono");
        String email = rs.getString("email");
        
        return new Veterinario(idVeterinario, nombre, especialidad, cedulaProfesional, telefono, email);
    }
    
    public boolean actualizar(Veterinario veterinario) throws SQLException {
        String sql = "UPDATE Veterinarios SET nombre = ?, especialidad = ?, cedulaProfesional = ?, telefono = ?, email = ? WHERE idVeterinario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, veterinario.getNombre());
            stmt.setString(2, veterinario.getEspecialidad());
            stmt.setString(3, veterinario.getCedulaProfesional());
            stmt.setString(4, veterinario.getTelefono());
            stmt.setString(5, veterinario.getEmail());
            stmt.setInt(6, veterinario.getIdVeterinario());
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
    
    public boolean eliminar(int idVeterinario) throws SQLException {
        String sql = "DELETE FROM Veterinarios WHERE idVeterinario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVeterinario);
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }

    public Veterinario obtenerPorId(int idVeterinario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPorId'");
    }

    public Callback obtenerVeterinariosActivos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerVeterinariosActivos'");
    }
}
