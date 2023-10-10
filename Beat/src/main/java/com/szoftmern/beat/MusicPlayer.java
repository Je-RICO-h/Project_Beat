package com.szoftmern.beat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

import static com.szoftmern.beat.DatabaseManager.*;
import static java.lang.Math.round;

public class MusicPlayer {
    //Declaration of Labels, Buttons etc.
    @FXML
    private ImageView heart;

    @FXML
    private ImageView play_pause;
    @FXML
    private ImageView sound;
    public Label statuslabel;
    public Label artistNameLabel;
    public TextField searchTextField;
    public ListView<String> searchResultView;
    public Label Volumelabel;
    public Button playbutton;
    public Slider volumeSlider;
    public Label musicDuration;
    public Label starttime;
    public Label endtime;
    private MediaPlayer player;
    public Slider timeSlider;
    private int pos = -1;

    //List for the musics
    private List<String> musicList = new ArrayList<>();

    //List for the music names
    private List<String> musicNames = new ArrayList<>();

    boolean liked=false;

    //Constructor
    public MusicPlayer() {
        this.musicNames = getEveryTitle();

        for (String title: musicNames) {
            String URL = getTrackURL(title);
            this.musicList.add(URL);
        }
        pos = 0;
    }

    @FXML
    public void selectedSearchItem(){
        String selectedItem = searchResultView.getSelectionModel().getSelectedItem();
        pos = this.musicList.indexOf(getTrackURL(selectedItem));
        playMusic();
        searchResultView.setVisible(false);
        System.out.println("Kiválasztott elem: " + selectedItem);
    }

//    public void selectedTopListItem(){
//        String selectedItem = topMusicList.getSelectionModel().getSelectedItem();
//        this.musicName = selectedItem;
//        System.out.println("Kiválasztott elem: " + selectedItem);
//    }

    @FXML
    public void search() {
        String keyword = searchTextField.getText();
        if (!keyword.isEmpty()) {
            ObservableList<String> result = FXCollections.observableArrayList(searchDatabaseForTracks(keyword));
            searchResultView.setItems(result);
            searchResultView.setVisible(true);
        } else {
            searchResultView.setVisible(false);
        }
    }
//
//    public void refreshTopList() {
//        Thread updateThread = new Thread(() -> {
//            while (true) {
//
//                Platform.runLater(() -> {
//                    int topNumber = Integer.parseInt(topNumberLabel.getText());
//                    ObservableList<String> top = FXCollections.observableArrayList(getTopMusic(topNumber));
//                    topMusicList.setItems(top);
//
//                    topMusicList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
//                        @Override
//                        public ListCell<String> call(ListView<String> param) {
//                            return new ListCell<String>() {
//                                @Override
//                                protected void updateItem(String item, boolean empty) {
//                                    super.updateItem(item, empty);
//
//                                    if (item == null || empty) {
//                                        setText(null);
//                                    } else {
//                                        int index = getIndex() + 1;
//                                        setText(index + ". " + item);
//                                    }
//                                }
//                            };
//                        }
//                    });
//                });
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        updateThread.setDaemon(true);
//        updateThread.start();
//    }

    @FXML
    public void mute()
    {
        //If volume is 0 percent and muting, get it to 50%
        if (player.getVolume() == 0.0) {
            player.setVolume(0.5);
            volumeSlider.setValue(50);
        }
        else
            //Not logic on the mute button
            player.setMute(!player.isMute());


        //Set the volume status text

        if(player.isMute() && player.getVolume() != 0.0) {
            Volumelabel.setText("Volume: 0 %");
            sound.setImage(new Image(getClass().getResourceAsStream("img/mute.png")));

        }
        else
        {
            //Formatting the text and convert it into percentage
            String text = String.format("Volume: %.0f %%", player.getVolume() * 100);
            sound.setImage(new Image(getClass().getResourceAsStream("img/sound.png")));

            //Write out Volume
            Volumelabel.setText(text);
        }
    }


