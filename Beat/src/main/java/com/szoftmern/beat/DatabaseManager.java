package com.szoftmern.beat;

import lombok.Getter;

import java.util.*;
import static com.szoftmern.beat.EntityUtil.*;

public class DatabaseManager {
    private static JpaTrackDAO trackDAO;
    private static JpaArtistDAO artistDAO;
    // Returns every track's data in our DB
    @Getter
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

        // lassuuu!!! valahogy ki kene menteni es inkabb a memoriaba tarolni ennek az
        // eredmenyet egyszer a program elejen...
        resultList = trackDAO.entityManager
                .createQuery("""
                        SELECT T
                        FROM Track T
                        WHERE T.title LIKE :keyword
                        """,
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

        for (Artist artist:artistList) {
            for (Track track:artist.getTracks()) {
                if (!(resultList.equals(track))) {
                    resultList.add(track);
                }
            }
        }

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
}