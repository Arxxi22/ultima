package neovet;

import java.time.LocalDateTime;

public class Cita {
    private int idCita;
    private int idMascota;
    private int idVeterinario;
    private String motivo;
    private String sintomas;
    private String medicamentos;
    private LocalDateTime fechaHora;
    private String estado;
    private double valorServicio;
    private String descripcionServicio;

    // Relaciones
    private Mascota mascota;
    private Veterinario veterinario;
    
    // Variable temporal para mostrar en la tabla
    private String nombreMascota;
    
    // Constructor vacío
    public Cita() {
        this.estado = "Programada";
        this.valorServicio = 0.0;
    }
    
    // Constructor completo
    public Cita(int idCita, int idMascota, int idVeterinario, String motivo, String sintomas,
               String medicamentos, LocalDateTime fechaHora, String estado, double valorServicio,
               String descripcionServicio) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.motivo = motivo;
        this.sintomas = sintomas;
        this.medicamentos = medicamentos;
        this.fechaHora = fechaHora;
        this.estado = estado != null ? estado : "Programada";
        this.valorServicio = valorServicio;
        this.descripcionServicio = descripcionServicio;
    }
    
    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }
    
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    
    public int getIdMascota() {
        return idMascota;
    }
    
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
    
    public int getIdVeterinario() {
        return idVeterinario;
    }
    
    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getSintomas() {
        return sintomas;
    }
    
    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }
    
    public String getMedicamentos() {
        return medicamentos;
    }
    
    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public double getValorServicio() {
        return valorServicio;
    }
    
    public void setValorServicio(double valorServicio) {
        this.valorServicio = valorServicio;
    }
    
    public String getDescripcionServicio() {
        return descripcionServicio;
    }
    
    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }
    
    // Getters y setters para relaciones
    public Mascota getMascota() {
        return mascota;
    }
    
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
        if (mascota != null) {
            this.nombreMascota = mascota.getNombre();
        }
    }
    
    public Veterinario getVeterinario() {
        return veterinario;
    }
    
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
    
    // Getter y setter para el nombre de mascota (para mostrar en tablas)
    public String getNombreMascota() {
        return nombreMascota;
    }
    
    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public LocalDateTime getFechaCita() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFechaCita'");
    }
}
