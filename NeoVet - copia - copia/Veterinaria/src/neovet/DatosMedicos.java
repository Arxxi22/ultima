package neovet;

import java.time.LocalDate;

public class DatosMedicos {
    private int idDatosMedicos;
    private int idMascota;
    private String tipoSangre;
    private String alergias;
    private String enfermedadesCronicas;
    private boolean vacunasAlDia;
    private LocalDate ultimaVisita;
    private String microchip;
    
    // Constructor vacío
    public DatosMedicos() {
    }
    
    // Constructor completo
    public DatosMedicos(int idDatosMedicos, int idMascota, String tipoSangre, String alergias,
                        String enfermedadesCronicas, boolean vacunasAlDia, LocalDate ultimaVisita,
                        String microchip) {
        this.idDatosMedicos = idDatosMedicos;
        this.idMascota = idMascota;
        this.tipoSangre = tipoSangre;
        this.alergias = alergias;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.vacunasAlDia = vacunasAlDia;
        this.ultimaVisita = ultimaVisita;
        this.microchip = microchip;
    }
    
    // Getters y Setters
    public int getIdDatosMedicos() {
        return idDatosMedicos;
    }
    
    public void setIdDatosMedicos(int idDatosMedicos) {
        this.idDatosMedicos = idDatosMedicos;
    }
    
    public int getIdMascota() {
        return idMascota;
    }
    
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
    
    public String getTipoSangre() {
        return tipoSangre;
    }
    
    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
    
    public String getAlergias() {
        return alergias;
    }
    
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
    
    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }
    
    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }
    
    public boolean isVacunasAlDia() {
        return vacunasAlDia;
    }
    
    public void setVacunasAlDia(boolean vacunasAlDia) {
        this.vacunasAlDia = vacunasAlDia;
    }
    
    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }
    
    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }
    
    public String getMicrochip() {
        return microchip;
    }
    
    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }
}
