package com.szoftmern.beat;

import javafx.scene.chart.BarChart;

import java.util.*;

public class StatisticManager {
    private final MusicPlayer musicPlayer;
    public StatisticManager(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void writeAllRegistrationNumber() {
        long allRegistrationNumber = DatabaseManager.getUserCount();
        musicPlayer.allRegistrationNumber.setText("" + allRegistrationNumber);
    }

    public void populateCountryUserDistributionChart() {
        Map<Object, Object> countryUserMap = DatabaseManager.fetchCountryUserCounts();
        String title = "A top 10 legtöbb felhasználó számú ország";
        String xAxisLabel = "Országok";
        String yAxisLabel = "Felhasználók száma";

        BarChart<String, Number> barChart = UIController.createBarChar(countryUserMap, title, xAxisLabel, yAxisLabel);

        musicPlayer.VBoxCountryUserDistributionChart.getChildren().add(barChart);
    }

    public void populateCountryTrackDistributionChart() {
        Map<Object, Object> countryUserMap = DatabaseManager.fetchCountryTrackCounts();
        String title = "A top 10 legtöbb zenét meghallgató ország";
        String xAxisLabel = "Országok";
        String yAxisLabel = "Meghallgatott zenék száma";

        BarChart<String, Number> barChart = UIController.createBarChar(countryUserMap, title, xAxisLabel, yAxisLabel);

        musicPlayer.VBoxCountryTrackDistributionChart.getChildren().add(barChart);
    }
}
