package com.szoftmern.beat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.EntityUtil.*;
import static com.szoftmern.beat.UIController.loadCountriesIntoCombobox;
import static java.lang.Math.round;

public class MusicPlayer {
    @FXML
    public BorderPane border;
    @FXML
    private VBox userbox;
    @FXML
    protected VBox historylistContener;
    @FXML
    protected VBox toplistContener;
    @FXML
    protected VBox searchResultView;
    @FXML
    protected ScrollPane searchcontener;
    @FXML
    private ImageView loop_icon;
    //Declaration of Labels, Buttons etc.
    @FXML
    private ImageView heart;
    @FXML
    protected ImageView play_pause;
    @FXML
    private ImageView sound;
    public Label statuslabel;
    public Label artistNameLabel;
    public TextField searchTextField;

    public Button playbutton;
    public Slider volumeSlider;
    public Label starttime;
    public Label endtime;
    private MediaPlayer player;
    public Slider timeSlider;
    public int pos = -1;

    //List for the musicsList<Track> musicList
    public List<Track> musicList = new ArrayList<>();

    //List for previously played music
    public List<Track> musicHistory = new ArrayList<>();

    boolean liked = false;
    boolean loop = false;

    private SearchManager searchManager;
    private TopMusicManager topMusicManager;
    private HistoryManager historyManager;
    private boolean volumeInit = false;

    //Constructor
    public MusicPlayer() {
        this.searchManager = new SearchManager(this);
        this.topMusicManager = new TopMusicManager(this);
        this.historyManager = new HistoryManager(this);

        this.musicList = getEveryTrack();

        this.pos = 0;

        topMusicManager.updateTopList();

        //Dummy init to access the property
        Media media = new Media(musicList.get(this.pos).getResourceUrl());
        this.player = new MediaPlayer(media);
    }

    //Init for the volume slider
    public void initVolumeSlider(){

        // Volume Control
        this.volumeSlider.setValue(50);
        this.volumeSlider.setValue(50);

        //Volume slider init
        this.volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

   /* @FXML
    public void selectedSearchItem() {
        searchManager.selectedSearchItem();
    }*/

    @FXML
    public void onActionSearchButton() {
        searchManager.onActionSearchButton();
    }

    @FXML
    public void onKeyPressedSearchTextField() {
        searchManager.onKeyPressedSearchTextField();
    }

    @FXML
    public void mute() {
        //If volume is 0 percent and muting, get it to 50%
        if (player.getVolume() == 0.0) {
            player.setVolume(0.5);
            volumeSlider.setValue(50);
        } else
            //Not logic on the mute button
            player.setMute(!player.isMute());


        //Set the volume status text

        if (player.isMute() && player.getVolume() != 0.0)
            sound.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/mute.png"))));
        else
            sound.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/sound.png"))));
    }

    @FXML
    void pausePlay() {
        //If status of the player is playing, pause the music, else play it
        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
            play_pause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/play.png"))));
        } else {
            player.play();
//            changeStatus(musicList.get(this.pos).getTitle());
            play_pause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/pause.png"))));
        }
    }

    @FXML
    public void changeStatus(String text) {
        //Function to change the label
        statuslabel.setText(text);
    }

    @FXML
    public void changeArtist() {
        List<String> artistsName = getArtistNameList(musicList.get(pos).getArtists());
        String text = artistsName.toString();
        artistNameLabel.setText(text.substring(1, text.length() - 1));
    }

