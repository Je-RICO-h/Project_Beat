package com.szoftmern.beat;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class SceneSwitcher {
    public static void switchScene(Pane currentPane, String fxml) {
        try {
            Pane nextPane = FXMLLoader.load(Main.class.getResource(fxml));

            currentPane.getChildren().removeAll();
            currentPane.getChildren().setAll(nextPane);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
