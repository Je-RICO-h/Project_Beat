package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import static com.szoftmern.beat.EntityUtil.getArtistNameList;

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
    boolean songlike=false;

    @FXML
    void songLikeClicked(){
        if (songlike) {
            song_like.setImage(new Image(getClass().getResourceAsStream("img/heart2.png")));
            songlike = false;
        } else {
            song_like.setImage(new Image(getClass().getResourceAsStream("img/heart1.png")));
            songlike = true;
        }

    }

    public void SetData(Track track){
        song_name.setText(track.getTitle());
        String artist = String.valueOf(getArtistNameList(track.getArtists()));
        song_artist.setText(artist.substring(1, artist.length() - 1));
    }

    public void SetArtistToItem(String artist){
        artist_item.setText(artist);

    }
}
