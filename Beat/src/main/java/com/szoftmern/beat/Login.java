package com.szoftmern.beat;

import javafx.event.ActionEvent;
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
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.szoftmern.beat.UIController.*;

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
    private TextField visiblePassword;
    @FXML
    private Label showPasswordButton;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // log the user in if they press ENTER while
        // the password field has focus
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        loginUser(ke);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        // log the user in if they press ENTER while
        // the password field has focus AND password is visible
        visiblePassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        loginUser(ke);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // set the focus on the password field if the user
        // presses ENTER while the username field has focus
        UIController.setFocusOnEnterKeyPressed(usernameField, passwordField);
    }

    // Validate the given user info and try to log in the user
    @FXML
    protected void loginUser(Event event) throws IOException {
        try {
            checkIfEveryInfoIsEntered();

            String username = returnUserIfItExists();
            validatePassword(username);

        } catch (IncorrectInformationException e) {
            welcomeText.setText(e.getMessage());

            return;
        }

        // every info is correct, log the user in...
        welcomeText.setText("Bejelentkezés...");
        UIController.makeNewStage(event,"screen.fxml");
    }

    private void checkIfEveryInfoIsEntered() throws IncorrectInformationException{
        if ( usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ) {
            throw new IncorrectInformationException("Add meg az adataid!");
        }
    }

    // Returns the username if it exists, otherwise it throws an exception
    private String returnUserIfItExists() throws IncorrectInformationException{
        String username = usernameField.getText();

        // return the username if it exists
        for (User user : DatabaseManager.userDAO.getEntities()) {
            if (user.getName().equals(username)) {
                return username;
            }
        }

        // if the user doesn't exist...
        throw new IncorrectInformationException("Ez a felhasználó nem létezik!");
    }

    private void validatePassword(String username) throws IncorrectInformationException{
        String enteredPass = passwordField.getText();
        String passHashOfValidUser = "";

        // get the password hash for the user trying to log in
        for (User user : DatabaseManager.userDAO.getEntities()) {
            if (user.getName().equals(username)) {
                passHashOfValidUser = new String(user.getPassHash(), StandardCharsets.UTF_8);
            }
        }

        // compares the entered password to the one in the db
        if ( !BCrypt.checkpw(enteredPass, passHashOfValidUser)) {
            throw new IncorrectInformationException("Hibás jelszó!");
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

    // shows or hides password
    @FXML
    protected void showPassword(){
        UIController.showAndHidePassword(passwordField, visiblePassword, showPasswordButton);
        loginButton.requestFocus();
    }

    // when password is visible sets passwordField text
    @FXML
    protected void setPasswordText() {
        if (visiblePassword.isVisible()){
            passwordField.setText(visiblePassword.getText());
        }
    }
}