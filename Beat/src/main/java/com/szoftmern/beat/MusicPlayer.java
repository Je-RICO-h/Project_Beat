package com.szoftmern.beat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //List for previously played music
    private Set<String> musicHistory = new HashSet<>();

    boolean liked = false;
    boolean loop = false;

    //Constructor
    public MusicPlayer() {
//        //Get music files from folder
//        File folder = new File("Assets");
//
//        //Initialize the .mp3 filter
//        FileFilter filter = new FileFilter() {
//            public boolean accept(File f)
//            {
//                return f.getName().endsWith("mp3");
//            }
//        };

        //Loop through the folder, and get every mp3 music file, and load it in
//        for (File file : folder.listFiles(filter)) {
//            Media sound = new Media(file.toURI().toString());
//            this.musicList.add(sound);
//            this.musicNames.add(file.getName());
//            pos = 0;
//        }
        this.musicNames = getSearchDatabase("");

        for (String title: musicNames) {
            String URL = getTrackURL(title);
            this.musicList.add(URL);
        }
        pos = 0;
    }

//    @FXML
//    public void selectedSearchItem(){
//        String selectedItem = searchResultView.getSelectionModel().getSelectedItem();
//        this.musicName = selectedItem;
//        System.out.println("Kiválasztott elem: " + selectedItem);
//    }
//
//    public void selectedTopListItem(){
//        String selectedItem = topMusicList.getSelectionModel().getSelectedItem();
//        this.musicName = selectedItem;
//        System.out.println("Kiválasztott elem: " + selectedItem);
//    }
//
//    @FXML
//    public void search() {
//        String keyword = searchBar.getText();
//        ObservableList<String> result = FXCollections.observableArrayList(getSearchDatabase(keyword));
//        searchResultView.setItems(result);
//    }
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

    public void refreshTimeSlider()
    {
        //Set the sliders max value to the duration
        timeSlider.setMax(player.getTotalDuration().toSeconds());

        //Update end time label
        String smax = String.format("%02d:%02d", round((timeSlider.getMax() / 60) % 60), round(timeSlider.getMax() % 60));

        endtime.setText(smax);

        //Update the sliders time
        player.currentTimeProperty().addListener((obs2, oldTime, newTime) -> {
            //If it is not being dragged, update the time
            if (! timeSlider.isValueChanging()) {
                timeSlider.setValue(newTime.toSeconds());
            }

            //Update the start time label
            String smin = String.format("%02d:%02d", round((newTime.toSeconds() / 60) % 60), round(newTime.toSeconds() % 60));

            starttime.setText(smin);

            //If music is finished, play the next song
            if (newTime.toSeconds() >= player.getTotalDuration().toSeconds())
                next();
        });

        //Seek the player, if the slider drag is stopped
        timeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                player.seek(Duration.seconds(timeSlider.getValue()));
            }
        });

        //Change the value property of the player
        timeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!timeSlider.isValueChanging()) {
                double currentTime = player.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    player.seek(Duration.seconds(newValue.doubleValue()));
                }
            }
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

            //Update the music history by appending this music to it if its not into it
            this.musicHistory.add(musicList.get(this.pos));

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
            //If the music is set to loop, replay the music, else go to the next
            if(!loop)
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

    @FXML
    void loop(){
        //Set loop with button
        loop = !loop;
    }
}
