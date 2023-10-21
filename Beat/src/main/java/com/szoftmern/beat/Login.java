package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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
    protected void onLoginButtonClick() throws IOException {
        if(username.getText().toString().equals("username") && password.getText().toString().equals("password")) {
            welcomeText.setText("Bejelentkezés...");
            new SceneSwitch(loginPanel, "screen.fxml");
        }
        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            welcomeText.setText("Add meg az adataid!");
        }
        else {
            welcomeText.setText("Hibás felhasználónév vagy jelszó!");
        }
    }
    @FXML
    protected void onRegistButtonClick() throws IOException {
        new SceneSwitch(loginPanel, "registration.fxml");
    }

    @FXML
    protected void onPasswordButtonClick() throws IOException {
        welcomeText.setText("Elfelejtett jelszó.");
    }
}