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
import java.sql.SQLException;

public class GestionVeterinarios extends Application {
    private TextField txtNombre;
    private TextField txtEspecialidad;
    private TextField txtTelefono;
    private TextField txtEmail;
    private CheckBox chkActivo;
    private Button btnGuardar;
    private Button btnEliminar;
    private TableView<Veterinario> tablaVeterinarios;
    private VeterinarioDAO veterinarioDAO;
    private int idVeterinarioSeleccionado = -1;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Veterinarios");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // Inicializar DAO
        veterinarioDAO = new VeterinarioDAO();

        // Crear layout principal
        VBox layoutPrincipal = new VBox(10);
        layoutPrincipal.setPadding(new Insets(20));

        // Configurar tabla de veterinarios
        configurarTablaVeterinarios();

        // Formulario para agregar/editar veterinarios
        GridPane formGrid = configurarFormulario();

        // Botones de acción
        HBox botonesContainer = configurarBotones(primaryStage);

        // Agregar componentes al layout principal
        layoutPrincipal.getChildren().addAll(
            tablaVeterinarios, 
            formGrid, 
            botonesContainer
        );

        // Crear escena
        Scene scene = new Scene(layoutPrincipal);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Cargar veterinarios
        cargarVeterinarios();
    }

    private void configurarTablaVeterinarios() {
        tablaVeterinarios = new TableView<>();

        // Columna ID
        TableColumn<Veterinario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idVeterinario"));

        // Columna Nombre
        TableColumn<Veterinario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Columna Especialidad
        TableColumn<Veterinario, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        // Columna Teléfono
        TableColumn<Veterinario, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Columna Email
        TableColumn<Veterinario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Columna Activo
        TableColumn<Veterinario, Boolean> colActivo = new TableColumn<>("Activo");
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Configurar columnas
        tablaVeterinarios.getColumns().addAll(
            colId, colNombre, colEspecialidad, 
            colTelefono, colEmail, colActivo
        );

        // Evento de selección
        tablaVeterinarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Cargar los datos del veterinario seleccionado en los campos
                idVeterinarioSeleccionado = newSelection.getIdVeterinario();
                txtNombre.setText(newSelection.getNombre());
                txtEspecialidad.setText(newSelection.getEspecialidad());
                txtTelefono.setText(newSelection.getTelefono());
                txtEmail.setText(newSelection.getEmail());
                chkActivo.setSelected(newSelection.isActivo());
                
                // Cambiar el texto del botón
                btnGuardar.setText("Actualizar");
                btnEliminar.setDisable(false);
            }
        });
    }

    private GridPane configurarFormulario() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setAlignment(Pos.CENTER);

        Label lblTitulo = new Label("Datos del Veterinario");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblNombre = new Label("Nombre Completo:");
        txtNombre = new TextField();
        txtNombre.setPrefWidth(250);

        Label lblEspecialidad = new Label("Especialidad:");
        txtEspecialidad = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        txtTelefono = new TextField();

        Label lblEmail = new Label("Email:");
        txtEmail = new TextField();

        Label lblActivo = new Label("Activo:");
        chkActivo = new CheckBox();
        chkActivo.setSelected(true);

        // Añadir componentes al grid
        formGrid.add(lblTitulo, 0, 0, 2, 1);
        formGrid.add(lblNombre, 0, 1);
        formGrid.add(txtNombre, 1, 1);
        formGrid.add(lblEspecialidad, 0, 2);
        formGrid.add(txtEspecialidad, 1, 2);
        formGrid.add(lblTelefono, 0, 3);
        formGrid.add(txtTelefono, 1, 3);
        formGrid.add(lblEmail, 0, 4);
        formGrid.add(txtEmail, 1, 4);
        formGrid.add(lblActivo, 0, 5);
        formGrid.add(chkActivo, 1, 5);

        return formGrid;
    }

    private HBox configurarBotones(Stage stage) {
        HBox botonesContainer = new HBox(10);
        botonesContainer.setAlignment(Pos.CENTER);

        btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> guardarVeterinario());

        btnEliminar = new Button("Eliminar");
        btnEliminar.setDisable(true);
        btnEliminar.setOnAction(e -> eliminarVeterinario());

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setOnAction(e -> limpiarFormulario());

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close());

        botonesContainer.getChildren().addAll(
            btnGuardar, btnEliminar, btnLimpiar, btnCerrar
        );

        return botonesContainer;
    }

    private void guardarVeterinario() {
        // Validar campos obligatorios
        if (txtNombre.getText().isEmpty() || txtEspecialidad.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obligatorios", 
                "Nombre y Especialidad son campos obligatorios.");
            return;
        }

        try {
            Veterinario veterinario;
            
            // Si es una actualización, usar el ID existente
            if (idVeterinarioSeleccionado > 0) {
                veterinario = veterinarioDAO.obtenerPorId(idVeterinarioSeleccionado);
            } else {
                // Si es un nuevo registro, crear un nuevo objeto
                veterinario = new Veterinario();
            }

            // Establecer valores
            veterinario.setNombre(txtNombre.getText());
            veterinario.setEspecialidad(txtEspecialidad.getText());
            veterinario.setTelefono(txtTelefono.getText());
            veterinario.setEmail(txtEmail.getText());
            veterinario.setActivo(chkActivo.isSelected());
            
            int id = veterinarioDAO.guardar(veterinario);
            
            if (id > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", 
                    idVeterinarioSeleccionado > 0 ? "Veterinario actualizado correctamente." : "Veterinario guardado correctamente.");
                limpiarFormulario();
                cargarVeterinarios();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el veterinario.");
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al guardar el veterinario: " + e.getMessage());
        }
    }

    private void eliminarVeterinario() {
        if (idVeterinarioSeleccionado <= 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un veterinario", "Debe seleccionar un veterinario para eliminarlo.");
            return;
        }
        
        // Confirmar eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro que desea eliminar este veterinario? Esta acción no se puede deshacer.");
        
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean eliminado = veterinarioDAO.eliminar(idVeterinarioSeleccionado);
                    if (eliminado) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Veterinario eliminado correctamente.");
                        limpiarFormulario();
                        cargarVeterinarios();
                    } else {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el veterinario.");
                    }
                } catch (SQLException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al eliminar el veterinario: " + e.getMessage());
                }
            }
        });
    }

    private void cargarVeterinarios() {
        try {
            ObservableList<Veterinario> veterinarios = FXCollections.observableArrayList(
                veterinarioDAO.obtenerTodos()
            );
            tablaVeterinarios.setItems(veterinarios);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar los veterinarios: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtTelefono.clear();
        txtEmail.clear();
        chkActivo.setSelected(true);
        
        idVeterinarioSeleccionado = -1;
        btnGuardar.setText("Guardar");
        btnEliminar.setDisable(true);
        
        tablaVeterinarios.getSelectionModel().clearSelection();
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
