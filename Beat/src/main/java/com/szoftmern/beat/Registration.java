package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.time.LocalDate;

public class Registration {
    @FXML
    private Pane registrationPanel;
    @FXML
    private Label info;
    @FXML
    private Label info1;
    @FXML
    private TextField passwordField;
    @FXML
    private PasswordField nonvisiblePasswordField;
    @FXML
    private TextField passwordAgainField;
    @FXML
    private PasswordField nonvisiblePasswordField2;
    @FXML
    private Label showPassword1;
    @FXML
    private Label showPassword2;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ComboBox<String> genderPicker;
    @FXML
    private ComboBox<String> countryPicker;
    @FXML
    private Pane captchaPane;
    @FXML
    private TextField sumTextField;
    @FXML
    private Label labelWrongSum;
    @FXML
    private Label labelRightSum;
    @FXML
    private Label labelNumbers;
    @FXML
    private CheckBox iAmNotARobot;

    private boolean isCaptchaCorrect = false;

    @FXML
    public void initialize() {
        UIController.loadCountriesIntoCombobox(countryPicker);

        // set triggers for changing the focus when the user
        // presses the ENTER key
        UIController.setFocusOnEnterKeyPressed(usernameField, emailField);
        UIController.setFocusOnEnterKeyPressed(emailField, passwordField);
        UIController.setFocusOnEnterKeyPressed(passwordField, passwordAgainField);
        UIController.setFocusOnEnterKeyPressed(passwordAgainField, birthDatePicker);
        UIController.setFocusOnEnterKeyPressed(birthDatePicker, genderPicker);
        UIController.setFocusOnEnterKeyPressed(genderPicker, countryPicker);
    }

    @FXML
    protected void switchBackToLoginScene() {
        UIController.switchScene(registrationPanel, "login.fxml");
    }

    @FXML
    protected void showInfo() {
        info1.setVisible(true);
    }
    @FXML
    protected void hideInfo() {
        info1.setVisible(false);
    }

    // when the checkbox is checked, shows captcha panel
    @FXML
    protected void iAmNotARobot() {
        captchaPane.setVisible(true);
        labelWrongSum.setVisible(false);

        sumTextField.setText("");

        Captcha.setMathProblem(labelNumbers);
        iAmNotARobot.setDisable(true);
    }

    // checks the entered sum
    @FXML
    protected void isCaptchaCorrect() {
        this.isCaptchaCorrect = sumTextField.getText().equals(Captcha.getSum(labelNumbers));

        if (!this.isCaptchaCorrect) {
            labelWrongSum.setVisible(true);
        }
        else {
            captchaPane.setVisible(false);
            labelRightSum.setVisible(true);
            iAmNotARobot.setDisable(true);
        }
    }
    @FXML
    protected void hideCaptchaPane() {
        if (!labelRightSum.isVisible()) {
            captchaPane.setVisible(false);
            iAmNotARobot.setSelected(false);
            iAmNotARobot.setDisable(false);
        }
    }

    // Saves the new user's info to the database
    @FXML
    protected void registerUser() {
        User newUser = prepareUserForRegistration();

        // if the user gave valid info...
        if (newUser != null) {
            DatabaseManager.userDAO.saveEntity(newUser);

            System.out.println("New user successfully added to the database");
            UIController.switchScene(registrationPanel, "login.fxml");
        }
    }

    // Validates all the information given by the user.
    // If the user gave valid info it returns with a new User class,
    // otherwise it returns with null.
    private User prepareUserForRegistration() {
        String username, email, pass;



        System.out.println(passwordField.getText()+ " "+passwordAgainField.getText()
                +" " +nonvisiblePasswordField.getText()+ " "+nonvisiblePasswordField2.getText());

        try {
            checkIfEveryInfoIsEntered();

            if (!this.isCaptchaCorrect) {
                throw new IncorrectInformationException("Rossz captcha válasz");
            }

            String p1 = passwordField.getText();
            if (nonvisiblePasswordField.isVisible()) {
                p1 = nonvisiblePasswordField.getText();
            }

            String p2 = passwordAgainField.getText();
            if (nonvisiblePasswordField2.isVisible()) {
                p2 = nonvisiblePasswordField2.getText();
            }

            username = UserInfoHelper.validateUsername(usernameField.getText());
            email = UserInfoHelper.validateEmail(emailField.getText());
            pass = UserInfoHelper.validatePassword(p1, p2);

        } catch (IncorrectInformationException e) {
            info.setText(e.getMessage());

            return null;
        }

        byte gender = UserInfoHelper.getSelectedGender(genderPicker);
        Date dateOfBirth = Date.valueOf(getDateOfBirth());

        String countryName = UserInfoHelper.getSelectedCountry(countryPicker);
        long countryId = DatabaseManager.getCountryIdFromName(countryName);

        byte[] passwordHash = UserInfoHelper.generatePasswordHashForUser(pass);
        Date currentDate = UserInfoHelper.getCurrentDate();

        return new User(username, email, passwordHash, gender, dateOfBirth, countryId, currentDate, true);
    }

    // Checks whether the user has entered every piece of info
    private void checkIfEveryInfoIsEntered() throws IncorrectInformationException{
        if ( emailField.getText().isEmpty() ||
             usernameField.getText().isEmpty() ||
             nonvisiblePasswordField.getText().isEmpty() ||
             nonvisiblePasswordField2.getText().isEmpty() ||
             birthDatePicker.getValue() == null ||
             genderPicker.getValue() == null ||
             countryPicker.getValue() == null
        ) {
            throw new IncorrectInformationException(UserInfoHelper.missingInfo);
        }
    }

    public LocalDate getDateOfBirth() {
        return birthDatePicker.getValue();
    }


    // show/hide password, passing the entered text to textfield/passwordfield
    @FXML
    protected void showPassword1(){
        UIController.showAndHidePassword(nonvisiblePasswordField, passwordField, showPassword1);
    }
    @FXML
    protected void setPasswordText1() {
        if (nonvisiblePasswordField.isVisible()){
            passwordField.setText(nonvisiblePasswordField.getText());
        }
    }
    @FXML
    protected void showPassword2(){
        UIController.showAndHidePassword(nonvisiblePasswordField2, passwordAgainField, showPassword2);
    }
    @FXML
    protected void setPasswordText2() {
        if (nonvisiblePasswordField2.isVisible()){
            passwordAgainField.setText(nonvisiblePasswordField2.getText());
        }
    }
}