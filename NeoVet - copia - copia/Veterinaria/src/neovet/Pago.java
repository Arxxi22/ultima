package neovet;

import java.time.LocalDateTime;

public class Pago {
    private int idPago;
    private int idCita;
    private double valor;
    private LocalDateTime fechaPago;
    private String formaPago;
    private String concepto;
    private boolean pagado;

    // Constructor sin argumentos
    public Pago() {
    }

    // Constructor con todos los campos
    public Pago(int idPago, int idCita, double valor, LocalDateTime fechaPago, String formaPago, String concepto, boolean pagado) {
        this.idPago = idPago;
        this.idCita = idCita;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.formaPago = formaPago;
        this.concepto = concepto;
        this.pagado = pagado;
    }

    // Getters y setters
    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    @Override
    public String toString() {
        return "Pago #" + idPago + " - Valor: $" + valor + " - " + (pagado ? "Pagado" : "Pendiente");
    }
}
