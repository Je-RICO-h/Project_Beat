package com.szoftmern.beat;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class SceneSwitch {
    public SceneSwitch(Pane currentPane, String fxml) throws IOException {
        Pane nextPane = FXMLLoader.load((Main.class.getResource(fxml)));
        currentPane.getChildren().removeAll();
        currentPane.getChildren().setAll(nextPane);
    }
}
