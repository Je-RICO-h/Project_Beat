package com.szoftmern.beat;

import java.util.*;

import static com.szoftmern.beat.DatabaseManager.*;

public class EntityUtil {
    private static final List<String> artistNameList = new ArrayList<>();
    private static Map<Long, Integer> tarckPlayCount = new HashMap<>();

    public static List<String> getArtistNameList(List<Artist> artists) {
        artistNameList.clear();

        for (Artist artist:artists) {
            artistNameList.add(artist.getName());
        }

        return artistNameList;
    }

    public static void incrementListenCount(Track track) {
        int playCount = 1;

        if (tarckPlayCount.containsKey(track.getId())) {
            playCount = tarckPlayCount.get(track.getId());
            playCount++;
        }

        tarckPlayCount.put(track.getId(), playCount);
   }

   public static List<Track> updateTrack() {
        List<Track> trackList = new ArrayList<>();
        Track track;

        for (Long id : tarckPlayCount.keySet()) {
             track = getTrackFromId(id);
             track.setPlayCount(track.getPlayCount() + tarckPlayCount.get(id));

             trackList.add(track);
        }

        return trackList;
   }

   public static void updateDatabaseTrackPlayCount() {
       List<Track> updateTrackList = updateTrack();

       for (Track track : updateTrackList) {
           trackDAO.updateEntity(track);
       }
   }
}

