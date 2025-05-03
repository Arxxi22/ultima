package neovet;

import neovet.DatosMedicos;
import neovet.ConexionBD;
import java.sql.*;
import java.time.LocalDate;

public class DatosMedicosDAO {
    
    public int guardar(DatosMedicos datosMedicos) throws SQLException {
        String sql;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int idGenerado = -1;
        
        try {
            conn = ConexionBD.obtenerConexion();
            
            // Verificar si ya existen datos médicos para esta mascota
            sql = "SELECT id_datos_medicos FROM DatosMedicos WHERE id_mascota = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, datosMedicos.getIdMascota());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Actualizar datos médicos existentes
                idGenerado = rs.getInt("id_datos_medicos");
                datosMedicos.setIdDatosMedicos(idGenerado);
                
                sql = "UPDATE DatosMedicos SET tipo_sangre=?, alergias=?, enfermedades_cronicas=?, " +
                      "vacunas_al_dia=?, ultima_visita=?, microchip=? WHERE id_datos_medicos=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, datosMedicos.getTipoSangre());
                stmt.setString(2, datosMedicos.getAlergias());
                stmt.setString(3, datosMedicos.getEnfermedadesCronicas());
                stmt.setBoolean(4, datosMedicos.isVacunasAlDia());
                stmt.setDate(5, datosMedicos.getUltimaVisita() != null ? 
                           Date.valueOf(datosMedicos.getUltimaVisita()) : null);
                stmt.setString(6, datosMedicos.getMicrochip());
                stmt.setInt(7, idGenerado);
                stmt.executeUpdate();
            } else {
                // Insertar nuevos datos médicos
                sql = "INSERT INTO DatosMedicos (id_mascota, tipo_sangre, alergias, enfermedades_cronicas, " +
                      "vacunas_al_dia, ultima_visita, microchip) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, datosMedicos.getIdMascota());
                stmt.setString(2, datosMedicos.getTipoSangre());
                stmt.setString(3, datosMedicos.getAlergias());
                stmt.setString(4, datosMedicos.getEnfermedadesCronicas());
                stmt.setBoolean(5, datosMedicos.isVacunasAlDia());
                stmt.setDate(6, datosMedicos.getUltimaVisita() != null ? 
                           Date.valueOf(datosMedicos.getUltimaVisita()) : null);
                stmt.setString(7, datosMedicos.getMicrochip());
                stmt.executeUpdate();
                
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                    datosMedicos.setIdDatosMedicos(idGenerado);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        
        return idGenerado;
    }
    
    public DatosMedicos obtenerPorMascota(int idMascota) throws SQLException {
        String sql = "SELECT * FROM DatosMedicos WHERE id_mascota = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        DatosMedicos datosMedicos = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMascota);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                datosMedicos = new DatosMedicos();
                datosMedicos.setIdDatosMedicos(rs.getInt("id_datos_medicos"));
                datosMedicos.setIdMascota(rs.getInt("id_mascota"));
                datosMedicos.setTipoSangre(rs.getString("tipo_sangre"));
                datosMedicos.setAlergias(rs.getString("alergias"));
                datosMedicos.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
                datosMedicos.setVacunasAlDia(rs.getBoolean("vacunas_al_dia"));
                Date ultimaVisita = rs.getDate("ultima_visita");
                if (ultimaVisita != null) {
                    datosMedicos.setUltimaVisita(ultimaVisita.toLocalDate());
                }
                datosMedicos.setMicrochip(rs.getString("microchip"));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        
        return datosMedicos;
    }
}
