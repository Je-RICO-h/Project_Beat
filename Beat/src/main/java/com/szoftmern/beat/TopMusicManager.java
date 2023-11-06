package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.szoftmern.beat.DatabaseManager.getTopMusicList;
import static com.szoftmern.beat.DatabaseManager.getTrackFromTitle;

public class TopMusicManager {
    private final MusicPlayer musicPlayer;
    public TopMusicManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void updateTopList() {
        ObservableList<Track> top = FXCollections.observableArrayList(getTopMusicList());

        Platform.runLater(() -> {
            int counter=1;
            musicPlayer.toplistContener.getChildren().clear();
            for(Track track:top) {
                String img="img/numbers/"+String.valueOf(counter)+".png";

                HBox hBox1=new HBox();
                ImageView imageView=new ImageView(new Image(getClass().getResourceAsStream(img)));
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);

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

                hBox1.getChildren().addAll(imageView,hBox);
                musicPlayer.toplistContener.getChildren().add(hBox1);
                hBox1.setSpacing(15);
                counter++;
                if (counter>10){
                    img="img/numbers/10.png";
                }
            }
        });
    }
}
