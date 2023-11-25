package com.szoftmern.beat;

import com.almasb.fxgl.physics.box2d.dynamics.joints.RopeJoint;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

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

        BarChart<String, Number> barChart = UIController.createBarChaer(countryUserMap, title, xAxisLabel, yAxisLabel);

        musicPlayer.VBoxCountryUserDistributionChart.getChildren().add(barChart);
    }

    public void populateCountryTrackDistributionChart() {
        Map<Object, Object> countryUserMap = DatabaseManager.fetchCountryTrackCounts();
        String title = "A top 10 legtöbb zenét hallgató ország";
        String xAxisLabel = "Országok";
        String yAxisLabel = "Meghallgatott zenék száma";

        BarChart<String, Number> barChart = UIController.createBarChaer(countryUserMap, title, xAxisLabel, yAxisLabel);

        musicPlayer.VBoxCountryTrackDistributionChart.getChildren().add(barChart);
    }
}