    @FXML void pausePlay()
    {
        //If player is not initialized, initialize it
        if(player == null)
        {
            playMusic();
            //playbutton.setText("Pause");
            play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));

        }
        //If status of the player is playing, pause the music, else play it
        else if(player.getStatus() == MediaPlayer.Status.PLAYING)
        {
            player.pause();
            //changeStatus("Music Paused");
            //playbutton.setText("Play");
            play_pause.setImage(new Image(getClass().getResourceAsStream("img/play.png")));
        }
        else
        {
            player.play();
            changeStatus( musicNames.get(this.pos));
            //playbutton.setText("Pause");
            play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));
        }

    }

    @FXML
    public void changeStatus(String text)
    {
        //Function to change the label
        statuslabel.setText(text);
    }

    @FXML
    public void changeArtist(String title)
    {
        //Function to change the label
        String text = getArtist(title);
        artistNameLabel.setText(text);
    }

    public void refreshTimeSlider()
    {
        //Set the sliders max value to the duration
        timeSlider.setMax(player.getTotalDuration().toSeconds());

        //Update end time label
        String smax = String.format("%02d:%02d", round((timeSlider.getMax() / 60) % 60), round(timeSlider.getMax() % 60));

        endtime.setText(smax);

        //Update the sliders time
        player.currentTimeProperty().addListener((obs2, oldTime, newTime) -> {

            //Update the slider
            timeSlider.setValue(newTime.toSeconds());

            //Update the start time label
            String smin = String.format("%02d:%02d", round((newTime.toSeconds() / 60) % 60), round(newTime.toSeconds() % 60));

            starttime.setText(smin);
        });
    }

    @FXML
    public void playMusic()
    {

        //If the folder was empty, there is nothing to play
        if(this.pos == -1)
            changeStatus("No music is available!");
        else {
            //If the player can't get the volume, then it's the first initialization stage
            try{
                player.getVolume();
            } catch(Exception e) {

                //Dummy init to access the property
                Media media = new Media(musicList.get(this.pos));
                this.player = new MediaPlayer(media);

                // Volume Control
                volumeSlider.setValue(100);
                volumeSlider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        player.setVolume(volumeSlider.getValue() / 100);
                        //Formatting the text and convert it into percentage
                        String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                        //Write out Volume
                        Volumelabel.setText(text);
                    }
                });
            }
            //Create a new player with the new music and set the previous volume for it
            this.player = new MediaPlayer(new Media(musicList.get(this.pos)));

            //Set the volume
            player.setVolume(volumeSlider.getValue() / 100);

            //Initialize Music slider

            if (player.getStatus() == MediaPlayer.Status.UNKNOWN) {

                //If the player is not initialized, wait until it is initialized

                player.statusProperty().addListener((obs, oldStatus, newStatus) -> {

                    if (newStatus == MediaPlayer.Status.READY)
                        //Update the slider
                        refreshTimeSlider();
                });
            }
            else
                //Update the slider
                refreshTimeSlider();

            //Play the music
            player.play();

            //Update the status label
            changeStatus( musicNames.get(this.pos));

            changeArtist(musicNames.get(this.pos));

        }
    }

    @FXML
    public void volUp()
    {
        //If volume is not at max, increase the volume
        if(player.getVolume() != 1.0)
        {
            player.setVolume(player.getVolume() + 0.1);
            if (player.getVolume()>=1.0)
            {
                player.setVolume(1.0);
            }

            //Only display if its not muted

            if (!player.isMute())
            {
                //Formatting the text and convert it into percentage
                String text = String.format("Volume: %.0f %%", player.getVolume() * 100);
                volumeSlider.setValue(player.getVolume()*100);

                //Write out Volume
                Volumelabel.setText(text);
            }
        }
    }

    @FXML
    public void volDown()
    {
        //If music is not at 0 volume, decrease it
        if(player.getVolume() >= 0.0)
        {
            player.setVolume(player.getVolume() - 0.1);
            if (player.getVolume()<=0.1)
            {
                player.setVolume(0.0);
            }

            //Only display if its not muted

            if (!player.isMute())
            {
                //Formatting the text and convert it into percentage
                String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                volumeSlider.setValue(player.getVolume()*100);

                //Write out Volume
                Volumelabel.setText(text);
            }
        }
    }



    @FXML
    public void next()
    {
        //If the music list reached the end, restart it
        if (pos == musicList.size()-1)
            pos = 0;
        else
            pos += 1;

        //Play the next music
        player.stop();
        playMusic();
    }

    @FXML
    public void prev()
    {
        //If the list reached the beginning, start from behind
        if (pos == 0)
            pos = musicList.size() - 1;
        else
            pos -= 1;

        //Play the previous music
        player.stop();
        playMusic();
    }

    @FXML
    void like() {
        if(liked==false)
        {
            heart.setImage(new Image(getClass().getResourceAsStream("img/heart1.png")));
            liked=true;

        }
        else
        {
            heart.setImage(new Image(getClass().getResourceAsStream("img/heart2.png")));
            liked=false;

        }


    }
}
