package com.szoftmern.beat;
import javafx.animation.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.szoftmern.beat.DatabaseManager.*;

public class UIController {
    private static HBox hBox;
    public static Timeline timeline;

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
            musicPlayer.movingItem.setImage(new Image(Objects.requireNonNull(UIController.class.getResourceAsStream("img/giphy.gif"))));
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


    public static AnchorPane loadAndSetArtist(String artistname, MusicPlayer musicPlayer) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UIController.class.getResource("artist.fxml"));

        AnchorPane anchorPane;
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SongController songController = fxmlLoader.getController();
        songController.SetArtistToItem(artistname);

        anchorPane.setOnMouseClicked(mouseEvent -> {
            System.out.println(artistname);
            musicPlayer.oneArtistName.setText(artistname);
            musicPlayer.oneArtistSongs.getChildren().clear();
            setMiddlePain(musicPlayer.oneArtistbox,musicPlayer.homebox, musicPlayer.settingsbox, musicPlayer.artistbox, musicPlayer.favouritebox, musicPlayer.statisticbox,musicPlayer.playlistbox,musicPlayer.onePlaylistbox);

            for (Track track : Objects.requireNonNull(getTracksFromArtist(artistname))) {
                hBox = loadAndSetHBox(track, musicPlayer);

                musicPlayer.oneArtistSongs.getChildren().add(hBox);
            }
        });

        return anchorPane;
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

    // Shows or hides password
    // changes password field to text field to show password
    public static void showAndHidePassword(PasswordField password, TextField visiblepassword, Label text){
        if (password.isVisible()) {
            password.setVisible(false);
            visiblepassword.setVisible(true);
            visiblepassword.setText(password.getText());
            text.setText("hide");
        }
        else {
            visiblepassword.setVisible(false);
            password.setVisible(true);
            password.setText(visiblepassword.getText());
            text.setText("show");
        }
    }


    public static void makeNewStage(Event event, String file) {
        //new stage to make screen.fxml responsive
        FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource(file));
        Parent root1 = null;

        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.setMinWidth(1300);
        stage2.setMinHeight(750);
        stage2.show();

        //automatically close the other fxml
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.close();

        //If window is closed, do cleanup
        UIController.setOnCloseRequestForStage(stage2);
    }

    public static void setMiddlePain2(Pane first, Pane... others) {

        first.setDisable(false);
        first.setVisible(true);

        for (Pane other : others) {
            other.setDisable(true);
            other.setVisible(false);

        }
    }
    public static void setMiddlePain(AnchorPane first, AnchorPane... others) {

        first.setDisable(false);
        first.setVisible(true);

        for (Pane other : others) {
            other.setDisable(true);
            other.setVisible(false);
        }
    }


    public static void setOnCloseRequestForStage(Stage stage) {
        //If window is closed, do cleanup
        stage.setOnCloseRequest(windowevent -> {

            EntityUtil.updateDatabaseTrackPlayCount();

            System.out.println("App is closing");

            Main.manager.close();
            stage.close();
            System.exit(0);
        });
    }


    public static void movingLabel(Label newsFeedText) {
        stopTimeLine();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                event -> scrollText(newsFeedText)
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void stopTimeLine() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    
    public static void scrollText(Label label) {
        // Get the current text and create a new text with shifted characters
        String currentText =label.getText();
        String shiftedText = currentText.substring(1) + currentText.charAt(0);

        // Update the label with the shifted text
        label.setText(shiftedText);
    }


    public static void settingLikeButton(MusicPlayer musicPlayer, boolean isLiked)  {
        if (isLiked) {
            musicPlayer.heart.setImage(new Image(Objects.requireNonNull(UIController.class.getResourceAsStream("img/heart1.png"))));
        } else {
            musicPlayer.heart.setImage(new Image(Objects.requireNonNull(UIController.class.getResourceAsStream("img/heart2.png"))));
        }
    }


    public static BarChart<String, Number> createBarChar(Map<Object, Object> dataMap, String title, String xAxisLabel, String yAxisLabel) {
        XYChart.Series dataSeries = new XYChart.Series();

        for (Object name : dataMap.keySet()) {
            dataSeries.getData().add(new XYChart.Data(name, dataMap.get(name)));
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle(title);
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);

        barChart.setMinWidth(1000);
        barChart.setMinHeight(500);

        barChart.getData().add(dataSeries);

        barChart.setLegendVisible(false);
        xAxis.setTickLabelRotation(20);

        barChart.setBarGap(5);
        barChart.setCategoryGap(30);

        return barChart;
    }

    public static void setOnCloseRequestForStage(Stage stage) {
        //If window is closed, do cleanup
        stage.setOnCloseRequest(windowevent -> {
            EntityUtil.updateDatabaseTrackPlayCount();
            System.out.println("App is closing");
            Main.manager.close();
            stage.close();
            System.exit(0);
        });
    }
}
