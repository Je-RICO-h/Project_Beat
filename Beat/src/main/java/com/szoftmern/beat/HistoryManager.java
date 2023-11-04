package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.szoftmern.beat.DatabaseManager.getTrackFromTitle;

public class HistoryManager {

    private final MusicPlayer musicPlayer;

    public HistoryManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void displayhistory() {
        ObservableList<Track> result = FXCollections.observableArrayList(musicPlayer.musicHistory);

        Platform.runLater(() -> {
            musicPlayer.historylistContener.getChildren().clear();
            for(Track track:result) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("song.fxml"));
                HBox hBox = null;

                try {
                    hBox = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                SongController songController = fxmlLoader.getController();
                songController.SetData(track);

                hBox.setOnMouseClicked(mouseEvent -> {
                    System.out.println(track.getTitle());
                    String title = track.getTitle();
                    musicPlayer.pos = musicPlayer.musicList.indexOf(getTrackFromTitle(title)) - 1;
                    musicPlayer.next();
                    System.out.println("Kiválasztott elem: " + title);
                    musicPlayer.play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));
                });

                musicPlayer.historylistContener.getChildren().add(hBox);
            }
        });
    }
}
