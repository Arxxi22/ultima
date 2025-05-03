package neovet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.HashMap;

public class Registro extends Application {
    // Definir el color azul específico de NEOVET
    private final Color NEOVET_BLUE = Color.rgb(31, 93, 167);
    
    // Convertir el color a cadena hexadecimal para CSS
    private final String NEOVET_BLUE_HEX = String.format("#%02X%02X%02X",
        (int)(NEOVET_BLUE.getRed() * 255),
        (int)(NEOVET_BLUE.getGreen() * 255),
        (int)(NEOVET_BLUE.getBlue() * 255));
    
    // Mapas para almacenar todos los controles de los formularios
    private final HashMap<String, Control> datosMascotaControls = new HashMap<>();
    private final HashMap<String, Control> datosMedicosControls = new HashMap<>();
    private final HashMap<String, Control> datosDuenoControls = new HashMap<>();
    private final HashMap<String, Control> datosCitaControls = new HashMap<>();
    
    @Override
    public void start(Stage primaryStage) {
        // Configuración básica de la ventana
        primaryStage.setTitle("NEOVET - Registro de Mascotas");
        
        // Crear un TabPane para organizar los datos en pestañas
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Pestaña: Datos de la Mascota
        Tab tabMascota = new Tab("Datos de la Mascota");
        tabMascota.setContent(crearFormularioDatosMascota());
        
        // Pestaña: Datos Médicos
        Tab tabMedicos = new Tab("Datos Médicos");
        tabMedicos.setContent(crearFormularioDatosMedicos());
        
        // Pestaña: Datos del Dueño
        Tab tabDueno = new Tab("Datos del Dueño");
        tabDueno.setContent(crearFormularioDatosDueno());
        
        // Pestaña: Datos de la Cita
        Tab tabCita = new Tab("Datos de la Cita");
        tabCita.setContent(crearFormularioDatosCita());
        
        // Agregar las pestañas al TabPane
        tabPane.getTabs().addAll(tabMascota, tabMedicos, tabDueno, tabCita);
        
        // Panel principal que contiene el logo y el TabPane
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: white;");
        
        // Cargar imagen del logo
        ImageView logoImageView = loadLogoImage();
        
        if (logoImageView != null) {
            // Crear un contenedor para centrar el logo
            HBox logoContainer = new HBox();
            logoContainer.setAlignment(Pos.CENTER);
            logoContainer.getChildren().add(logoImageView);
            mainContainer.getChildren().add(logoContainer);
        }
        
        // Agregar el TabPane al contenedor principal
        mainContainer.getChildren().add(tabPane);
        
        // Crear botón para avanzar entre pestañas y botón final para guardar
        Button btnSiguiente = new Button("Siguiente");
        btnSiguiente.setStyle(
            "-fx-background-color: " + NEOVET_BLUE_HEX + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 25px;" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;"
        );
        btnSiguiente.setPrefSize(150, 40);
        
        Button btnAnterior = new Button("Anterior");
        btnAnterior.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: " + NEOVET_BLUE_HEX + ";" +
            "-fx-border-color: " + NEOVET_BLUE_HEX + ";" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 25px;" +
            "-fx-background-radius: 25px;" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;"
        );
        btnAnterior.setPrefSize(150, 40);
        
        Button btnGuardar = new Button("Guardar Registro");
        btnGuardar.setStyle(
            "-fx-background-color: green;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 25px;" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;"
        );
        btnGuardar.setPrefSize(180, 40);
        btnGuardar.setVisible(false); // Inicialmente oculto
        
