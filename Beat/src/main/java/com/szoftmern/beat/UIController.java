package com.szoftmern.beat;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    // Fills a ComboBox with the countries listed in countries.txt
    public static void loadCountriesIntoCombobox(ComboBox<String> countryPicker) {
        Path countryListPath = Paths.get("src/main/resources/com/szoftmern/beat/countries.txt");
        List<String> countries = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(countryListPath);

            countries.addAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        countryPicker.getItems().addAll(countries);
    }

    // Sets the focus from one Control to another when the ENTER
    // key is pressed. The key event handler is set on the sender.
    public static void setFocusOnEnterKeyPressed(Control sender, Control receiver) {
        sender.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    receiver.requestFocus();
                }
            }
        });
    }
  
    public static void makeNewStage(Event event, String file) throws IOException {
        //new stage to make screen.fxml responsive
        FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource(file));
        Parent root1 = fxmlLoader.load();
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();

        //automatically close the other fxml
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.close();

        //If window is closed, do cleanup
        stage2.setOnCloseRequest(windowevent -> {
            System.out.println("App is closing");
            stage2.close();
            System.exit(0);
        });
    }

    public static void setMiddlePain(Pane first,Pane other1,Pane other2,Pane other3){
        //set only the first parameter valid and disable the other
        first.setDisable(false);
        first.setVisible(true);

        other1.setDisable(true);
        other1.setVisible(false);
        other2.setDisable(true);
        other2.setVisible(false);
        other3.setDisable(true);
        other3.setVisible(false);
    }
}
