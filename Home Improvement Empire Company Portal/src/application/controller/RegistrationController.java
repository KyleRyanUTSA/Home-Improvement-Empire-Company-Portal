package application.controller;

import java.net.URL;

import application.SceneManager;
import application.model.Credential;
import application.model.CredentialLoader;
import application.model.CredentialVerifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class RegistrationController {
	
	@FXML 
	private Label errorLabel;
	@FXML
	private TextField usernameField;
	@FXML 
	private PasswordField passwordField;
	@FXML 
	private PasswordField confirmPasswordField;
	@FXML 
	private TextField addressField;
	@FXML
	private TextField phoneField;
	
	@FXML
    private void initialize() {
        errorLabel.setText("");
    }
	
	@FXML
	private void handleSignUpAction(ActionEvent event) {
		
		if(usernameField.getText().equals("")) {
			errorLabel.setText("Invalid Username");
		}else if(passwordField.getText().equals("")) {
			errorLabel.setText("Invalid Password");
		}else if (addressField.getText().equals("")) {
			errorLabel.setText("Invalid Address");
		}else if(!passwordField.getText().equals(confirmPasswordField.getText())) {
			errorLabel.setText("Password and Confirm password do not match");
			passwordField.setText("");
			confirmPasswordField.setText("");
		}else if(CredentialVerifier.userExists(usernameField.getText())) {
			errorLabel.setText("User Already Exists");
		}else if((phoneField.getText().length() != 10 || !phoneField.getText().matches("\\d+")) && !phoneField.getText().equals("")) {
			errorLabel.setText("Invalid Phone number");
			
		}
		
			else {
			if(phoneField.getText().equals("")) {
				CredentialLoader.saveCredentials(new Credential(usernameField.getText(),passwordField.getText(),addressField.getText()));
			}else {
				CredentialLoader.saveCredentials(new Credential(usernameField.getText(),passwordField.getText(),addressField.getText(),phoneField.getText()));
			}
			
			
			SceneManager.switchTo("/Data/views/HIELS.fxml");
		}
		
		
	}
	@FXML
	private void backToSignUpAction(ActionEvent event) {
		SceneManager.switchTo("/Data/views/HIELS.fxml");
	}
	
	
}
