package neovet;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {
    
    public int guardar(Mascota mascota) throws SQLException {
        String sql = "INSERT INTO Mascotas (idCliente, nombre, especie, raza, color, fechaNacimiento, sexo, peso) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, mascota.getIdCliente());
            stmt.setString(2, mascota.getNombre());
            stmt.setString(3, mascota.getEspecie());
            stmt.setString(4, mascota.getRaza());
            stmt.setString(5, mascota.getColor());
            
            // Convertir LocalDate a Date SQL
            if (mascota.getFechaNacimiento() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            
            stmt.setString(7, mascota.getSexo());
            stmt.setDouble(8, mascota.getPeso());
            
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
    
    public List<Mascota> obtenerPorCliente(int idCliente) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "SELECT * FROM Mascotas WHERE idCliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mascota mascota = extraerMascotaDeLaDB(rs);
                    mascotas.add(mascota);
                }
            }
        }
        
        return mascotas;
    }
    
    private Mascota extraerMascotaDeLaDB(ResultSet rs) throws SQLException {
        int idMascota = rs.getInt("idMascota");
        int idCliente = rs.getInt("idCliente");
        String nombre = rs.getString("nombre");
        String especie = rs.getString("especie");
        String raza = rs.getString("raza");
        String color = rs.getString("color");
        
        // Convertir Date SQL a LocalDate
        java.sql.Date fechaSQL = rs.getDate("fechaNacimiento");
        LocalDate fechaNacimiento = (fechaSQL != null) ? fechaSQL.toLocalDate() : null;
        
        String sexo = rs.getString("sexo");
        double peso = rs.getDouble("peso");
        
        return new Mascota(idMascota, idCliente, nombre, especie, raza, color, fechaNacimiento, sexo, peso);
    }
    
    public boolean actualizar(Mascota mascota) throws SQLException {
        String sql = "UPDATE Mascotas SET idCliente = ?, nombre = ?, especie = ?, raza = ?, " +
                    "color = ?, fechaNacimiento = ?, sexo = ?, peso = ? WHERE idMascota = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mascota.getIdCliente());
            stmt.setString(2, mascota.getNombre());
            stmt.setString(3, mascota.getEspecie());
            stmt.setString(4, mascota.getRaza());
            stmt.setString(5, mascota.getColor());
            
            // Convertir LocalDate a Date SQL
            if (mascota.getFechaNacimiento() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            
            stmt.setString(7, mascota.getSexo());
            stmt.setDouble(8, mascota.getPeso());
            stmt.setInt(9, mascota.getIdMascota());
            
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;
        }
    }
}
