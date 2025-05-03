package neovet;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionCitas extends Application {
    private TableView<Cita> tablaCitas;
    private ObservableList<Cita> listaCitas;
    private CitaDAO citaDAO;
    private ComboBox<String> comboEstado;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Citas");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        
        // Configurar DAO
        citaDAO = new CitaDAO();
        
        // Crear layout principal
        VBox layoutPrincipal = new VBox(10);
        layoutPrincipal.setPadding(new Insets(20));
        
        // Configurar tabla de citas
        configurarTablaCitas();
        
        // Panel para modificar estado
        GridPane panelEstado = configurarPanelEstado();
        
        // Botones
        HBox botonesAccion = configurarBotones(primaryStage);
        
        // Agregar componentes al layout principal
        layoutPrincipal.getChildren().addAll(
            tablaCitas, 
            panelEstado, 
            botonesAccion
        );
        
        // Crear escena
        Scene scene = new Scene(layoutPrincipal);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Cargar datos
        cargarDatosCitas();
    }
    
    private void configurarTablaCitas() {
        tablaCitas = new TableView<>();
        
        // Columna ID Cita
        TableColumn<Cita, Integer> colId = new TableColumn<>("ID Cita");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        
        // Columna Nombre Mascota
        TableColumn<Cita, String> colNombreMascota = new TableColumn<>("Mascota");
        colNombreMascota.setCellValueFactory(new PropertyValueFactory<>("nombreMascota"));
        
        // Columna Veterinario
        TableColumn<Cita, String> colVeterinario = new TableColumn<>("Veterinario");
        colVeterinario.setCellValueFactory(cellData -> {
            Veterinario veterinario = new VeterinarioDAO().obtenerPorId(cellData.getValue().getIdVeterinario());
            return new javafx.beans.property.SimpleStringProperty(
                veterinario != null ? veterinario.getNombre() : "No disponible"
            );
        });
        
        // Columna Motivo
        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        
        // Columna Fecha
        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaHora();
            return new javafx.beans.property.SimpleStringProperty(
                fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : ""
            );
        });
        
        // Columna Estado
        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Configurar columnas
        tablaCitas.getColumns().addAll(
            colId, colNombreMascota, colVeterinario, 
            colMotivo, colFecha, colEstado
        );
        
        // Hacer tabla no editable
        tablaCitas.setEditable(false);
        
        // Ajustar tamaño de columnas
        tablaCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Evento de selección
        tablaCitas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    comboEstado.setValue(newSelection.getEstado());
                }
            }
        );
    }
    
    private GridPane configurarPanelEstado() {
        GridPane panelEstado = new GridPane();
        panelEstado.setHgap(10);
        panelEstado.setVgap(10);
        panelEstado.setPadding(new Insets(10));
        
        Label lblEstado = new Label("Cambiar Estado:");
        
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll(
            "Programada", "En Proceso", "Completada", "Cancelada"
        );
        
        panelEstado.add(lblEstado, 0, 0);
        panelEstado.add(comboEstado, 1, 0);
        
        return panelEstado;
    }
    
    private HBox configurarBotones(Stage stage) {
        HBox botonesAccion = new HBox(10);
        botonesAccion.setAlignment(Pos.CENTER);
        
        Button btnGuardarCambios = new Button("Guardar Cambios");
        btnGuardarCambios.setOnAction(e -> guardarCambioEstado());
        
        Button btnVolver = new Button("Volver al Inicio");
        btnVolver.setOnAction(e -> {
            try {
                Inicio inicio = new Inicio();
                inicio.start(new Stage());
                stage.close();
            } catch (Exception ex) {
                mostrarAlerta("Error al volver al inicio: " + ex.getMessage());
            }
        });
        
        botonesAccion.getChildren().addAll(btnGuardarCambios, btnVolver);
        
        return botonesAccion;
    }
    
    private void cargarDatosCitas() {
        try {
            listaCitas = FXCollections.observableArrayList(citaDAO.obtenerTodos());
            tablaCitas.setItems(listaCitas);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar citas: " + e.getMessage());
        }
    }
    
    private void guardarCambioEstado() {
        Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        
        if (citaSeleccionada == null) {
            mostrarAlerta("Por favor, seleccione una cita.");
            return;
        }
        
        String nuevoEstado = comboEstado.getValue();
        
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            mostrarAlerta("Seleccione un estado válido.");
            return;
        }
        
        try {
            // Actualizar estado en la base de datos
            citaSeleccionada.setEstado(nuevoEstado);
            citaDAO.actualizar(citaSeleccionada);
            
            // Refrescar tabla
            cargarDatosCitas();
            
            mostrarMensaje("Estado de cita actualizado correctamente.");
        } catch (SQLException e) {
            mostrarAlerta("Error al actualizar estado: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
