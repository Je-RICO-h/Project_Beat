package com.szoftmern.beat;

import java.util.*;

public class DatabaseManager {
    private static JpaTrackDAO trackDAO;
    private static JpaArtistDAO artistDAO;
    private static List<Track> everyTrack;

    public DatabaseManager() {
        try {
            trackDAO = new JpaTrackDAO();
            artistDAO = new JpaArtistDAO();

            // get every Track class via JPA from the DB
            everyTrack = trackDAO.getEntities();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Deallocating resources
    public void close() {
        try {
            trackDAO.close();
            artistDAO.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Returns every track's title in our DB
    public static List<String> getEveryTitle() {
        List<String> titleList = new ArrayList<>();

        for (Track t : everyTrack) {
            titleList.add(t.getTitle());
        }

        return titleList;
    }

    // Gets the top 10 most played music
    public static List<String> getTopMusicList() {
        List<String> topMusicList = new ArrayList<>();

        List<Track> tracks = everyTrack;

        // sorts the tracks in descending order by their play count
        Collections.sort(tracks, Collections.reverseOrder(Track.playCountComparator));

        // get the top 10 most played music
        for (int i = 0; i < 10; i++) {
            topMusicList.add(tracks.get(i).getTitle());
        }

        return topMusicList;
    }

    // Get a track's artists, but only returns one of them
    public static String getArtist(String title) {
        String artist = "";

        for (Track t : everyTrack) {
            if (t.getTitle().equals(title)) {
                artist = t.getArtists().get(0).getName();
            }
        }

        return artist;
    }

    // Searches the DB for any tracks or artists which contain the specified keyword
    public static List<String> searchDatabaseForTracks(String keyword) {
        List<String> titleList;

        // lassuuu!!! valahogy ki kene menteni es inkabb a memoriaba tarolni ennek az
        // eredmenyet egyszer a program elejen...
        titleList = trackDAO.entityManager
                .createQuery("""
                        SELECT T.title 
                        FROM Track T 
                        WHERE title LIKE :keyword 
                        UNION 
                        SELECT A.name 
                        FROM Artist A 
                        WHERE name 
                        LIKE :keyword
                        """,
                        String.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        return titleList;
    }

    // Get a track's URL from its title
    public static String getTrackURL(String title) {
        String url = "";

        for (Track t : everyTrack) {
            if (t.getTitle().equals(title)) {
                url = t.getResourceUrl();
            }
        }

        return url;
    }

    // Get a track's title from its URL
    public static String getTitleFromURL(String url) {
        String title = "";

        for (Track t : everyTrack) {
            if (t.getResourceUrl().equals(url)) {
                title = t.getTitle();
            }
        }

        return title;
    }
}