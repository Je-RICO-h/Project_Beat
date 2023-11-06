package com.szoftmern.beat;

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

    public static void incrementListenCount(Track track) {
        int playCount = track.getPlayCount();
        playCount++;
        track.setPlayCount(playCount);
    }
}

