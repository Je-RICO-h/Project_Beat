package com.szoftmern.beat;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class DatabaseManager {
    public static JpaTrackDAO trackDAO;
    public static JpaArtistDAO artistDAO;
    public static JpaUserDAO userDAO;
    public static JpaFavoriteTracksDAO favTracksDAO;
    public static JpaCountryDAO countryDAO;

    public static User loggedInUser = null;

    // Returns every track's data in our DB
    @Getter
    @Setter
    private static List<Track> everyTrack;

    @Getter
    @Setter
    private static List<Country> everyCountry;

    public DatabaseManager() {
        try {
            trackDAO     = new JpaTrackDAO();
            artistDAO    = new JpaArtistDAO();
            userDAO      = new JpaUserDAO();
            favTracksDAO = new JpaFavoriteTracksDAO();
            countryDAO   = new JpaCountryDAO();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Deallocating resources
    public void close() {
        try {
            trackDAO.close();
            artistDAO.close();
            userDAO.close();
            favTracksDAO.close();
            countryDAO.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Track> loadAllTracksFromDatabase() {
        return trackDAO.getEntities();
    }

    public static List<Track> loadAllNonExplicitTracksFromDatabase() {
        List<Track> resultList;

        resultList = trackDAO.entityManager
                .createQuery("""
                        SELECT T
                        FROM Track T
                        WHERE T.isLyricsExplicit = false
                        """,
                        Track.class)
                .getResultList();

        return resultList;
    }

    // Gets the top 10 most played music
    public static List<Track> getTopMusicList() {
        List<Track> topMusicList = new ArrayList<>();

        List<Track> tracks = everyTrack;

        // sorts the tracks in descending order by their play count
        Collections.sort(tracks, Collections.reverseOrder(Track.playCountComparator));

        // get the top 10 most played music
        for (int i = 0; i < 10; i++) {
            topMusicList.add(tracks.get(i));
        }

        return topMusicList;
    }


    // Searches the DB for any tracks or artists which contain the specified keyword
    public static List<Track> searchDatabaseForTracks(String keyword) {
        List<Artist> artistList;
        List<Track> resultList;

        keyword = keyword.trim();

        String explicitTrackFilterString = loggedInUser.isFilteringExplicitLyrics()
                ? "AND T.isLyricsExplicit = false"
                : "";

        // lassuuu!!! valahogy ki kene menteni es inkabb a memoriaba tarolni ennek az
        // eredmenyet egyszer a program elejen...
        resultList = trackDAO.entityManager
                .createQuery("""
                        SELECT T
                        FROM Track T
                        WHERE T.title LIKE :keyword
                        """ + explicitTrackFilterString,
                        Track.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        artistList = trackDAO.entityManager
                .createQuery("""
                        SELECT A
                        FROM Artist A
                        WHERE A.name LIKE :keyword
                        """,
                        Artist.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        for (Artist artist : artistList) {
            resultList.addAll(artist.getTracks());
        }

        System.out.println(resultList);
        return resultList;
    }

    public static Track getTrackFromTitle(String title) {
        for (Track track:getEveryTrack()) {
           if (track.getTitle().equals(title)) {
               return track;
           }
        }

        return null;
    }

    public static Track getTrackFromId(Long id) {
        Track track = trackDAO.entityManager
                .createQuery("""
                        SELECT T
                        FROM Track T
                        WHERE T.id = :id
                        """,
                        Track.class)
                .setParameter("id", id)
                .getResultList()
                .get(0);

        return track;
    }

    // Gets the password hash of a user
    public static String getPassHashFromUsername(String username) {
        String hash = "";

        for (User user : userDAO.getEntities()) {
            if (user.getName().equals(username)) {
                hash = new String(user.getPassHash(), StandardCharsets.UTF_8);
            }
        }

        return hash;
    }

    public static List<Track> getTracksFromArtist(String name) {
        for (Artist artist : artistDAO.getEntities()) {
            if (artist.getName().equals(name)) {
                return artist.getTracks();
            }
        }

        return null;
    }

    public static FavoriteTracks getFavorite(Long userId, Long trackId) {
        for (FavoriteTracks favoriteTrack : favTracksDAO.getEntities()) {
            if (favoriteTrack.getUser_id() == userId && favoriteTrack.getTrack_id() == trackId)
                return favoriteTrack;
        }
        return null;
    }

    public static Country getCountryFromName(String countryName) {
        for (Country c : everyCountry) {
            if (c.getName().equals(countryName)) {
                return c;
            }
        }

        return null;
    }
}