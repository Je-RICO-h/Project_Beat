package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

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
    protected void switchBackToLoginScene() {
        SceneSwitcher.switchScene(registrationPanel, "login.fxml");
    }

    @FXML
    protected void showPasswordRequirements() {
        info.setText("A jelszónak minimum 8 karakterből \nkell állnia, tartalmaznia kell \nkisbetűt, nagybetűt, és számot!");
    }

    // Saves the new user's info to the database
    @FXML
    protected void registerUser() {
        User newUser = prepareUserForRegistration();

        // if the user gave valid info...
        if (newUser != null) {
            DatabaseManager.userDAO.saveEntity(newUser);

            System.out.println("New user successfully added to the database");
            SceneSwitcher.switchScene(registrationPanel, "login.fxml");
        }
    }

    // Validates all the information given by the user.
    // If the user gave valid info it returns with a new User class,
    // otherwise it returns with null.
    private User prepareUserForRegistration() {
        String username, email, pass;

        try {
            checkIfEveryInfoIsEntered();

            username = getAndValidateUsername();
            email = getAndValidateEmail();
            pass = getAndValidatePassword();

        } catch (IncorrectInformationException e) {
            info.setText(e.getMessage());

            return null;
        }

        byte gender = getSelectedGender();
        Date dateOfBirth = Date.valueOf(getDateOfBirth());
        String country = getSelectedCountry();

        byte[] passwordHash = generatePasswordHashForUser(pass);
        Date currentDate = Date.valueOf(LocalDate.now());

        return new User(username, email, passwordHash, gender, dateOfBirth, country, currentDate);
    }

    // Generates the bcrypt hash for the user's password
    private byte[] generatePasswordHashForUser(String plainPassword) {
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return hash.getBytes();
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
            throw new IncorrectInformationException("Nem adtál meg minden adatot!");
        }
    }

    private String getAndValidateUsername() throws IncorrectInformationException{
        String username = usernameField.getText();

        // if user already exists, bail
        if (doesUserAlreadyExist(username)) {
            throw new IncorrectInformationException("Ez a felhasználónév már foglalt.");
        }

        // regex for validating the username
        Pattern userPattern = Pattern.compile(
                "^(?![-_.])" +        // no -, _ or . at the start
                "(?!.*[-_.]{2})" +    // no combination of -, _ or . should repeat
                "[a-zA-Z0-9._-]" +    // allowed characters
                "{5,20}" +            // length is between 5 and 20 characters
                "(?<![-_.])$"         // no -, _ or . at the end either
        );
        Matcher matcher = userPattern.matcher(username);

        // if the username is invalid, bail
        if (!matcher.matches()) {
            throw new IncorrectInformationException("A megadott felhasználónév helytelen!");
        }

        return username;
    }

    private boolean doesUserAlreadyExist(String username) {
        for (User user : DatabaseManager.userDAO.getEntities()) {
            if (user.getName().equals(username)) {
                return true;
            }
        }

        return false;
    }

    private String getAndValidateEmail() throws IncorrectInformationException {
        String emailAddress = emailField.getText();

        // if the email is not valid, bail
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new IncorrectInformationException("A megadott email cím helytelen!");
        }

        return emailAddress;
    }

    private String getAndValidatePassword() throws IncorrectInformationException {
        String pass = passwordField.getText();
        String passAgain = passwordAgainField.getText();

        if (!pass.equals(passAgain)) {
            throw new IncorrectInformationException("A jelszavak nem egyeznek!");
        }

        // regex pattern for validating the password
        Pattern passPattern = Pattern.compile(
                "^(?=\\S*[0-9])" +    // at least one digit
                "(?=\\S*[a-z])" +     // at least one lowercase character
                "(?=\\S*[A-Z])" +     // at least one uppercase character
                "\\S{8,30}$"          // length is between 8 and 30 characters
        );
        Matcher matcher = passPattern.matcher(pass);

        // if the password is not valid, bail
        if (!matcher.matches()) {
            throw new IncorrectInformationException("A jelszónak minimum 8 karakterből \nkell állnia, tartalmaznia kell \nkisbetűt, nagybetűt, és számot!");
        }

        return pass;
    }

    private byte getSelectedGender() {
        return (byte)genderPicker.getSelectionModel().getSelectedIndex();
    }

    public LocalDate getDateOfBirth() {
        return birthDatePicker.getValue();
    }

    public String getSelectedCountry() {
        return countryPicker.getValue();
    }
}