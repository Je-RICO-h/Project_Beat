package com.szoftmern.beat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseManager {
    private static Connection conn;

    public DatabaseManager() {
        String host = "jdbc:mariadb://beat-db.cunlipdvpspb.eu-north-1.rds.amazonaws.com:3306/beat-db";
        String user = "beatdev_admin";
        String password = "Beat_Proj10";

        try {
            conn = DriverManager.getConnection(host, user, password);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try (JpaTrackDAO tDAo = new JpaTrackDAO()) {
            System.out.println("hello");

            for (Track t : tDAo.getEntities()) {
               System.out.println(t.getTitle());
            }
        } catch (Exception e) {
            System.out.println("Hiba!");
            System.out.println(e.getMessage());
        }
    }

    public static Collection<Object> executeQuery(String queryStr, String column) {
        Collection<Object> results = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(queryStr)) {

                // from every row, add the column we specified to the results
                while (rs.next()) {
                    results.add(rs.getObject(column));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    public static List<String> getEveryTitle() {
        List<String> titleList = new ArrayList<>();

        String query = "SELECT title " +
                "FROM Tracks " +
                "ORDER BY title";
        Collection<Object> results = executeQuery(query, "title");

        for (Object obj : results) {
            titleList.add("" + obj);
        }

        return titleList;
    }

    public static List<String> getTopMusicList() {
        List<String> topMusicList = new ArrayList<>();

        String query = "SELECT title " +
                "FROM Tracks ORDER " +
                "BY play_count DESC " +
                "LIMIT 10";

        Collection<Object> results = executeQuery(query, "title");

        for (Object obj : results) {
            topMusicList.add("" + obj);
        }

        return topMusicList;
    }

    public static String getArtist(String title) {
        String artist = "";

        String query = "SELECT name " +
                "FROM Artists A, Tracks T, Track_Artists " +
                "WHERE artist_id = A.id AND track_id = T.id AND title LIKE \"" + title + "\"" +
                "GROUP BY title";

        Collection<Object> results = executeQuery(query, "name");

        for (Object obj : results) {
            artist = "" + obj;
        }

        return artist;
    }

    public static List<String> searchDatabaseForTracks(String keyword)
    {
        List<String> titleList = new ArrayList<>();

        String query = "SELECT title FROM Tracks WHERE title LIKE \"%" + keyword + "%\" ORDER BY title";
        Collection<Object> results = executeQuery(query, "title");

        for (Object obj : results) {
            titleList.add("" + obj);
        }

        return titleList;
    }

    public static String getTrackURL(String title) {
        String query = "SELECT resource_url " +
                "FROM Tracks " +
                "WHERE title = \"" + title + "\"";
        Collection<Object> results = executeQuery(query, "resource_url");

        return "" + results.toArray()[0];
    }

    public static String getTitleFromURL(String url) {
        String query = "SELECT title " +
                "FROM Tracks " +
                "WHERE resource_url = \"" + url + "\"";
        Collection<Object> results = executeQuery(query, "title");

        return "" + results.toArray()[0];
    }
}