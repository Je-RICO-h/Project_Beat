package com.szoftmern.beat;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

import static com.szoftmern.beat.DatabaseManager.getSearchDatabase;

public class MusicPlayer extends Application {
    //Declaration of Labels, Buttons etc.
    public ListView<String> listView;
    public TextField searchBar;
    public Label statusLabel;
   public Label volumeLabel;
    private MediaPlayer player;
    private String musicName;

    //Constructor
    public MusicPlayer() {}

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @FXML
    public void selectedItem(){
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        this.musicName = selectedItem;
        System.out.println("Kiválasztott elem: " + selectedItem);
    }

    @FXML
    public void search() {
        String keyword = searchBar.getText();
        ObservableList<String> result = FXCollections.observableArrayList(getSearchDatabase(keyword));
        listView.setItems(result);
    }

    @FXML
    public void volDown()
    {
        //If music is not at 0 volume, decrease it
        if(player.getVolume() >= 0.1)
        {
            player.setVolume(player.getVolume() - 0.1);

            //Only display if its not muted

            if (!player.isMute())
            {
                //Formatting the text and convert it into percentage
                String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                //Write out Volume
                volumeLabel.setText(text);
            }
        }
    }

    @FXML
    public void mute()
    {
        //Not logic on the mute button
        player.setMute(!player.isMute());

        if(player.isMute())
            volumeLabel.setText("Volume: 0%");
        else
        {
            //Formatting the text and convert it into percentage
            String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

            //Write out Volume
            volumeLabel.setText(text);
        }
    }

    @FXML
    public void volUp()
    {
        //If volume is not at max, increase the volume
        if(player.getVolume() != 1.0)
        {
            player.setVolume(player.getVolume() + 0.1);

            //Only display if its not muted

            if (!player.isMute())
            {
                //Formatting the text and convert it into percentage
                String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                //Write out Volume
                volumeLabel.setText(text);
            }
        }
    }

    @FXML
    public void pause()
    {
        //If status of the player is playing, pause the music, else play it
        if(player.getStatus() == MediaPlayer.Status.PLAYING)
        {
            player.pause();
            changeStatus("Music Paused");
        }
        else
        {
            player.play();
            changeStatus("Playing: " + this.musicName);
        }

    }

    @FXML
    public void stop()
    {
        //Resets and stops the music
        player.stop();
        changeStatus("Music Stopped");
    }

    @FXML
    public void changeStatus(String text)
    {
        statusLabel.setText(text);
    }

    @FXML
    public void testMusicPlayer()
    {
        String musicURL;

        if (this.musicName != null) {
            musicURL = DatabaseManager.getTrackURL(musicName);
        } else {
            String randomMusicTitle = randomMusicTitle();
            this.musicName = randomMusicTitle;
            musicURL = DatabaseManager.getTrackURL(musicName);
        }
//            String trackTitle = DatabaseManager.getTrackTitleFromURL(musicURL);
            //Open fileexplorer, and choose a music
    //        FileChooser fileChooser = new FileChooser();

            //Set a default directory everytime it opened, null means the same window
    //        fileChooser.setInitialDirectory(new File("Assets"));

            //Open filexplorer to select a file the return value is the file itself
    //        File musicFile = fileChooser.showOpenDialog(null);

            //Play Music
            //Media sound = new Media(musicFile.toURI().toString());
            Media sound = new Media(musicURL);
            this.player = new MediaPlayer(sound);

            player.play();

            //Save the music name, as this is required for pause function status change
            //this.musicName = musicFile.getName();
//            this.musicName = trackTitle;

            //Update the status label
            changeStatus("Playing: " + this.musicName);
    }

    public String randomMusicTitle(){
        Random random = new Random();

        ObservableList<String> result = FXCollections.observableArrayList(getSearchDatabase(""));
        int index = random.nextInt(result.size());
        return result.get(index);
    }
}
