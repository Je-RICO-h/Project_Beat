package com.szoftmern.beat;

import java.sql.*;
import java.util.ArrayList;
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
    }

    public static List<String> getTopMusic(int topNumber)
    {
        List<String> topMusicList = new ArrayList<>();

        String query = "SELECT title FROM Tracks ORDER BY play_count DESC LIMIT " + topNumber;

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    topMusicList.add(rs.getString("title"));
                }
                System.out.println(topMusicList);
                return topMusicList;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return topMusicList;
        }
    }

    public static List<String> getSearchDatabase(String keyword)
    {

        List<String> tilteList = new ArrayList<>();

        String query = "SELECT title FROM Tracks WHERE title LIKE \"%" + keyword + "%\" ORDER BY title";

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    tilteList.add(rs.getString("title"));
                }
                System.out.println(tilteList);
                return tilteList;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return tilteList;
        }
    }

    public static String getTrackURL(String title) {
        String query = "SELECT resource_url FROM Tracks WHERE title=\"" + title + "\"";

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {

                rs.next();
                return rs.getString(1);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
    // TODO: make an executeQuery function to avoid the repetition of the try-catch block
    public static String getTrackTitleFromURL(String musicURL) {

        String query = "SELECT title FROM Tracks WHERE resource_url=\"" + musicURL + "\"";

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {

                rs.next();
                return rs.getString(1); // returns the 1st column

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "no title? :(";
        }
    }
}





