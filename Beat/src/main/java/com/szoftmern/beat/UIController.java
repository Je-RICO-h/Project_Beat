package com.szoftmern.beat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;



import static com.szoftmern.beat.DatabaseManager.*;

public class UIController {
    private static HBox hBox;

    public static void switchScene(Pane currentPane, String fxml) {
        try {
            Pane nextPane = FXMLLoader.load(Main.class.getResource(fxml));

            currentPane.getChildren().removeAll();
            currentPane.getChildren().setAll(nextPane);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setHBoxOnMouseClicked(HBox hBox, Track track, MusicPlayer musicPlayer) {
        hBox.setOnMouseClicked(mouseEvent -> {
            System.out.println(track.getTitle());

            String title = track.getTitle();

            musicPlayer.pos = musicPlayer.musicList.indexOf(getTrackFromTitle(title)) - 1;

            musicPlayer.next();

            System.out.println("Kiválasztott elem: " + title);

            musicPlayer.play_pause.setImage(new Image(Objects.requireNonNull(UIController.class.getResourceAsStream("img/pause.png"))));
        });
    }

    public static HBox loadAndSetHBox(Track track, MusicPlayer musicPlayer) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UIController.class.getResource("song.fxml"));

        try {
            hBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SongController songController = fxmlLoader.getController();
        songController.SetData(track);

        setHBoxOnMouseClicked(hBox, track, musicPlayer);

        return hBox;
    }

    public static void loadCountriesIntoCombobox(ComboBox<String> countryPicker) {
        Path countryListPath = Paths.get("src/main/resources/com/szoftmern/beat/countries.txt");
        ObservableList<String> countries = FXCollections.observableArrayList();

        try {
            List<String> lines = Files.readAllLines(countryListPath);
            countries.addAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        countryPicker.setItems(countries);
    }

}