        Button btnSalir = new Button("Salir");
        btnSalir.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: red;" +
            "-fx-border-color: red;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 25px;" +
            "-fx-background-radius: 25px;" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;"
        );
        btnSalir.setPrefSize(150, 40);
        btnSalir.setOnAction(e -> System.exit(0));
        
        // Crear un contenedor para los botones
        HBox botonesContainer = new HBox(20, btnAnterior, btnSiguiente, btnGuardar, btnSalir);
        botonesContainer.setAlignment(Pos.CENTER);
        botonesContainer.setPadding(new Insets(20, 0, 20, 0));
        
        // Agregar el contenedor de botones al contenedor principal
        mainContainer.getChildren().add(botonesContainer);
        
        // Lógica para navegar entre pestañas
        btnAnterior.setDisable(true); // Inicialmente deshabilitado
        
        // Manejar navegación de pestañas
        btnSiguiente.setOnAction(e -> {
            int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
            if (currentIndex < tabPane.getTabs().size() - 1) {
                tabPane.getSelectionModel().select(currentIndex + 1);
                btnAnterior.setDisable(false);
                
                // Si estamos en la última pestaña, cambiar a botón de guardar
                if (currentIndex + 1 == tabPane.getTabs().size() - 1) {
                    btnSiguiente.setVisible(false);
                    btnGuardar.setVisible(true);
                }
            }
        });
        
        btnAnterior.setOnAction(e -> {
            int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
            if (currentIndex > 0) {
                tabPane.getSelectionModel().select(currentIndex - 1);
                btnSiguiente.setVisible(true);
                btnGuardar.setVisible(false);
                
                if (currentIndex - 1 == 0) {
                    btnAnterior.setDisable(true);
                }
            }
        });
        
        // Actualizar botones cuando se cambia de pestaña manualmente
        tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            btnAnterior.setDisable(newValue.intValue() == 0);
            if (newValue.intValue() == tabPane.getTabs().size() - 1) {
                btnSiguiente.setVisible(false);
                btnGuardar.setVisible(true);
            } else {
                btnSiguiente.setVisible(true);
                btnGuardar.setVisible(false);
            }
        });
        
        // Acción para guardar todos los datos
        btnGuardar.setOnAction(e -> guardarTodosLosDatos());
        
        // Configurar la escena
        Scene scene = new Scene(mainContainer, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private ImageView loadLogoImage() {
        try {
            // Intenta cargar la imagen desde el mismo paquete
            Image logo = new Image(getClass().getResourceAsStream("logo.png"));
            ImageView imageView = new ImageView(logo);
            // Redimensionar manteniendo proporciones
            imageView.setFitWidth(300);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }
    
    private GridPane crearFormularioDatosMascota() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Etiquetas y campos para Datos de la Mascota
        Label lblNombre = new Label("Nombre de la mascota:");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Max");
        datosMascotaControls.put("nombre", txtNombre);
        
        // Campo para especificar otra especie
        TextField txtOtraEspecie = new TextField();
        txtOtraEspecie.setPromptText("Especifique la especie");
        txtOtraEspecie.setVisible(false); // Inicialmente oculto
        datosMascotaControls.put("otraEspecie", txtOtraEspecie);
        
        Label lblEspecie = new Label("Especie:");
        ComboBox<String> cmbEspecie = new ComboBox<>();
        cmbEspecie.getItems().addAll("Perro", "Gato", "Ave", "Conejo", "Otro");
        cmbEspecie.setPromptText("Seleccione especie");
        datosMascotaControls.put("especie", cmbEspecie);
        
        // Mostrar u ocultar el campo de texto para otra especie según la selección
        cmbEspecie.setOnAction(e -> {
            if (cmbEspecie.getValue() != null && cmbEspecie.getValue().equals("Otro")) {
                txtOtraEspecie.setVisible(true);
            } else {
                txtOtraEspecie.setVisible(false);
                txtOtraEspecie.clear();
            }
        });
        
        Label lblRaza = new Label("Raza:");
        TextField txtRaza = new TextField();
        txtRaza.setPromptText("Ej: Labrador, Siamés");
        datosMascotaControls.put("raza", txtRaza);
        
        Label lblFechaNacimiento = new Label("Fecha de nacimiento:");
        DatePicker dateFechaNacimiento = new DatePicker();
        datosMascotaControls.put("fechaNacimiento", dateFechaNacimiento);
        
        Label lblEdad = new Label("Edad:");
        TextField txtEdad = new TextField();
        txtEdad.setPromptText("Ej: 3 años");
        datosMascotaControls.put("edad", txtEdad);
        
        Label lblSexo = new Label("Sexo:");
        HBox sexoContainer = new HBox(15);
        ToggleGroup sexoGroup = new ToggleGroup();
        RadioButton rbMacho = new RadioButton("Macho");
        RadioButton rbHembra = new RadioButton("Hembra");
        rbMacho.setToggleGroup(sexoGroup);
        rbHembra.setToggleGroup(sexoGroup);
        sexoContainer.getChildren().addAll(rbMacho, rbHembra);
        datosMascotaControls.put("sexoMacho", rbMacho);
        datosMascotaControls.put("sexoHembra", rbHembra);
        
        Label lblColor = new Label("Color:");
        TextField txtColor = new TextField();
        txtColor.setPromptText("Ej: Negro y café");
        datosMascotaControls.put("color", txtColor);
        
        Label lblPeso = new Label("Peso (kg):");
        TextField txtPeso = new TextField();
        txtPeso.setPromptText("Ej: 12.5");
        datosMascotaControls.put("peso", txtPeso);
        
        Label lblTamano = new Label("Tamaño:");
        ComboBox<String> cmbTamano = new ComboBox<>();
        cmbTamano.getItems().addAll("Pequeño", "Mediano", "Grande");
        cmbTamano.setPromptText("Seleccione tamaño");
        datosMascotaControls.put("tamano", cmbTamano);
        
        // Añadir todos los campos al grid
        int row = 0;
        grid.add(lblNombre, 0, row);
        grid.add(txtNombre, 1, row++);
        
        grid.add(lblEspecie, 0, row);
        
        // Contenedor para el combobox y el campo de texto adicional
        VBox especieContainer = new VBox(5);
        especieContainer.getChildren().addAll(cmbEspecie, txtOtraEspecie);
        grid.add(especieContainer, 1, row++);
        
        grid.add(lblRaza, 0, row);
        grid.add(txtRaza, 1, row++);
        
        grid.add(lblFechaNacimiento, 0, row);
        grid.add(dateFechaNacimiento, 1, row++);
        
        grid.add(lblEdad, 0, row);
        grid.add(txtEdad, 1, row++);
        
        grid.add(lblSexo, 0, row);
        grid.add(sexoContainer, 1, row++);
        
        grid.add(lblColor, 0, row);
        grid.add(txtColor, 1, row++);
        
        grid.add(lblPeso, 0, row);
        grid.add(txtPeso, 1, row++);
        
        grid.add(lblTamano, 0, row);
        grid.add(cmbTamano, 1, row++);
        
        // Estilos para las etiquetas
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> 
            ((Label)node).setFont(Font.font("Arial", FontWeight.BOLD, 14))
        );
        
        return grid;
    }
    
    private GridPane crearFormularioDatosMedicos() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Etiquetas y campos para Datos Médicos
        Label lblTipoSangre = new Label("Tipo de sangre:");
        ComboBox<String> cmbTipoSangre = new ComboBox<>();
        cmbTipoSangre.getItems().addAll("DEA 1.1+", "DEA 1.1-", "DEA 1.2+", "DEA 1.2-", "Tipo A", "Tipo B", "Tipo AB");
        cmbTipoSangre.setPromptText("Seleccione tipo de sangre");
        datosMedicosControls.put("tipoSangre", cmbTipoSangre);
        
        Label lblAlergias = new Label("Alergias:");
        TextArea txtAlergias = new TextArea();
        txtAlergias.setPromptText("Ej: Penicilina");
        txtAlergias.setPrefRowCount(3);
        datosMedicosControls.put("alergias", txtAlergias);
        
        Label lblEnfermedades = new Label("Enfermedades crónicas:");
        TextField txtEnfermedades = new TextField();
        txtEnfermedades.setPromptText("Ej: Diabetes");
        datosMedicosControls.put("enfermedades", txtEnfermedades);
        
        Label lblVacunas = new Label("Vacunas al día:");
        CheckBox chkVacunas = new CheckBox("Sí");
        datosMedicosControls.put("vacunas", chkVacunas);
        
        Label lblUltimaVisita = new Label("Última visita:");
        DatePicker dateUltimaVisita = new DatePicker();
        datosMedicosControls.put("ultimaVisita", dateUltimaVisita);
        
        Label lblMicrochip = new Label("Microchip:");
        TextField txtMicrochip = new TextField();
        txtMicrochip.setPromptText("Ej: 123456789");
        datosMedicosControls.put("microchip", txtMicrochip);
        
        // Añadir todos los campos al grid
        int row = 0;
        grid.add(lblTipoSangre, 0, row);
        grid.add(cmbTipoSangre, 1, row++);
        
        grid.add(lblAlergias, 0, row);
        grid.add(txtAlergias, 1, row++);
        
        grid.add(lblEnfermedades, 0, row);
        grid.add(txtEnfermedades, 1, row++);
        
        grid.add(lblVacunas, 0, row);
        grid.add(chkVacunas, 1, row++);
        
        grid.add(lblUltimaVisita, 0, row);
        grid.add(dateUltimaVisita, 1, row++);
        
        grid.add(lblMicrochip, 0, row);
        grid.add(txtMicrochip, 1, row++);
        
        // Estilos para las etiquetas
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> 
            ((Label)node).setFont(Font.font("Arial", FontWeight.BOLD, 14))
        );
        
        return grid;
    }
    
    private GridPane crearFormularioDatosDueno() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Etiquetas y campos para Datos del Dueño
        Label lblNombreDueno = new Label("Nombre del dueño:");
        TextField txtNombreDueno = new TextField();
        txtNombreDueno.setPromptText("Ej: Maria Gonzalez");
        datosDuenoControls.put("nombreDueno", txtNombreDueno);
        
        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Ej: 312 2615381");
        datosDuenoControls.put("telefono", txtTelefono);
        
        Label lblEmail = new Label("Correo electrónico:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Ej: ejemplo@example.com");
        datosDuenoControls.put("email", txtEmail);
        
        Label lblDireccion = new Label("Dirección:");
        TextArea txtDireccion = new TextArea();
        txtDireccion.setPromptText("Ej: Calle Falsa 123");
        txtDireccion.setPrefRowCount(3);
        datosDuenoControls.put("direccion", txtDireccion);
        
        Label lblRFC = new Label("RFC (para facturación):");
        TextField txtRFC = new TextField();
        txtRFC.setPromptText("Ej: GOMJ800101");
        datosDuenoControls.put("rfc", txtRFC);
        
        // Añadir todos los campos al grid
        int row = 0;
        grid.add(lblNombreDueno, 0, row);
        grid.add(txtNombreDueno, 1, row++);
        
        grid.add(lblTelefono, 0, row);
        grid.add(txtTelefono, 1, row++);
        
        grid.add(lblEmail, 0, row);
        grid.add(txtEmail, 1, row++);
        
        grid.add(lblDireccion, 0, row);
        grid.add(txtDireccion, 1, row++);
        
        grid.add(lblRFC, 0, row);
        grid.add(txtRFC, 1, row++);
        
        // Estilos para las etiquetas
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> 
            ((Label)node).setFont(Font.font("Arial", FontWeight.BOLD, 14))
        );
        
        return grid;
    }
    
    private GridPane crearFormularioDatosCita() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Etiquetas y campos para Datos de la Cita
        Label lblMotivo = new Label("Motivo de la consulta:");
        ComboBox<String> cmbMotivo = new ComboBox<>();
        cmbMotivo.getItems().addAll("Urgencia", "Control", "Cirugía", "Vacunación", "Desparasitación");
        cmbMotivo.setPromptText("Seleccione motivo");
        datosCitaControls.put("motivo", cmbMotivo);
        
        Label lblEstadoCita = new Label("Estado de la cita:");
        ComboBox<String> cmbEstadoCita = new ComboBox<>();
        cmbEstadoCita.getItems().addAll("Por tener", "En proceso", "Terminada", "Cancelada");
        cmbEstadoCita.setPromptText("Seleccione estado");
        cmbEstadoCita.setValue("Por tener"); // Valor por defecto
        datosCitaControls.put("estadoCita", cmbEstadoCita);
        
        Label lblSintomas = new Label("Síntomas:");
        TextArea txtSintomas = new TextArea();
        txtSintomas.setPromptText("Ej: Vómito, diarrea");
        txtSintomas.setPrefRowCount(3);
        datosCitaControls.put("sintomas", txtSintomas);
        
        Label lblMedicamentos = new Label("Medicamentos actuales:");
        TextField txtMedicamentos = new TextField();
        txtMedicamentos.setPromptText("Ej: Omeprazol");
        datosCitaControls.put("medicamentos", txtMedicamentos);
        
        Label lblVeterinario = new Label("Veterinario asignado:");
        ComboBox<String> cmbVeterinario = new ComboBox<>();
        cmbVeterinario.getItems().addAll("Dr. Pérez", "Dra. González", "Dr. Rodríguez");
        cmbVeterinario.setPromptText("Seleccione veterinario");
        datosCitaControls.put("veterinario", cmbVeterinario);
        
        Label lblFechaHora = new Label("Fecha y hora de la cita:");
        HBox fechaHoraContainer = new HBox(10);
        DatePicker dateFechaCita = new DatePicker();
        ComboBox<String> cmbHoraCita = new ComboBox<>();
        cmbHoraCita.getItems().addAll(
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00"
        );
        cmbHoraCita.setPromptText("Hora");
        fechaHoraContainer.getChildren().addAll(dateFechaCita, cmbHoraCita);
        datosCitaControls.put("fechaCita", dateFechaCita);
        datosCitaControls.put("horaCita", cmbHoraCita);
        
        Label lblValorServicio = new Label("Valor del servicio ($):");
        TextField txtValorServicio = new TextField();
        txtValorServicio.setPromptText("Ej: 500.00");
        datosCitaControls.put("valorServicio", txtValorServicio);
        
        Label lblDescripcionServicio = new Label("Descripción del servicio:");
        TextArea txtDescripcionServicio = new TextArea();
        txtDescripcionServicio.setPromptText("Ej: Consulta general con análisis de sangre");
        txtDescripcionServicio.setPrefRowCount(3);
        datosCitaControls.put("descripcionServicio", txtDescripcionServicio);
        
        // Añadir todos los campos al grid
        int row = 0;
        grid.add(lblMotivo, 0, row);
        grid.add(cmbMotivo, 1, row++);
        
        grid.add(lblEstadoCita, 0, row);
        grid.add(cmbEstadoCita, 1, row++);
        
        grid.add(lblSintomas, 0, row);
        grid.add(txtSintomas, 1, row++);
        
        grid.add(lblMedicamentos, 0, row);
        grid.add(txtMedicamentos, 1, row++);
        
        grid.add(lblVeterinario, 0, row);
        grid.add(cmbVeterinario, 1, row++);
        
        grid.add(lblFechaHora, 0, row);
        grid.add(fechaHoraContainer, 1, row++);
        
        grid.add(lblValorServicio, 0, row);
        grid.add(txtValorServicio, 1, row++);
        
        grid.add(lblDescripcionServicio, 0, row);
        grid.add(txtDescripcionServicio, 1, row++);
        
        // Estilos para las etiquetas
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> 
            ((Label)node).setFont(Font.font("Arial", FontWeight.BOLD, 14))
        );
        
        return grid;
    }
    
    private void guardarTodosLosDatos() {
        // Validar al menos algunos campos obligatorios
        TextField nombreMascota = (TextField) datosMascotaControls.get("nombre");
        TextField nombreDueno = (TextField) datosDuenoControls.get("nombreDueno");
        
        if (nombreMascota.getText().isEmpty() || nombreDueno.getText().isEmpty()) {
            mostrarAlerta("Error", "Debe completar al menos el nombre de la mascota y del dueño.");
            return;
        }
        
        // Aquí iría la lógica para guardar todos los datos de los formularios
        // Por ahora, solo mostramos una alerta de éxito
        
        // Construir un resumen de los datos para mostrar
        StringBuilder resumen = new StringBuilder();
        resumen.append("DATOS DE LA MASCOTA:\n");
        resumen.append("Nombre: ").append(nombreMascota.getText()).append("\n");
        
        ComboBox<String> especie = (ComboBox<String>) datosMascotaControls.get("especie");
        if (especie.getValue() != null) {
            if (especie.getValue().equals("Otro")) {
                TextField otraEspecie = (TextField) datosMascotaControls.get("otraEspecie");
                resumen.append("Especie: ").append(otraEspecie.getText()).append("\n");
            } else {
                resumen.append("Especie: ").append(especie.getValue()).append("\n");
            }
        }
        
        // Incluir datos del dueño
        resumen.append("\nDATOS DEL DUEÑO:\n");
        resumen.append("Nombre: ").append(nombreDueno.getText()).append("\n");
        
        TextField telefono = (TextField) datosDuenoControls.get("telefono");
        if (!telefono.getText().isEmpty()) {
            resumen.append("Teléfono: ").append(telefono.getText()).append("\n");
        }
        
        // Incluir datos de la cita
        DatePicker fechaCita = (DatePicker) datosCitaControls.get("fechaCita");
        ComboBox<String> horaCita = (ComboBox<String>) datosCitaControls.get("horaCita");
        
        if (fechaCita.getValue() != null && horaCita.getValue() != null) {
            resumen.append("\nCITA PROGRAMADA:\n");
            resumen.append("Fecha: ").append(fechaCita.getValue()).append("\n");
            resumen.append("Hora: ").append(horaCita.getValue()).append("\n");
        }
        
        // Mostrar el resumen de datos guardados
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Registro Guardado");
        alerta.setHeaderText("El registro se ha guardado correctamente");
        alerta.setContentText(resumen.toString());
        alerta.showAndWait();
        
        // Aquí iría el código para guardar en base de datos, archivo, etc.
        System.out.println("Guardando datos en el sistema...");
        
        // Limpiar formularios después de guardar
        limpiarFormularios();
    }
    
    private void limpiarFormularios() {
        // Limpiar todos los campos de los formularios
        datosMascotaControls.values().forEach(control -> {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                ((ComboBox<?>) control).setValue(null);
            } else if (control instanceof DatePicker) {
                ((DatePicker) control).setValue(null);
            } else if (control instanceof TextArea) {
                ((TextArea) control).clear();
            } else if (control instanceof CheckBox) {
                ((CheckBox) control).setSelected(false);
            } else if (control instanceof RadioButton) {
                ((RadioButton) control).setSelected(false);
            }
        });
        
        datosMedicosControls.values().forEach(control -> {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                ((ComboBox<?>) control).setValue(null);
            } else if (control instanceof DatePicker) {
                ((DatePicker) control).setValue(null);
            } else if (control instanceof TextArea) {
                ((TextArea) control).clear();
            } else if (control instanceof CheckBox) {
                ((CheckBox) control).setSelected(false);
            }
        });
        
        datosDuenoControls.values().forEach(control -> {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                ((ComboBox<?>) control).setValue(null);
            } else if (control instanceof DatePicker) {
                ((DatePicker) control).setValue(null);
            } else if (control instanceof TextArea) {
                ((TextArea) control).clear();
            }
        });
        
        datosCitaControls.values().forEach(control -> {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                if (control == datosCitaControls.get("estadoCita")) {
                    ((ComboBox<String>) control).setValue("Por tener"); // Valor por defecto
                } else {
                    ((ComboBox<?>) control).setValue(null);
                }
            } else if (control instanceof DatePicker) {
                ((DatePicker) control).setValue(null);
            } else if (control instanceof TextArea) {
                ((TextArea) control).clear();
            }
        });
        
        // Volver a la primera pestaña
        TabPane tabPane = (TabPane) ((VBox) datosMascotaControls.get("nombre").getParent().getParent().getParent()).getChildren().get(1);
        tabPane.getSelectionModel().select(0);
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}