    public void refreshTimeSlider() {
        //Set the sliders max value to the duration
        timeSlider.setMax(player.getTotalDuration().toSeconds() - 0.3);

        //Update end time label
        String smax = String.format("%02d:%02d", round((timeSlider.getMax() / 60) % 60), round(timeSlider.getMax() % 60));

        endtime.setText(smax);

        //Update the sliders time
        player.currentTimeProperty().addListener((obs2, oldTime, newTime) -> {
            //If it is not being dragged, update the time
            if (!timeSlider.isValueChanging()) {
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
    public void playMusic() {

        //If the folder was empty, there is nothing to play
        if (this.pos == -1)
            changeStatus("No music is available!");
        else {

            //Create a new player with the new music and set the previous volume for it
            this.player = new MediaPlayer(new Media(musicList.get(this.pos).getResourceUrl()));

            //Update the music history by appending this music to it if its not into it
            historyManager.addTrackToHistoryList(musicList.get(this.pos));
            historyManager.displayhistory();

            //If volume not initialized, initialize it
            if(!volumeInit) {
                initVolumeSlider();
                volumeInit = true;
            }

            player.setVolume(volumeSlider.getValue() / 100);


            //Initialize Music slider

            if (player.getStatus() == MediaPlayer.Status.UNKNOWN) {

                //If the player is not initialized, wait until it is initialized

                player.statusProperty().addListener((obs, oldStatus, newStatus) -> {

                    if (newStatus == MediaPlayer.Status.READY)
                        //Update the slider
                        refreshTimeSlider();
                });
            } else
                //Update the slider
                refreshTimeSlider();

            //Play the music
            player.play();

            //Update the status label
            changeStatus(musicList.get(this.pos).getTitle());

            changeArtist();

            System.out.println(musicList.get(this.pos).getPlayCount());
            incrementListenCount(musicList.get(this.pos));
            System.out.println(musicList.get(this.pos).getPlayCount());
        }
    }

    @FXML
    public void volUp() {
        //If volume is not at max, increase the volume
        if (player.getVolume() != 1.0) {
            player.setVolume(player.getVolume() + 0.1);
            if (player.getVolume() >= 1.0) {
                player.setVolume(1.0);
            }

            //Only display if its not muted

            if (!player.isMute()) {
                //Formatting the text and convert it into percentage
                //String text = String.format("Volume: %.0f %%", player.getVolume() * 100);
                volumeSlider.setValue(player.getVolume() * 100);

                //Write out Volume
                //Volumelabel.setText(text);
            }
        }
    }

    @FXML
    public void volDown() {
        //If music is not at 0 volume, decrease it
        if (player.getVolume() >= 0.0) {
            player.setVolume(player.getVolume() - 0.1);
            if (player.getVolume() <= 0.1) {
                player.setVolume(0.0);
            }

            //Only display if its not muted

            if (!player.isMute()) {
                //Formatting the text and convert it into percentage
                //String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                volumeSlider.setValue(player.getVolume() * 100);

                //Write out Volume
                //Volumelabel.setText(text);
            }
        }
    }


    @FXML
    public void next() {
        //If the music list reached the end, restart it
        if (pos == musicList.size() - 1)
            pos = 0;
        else
            //If the music is set to loop, replay the music, else go to the next
            if (!loop)
                pos += 1;

        //Play the next music
        player.stop();
        playMusic();
    }

    @FXML
    public void prev() {
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
        if (liked == false) {
            heart.setImage(new Image(getClass().getResourceAsStream("img/heart1.png")));
            liked = true;

        } else {
            heart.setImage(new Image(getClass().getResourceAsStream("img/heart2.png")));
            liked = false;

        }
    }

    @FXML
    void loop() {
        //Set loop with button
        loop = !loop;

        if (loop) {
            loop_icon.setImage(new Image(getClass().getResourceAsStream("img/loop2.png")));
        } else {
            loop_icon.setImage(new Image(getClass().getResourceAsStream("img/loop1.png")));
        }
    }


    boolean user = false;
    @FXML
    void user_selected() {

        if(user == false){
            userbox.setVisible(true);
            userbox.setDisable(false);
            user = true;
        }
        else {
            userbox.setVisible(false);
            userbox.setDisable(true);
            user = false;
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {

        //Stop the player
        this.player.stop();

        //UIController.switchScene(border, "login.fxml");
        UIController.makeNewStage(event,"login.fxml");
        userbox.setVisible(false);
        userbox.setDisable(true);
        user=false;
    }


    @FXML
    private Pane homebox;
    @FXML
    private Pane settingsbox;
    @FXML
    private Pane artistbox;
    @FXML
    private Pane favouritebox;
    @FXML
    public TextField username_settings;
    @FXML
    public TextField password_settings;
    @FXML
    public TextField email_settings;
    @FXML
    private ComboBox<String> country_setting;
    @FXML
    private ComboBox<String> gender_settings;
    @FXML
    private ComboBox<String> color_settings;

    @FXML
    public void initialize() {
        //set homepage firs
        UIController.setMiddlePain(homebox,settingsbox,artistbox,favouritebox);

        //set the country list
        loadCountriesIntoCombobox(country_setting);

        //set the original data from database
        SettingsManager.originalTexts(username_settings,email_settings,password_settings,country_setting,gender_settings);

        SettingsManager.setColorPickerBox(color_settings);
    }



    @FXML
    void settings_selected() {
        UIController.setMiddlePain(settingsbox,homebox,artistbox,favouritebox);
        userbox.setVisible(false);
        userbox.setDisable(true);
        user = false;

    }

    @FXML
    void home_selected() {UIController.setMiddlePain(homebox,settingsbox,artistbox,favouritebox);}
    @FXML
    void artist_selected() {UIController.setMiddlePain(artistbox,homebox,settingsbox,favouritebox);}
    @FXML
    void favourite_selected() {UIController.setMiddlePain(favouritebox,artistbox,homebox,settingsbox);}
    @FXML
    void logo_selected() {home_selected();}

    //Save the new settings data
    @FXML
    void save_newData(){SettingsManager.saveData(username_settings,email_settings,password_settings,country_setting,gender_settings);};


}