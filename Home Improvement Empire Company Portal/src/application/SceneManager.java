package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.getScene().setRoot(root);

            if (fxmlPath.endsWith("MainView.fxml")) {
                primaryStage.setWidth(1200);
                primaryStage.setHeight(750);
                primaryStage.centerOnScreen();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}