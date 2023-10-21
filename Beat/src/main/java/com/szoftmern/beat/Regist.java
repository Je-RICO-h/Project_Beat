package com.szoftmern.beat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.time.LocalDate;
import java.io.IOException;

public class Regist {
    @FXML
    private Pane registPanel;
    @FXML
    private Label passwordInfo;
    @FXML
    private TextField password;
    @FXML
    private TextField passwordAgain;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ChoiceBox<String> genderPicker;
    @FXML
    private ChoiceBox<String> nationalityPicker;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        new SceneSwitch(registPanel, "login.fxml");
    }

    @FXML
    protected void PasswordInfo() {
        passwordInfo.setText("A jelszónak minimum 8 karakterből \nkell állnia, tartalmaznia kell \nkisbetűt, nagybetűt, és számot!");
    }

    public void getDate(ActionEvent event) {
        LocalDate birthDate = birthDatePicker.getValue();
    }

    @FXML
    protected void tovabb() {
        String pwd = password.getText().toString();
        String pwd2 = passwordAgain.getText().toString();

        if(password.getText().isEmpty() || passwordAgain.getText().isEmpty() || username.getText().isEmpty() || email.getText().isEmpty())  {
            passwordInfo.setText("Add meg az adataid!");
        }
        else if (pwd.equals(pwd2)) {
            boolean lower = false;
            boolean upper = false;
            boolean digit = false;
            for (char c : pwd.toCharArray()) {
                if (Character.isDigit(c)) { digit = true; }
                if (Character.isLowerCase(c)) { lower = true; }
                if (Character.isUpperCase(c)) { upper = true; }
            }
            if (digit && lower && upper && pwd.length() >= 8) {
                passwordInfo.setText("Regisztráció...");
            }
            else {
                passwordInfo.setText("A jelszónak minimum 8 karakterből \nkell állnia, tartalmaznia kell \nkisbetűt, nagybetűt, és számot!");
            }
        }
        else if (!(pwd.equals(pwd2))) {
            passwordInfo.setText("A jelszó nem egyezik!");
        }
    }
}