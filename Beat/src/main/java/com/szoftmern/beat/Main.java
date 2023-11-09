package com.szoftmern.beat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.szoftmern.beat.EntityUtil.updateDatabaseTrackPlayCount;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Start Database
        DatabaseManager manager = new DatabaseManager();

        //Start JavaFX and load login scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1280,720 );

        stage.setTitle("Beat!");

        stage.setScene(scene);

        stage.show();

        //If window is closed, do cleanup
        stage.setOnCloseRequest(windowevent -> {
            updateDatabaseTrackPlayCount();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch();
    }


}

