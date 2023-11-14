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

        trackPlayCount.clear();

        return trackList;
   }

   public static int sumPlayCounts() {
        int counter = 0;

       for (int count : trackPlayCount.values()) {
           counter += count;
       }

       return counter;
   }

   public static void updateDatabaseTrackPlayCount() {
       List<Track> updateTrackList = updateTrack();

       for (Track track : updateTrackList) {
           trackDAO.updateEntity(track);
       }
   }

    public static void updateDatabaseCountryPlayCount() {
        int countryId = (int) DatabaseManager.loggedInUser.getCountryId();
        Country userCountry = DatabaseManager.getEveryCountry().get(countryId - 2 < 0 ? 0 : countryId - 2);

        int currentPlayCount = userCountry.getTotalPlayCount();
        int countryTotalPlayCount = sumPlayCounts();

        userCountry.setTotalPlayCount(currentPlayCount + countryTotalPlayCount);

        countryDAO.updateEntity(userCountry);
    }

    public static boolean doesUserAlreadyExist(String username) {
        try {
            returnUserIfItExists(username);

        } catch (IncorrectInformationException e) {
            return false;
        }

        return true;
    }

    // Returns the user if it exists, otherwise it throws an exception
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


    public static void addTrackToFavorites(Track track, Boolean isLiked) {
        FavoriteTracks existingFavorite = getFavorite(loggedInUser.getId(), track.getId());

        if (isLiked) {
            if (existingFavorite == null) {
                FavoriteTracks newFavorite = new FavoriteTracks();
                newFavorite.setTrack_id(track.getId());
                newFavorite.setUser_id(loggedInUser.getId());
                favTracksDAO.saveEntity(newFavorite);
            }
        } else {
            if (existingFavorite != null) {
                favTracksDAO.deleteEntity(existingFavorite);
            }
        }
    }

    public static boolean isLiked(Track track) {
        FavoriteTracks existingFavorite = getFavorite(loggedInUser.getId(), track.getId());
        System.out.println(existingFavorite);
        return existingFavorite != null;
    }
<<<<<<< HEAD
=======

    // If a user exists with the given email, it returns it, otherwise it throws an exception
    public static User findUserWithEmail(String email) throws IncorrectInformationException {
        for (User user : DatabaseManager.userDAO.getEntities()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        throw new IncorrectInformationException("Nem létezik felhasználó ezzel az email címmel!\n");
    }

    public static Playlist getPlaylistFromName(String playlistName) {
        for (Playlist pl : playlistDAO.getEntities()) {
            if (pl.getName().equals(playlistName)) {
                return pl;
            }
        }

        return null;
    }

    public static boolean doesPlaylistAlreadyContainTrack(Playlist playlist, Track track) {
        for (PlaylistTracks pt : playlistTracksDAO.getEntities()) {
            if (
                pt.getPlaylist_id() == playlist.getId() &&
                pt.getTrack_id() == track.getId()
            ) {
                return true;
            }
        }

        return false;
    }
}
