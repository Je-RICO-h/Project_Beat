package com.szoftmern.beat;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayListManager {
    private final MusicPlayer musicPlayer;
    public PlayListManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    // Loads and displays the user's playlists
    public void loadUserPlaylists() {
        musicPlayer.playlistList.getItems().add("Lejátszási listáid:");

        for (Playlist pl : DatabaseManager.loggedInUser.getPlaylists()) {
            addNewPlaylistToUI(pl);
        }
    }

    public void createNewPlaylist(String playlistName) {
        try {
            UserInfoHelper.validateNewPlaylistName(playlistName);

        } catch (IncorrectInformationException e) {
            musicPlayer.newPlaylistNameInfoLabel.setText(e.getMessage());

            return;
        }


        Playlist newPlaylist = saveNewPlaylistToDatabase(playlistName);
        addNewPlaylistToUI(newPlaylist);
    }

    private Playlist saveNewPlaylistToDatabase(String playlistName) {
        Date currentDate = UserInfoHelper.getCurrentDate();

        Playlist pl = new Playlist(playlistName, DatabaseManager.loggedInUser, currentDate);
        DatabaseManager.playlistDAO.saveEntity(pl);

        // also update the user's playlists locally
        DatabaseManager.loggedInUser.getPlaylists().add(pl);

        System.out.println("New playlist named \"" + pl.getName() + "\" has been saved.");
        return pl;
    }

    private void addNewPlaylistToUI(Playlist pl) {
        Node deleteButton = createPlaylistDeleteButton(pl);
        Node playlistPane = createPlaylistPane(pl);

        // create an HBox containing a playlist and a delete button
        HBox playlistWithDeleteButtonBox = new HBox(10, playlistPane, deleteButton);
        playlistWithDeleteButtonBox.setAlignment(Pos.CENTER_LEFT);

        musicPlayer.userPlaylistsVBox.getChildren().add(playlistWithDeleteButtonBox);
        musicPlayer.playlistList.getItems().add(pl.getName());
    }

    private Node createPlaylistPane(Playlist pl) {
        AnchorPane playlistPane;
        FXMLLoader fxmlLoader = new FXMLLoader(
                UIController.class.getResource("artist.fxml"));

        try {
            playlistPane = fxmlLoader.load();
            onPlaylistSelected(pl, playlistPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set the playlist's name on the playlistPane
        SongController songController = fxmlLoader.getController();
        songController.SetArtistToItem(pl.getName());

        return playlistPane;
    }

    private Node createPlaylistDeleteButton(Playlist pl) {
        ImageView deleteButton = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/delete.png")))
        );

        deleteButton.setOnMouseClicked(mouseEvent -> {
            musicPlayer.userPlaylistsVBox.getChildren().remove(deleteButton.getParent());
            musicPlayer.playlistTracksVBox.getChildren().clear();

            musicPlayer.playlistList.getItems().remove(pl.getName());

            deletePlaylistFromDatabase(pl);
        });

        return deleteButton;
    }

    private void deletePlaylistFromDatabase(Playlist playlist) {
        // first delete the playlist's tracklist locally
        for (Playlist pl : DatabaseManager.loggedInUser.getPlaylists()) {
            if (pl.getId() == playlist.getId()) {
                pl.getTracks().clear();
            }
        }

        // then delete the playlist locally
        DatabaseManager.loggedInUser.getPlaylists().remove(playlist);


        // now delete the playlist-track pairs from the database
        for (PlaylistTracks pt : DatabaseManager.playlistTracksDAO.getEntities()) {
            if (pt.getPlaylist_id() == playlist.getId()) {
                DatabaseManager.playlistTracksDAO.deleteEntity(pt);
            }
        }

        // and finally delete the playlist from the database
        DatabaseManager.playlistDAO.deleteEntity(playlist);

        System.out.println("Playlist \"" + playlist.getName() + "\" removed");
    }

    private void onPlaylistSelected(Playlist pl, AnchorPane playlistPane) {
        playlistPane.setOnMouseClicked(mouseEvent -> {
            switchToSinglePlaylistView(pl.getName());

            if (!pl.getTracks().isEmpty()) {
                List<Track> currentTracksInPlaylist = new ArrayList<>(pl.getTracks());

                populatePlaylistVBoxWithTracks(currentTracksInPlaylist, pl);
            }
        });
    }

    private void switchToSinglePlaylistView(String playlistName) {
        System.out.println("Selected playlist: " + playlistName);

        musicPlayer.selectedPlaylistNameLabel.setText(playlistName);
        musicPlayer.playlistTracksVBox.getChildren().clear();

        UIController.setMiddlePain(
                musicPlayer.onePlaylistbox, musicPlayer.oneArtistbox, musicPlayer.homebox,
                musicPlayer.settingsbox, musicPlayer.artistbox, musicPlayer.favouritebox,
                musicPlayer.statisticbox, musicPlayer.playlistbox
        );
    }

    private void populatePlaylistVBoxWithTracks(List<Track> tracks, Playlist playlist) {
        //Platform.runLater(() -> {

        for (Track track : tracks) {
            Node deleteButton = createTrackDeleteButton(playlist, track);

            HBox trackBox = UIController.loadAndSetHBox(track, musicPlayer);
            HBox.setHgrow(trackBox, Priority.ALWAYS);

            HBox trackWithDeleteButtonBox = new HBox(15, deleteButton, trackBox);
            musicPlayer.playlistTracksVBox.getChildren().add(trackWithDeleteButtonBox);
        }

        //});
    }

    private Node createTrackDeleteButton(Playlist pl, Track track) {
        ImageView deleteTrackFromPlaylistButton = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("img/delete.png")))
        );

        // remove tracks from the playlist upon clicking the delete button
        deleteTrackFromPlaylistButton.setOnMouseClicked(mouseEvent2 -> {
            musicPlayer.playlistTracksVBox
                    .getChildren()
                    .remove(deleteTrackFromPlaylistButton.getParent());

            deleteTrackFromPlaylist(pl, track);
        });

        deleteTrackFromPlaylistButton.setFitWidth(45);
        deleteTrackFromPlaylistButton.setFitHeight(45);

        return deleteTrackFromPlaylistButton;
    }

    // Removes an association from the db's Playlist_Tracks table
    private void deleteTrackFromPlaylist(Playlist playlist, Track track) {
        for (PlaylistTracks pt : DatabaseManager.playlistTracksDAO.getEntities()) {
            if (pt.getPlaylist_id() == playlist.getId() && pt.getTrack_id() == track.getId()) {
                DatabaseManager.playlistTracksDAO.deleteEntity(pt);
            }
        }

        // update playlists locally too
        for (Playlist pl : DatabaseManager.loggedInUser.getPlaylists()) {
            if (pl.getId() == playlist.getId()) {
                pl.getTracks().remove(track);
            }
        }

        System.out.println("Track \"" + track.getTitle() + "\" deleted from playlist \"" + playlist.getName() + "\"");
    }

    public void onClickedSaveTrackToSelectedPlaylist(ListView listView) {
        listView.setOnMouseClicked(event -> {
            String playlistName = (String)listView.getSelectionModel().getSelectedItem();

            Playlist desinationPlaylist = EntityUtil.getPlaylistFromName(playlistName);
            Track trackToBeAdded = musicPlayer.musicList.get(musicPlayer.pos);

            addNewTrackToPlaylist(desinationPlaylist, trackToBeAdded);

        });
    }

    private void addNewTrackToPlaylist(Playlist playlist, Track track) {
        if (EntityUtil.doesPlaylistAlreadyContainTrack(playlist, track)) {
            return;
        }

        PlaylistTracks pt = new PlaylistTracks(
                playlist.getId(),
                track.getId()
        );

        DatabaseManager.playlistTracksDAO.saveEntity(pt);

        // update playlists locally too
        for (Playlist pl : DatabaseManager.loggedInUser.getPlaylists()) {
            if (pl.getId() == playlist.getId()) {
                pl.getTracks().add(track);
            }
        }

        System.out.println("Track \"" + track.getTitle() + "\" saved to playlist \"" + playlist.getName() + "\"");
    }
}
