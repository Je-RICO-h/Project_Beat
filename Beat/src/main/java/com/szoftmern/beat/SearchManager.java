package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.util.Timer;
import java.util.TimerTask;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.EntityUtil.*;
import static com.szoftmern.beat.UIController.loadAndSetHBox;

public class SearchManager{
    private static Timer searchTimer;
    private static String currentKeyword = "";
    private final MusicPlayer musicPlayer;
    private HBox hBox;

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
                musicPlayer.searchResultView.getChildren().clear();

                for (int i = result.size() - 1; i >= 0 ; i--) {
                    hBox = loadAndSetHBox(result.get(i), musicPlayer);

                    musicPlayer.searchResultView.getChildren().add(hBox);
                }

            });

            musicPlayer.searchcontener.setVisible(true);
            musicPlayer.searchcontener.setDisable(false);
        } else {
            musicPlayer.searchcontener.setVisible(false);
            musicPlayer.searchcontener.setDisable(true);
        }
    }

    /*public void selectedSearchItem() {
        String selectedItem = musicPlayer.searchResultView.getSelectionModel().getSelectedItem();

        System.out.println(selectedItem);

        musicPlayer.pos = musicPlayer.musicList.indexOf(getTrackFromTitle(selectedItem.split("\n")[0])) - 1;

        musicPlayer.next();

        musicPlayer.searchResultView.setVisible(false);

        System.out.println("Kiválasztott elem: " + selectedItem);
    }*/
}

