package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.util.Objects;

import static com.szoftmern.beat.DatabaseManager.getTopMusicList;
import static com.szoftmern.beat.DatabaseManager.getTracksFromArtist;
import static com.szoftmern.beat.UIController.loadAndSetHBox;

public class PlayListManager {

    private final MusicPlayer musicPlayer;
    public PlayListManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void creatNewPlaylist(String s){

        //cak akkor hozzon létre ha van név megadva
        if(!s.isEmpty()){

            FXMLLoader fxmlLoader = new FXMLLoader();
            //artist.fxml hogy ne kelljen másikat létrehozni
            fxmlLoader.setLocation(UIController.class.getResource("artist.fxml"));
            HBox hBox = new HBox();

            ImageView delete = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/delete.png"))));

            AnchorPane anchorPane;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            SongController songController = fxmlLoader.getController();
            songController.SetArtistToItem(s);

            //törli az albumot és az alsó listából is törli
            delete.setOnMouseClicked(mouseEvent -> {
                musicPlayer.allPlayList.getChildren().remove(delete.getParent());
                musicPlayer.playlistList.getItems().remove(s);
            });

            //amikor kiválasztom az albumot
            anchorPane.setOnMouseClicked(mouseEvent -> {
                System.out.println(s);
                musicPlayer.onePlaylistName.setText(s);
                musicPlayer.onePlaylistSongs.getChildren().clear();
                UIController.setMiddlePain(musicPlayer.onePlaylistbox,musicPlayer.oneArtistbox,musicPlayer.homebox, musicPlayer.settingsbox, musicPlayer.artistbox, musicPlayer.favouritebox, musicPlayer.statisticbox,musicPlayer.playlistbox);

                //itt azért van a toplista hogy tudjam tesztelni ide kellene az adott lejátszási lista
                ObservableList<Track> songs = FXCollections.observableArrayList(getTopMusicList());

                Platform.runLater(() -> {

                    for(Track track : songs) {

                        HBox hBox1 = new HBox();

                        ImageView delete2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/delete.png"))));
                        delete2.setFitWidth(45);
                        delete2.setFitHeight(45);

                        HBox hBox2 = loadAndSetHBox(track, musicPlayer);

                        //itt törli az adott zenét az adott listából
                        delete2.setOnMouseClicked(mouseEvent2-> {
                            musicPlayer.onePlaylistSongs.getChildren().remove(delete2.getParent());

                        });

                        hBox1.getChildren().addAll(delete2, hBox2);

                        musicPlayer.onePlaylistSongs.getChildren().add(hBox1);

                        hBox1.setSpacing(15);
                        HBox.setHgrow(hBox2, Priority.ALWAYS);


                    }
                });
            });

            hBox.getChildren().addAll(anchorPane,delete);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            musicPlayer.allPlayList.getChildren().add(hBox);
        }
    }

    //musicplayerbe betöltött zene belerakása valamelyik playlistbe
    public static void addMusicToOneOfPlaylist(ListView listView){
        listView.setOnMouseClicked(event -> {
            // Kiválasztott elem lekérése
            String selectedItem = (String) listView.getSelectionModel().getSelectedItem();
            System.out.println("Kattintott elem: " + selectedItem);
        });
    }
}
