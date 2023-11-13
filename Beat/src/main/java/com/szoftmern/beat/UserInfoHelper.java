package com.szoftmern.beat;

import javafx.scene.control.ComboBox;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoHelper {
    public static final String passwordInfo =
            "A jelszónak minimum 8 karakterből " +
            "\nkell állnia, tartalmaznia kell " +
            "\nkisbetűt, nagybetűt, és számot! " +
            "\nEzen kívül csak a következő speciális " +
            "\nkaraktereket tartalmazhatja: " +
            "\n!#$%&@+_-\n";

    public static final String missingInfo = "Nem adtál meg minden adatot!\n";

    public static String validateUsername(String username) throws IncorrectInformationException{
        // if user already exists, bail
        if (EntityUtil.doesUserAlreadyExist(username)) {
            throw new IncorrectInformationException("Ez a felhasználónév már foglalt.\n");
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
            throw new IncorrectInformationException("A megadott felhasználónév helytelen!\n");
        }

        return username;
    }

    public static String validateEmail(String emailAddress) throws IncorrectInformationException {
        // if the email is not valid, bail
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new IncorrectInformationException("A megadott email cím helytelen!\n");
        }

        return emailAddress;
    }

    public static String validatePassword(String pass, String passAgain) throws IncorrectInformationException {
        if (!pass.equals(passAgain)) {
            throw new IncorrectInformationException("A jelszavak nem egyeznek!\n");
        }

        // regex pattern for validating the password
        Pattern passPattern = Pattern.compile(
                "^(?=\\S*[0-9])" +         // at least one digit
                        "(?=\\S*[a-z])" +          // at least one lowercase character
                        "(?=\\S*[A-Z])" +          // at least one uppercase character
                        "[a-zA-Z0-9!#$%&@+_-]" +   // optionally allow ONLY these special characters: ! # $ % & @ + _ -
                        "{8,30}$"                  // length is between 8 and 30 characters
        );
        Matcher matcher = passPattern.matcher(pass);

        // if the password is not valid, bail
        if (!matcher.matches()) {
            throw new IncorrectInformationException(UserInfoHelper.passwordInfo);
        }

        return pass;
    }

    public static void checkIfUserHasEnteredCorrectPassword(String username, String enteredPass) throws IncorrectInformationException {
        String passHashOfValidUser = DatabaseManager.getPassHashFromUsername(username);

        // compares the entered password to the one in the db
        if ( !BCrypt.checkpw(enteredPass, passHashOfValidUser)) {
            throw new IncorrectInformationException("Hibás jelszó!");
        }
    }

    // Generates the bcrypt hash for the entered password
    public static byte[] generatePasswordHashForUser(String plainPassword) {
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return hash.getBytes();
    }

    public static byte getSelectedGender(ComboBox<String> genderPicker) {
        return (byte)genderPicker.getSelectionModel().getSelectedIndex();
    }

    public static String getSelectedCountry(ComboBox<String> countryPicker) {
        return countryPicker.getValue();
    }
}
