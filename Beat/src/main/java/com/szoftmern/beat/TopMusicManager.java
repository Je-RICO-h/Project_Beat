package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.UIController.*;

public class TopMusicManager {
    private final MusicPlayer musicPlayer;
    public TopMusicManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void updateTopList() {
        ObservableList<Track> top = FXCollections.observableArrayList(getTopMusicList());

        Platform.runLater(() -> {
            int counter = 1;

            for(Track track : top) {
                String img = "img/numbers/"  + String.valueOf(counter) + ".png";

                HBox hBox1 = new HBox();

                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(img))));
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);

                HBox hBox = loadAndSetHBox(track, musicPlayer);

                hBox1.getChildren().addAll(imageView, hBox);

                musicPlayer.toplistContener.getChildren().add(hBox1);

                hBox1.setSpacing(15);

                counter++;

                if (counter > 10){
                    img = "img/numbers/10.png";
                }
            }
        });
    }
}