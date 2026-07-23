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

public class LoginController {
	@FXML 
	private Label testingLabel;
	@FXML
	private TextField usernameField;
	@FXML 
	private PasswordField passwordField;
	@FXML 
	private void initialize() {
		testingLabel.setText("");
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
}
