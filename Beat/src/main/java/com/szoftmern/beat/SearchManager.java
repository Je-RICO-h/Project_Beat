package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Timer;
import java.util.TimerTask;

import static com.szoftmern.beat.DatabaseManager.getTrackFromTitle;
import static com.szoftmern.beat.DatabaseManager.searchDatabaseForTracks;
import static com.szoftmern.beat.EntityUtil.getArtistNameList;

public class SearchManager{
    private static Timer searchTimer;
    private static String currentKeyword = "";
    private final MusicPlayer musicPlayer;

    public SearchManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        searchTimer = new Timer();
    }

    public void onActionSearchButton() {
        search();
    }

    public void onKeyPressedSearchTextField() {
        searchTimer.cancel();
        searchTimer = new Timer();
        searchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                search();
            }
        }, 1500); // 1500 ms = 1.5 másodperc késleltetés
    }

    public void search() {
        String keyword = musicPlayer.searchTextField.getText();
        currentKeyword = keyword;

        if (!keyword.isEmpty()) {
            // Ellenőrizd, hogy a keresett kulcsszó megegyezik-e a jelenlegi kulcsszóval
            if (!currentKeyword.equals(musicPlayer.searchTextField.getText())) {
                return; // Ha nem, ne végezd el a keresést
            }

            ObservableList<Track> result = FXCollections.observableArrayList(searchDatabaseForTracks(keyword));

            Platform.runLater(() -> {
                musicPlayer.searchResultView.getItems().clear();
                for (Track track : result) {
                    musicPlayer.searchResultView.getItems().add(track.getTitle() + "\n" + getArtistNameList(track.getArtists()));
                }
            });

            musicPlayer.searchResultView.setVisible(true);
            musicPlayer.searchResultView.toFront();
        } else {
            musicPlayer.searchResultView.setVisible(false);
        }
    }

    public void selectedSearchItem() {
        String selectedItem = musicPlayer.searchResultView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);
        musicPlayer.pos = musicPlayer.musicList.indexOf(getTrackFromTitle(selectedItem.split("\n")[0])) - 1;
        musicPlayer.next();
        musicPlayer.searchResultView.setVisible(false);
        System.out.println("Kiválasztott elem: " + selectedItem);
    }
}

