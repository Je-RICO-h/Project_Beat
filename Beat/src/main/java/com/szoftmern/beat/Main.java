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

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Start Database
        DatabaseManager manager = new DatabaseManager();

        //Start JavaFX and load login scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("screen.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1280,720 );

        stage.setTitle("Beat!");

        stage.setScene(scene);

        stage.show();

        //If window is closed, do cleanup
        stage.setOnCloseRequest(windowevent -> {
            System.out.println("App is closing");
            manager.close();
            stage.close();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        //Start Logging
        try {
            Logging.startLogging();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch();
    }


}

