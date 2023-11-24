package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.szoftmern.beat.EntityUtil.getArtistNameList;
import static com.szoftmern.beat.EntityUtil.isLiked;

public class SongController {

    @FXML
    private Label song_artist;

    @FXML
    private Label song_name;

    @FXML
    private ImageView song_like;

    //ez az artist.fxml nél a label
    @FXML
    private Label artist_item;
    boolean songlike = false;
    private Timer waitTimer = new Timer();

    @FXML
    public void songLikeClicked() {
        settingSongLikeButton();

        waitTimer.cancel();
        waitTimer = new Timer();
        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EntityUtil.addTrackToFavorites(Objects.requireNonNull(DatabaseManager.getTrackFromTitle(song_name.getText())), songlike);
            }
        }, 1000); // 1000 ms = 1.0 másodperc késleltetés
    }

    public void settingSongLikeButton()  {
        songlike = isLiked(Objects.requireNonNull(DatabaseManager.getTrackFromTitle(song_name.getText())));

        if (songlike) {
            song_like.setImage(new Image(getClass().getResourceAsStream("img/heart1.png")));
        } else {
            song_like.setImage(new Image(getClass().getResourceAsStream("img/heart2.png")));
        }
    }

    public void SetData(Track track){
        song_name.setText(track.getTitle());
        song_name.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold';");
        String artist = String.valueOf(getArtistNameList(track.getArtists()));
        song_artist.setText(artist.substring(1, artist.length() - 1));
        song_artist.setStyle("-fx-font-size: 15; -fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold';");
        settingSongLikeButton();
    }

    public void SetArtistToItem(String artist){
        artist_item.setText(artist);
    }
}
