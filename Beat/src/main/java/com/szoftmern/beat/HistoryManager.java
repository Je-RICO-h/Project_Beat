package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.szoftmern.beat.UIController.*;

public class HistoryManager {
    private final MusicPlayer musicPlayer;

    public HistoryManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void displayhistory() {
        ObservableList<Track> result = FXCollections.observableArrayList(musicPlayer.musicHistory);

        Platform.runLater(() -> {
            musicPlayer.historylistContener.getChildren().clear();

            for(Track track : result) {

                HBox hBox = loadAndSetHBox(track, musicPlayer);

                musicPlayer.historylistContener.getChildren().add(hBox);
            }
        });
    }
}
