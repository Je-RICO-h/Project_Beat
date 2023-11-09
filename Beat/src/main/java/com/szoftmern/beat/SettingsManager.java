package com.szoftmern.beat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class SettingsManager {

    public static void originalTexts(TextField username, TextField email, TextField password, ComboBox<String> country, ComboBox<String> gender){
        //set the origin data from database
        username.setPromptText("Eredeti felhnév");
        email.setPromptText("Eredeti email");
        password.setPromptText("Eredeti jelszo");
        country.setPromptText("Ország");
        gender.setPromptText("Nem");
    }

    public static void saveData(TextField username, TextField email, TextField password, ComboBox<String> country, ComboBox<String> gender){
        //set the new data to database
        String new_username = username.getText();
        String new_email = email.getText();
        String new_password = password.getText();
        String new_country = country.getValue();
        String new_gender = gender.getValue();

        System.out.println(new_username+new_email+new_password+new_country+new_gender);

    }

    public static void setColorPickerBox(ComboBox<String> colors){

        ObservableList<String> color = FXCollections.observableArrayList();
        color.add("linear-gradient(to top,#E3B1EF, #86E09A)");
        color.add("linear-gradient(to top,#E3B1EF, #9b86e0)");
        color.add("linear-gradient(to top,#E3B1EF, #86E09A)");
        color.add("linear-gradient(to top,#E3B1EF, #86E09A)");
        colors.setItems(color);


    }


}
