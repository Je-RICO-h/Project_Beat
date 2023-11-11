package com.szoftmern.beat;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Login {
    @FXML
    private Pane loginPanel;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void skipChecksAndLogin(Event event) {
        UIController.makeNewStage(event,"screen.fxml");
    }

    @FXML
    public void initialize() {
        // log the user in if they press ENTER while
        // the password field has focus
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    loginUser(ke);
                }
            }
        });

        // set the focus on the password field if the user
        // presses ENTER while the username field has focus
        UIController.setFocusOnEnterKeyPressed(usernameField, passwordField);
    }

    // Validate the given user info and try to log in the user
    @FXML
    protected void loginUser(Event event) {
        try {
            checkIfEveryInfoIsEntered();

            // save the currently logged-in user
            DatabaseManager.loggedInUser = EntityUtil.returnUserIfItExists(usernameField.getText());

            String username = DatabaseManager.loggedInUser.getName();
            UserInfoHelper.checkIfUserHasEnteredCorrectPassword(username, passwordField.getText());

        } catch (IncorrectInformationException e) {
            welcomeText.setText(e.getMessage());

            return;
        }

        // every info is correct, log the user in...
        welcomeText.setText("Bejelentkezés...");
        UIController.makeNewStage(event,"screen.fxml");

        System.out.println("User " + DatabaseManager.loggedInUser.getName() + " logged in successfully");
    }

    private void checkIfEveryInfoIsEntered() throws IncorrectInformationException{
        if ( usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ) {
            throw new IncorrectInformationException(UserInfoHelper.missingInfo);
        }
    }

    @FXML
    protected void switchToRegistrationScene() {
        UIController.switchScene(loginPanel, "registration.fxml");
    }

    @FXML
    protected void onForgottenPasswordButtonClicked() {
        UIController.switchScene(loginPanel, "forgottenPassword.fxml");
    }
}