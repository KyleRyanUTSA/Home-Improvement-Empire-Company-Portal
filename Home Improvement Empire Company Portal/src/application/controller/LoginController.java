package application.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import application.SceneManager;
import application.model.Credential;
import application.model.CredentialLoader;
import application.model.CredentialVerifier;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {
	@FXML 
	private Label testingLabel;
	@FXML
	private TextField usernameField;
	@FXML 
	private PasswordField passwordField;
	@FXML
	private ImageView logoImage;

	@FXML
    private void initialize() {
        testingLabel.setText("");

        var imageUrl = getClass().getResource(
                "/Data/images/home-improvement-logo-design-for-business-bulding-interior-and-exterior-vector.jpg"
        );

        if (imageUrl == null) {
            testingLabel.setText("Logo image could not be found.");
            System.err.println("Could not find login logo image.");
            return;
        }

        logoImage.setImage(new Image(imageUrl.toExternalForm()));
    }

	@FXML
	private void handleSignInAction(ActionEvent event) {
		if(CredentialVerifier.verifyCredential(new Credential(usernameField.getText(),passwordField.getText()))) {
			SceneManager.switchTo("/Data/views/MainView.fxml");
		}
		else {
			testingLabel.setText("Invalid Credentials, please try again");
		}
		
	}
	@FXML
	private void handleSignUpAction(ActionEvent event) {
		if(!(CredentialVerifier.verifyCredential(new Credential(usernameField.getText(),passwordField.getText())))) {
			CredentialLoader.saveCredentials(usernameField.getText(),passwordField.getText());
			testingLabel.setText("Sign Up successful");
		}
		else {
			testingLabel.setText("Sign Up failed, account already exists");
		}
	}
}
