package neovet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Pagos extends Application {
    
    // DAOs
    private PagoDAO pagoDAO;
    private CitaDAO citaDAO;
    
    // Listas para tablas
    private ObservableList<Cita> listaCitas;
    private ObservableList<Pago> listaPagos;
    
    // Elementos UI
    private TableView<Cita> tablaCitas;
    private TableView<Pago> tablaPagos;
    private TextField txtValor;
    private TextField txtConcepto;
    private ComboBox<String> cmbFormaPago;
    private CheckBox chkPagado;
    
    // Seleccionados
    private Cita citaSeleccionada;
    
    // Generador de PDF
    private GeneradorPDF generadorPDF;
    
    @Override
    public void start(Stage primaryStage) {
        // Inicializar DAOs y componentes
        pagoDAO = new PagoDAO();
        citaDAO = new CitaDAO();
        generadorPDF = new GeneradorPDF();
        
        // Configurar el stage
        primaryStage.setTitle("Gestión de Pagos - NeoVet");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        
        // Layout principal
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(15));
        
        // Panel superior - Título
        Label lblTitulo = new Label("GESTIÓN DE PAGOS");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox panelTitulo = new HBox(lblTitulo);
        panelTitulo.setAlignment(Pos.CENTER);
        layoutPrincipal.setTop(panelTitulo);
        
        // Panel izquierdo - Citas pendientes
        VBox panelCitas = new VBox(10);
        Label lblCitas = new Label("Citas Pendientes");
        lblCitas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Tabla de citas
        tablaCitas = new TableView<>();
        configurarTablaCitas();
        panelCitas.getChildren().addAll(lblCitas, tablaCitas);
        
        // Panel derecho - Pagos realizados
        VBox panelPagosRealizados = new VBox(10);
        Label lblPagosRealizados = new Label("Pagos Realizados");
        lblPagosRealizados.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Tabla de pagos
        tablaPagos = new TableView<>();
        configurarTablaPagos();
        panelPagosRealizados.getChildren().addAll(lblPagosRealizados, tablaPagos);
        
        // Panel central - Formulario de pago
        VBox panelFormulario = new VBox(15);
        Label lblFormularioPago = new Label("Registro de Pago");
        lblFormularioPago.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Formulario
        GridPane formularioPago = configurarFormularioPago();
        HBox panelBotones = configurarBotones();
        
        panelFormulario.getChildren().addAll(lblFormularioPago, formularioPago, panelBotones);
        panelFormulario.setAlignment(Pos.TOP_CENTER);
        panelFormulario.setPadding(new Insets(0, 15, 0, 15));
        
        // Configurar SplitPane para la parte central
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(panelCitas, panelFormulario, panelPagosRealizados);
        splitPane.setDividerPositions(0.33, 0.67);
        
        layoutPrincipal.setCenter(splitPane);
        
        // Crear escena
        Scene scene = new Scene(layoutPrincipal);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Cargar datos iniciales
        cargarCitas();
        cargarPagos();
        
        // Configurar selección de cita
        tablaCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            citaSeleccionada = newSelection;
        });
    }
    
    private void configurarTablaCitas() {
        tablaCitas.setPlaceholder(new Label("No hay citas pendientes"));
        
        // Definir columnas
        TableColumn<Cita, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colId.setPrefWidth(30);
        
        TableColumn<Cita, String> colMascota = new TableColumn<>("Mascota");
        colMascota.setCellValueFactory(new PropertyValueFactory<>("nombreMascota"));
        colMascota.setPrefWidth(100);
        
        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaCita();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
        });
        colFecha.setPrefWidth(120);
        
        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colMotivo.setPrefWidth(100);
        
        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(80);
        
        tablaCitas.getColumns().addAll(colId, colMascota, colFecha, colMotivo, colEstado);
    }
    
    private void configurarTablaPagos() {
        tablaPagos.setPlaceholder(new Label("No hay pagos registrados"));
        
        // Definir columnas
        TableColumn<Pago, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        colId.setPrefWidth(30);
        
        TableColumn<Pago, Integer> colIdCita = new TableColumn<>("Cita");
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colIdCita.setPrefWidth(40);
        
        TableColumn<Pago, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValor.setPrefWidth(80);
        
        TableColumn<Pago, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaPago();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : ""
            );
        });
        colFecha.setPrefWidth(120);
        
        TableColumn<Pago, String> colFormaPago = new TableColumn<>("Forma Pago");
        colFormaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));
        colFormaPago.setPrefWidth(90);
        
        TableColumn<Pago, Boolean> colPagado = new TableColumn<>("Pagado");
        colPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        colPagado.setPrefWidth(60);
        
        tablaPagos.getColumns().addAll(colId, colIdCita, colValor, colFecha, colFormaPago, colPagado);
    }
    
    private GridPane configurarFormularioPago() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
        
        // Campos de pago
        Label lblValor = new Label("Valor del Pago:");
        txtValor = new TextField();
        txtValor.setPromptText("Ingrese el valor");
        
        Label lblConcepto = new Label("Concepto:");
        txtConcepto = new TextField();
        txtConcepto.setPromptText("Descripción del pago");
        
        Label lblFormaPago = new Label("Forma de Pago:");
        cmbFormaPago = new ComboBox<>();
        cmbFormaPago.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
        cmbFormaPago.setPromptText("Seleccione forma de pago");
        cmbFormaPago.setPrefWidth(150);
        
        chkPagado = new CheckBox("Pagado");
        chkPagado.setSelected(true);
        
        // Añadir al grid
        grid.add(lblValor, 0, 0);
        grid.add(txtValor, 1, 0);
        grid.add(lblConcepto, 0, 1);
        grid.add(txtConcepto, 1, 1);
        grid.add(lblFormaPago, 0, 2);
        grid.add(cmbFormaPago, 1, 2);
        grid.add(chkPagado, 1, 3);
        
        return grid;
    }
    
    private HBox configurarBotones() {
        HBox botonesContainer = new HBox(10);
        botonesContainer.setAlignment(Pos.CENTER);
        botonesContainer.setPadding(new Insets(10, 0, 10, 0));
        
        Button btnGuardarPago = new Button("Guardar Pago");
        btnGuardarPago.setOnAction(e -> guardarPago());
        
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setOnAction(e -> limpiarFormulario());
        
        Button btnImprimirFactura = new Button("Imprimir Factura");
        btnImprimirFactura.setOnAction(e -> imprimirFactura());
        
        botonesContainer.getChildren().addAll(btnGuardarPago, btnLimpiar, btnImprimirFactura);
        
        return botonesContainer;
    }
    
    private void guardarPago() {
        if (citaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", 
                         "Debe seleccionar una cita para registrar el pago.");
            return;
        }
        
        if (txtValor.getText().isEmpty() || txtConcepto.getText().isEmpty() || cmbFormaPago.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", 
                         "Todos los campos son obligatorios.");
            return;
        }
        
        try {
            double valor = Double.parseDouble(txtValor.getText());
            
            Pago pago = new Pago();
            pago.setIdCita(citaSeleccionada.getIdCita());
            pago.setValor(valor);
            pago.setFechaPago(LocalDateTime.now());
            pago.setFormaPago(cmbFormaPago.getValue());
            pago.setConcepto(txtConcepto.getText());
            pago.setPagado(chkPagado.isSelected());
            
            int id = pagoDAO.guardar(pago);
            
            if (id > 0) {
                pago.setIdPago(id);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Pago registrado correctamente.");
                
                // Actualizar la cita a "Pagado" si corresponde
                if (chkPagado.isSelected()) {
                    citaSeleccionada.setEstado("Pagado");
                    citaDAO.actualizar(citaSeleccionada);
                }
                
                cargarCitas();
                cargarPagos();
                limpiarFormulario();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el pago.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El valor debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al registrar el pago: " + e.getMessage());
        }
    }
    
    private void imprimirFactura() {
        Pago pagoSeleccionado = tablaPagos.getSelectionModel().getSelectedItem();
        if (pagoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un pago", 
                         "Debe seleccionar un pago para imprimir la factura.");
            return;
        }
        
        try {
            String confirmacion = generadorPDF.generarFactura(pagoSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Factura Generada", confirmacion);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al generar factura: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        citaSeleccionada = null;
        txtValor.clear();
        txtConcepto.clear();
        cmbFormaPago.setValue(null);
        chkPagado.setSelected(true);
        tablaCitas.getSelectionModel().clearSelection();
        tablaPagos.getSelectionModel().clearSelection();
    }

    private void cargarCitas() {
        // Implementación para cargar citas desde la base de datos
        try {
            listaCitas = FXCollections.observableArrayList(citaDAO.obtenerTodos());
            tablaCitas.setItems(listaCitas);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las citas: " + e.getMessage());
        }
    }

    private void cargarPagos() {
        // Implementación para cargar pagos desde la base de datos
        try {
            listaPagos = FXCollections.observableArrayList(pagoDAO.obtenerTodosPagos());
            tablaPagos.setItems(listaPagos);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los pagos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
