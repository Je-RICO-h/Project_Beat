package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.UIController.loadAndSetHBox;

public class FavoriteManager {
    private final MusicPlayer musicPlayer;
    private Timer waitTimer;
    private HBox hBox;

    public FavoriteManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        waitTimer = new Timer();
    }

    public void onPressedLikeButton(Boolean liked) {
        UIController.settingLikeButton(musicPlayer, liked);

        musicPlayer.liked = !liked;

        waitTimer.cancel();
        waitTimer = new Timer();
        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EntityUtil.addTrackToFavorites(musicPlayer.musicList.get(musicPlayer.pos), !musicPlayer.liked);
            }
        }, 1000); // 1000 ms = 1.0 másodperc késleltetés
    }

    public void writeFavoriteTracks() {
        List<Track> favoriteTracks = new ArrayList<>();

        for (FavoriteTracks favoriteTrack : favTracksDAO.getEntities()) {
            if (favoriteTrack.getUser_id() == loggedInUser.getId())
                favoriteTracks.add(getTrackFromId(favoriteTrack.getTrack_id()));
        }

        ObservableList<Track> favoriteTracksList = FXCollections.observableArrayList(favoriteTracks);

        Platform.runLater(() -> {
            musicPlayer.favoriteListContener.getChildren().clear();

            for (Track track : favoriteTracksList) {
                hBox = loadAndSetHBox(track, musicPlayer);

                musicPlayer.favoriteListContener.getChildren().add(hBox);
            }
        });
    }
}
