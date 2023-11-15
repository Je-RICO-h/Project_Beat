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

    public void SetData(Track track){
        song_name.setText(track.getTitle());
        String artist = String.valueOf(getArtistNameList(track.getArtists()));
        song_artist.setText(artist.substring(1, artist.length() - 1));
    }
}
