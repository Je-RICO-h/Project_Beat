package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Login {
    @FXML
    private Pane loginPanel;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    // Validate the given user info and try to log in the user
    @FXML
    protected void loginUser() {
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
        SceneSwitcher.switchScene(loginPanel, "screen.fxml");
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
    protected void switchToRegistrationScene(){
        SceneSwitcher.switchScene(loginPanel, "registration.fxml");
    }

    @FXML
    protected void onForgottenPasswordButtonClicked() {
        welcomeText.setText("Elfelejtett jelszó.");
    }
}