package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

import static com.szoftmern.beat.DatabaseManager.getEveryArtist;

public class ArtistManager {
    private final MusicPlayer musicPlayer;
    public ArtistManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public  void writeArtistToBlock()

    {
        List<String> artists = new ArrayList<>();

        for (Artist artist : getEveryArtist()) {
            artists.add(artist.getName());
        }

        ObservableList<String> artistList = FXCollections.observableArrayList(artists);
        Platform.runLater(() -> {
            int row = 1;
            int colum=1;

            for(String s : artistList) {

                AnchorPane anchorPane = UIController.loadAndSetArtist(s, musicPlayer);
                musicPlayer.artistGrid.add(anchorPane,colum++,row);
                if(colum==4){
                    colum=1;
                    row++;
                }


            }
        });

    }
}
