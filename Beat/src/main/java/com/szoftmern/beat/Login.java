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

        // set the focus on the password field if the user
        // presses ENTER while the username field has focus
        UIController.setFocusOnEnterKeyPressed(usernameField, passwordField);
    }

    // Validate the given user info and try to log in the user
    @FXML
    protected void loginUser(Event event) throws IOException {
        User user;

        try {
            checkIfEveryInfoIsEntered();
            user = EntityUtil.returnUserIfItExists(usernameField.getText());

            if (user.isLoggedIn()) {
                System.out.println("user already logged in");
                throw new IncorrectInformationException("Máshol már be vagy jelentkezve!");
            }

            String username = user.getName();
            UserInfoHelper.checkIfUserHasEnteredCorrectPassword(username, passwordField.getText());

        } catch (IncorrectInformationException e) {
            welcomeText.setText(e.getMessage());

            return;
        }

        // save the currently logged-in user
        DatabaseManager.loggedInUser = user;

        // every info is correct, log the user in...
        DatabaseManager.loggedInUser.setLoggedIn(true);
        DatabaseManager.userDAO.saveEntity(DatabaseManager.loggedInUser);

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