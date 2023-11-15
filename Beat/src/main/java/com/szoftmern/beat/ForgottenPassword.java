package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ForgottenPassword {
    @FXML
    private Pane passwordPanel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private Label emailSentText;
    @FXML
    private Pane newPasswordPane1;
    @FXML
    private Pane newPasswordPane2;
    @FXML
    private TextField password1;
    @FXML
    private TextField password2;
    @FXML
    private Label newPasswordInfo;

    public static void setPane(Pane first, Pane second){
        //set one pane visible the other disable
        first.setDisable(false);
        first.setVisible(true);

        second.setDisable(true);
        second.setVisible(false);
    }

    @FXML
    public void requestNewPasswordForUser() {
        if (emailSentText.getText().equals("Email elküldve!")) {
            // pane váltása, a helyes kód beírása után kellene
            setPane(newPasswordPane2, newPasswordPane1);
        }
/*
        String email = emailField.getText();
        User userRequestingPassChange = null;

         try {
            userRequestingPassChange = EntityUtil.findUserWithEmail(email);

         } catch (IncorrectInformationException e) {
             emailSentText.setText(e.getMessage());

             return;
         }

        // send password recovery email to user
        MailingService.sendPlainTextEmail(
            MailingService.USERNAME,
            email,
           "Does it work?",
            """
                <h1 style='font-family: cursive'>Fancy text, eh?</h1>
            """,
            true
        );

        System.out.println(userRequestingPassChange);*/
        emailSentText.setText("Email elküldve!");
        codeField.setDisable(false);
        emailField.setDisable(true);
    }
    @FXML
    public void savePassword() {
        newPasswordInfo.setText("Új jelszó mentve!");
    }

    @FXML
    protected void switchBackToLoginScene() {
        UIController.switchScene(passwordPanel, "login.fxml");
    }
}