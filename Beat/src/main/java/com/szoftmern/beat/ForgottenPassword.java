package com.szoftmern.beat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ForgottenPassword {
    @FXML
    private Pane forgottenPasswordPane;
    @FXML
    private Pane emailAndCodeConfirmationPane;
    @FXML
    private Pane passwordUpdatePane;
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private Label emailSentText;

    @FXML
    private TextField newPassField;
    @FXML
    private TextField newPassAgainField;
    @FXML
    private Label newPasswordInfo;

    private User userRequestingPassChange;
    private boolean doesUserExist = false;
    private int confirmationCode;

    @FXML
    public void initialize()
    {
        emailField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    requestNewPasswordForUser();
                }
            }
        });

        codeField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    requestNewPasswordForUser();
                }
            }
        });

        // force the code field to be numeric only
        codeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue
            ) {
                // numeric chars only
                if ( !newValue.matches("\\d*") ) {
                    codeField.setText(newValue.replaceAll("\\D", ""));
                }

                // restrict the entered text's length to 6 digits
                if (newValue.length() > 6) {
                    codeField.setText(oldValue);
                }
            }
        });


        UIController.setFocusOnEnterKeyPressed(newPassField, newPassAgainField);

        newPassAgainField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    savePassword();
                }
            }
        });
    }

    @FXML
    public void requestNewPasswordForUser() {
        String email = emailField.getText();

        // the logic here dictates what the Tovább button does
        if ( !doesUserExist ) {
            try {
                // first we validate the email
                UserInfoHelper.validateEmail(email);

            } catch (IncorrectInformationException e) {
                emailSentText.setText(e.getMessage());
                return;
            }

            doesUserExist = doesUserWithGivenEmailExist(email);

            if (doesUserExist) {
                sendOutConfirmationEmail(email);

                emailSentText.setText("Email elküldve!");

                codeField.setDisable(false);
                emailField.setDisable(true);
            }

        codeField.requestFocus();
        } else {
            if ( !isConfirmationCodeCorrect() ) {
                emailSentText.setText("A megadott kód helytelen!");
                return;
            }

            // we need this in case the user presses the Vissza button,
            // then after entering another correct code comes back to
            // the password update pane
            newPassField.clear();
            newPassAgainField.clear();

            // if the use has entered the correct confirmation code
            // they can now change their password
            UIController.setMiddlePain2(passwordUpdatePane, emailAndCodeConfirmationPane);

            doesUserExist = false;
        }
    }

    // Checks whether the user has entered the correct confirmation code
    // sent to them via email.
    private boolean isConfirmationCodeCorrect() {
        return codeField.getText().equals(confirmationCode + "");
    }

    // Checks if there's a user associated with the given email
    private boolean doesUserWithGivenEmailExist(String email) {
        try {
            userRequestingPassChange = EntityUtil.findUserWithEmail(email);

        } catch (IncorrectInformationException e) {
            emailSentText.setText(e.getMessage());
            return false;
        }

        return true;
    }

    // Sends out a confirmation email to the given email address with
    // a 6-digit randomly generated number.
    private void sendOutConfirmationEmail(String email) {
        // generate a random confirmation code between 100000 and 999999
        confirmationCode = new Random().nextInt(900000) + 100000;

        // send password recovery email to user with the confirmation code
        MailingService.sendEmail(
                MailingService.USERNAME,
                email,
                "Confirmation code for new password request",
                """     
                   Here is your 6-digit confirmation code:
                """ + confirmationCode,
                true
        );

        System.out.println(userRequestingPassChange);
    }

    @FXML
    protected void switchBackToLoginScene() {
        UIController.switchScene(forgottenPasswordPane, "login.fxml");
    }

    @FXML
    public void switchBackToConfirmationPane() {
        UIController.setMiddlePain2(emailAndCodeConfirmationPane, passwordUpdatePane);

        // reset pane back to its original state
        emailSentText.setText("");
        emailField.clear();
        codeField.clear();
        codeField.setDisable(true);
        emailField.setDisable(false);
        doesUserExist = false;
    }

    @FXML
    public void savePassword() {
        String newPass = newPassField.getText();

        if ( !arePasswordInputsValid(newPass, newPassAgainField.getText()) ) {
            return;
        }

        if ( BCrypt.checkpw(
                newPass,
                new String(userRequestingPassChange.getPassHash(), StandardCharsets.UTF_8))
        ) {
            newPasswordInfo.setText("Az új jelszó nem lehet ugyanaz, mint a régi!");
            return;
        }

        // set new password hash for the user
        byte[] newPassHash = UserInfoHelper.generatePasswordHashForUser(newPass);
        userRequestingPassChange.setPassHash(newPassHash);

        // save the updated user entity to the database
        DatabaseManager.userDAO.saveEntity(userRequestingPassChange);

        switchBackToLoginScene();
    }

    private boolean arePasswordInputsValid(String pass, String passAgain) {
        if (pass.isEmpty() || passAgain.isEmpty()) {
            newPasswordInfo.setText("Tölts ki minden mezőt!");
            return false;
        }

        try {
            UserInfoHelper.validatePassword(pass, passAgain);

        } catch (IncorrectInformationException e) {
            newPasswordInfo.setText(e.getMessage());
            return false;
        }

        return true;
    }
}