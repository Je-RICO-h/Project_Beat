package com.szoftmern.beat;

import java.util.*;

import static com.szoftmern.beat.DatabaseManager.*;

public class EntityUtil {
    private static final List<String> artistNameList = new ArrayList<>();
    private static Map<Long, Integer> trackPlayCount = new HashMap<>();

    public static List<String> getArtistNameList(List<Artist> artists) {
        artistNameList.clear();

        for (Artist artist:artists) {
            artistNameList.add(artist.getName());
        }

        return artistNameList;
    }

    public static void incrementListenCount(Track track) {
        int playCount = 1;

        if (trackPlayCount.containsKey(track.getId())) {
            playCount = trackPlayCount.get(track.getId());
            playCount++;
        }

        trackPlayCount.put(track.getId(), playCount);
   }

   public static List<Track> updateTrack() {
        List<Track> trackList = new ArrayList<>();
        Track track;

        for (Long id : trackPlayCount.keySet()) {
             track = getTrackFromId(id);
             track.setPlayCount(track.getPlayCount() + trackPlayCount.get(id));

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

    public static boolean doesUserAlreadyExist(String username) {
        try {
            returnUserIfItExists(username);

        } catch (IncorrectInformationException e) {
            return false;
        }

        return true;
    }

    // Returns the username if it exists, otherwise it throws an exception
    public static User returnUserIfItExists(String username) throws IncorrectInformationException {
        for (User user : DatabaseManager.userDAO.getEntities()) {
            if (user.getName().equals(username)) {
                // return the username if it exists
                return user;
            }
        }

        // the user doesn't exist
        throw new IncorrectInformationException("Ez a felhasználó nem létezik!\n");
    }
}

