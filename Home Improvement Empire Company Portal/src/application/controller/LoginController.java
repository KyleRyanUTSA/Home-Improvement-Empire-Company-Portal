package application.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;

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
        testingLabel.setLayoutX(175);
		testingLabel.setLayoutY(370);

        URL imageUrl = getClass().getResource("/image/logo.jpg");
        System.out.println(imageUrl);

        if (imageUrl == null) {
            testingLabel.setText("Logo image could not be found.");
            System.err.println("Could not find login logo image.");
            return;
        }

        logoImage.setImage(new Image(imageUrl.toExternalForm()));
    }

	@FXML
	private void handleSignInAction(ActionEvent event) {
		Credential cred = CredentialVerifier.verifyCredential(usernameField.getText(),passwordField.getText());
		if(cred != null) {
			SceneManager.switchTo("/Data/views/MainView.fxml");
		}
		else {
			testingLabel.setText("Invalid Credentials, please try again");
			testingLabel.setLayoutX(200);
		}
		
	}
	@FXML
	private void handleSignUpAction(ActionEvent event) {
		SceneManager.switchTo("/Data/views/Registration.fxml");
	}
}
