package com.szoftmern.beat;

import java.util.*;

public class EntityUtil {
    private static final List<String> artistNameList = new ArrayList<>();

    public static List<String> getArtistNameList(List<Artist> artists) {
        artistNameList.clear();

        for (Artist artist:artists) {
            artistNameList.add(artist.getName());
        }

        return artistNameList;
    }

    public static void incrementListenCount(Track track) {
        int playCount = track.getPlayCount();
        playCount++;
        track.setPlayCount(playCount);
   }
}

