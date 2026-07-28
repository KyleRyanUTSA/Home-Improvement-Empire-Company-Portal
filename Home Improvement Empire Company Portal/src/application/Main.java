package application;
import application.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import application.model.ProductDatabaseInitializer;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ProductDatabaseInitializer.initializeProducts();

            SceneManager.setPrimaryStage(primaryStage);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/Data/views/HIELS.fxml"
                    )
            );

            Parent root = loader.load();

            primaryStage.setScene(
                    new Scene(root)
            );

            primaryStage.show();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
