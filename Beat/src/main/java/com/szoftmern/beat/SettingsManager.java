package com.szoftmern.beat;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class SettingsManager {
    private final User currentUser = DatabaseManager.loggedInUser;
    private int countryIdx = (int)currentUser.getCountryId();
    private boolean wasBadWordsButtonToggled = false;
    private Button saveButton;
    private TextField usernameField;
    private TextField emailField;
    private TextField oldPasswordField;
    private TextField newPasswordField;
    private TextField newPasswordConfirmationField;
    private ComboBox<String> genderPicker;
    private ComboBox<String> countryPicker;
    private Button badWordsToggleButton;
    private Label resetInfoLabel;

    public SettingsManager(
            Button saveButton, TextField usernameField,
            TextField emailField, TextField oldPasswordField,
            TextField newPasswordField, TextField newPasswordConfirmationField,
            ComboBox<String> genderPicker, ComboBox<String> countryPicker,
            Button badWordsToggle, Label resetInfoLabel
    ) {
        this.saveButton = saveButton;

        this.usernameField = usernameField;
        this.emailField = emailField;

        this.oldPasswordField = oldPasswordField;
        this.newPasswordField = newPasswordField;
        this.newPasswordConfirmationField = newPasswordConfirmationField;

        this.genderPicker = genderPicker;
        this.countryPicker = countryPicker;

        this.badWordsToggleButton = badWordsToggle;
        this.resetInfoLabel = resetInfoLabel;
    }

    public void displayCurrentAccountInfo() {
        usernameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());

        countryPicker.getSelectionModel().select(countryIdx - 1);

        genderPicker.getSelectionModel().select(currentUser.getGender());
        badWordsToggleButton.setText(
                currentUser.isFilteringExplicitLyrics() ? "Be" : "Ki"
        );
    }

    // If the user has any unsaved settings and those changes are valid,
    // it saves the updated User entity to the database
    public void uploadNewUserAccountInfoToDatabase() throws IncorrectInformationException {
        if (hasAnyAccountInfoBeenChanged() && canUpdateUserInfo()) {
            // save updated user entity to the database
            DatabaseManager.userDAO.saveEntity(currentUser);

            oldPasswordField.clear();
            newPasswordField.clear();
            newPasswordConfirmationField.clear();
            wasBadWordsButtonToggled = false;
            countryIdx = (int)currentUser.getCountryId();

            System.out.println("User " + currentUser.getName() + "'s info has been successfully updated.\n");
            System.out.println(currentUser);
        }
    }

    private boolean hasAnyAccountInfoBeenChanged() throws IncorrectInformationException {
        if (usernameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            throw new IncorrectInformationException("A felhasználónév és email mező nem lehet üres!\n");
        }

        byte selectedGender = UserInfoHelper.getSelectedGender(genderPicker);

        System.out.println(DatabaseManager.getEveryCountry().size());
        String selectedCountry = UserInfoHelper.getSelectedCountry(countryPicker);
        String userCountry = DatabaseManager.getEveryCountry().get(countryIdx - 1 > 169 ? 169 : countryIdx - 1).getName();

        if ( currentUser.getName().equals(usernameField.getText()) &&
             currentUser.getEmail().equals(emailField.getText()) &&
             currentUser.getGender() == selectedGender &&
             userCountry.equals(selectedCountry) &&
             oldPasswordField.getText().isEmpty() &&
             newPasswordField.getText().isEmpty() &&
             newPasswordConfirmationField.getText().isEmpty() &&
             !wasBadWordsButtonToggled
        ) {
            return false;
        }

        return true;
    }

    private boolean canUpdateUserInfo() throws IncorrectInformationException {
        boolean canUpdate = false;

        String username     = usernameField.getText();
        String email        = emailField.getText();
        String oldPass      = oldPasswordField.getText();
        String newPass      = newPasswordField.getText();
        String newPassAgain = newPasswordConfirmationField.getText();


        // set new username and email for the user if they are valid
        if (!currentUser.getName().equals(username)) {
            currentUser.setName(UserInfoHelper.validateUsername(username));
            canUpdate = true;
        }

        if (!currentUser.getEmail().equals(email)) {
            currentUser.setEmail(UserInfoHelper.validateEmail(email));
            canUpdate = true;
        }


        // set new gender and country for the user if they are valid
        byte selectedGender = UserInfoHelper.getSelectedGender(genderPicker);
        if (currentUser.getGender() != selectedGender) {
            currentUser.setGender(selectedGender);
            canUpdate = true;
        }

        int selectedCountryId = (int)DatabaseManager.getCountryIdFromName(
                UserInfoHelper.getSelectedCountry(countryPicker));


        if (selectedCountryId != countryIdx) {
            DatabaseManager.loggedInUser.setCountryId(selectedCountryId);
            canUpdate = true;
        }

        if (wasBadWordsButtonToggled) {
            // toggle the user's filtering preferences
            currentUser.setFilteringExplicitLyrics(!currentUser.isFilteringExplicitLyrics());
            resetInfoLabel.setText("A változtatás újbóli belépés után fog életbe lépni!");

            canUpdate = true;
        }


        // only do any password related checks if the user has entered something
        // in all the password fields
        if ( !oldPass.isEmpty() &&
             !newPass.isEmpty() &&
             !newPassAgain.isEmpty()
        ) {
            UserInfoHelper.checkIfUserHasEnteredCorrectPassword(currentUser.getName(), oldPass);

            if (newPass.equals(oldPass)) {
                throw new IncorrectInformationException("Az új jelszó nem lehet ugyanaz, mint a régi!\n");
            }

            // validate the newly entered passwords
            UserInfoHelper.validatePassword(newPass, newPassAgain);

            // generate a hash from the new password
            byte[] newPasswordHash = UserInfoHelper.generatePasswordHashForUser(newPass);
            currentUser.setPassHash(newPasswordHash);

            canUpdate = true;
        }

        return canUpdate;
    }


    public void toggleBadWordsFlag() {
        wasBadWordsButtonToggled = !wasBadWordsButtonToggled;

        // xnor logic, think it through
        if (currentUser.isFilteringExplicitLyrics() == wasBadWordsButtonToggled) {
            badWordsToggleButton.setText("Ki");
        } else {
            badWordsToggleButton.setText("Be");
        }
    }
}
