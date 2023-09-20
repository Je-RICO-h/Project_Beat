package com.szoftmern.beat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import java.io.File;

public class MusicPlayer {
    //Declaration of Labels, Buttons etc.
    public Label statuslabel;
    public Label Volumelabel;
    private MediaPlayer player;
    private String musicName;

    //Constructor
    public MusicPlayer() {}

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
                Volumelabel.setText(text);
            }
        }
    }

    @FXML
    public void mute()
    {
        //Not logic on the mute button
        player.setMute(!player.isMute());

        if(player.isMute())
            Volumelabel.setText("Volume: 0%");
        else
        {
            //Formatting the text and convert it into percentage
            String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

            //Write out Volume
            Volumelabel.setText(text);
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
                Volumelabel.setText(text);
            }
        }
    }

    @FXML void pause()
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

    @FXML void stop()
    {
        //Resets and stops the music
        player.stop();
        changeStatus("Music Stopped");
    }

    @FXML void changeStatus(String text)
    {
        statuslabel.setText(text);
    }

    @FXML
    public void testMusicPlayer()
    {

        //Open fileexplorer, and choose a music
        FileChooser fileChooser = new FileChooser();

        //Set a default directory everytime it opened, null means the same window
        fileChooser.setInitialDirectory(new File("Assets"));

        //Open filexplorer to select a file the return value is the file itself
        File musicFile = fileChooser.showOpenDialog(null);

        //Play Music
        Media sound = new Media(musicFile.toURI().toString());
        this.player = new MediaPlayer(sound);

        player.play();

        //Save the music name, as this is required for pause function status change
        this.musicName = musicFile.getName();

        //Update the status label
        changeStatus("Playing: " + this.musicName);
    }
}
