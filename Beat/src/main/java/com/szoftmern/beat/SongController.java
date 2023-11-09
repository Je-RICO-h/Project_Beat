package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class SongController {


    @FXML
    private Label song_artist;

    @FXML
    private Label song_name;

<<<<<<< Updated upstream



    public  void  SetData(Song song){
        song_img.setImage(new Image(getClass().getResourceAsStream(song.getCover())));
        song_name.setText(song.getName());
        song_artist.setText(song.getArtist_name());

=======
    public void SetData(Track track){
        song_name.setText(track.getTitle());
        String artist = String.valueOf(getArtistNameList(track.getArtists()));
        song_artist.setText(artist.substring(1, artist.length() - 1));
>>>>>>> Stashed changes
    }



}
