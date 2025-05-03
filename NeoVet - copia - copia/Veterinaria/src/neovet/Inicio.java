package neovet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;

public class Inicio extends Application {
    
    private final Color NEOVET_BLUE = Color.rgb(31, 93, 167);
    private final String LOGO_PATH = "Veterinaria//logo.png";
    private Image logoImage;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NEOVET");
        primaryStage.setWidth(600);
        primaryStage.setHeight(500);
        primaryStage.centerOnScreen();
        
        loadLogoImage();
        
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: white;");
        
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(50, 0, 0, 0));
        contentBox.setStyle("-fx-background-color: white;");
        
        // Logo
        ImageView logoView = new ImageView();
        if (logoImage != null) {
            logoView.setImage(logoImage);
            logoView.setFitWidth(280);
            logoView.setFitHeight(100);
            logoView.setPreserveRatio(true);
        } else {
            System.err.println("¡ALERTA! Logo no cargado correctamente");
        }
        
        VBox.setMargin(logoView, new Insets(0, 0, 30, 0));
        
        // Botones específicos
        Button registrarCitaBtn = createStyledButton("Registrar Cita");
        Button gestionCitasBtn = createStyledButton("Gestión de Citas");
        Button veterinariosBtn = createStyledButton("Veterinarios");
        Button pagosBtn = createStyledButton("Pagos");
        
        // Configuración de eventos
        registrarCitaBtn.setOnAction(e -> {
            try {
                Registro registro = new Registro();
                Stage registroStage = new Stage();
                registro.start(registroStage);
                primaryStage.close(); // Cerrar ventana principal
            } catch (Exception ex) {
                showErrorAlert("Error al abrir Registro de Cita: " + ex.getMessage());
            }
        });
        
        gestionCitasBtn.setOnAction(e -> {
            try {
                GestionCitas gestionCitas = new GestionCitas();
                Stage gestionCitasStage = new Stage();
                gestionCitas.start(gestionCitasStage);
                primaryStage.close(); // Cerrar ventana principal
            } catch (Exception ex) {
                showErrorAlert("Error al abrir Gestión de Citas: " + ex.getMessage());
            }
        });
        
        veterinariosBtn.setOnAction(e -> {
            try {
                GestionVeterinarios gestionVeterinarios = new GestionVeterinarios();
                Stage veterinariosStage = new Stage();
                gestionVeterinarios.start(veterinariosStage);
                primaryStage.close(); // Cerrar ventana principal
            } catch (Exception ex) {
                showErrorAlert("Error al abrir Gestión de Veterinarios: " + ex.getMessage());
            }
        });
        
        pagosBtn.setOnAction(e -> {
            try {
                Pagos pagos = new Pagos();
                Stage pagosStage = new Stage();
                pagos.start(pagosStage);
                primaryStage.close(); // Cerrar ventana principal
            } catch (Exception ex) {
                showErrorAlert("Error al abrir Pagos: " + ex.getMessage());
            }
        });
        
        // Botón de salir
        Button salirBtn = createStyledButton("Salir");
        salirBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #1F5DA7; " +
            "-fx-border-color: #1F5DA7; " +
            "-fx-border-radius: 25; " +
            "-fx-background-radius: 25; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px;"
        );
        salirBtn.setPrefSize(100, 40);
        VBox.setMargin(salirBtn, new Insets(20, 0, 0, 0));
        
        salirBtn.setOnAction(e -> Platform.exit());
        
        // Agregar elementos al contenedor principal
        contentBox.getChildren().addAll(
            logoView, 
            registrarCitaBtn, 
            gestionCitasBtn, 
            veterinariosBtn,
            pagosBtn,
            salirBtn
        );
        
        mainPane.setCenter(contentBox);
        
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void loadLogoImage() {
        try {
            // Intentar cargar desde diferentes rutas
            String[] rutas = {
                LOGO_PATH,
                "src/main/resources/" + LOGO_PATH,
                "logo.png",
                "src/neovet/logo.png"
            };
            
            for (String ruta : rutas) {
                File logoFile = new File(ruta);
                if (logoFile.exists()) {
                    logoImage = new Image(logoFile.toURI().toString());
                    System.out.println("Logo cargado desde: " + logoFile.getAbsolutePath());
                    return;
                }
            }
            
            // Intentar cargar desde recursos de la clase
            URL resourceUrl = getClass().getResource("/logo.png");
            if (resourceUrl != null) {
                logoImage = new Image(resourceUrl.toString());
                System.out.println("Logo cargado desde recursos");
                return;
            }
            
            System.err.println("No se pudo encontrar el logo");
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(250, 50);
        button.setStyle(
            "-fx-background-color: #1F5DA7; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 25;"
        );
        return button;
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
