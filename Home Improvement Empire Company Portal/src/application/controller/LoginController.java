package application.controller;

import application.SceneManager;
import application.model.Credential;
import application.model.CredentialVerifier;
import application.model.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

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

        URL imageUrl = getClass().getResource(
                "/image/logo.png"
        );

        System.out.println(imageUrl);

        if (imageUrl == null) {
            testingLabel.setText(
                    "Logo image could not be found."
            );

            System.err.println(
                    "Could not find login logo image."
            );

            return;
        }

        logoImage.setImage(
                new Image(imageUrl.toExternalForm())
        );
        logoImage.setLayoutX(250);
    }

    @FXML
    private void handleSignInAction(ActionEvent event) {
        String username = usernameField
                .getText()
                .trim();

        String password = passwordField.getText();

        Credential cred =
                CredentialVerifier.verifyCredential(
                        username,
                        password
                );

        if (cred != null) {
            UserSession.setLoggedInUser(cred);

            SceneManager.switchTo(
                    "/Data/views/MainView.fxml"
            );
        } else {
            testingLabel.setText(
                    "Invalid Credentials, please try again"
            );

            testingLabel.setLayoutX(200);
        }
    }

    @FXML
    private void handleSignUpAction(ActionEvent event) {
        SceneManager.switchTo(
                "/Data/views/Registration.fxml"
        );
    }
}