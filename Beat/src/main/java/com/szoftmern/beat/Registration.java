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
    private TextField passwordField;
    @FXML
    private TextField passwordAgainField;
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
    protected void showPasswordRequirements() {
        info.setText(UserInfoHelper.passwordInfo);
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

        try {
            checkIfEveryInfoIsEntered();

            username = UserInfoHelper.validateUsername(usernameField.getText());
            email = UserInfoHelper.validateEmail(emailField.getText());
            pass = UserInfoHelper.validatePassword(passwordField.getText(), passwordAgainField.getText());

        } catch (IncorrectInformationException e) {
            info.setText(e.getMessage());

            return null;
        }

        byte gender = UserInfoHelper.getSelectedGender(genderPicker);
        Date dateOfBirth = Date.valueOf(getDateOfBirth());

        String countryName = UserInfoHelper.getSelectedCountry(countryPicker);
        Country country = DatabaseManager.getCountryFromName(countryName);

        byte[] passwordHash = UserInfoHelper.generatePasswordHashForUser(pass);
        Date currentDate = Date.valueOf(LocalDate.now());

        return new User(username, email, passwordHash, gender, dateOfBirth, country, currentDate);
    }

    // Checks whether the user has entered every piece of info
    private void checkIfEveryInfoIsEntered() throws IncorrectInformationException{
        if ( emailField.getText().isEmpty() ||
             usernameField.getText().isEmpty() ||
             passwordField.getText().isEmpty() ||
             passwordAgainField.getText().isEmpty() ||
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
}