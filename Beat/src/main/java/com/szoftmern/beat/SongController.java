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
    private ImageView song_img;

    @FXML
    private Label song_name;




    public  void  SetData(Song song){
        song_img.setImage(new Image(getClass().getResourceAsStream(song.getCover())));
        song_name.setText(song.getName());
        song_artist.setText(song.getArtist_name());

    }



}
