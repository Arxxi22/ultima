package neovet;

import java.time.format.DateTimeFormatter;

public class GeneradorPDF {
    
    public String generarFactura(Pago pago) {
        // Usar DateTimeFormatter para formatear la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        // Formatear la fecha
        String fechaFormateada = pago.getFechaPago() != null 
            ? pago.getFechaPago().format(formatter) 
            : "Fecha no disponible";
        
        // Crear confirmación
        String confirmacion = "Se ha registrado el pago con ID: " + pago.getIdPago();
        
        // Información del pago
        System.out.println("=== INFORMACIÓN DEL PAGO ===");
        System.out.println("ID Pago: " + pago.getIdPago());
        System.out.println("ID Cita: " + pago.getIdCita());
        System.out.println("Valor: $" + pago.getValor());
        System.out.println("Fecha: " + fechaFormateada);
        System.out.println("Forma de Pago: " + pago.getFormaPago());
        System.out.println("Concepto: " + pago.getConcepto());
        System.out.println("Pagado: " + (pago.isPagado() ? "Sí" : "No"));
        
        return confirmacion;
    }
}
