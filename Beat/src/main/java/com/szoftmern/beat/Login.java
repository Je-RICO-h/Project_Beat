package com.szoftmern.beat;

<<<<<<< Updated upstream
=======
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
>>>>>>> Stashed changes
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
<<<<<<< Updated upstream
=======
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.szoftmern.beat.UIController.*;
>>>>>>> Stashed changes

import java.io.IOException;

public class Login {
    @FXML
    private Pane loginPanel;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
<<<<<<< Updated upstream
    protected void onLoginButtonClick() throws IOException {
        if(username.getText().toString().equals("username") && password.getText().toString().equals("password")) {
            welcomeText.setText("Bejelentkezés...");
            new SceneSwitch(loginPanel, "screen.fxml");
=======
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
>>>>>>> Stashed changes

        }
        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            welcomeText.setText("Add meg az adataid!");
        }
        else {
            welcomeText.setText("Hibás felhasználónév vagy jelszó!");
        }
    }
    @FXML
<<<<<<< Updated upstream
    protected void onRegistButtonClick() throws IOException {
        new SceneSwitch(loginPanel, "registration.fxml");
=======
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
>>>>>>> Stashed changes
    }

    @FXML
    protected void onPasswordButtonClick() throws IOException {
        welcomeText.setText("Elfelejtett jelszó.");
    }
}