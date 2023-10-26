package com.szoftmern.beat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

<<<<<<< Updated upstream
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
=======
        //Start Database
        DatabaseManager manager = new DatabaseManager();

        //Start JavaFX and load login scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("screen.fxml"));
>>>>>>> Stashed changes

        Scene scene = new Scene(fxmlLoader.load(), 1280,720 );
        stage.setTitle("Beat!");

        scene.setFill(Color.LIGHTGRAY);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        DatabaseManager manager = new DatabaseManager();
        launch();

        manager.close();
    }


}

