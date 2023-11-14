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
    private Label emailSentText;

    @FXML
    public void requestNewPasswordForUser() {
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

        System.out.println(userRequestingPassChange);
        emailSentText.setText("Email elküldve!");
    }

    @FXML
    protected void switchBackToLoginScene() {
        UIController.switchScene(passwordPanel, "login.fxml");
    }
}