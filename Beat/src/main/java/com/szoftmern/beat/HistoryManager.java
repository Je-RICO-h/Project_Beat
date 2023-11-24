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
    private HBox hBox;

    public HistoryManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void addTrackToHistoryList(Track track) {
        if (musicPlayer.musicHistory.size() <= 20) {
            musicPlayer.musicHistory.remove(track);
            musicPlayer.musicHistory.add(track);
        } else {
            musicPlayer.musicHistory.remove(0);
            musicPlayer.musicHistory.remove(track);
            musicPlayer.musicHistory.add(track);
        }
    }

    public void displayhistory() {
        ObservableList<Track> result = FXCollections.observableArrayList(musicPlayer.musicHistory);

        // Ha nem kommentelem ki, kivételt dob egyes esetekben. Azért mert
        // ugyanazt a JDBC sessions-t két külön szálról piszkálják egyidőben.
        //Platform.runLater(() -> {
            musicPlayer.historylistContener.getChildren().clear();

            for (int i = result.size() - 1; i >= 0 ; i--) {
                hBox = loadAndSetHBox(result.get(i), musicPlayer);

                musicPlayer.historylistContener.getChildren().add(hBox);
            }
        //});
    }
}
