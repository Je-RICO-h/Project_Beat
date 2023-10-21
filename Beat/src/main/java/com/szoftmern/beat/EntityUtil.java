package com.szoftmern.beat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class EntityUtil {
    private static List<String> artistNameList = new ArrayList<>();

    public static List<String> getArtistNameList(List<Artist> artists) {
        artistNameList.clear();
        for (Artist artist:artists) {
            artistNameList.add(artist.getName());
        }

        return artistNameList.stream().sorted().toList();
    }
}

