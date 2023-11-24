package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

import static com.szoftmern.beat.DatabaseManager.artistDAO;

public class ArtistManager {
    private final MusicPlayer musicPlayer;
    public ArtistManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void writeArtistToBlock()
    {
        List<String> artists = new ArrayList<>();

        for (Artist artist : artistDAO.getEntities()) {
            artists.add(artist.getName());
        }

        ObservableList<String> artistList = FXCollections.observableArrayList(artists);
        Platform.runLater(() -> {

            //keret szélesséneg meghatározása
            double size = musicPlayer.artistGrid.getParent().getBoundsInLocal().getWidth();

            int onerow = (int)(size / 245);
            if (onerow < 4) {
                onerow = 4;
            }

            int row = 1;
            int colum = 1;

            for(String s : artistList) {

                AnchorPane anchorPane = UIController.loadAndSetArtist(s, musicPlayer);
                musicPlayer.artistGrid.add(anchorPane,colum++,row);

                if(colum == onerow) {
                    colum=1;
                    row++;
                }
            }
        });
    }
}
