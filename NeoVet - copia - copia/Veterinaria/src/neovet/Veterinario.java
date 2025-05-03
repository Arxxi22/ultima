package neovet;

public class Veterinario {
    private int idVeterinario;
    private String nombre;
    private String especialidad;
    private String cedulaProfesional;
    private String telefono;
    private String email;
    private boolean activo;
    
    // Constructor vacío
    public Veterinario() {
        this.activo = true;
    }
    
    // Constructor completo
    public Veterinario(int idVeterinario, String nombre, String especialidad, 
                      String cedulaProfesional, String telefono, String email) {
        this.idVeterinario = idVeterinario;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cedulaProfesional = cedulaProfesional;
        this.telefono = telefono;
        this.email = email;
        this.activo = true;
    }
    
    // Getters y Setters
    public int getIdVeterinario() {
        return idVeterinario;
    }
    
    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getCedulaProfesional() {
        return cedulaProfesional;
    }
    
    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + especialidad + ")";
    }
}
