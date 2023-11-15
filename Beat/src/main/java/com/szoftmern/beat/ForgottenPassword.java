package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ForgottenPassword {
    @FXML
    private Pane passwordPanel;
    @FXML
    private TextField emailField;
    @FXML
    private Label emailSentText;

    @FXML
    public void newPasswordRequest(){
        emailSentText.setText("Email elküldve!");
    }
    @FXML
    protected void switchBackToLoginScene() {
        UIController.switchScene(passwordPanel, "login.fxml");
    }
}