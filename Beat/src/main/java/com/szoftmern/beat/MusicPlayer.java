package com.szoftmern.beat;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.EntityUtil.*;
import static java.lang.Math.round;

public class MusicPlayer implements Initializable {
    @FXML
    private VBox colorbox;
    @FXML
    private BorderPane border;
    @FXML
    private VBox userbox;
    @FXML
    private VBox historylistContener;
    @FXML
    private VBox toplistContener;
    @FXML
    private ImageView loop_icon;
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
    public ListView<String> topListView;
    public ListView<String> historyListView;

    public Button playbutton;
    public Slider volumeSlider;
    public Label musicDuration;
    public Label starttime;
    public Label endtime;
    private MediaPlayer player;
    public Slider timeSlider;
    private int pos = -1;

    //List for the musics
    private List<Track> musicList = new ArrayList<>();

    //List for previously played music
    private Set<Track> musicHistory = new HashSet<>();

    private Timer searchTimer;
    private String currentKeyword = "";

    boolean liked = false;
    boolean loop = false;

    //Constructor
    public MusicPlayer() {
        for (Track track: getEveryTrack()) {
            this.musicList.add(track);
        }

        this.pos = 0;

        searchTimer = new Timer();

        //Dummy init to access the property
        Media media = new Media(musicList.get(this.pos).getResourceUrl());
        this.player = new MediaPlayer(media);

        this.volumeSlider = new Slider(0,100,50); //NEEDS TO BE FIXED

        // Volume Control
        this.volumeSlider.setValue(100);

        //Volume slider init
        this.volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Frissítési feladat végrehajtása (toplista frissítése)
                updateTopList();
            }
        };

        // Időzítő beállítása 5 perces periódussal
        timer.schedule(task, 0, 300000);
    }

    public Song songmaker(Track s){
        Song song=new Song();
        song.setCover("img/zene.png");
        song.setName(s.getTitle());
        String artist=String.valueOf(getArtistNameList(s.getArtists()));
        song.setArtist_name(artist.substring(1, artist.length() - 1));
        return song;
    }

    public void updateTopList() {
        ObservableList<Track> top = FXCollections.observableArrayList(getTopMusicList());

        Platform.runLater(() -> {
            /*topListView.getItems().clear();
            int count = 1;
            for (Track track : top) {
                topListView.getItems().add(count + ". " + track.getTitle() + "\n" + getArtistNameList(track.getArtists()));
                count++;
            }*/

            int counter=1;
            toplistContener.getChildren().clear();
            for(Track s:top) {
                String img="img/numbers/"+String.valueOf(counter)+".png";

                HBox hBox1=new HBox();
                ImageView imageView=new ImageView(new Image(getClass().getResourceAsStream(img)));
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);

                Song song = songmaker(s);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("song.fxml"));
                HBox hBox = null;
                try {
                    hBox = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SongController songController = fxmlLoader.getController();
                songController.SetData(song);
                hBox.setOnMouseClicked(mouseEvent -> {
                    System.out.println(song.getName());
                    String title = song.getName();
                    pos = this.musicList.indexOf(getTrackFromTitle(title)) - 1;
                    next();
                    System.out.println("Kiválasztott elem: " + title);
                    play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));


                });

                hBox1.getChildren().addAll(imageView,hBox);
                toplistContener.getChildren().add(hBox1);
                hBox1.setSpacing(15);
                counter++;
                if (counter>10){
                    img="img/numbers/10.png";
                }
            }
        });
    }


    @FXML
    public void selectedSearchItem(){
        String selectedItem = searchResultView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);
        pos = this.musicList.indexOf(getTrackFromTitle(selectedItem.split("\n")[0])) - 1;
        next();
        searchResultView.setVisible(false);
        System.out.println("Kiválasztott elem: " + selectedItem);
    }


    /*@FXML
    public void selectedTopListItem(){
        String selectedItem = topListView.getSelectionModel().getSelectedItem();
        String title = selectedItem.split("\n")[0].substring(selectedItem.split("\n")[0].indexOf(" ") == 3 ? 4 : 3);
        System.out.println(title);
        pos = this.musicList.indexOf(getTrackFromTitle(title)) - 1;
        next();
        System.out.println("Kiválasztott elem: " + selectedItem);
    }*/


    @FXML
    public void selectedHistoryMusicItem(){
        String selectedItem = historyListView.getSelectionModel().getSelectedItem();
        pos = this.musicList.indexOf(getTrackFromTitle(selectedItem.split("\n")[0])) - 1;
        next();
        System.out.println("Kiválasztott elem: " + selectedItem);
    }

    @FXML
    public void onActionSearchButton() {
        search();
    }

    @FXML
    public void onKeyPressedSearchTextField() {
        searchTimer.cancel();
        searchTimer = new Timer();
        searchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                search();
            }
        }, 1500); // 1500 ms = 1.5 másodperc késleltetés
    }

    public void search() {
        String keyword = searchTextField.getText();
        currentKeyword = keyword;

        if (!keyword.isEmpty()) {
            // Ellenőrizd, hogy a keresett kulcsszó megegyezik-e a jelenlegi kulcsszóval
            if (!currentKeyword.equals(searchTextField.getText())) {
                return; // Ha nem, ne végezd el a keresést
            }

            ObservableList<Track> result = FXCollections.observableArrayList(searchDatabaseForTracks(keyword));

            Platform.runLater(() -> {
                searchResultView.getItems().clear();
                for (Track track : result) {
                    searchResultView.getItems().add(track.getTitle() + "\n" + getArtistNameList(track.getArtists()));
                }
            });

            searchResultView.setVisible(true);
            searchResultView.toFront();
        } else {
            searchResultView.setVisible(false);
        }
    }


    public void displayhistory() {
        ObservableList<Track> result = FXCollections.observableArrayList(musicHistory);

        Platform.runLater(() -> {
          
            /*topListView.getItems().clear();
            int count = 1;
            for (Track track : top) {
                topListView.getItems().add(count + ". " + track.getTitle() + "\n" + getArtistNameList(track.getArtists()));
                count++;
            }*/


            historylistContener.getChildren().clear();
            for(Track s:result) {

                Song song = songmaker(s);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("song.fxml"));
                HBox hBox = null;
                try {
                    hBox = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SongController songController = fxmlLoader.getController();
                songController.SetData(song);
                hBox.setOnMouseClicked(mouseEvent -> {
                    System.out.println(song.getName());
                    String title = song.getName();
                    pos = this.musicList.indexOf(getTrackFromTitle(title)) - 1;
                    next();
                    System.out.println("Kiválasztott elem: " + title);
                    play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));


                });
                historylistContener.getChildren().add(hBox);
            }
        });
    }


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

        if(player.isMute() && player.getVolume() != 0.0)
            sound.setImage(new Image(getClass().getResourceAsStream("img/mute.png")));
        else
            sound.setImage(new Image(getClass().getResourceAsStream("img/sound.png")));
    }


    @FXML void pausePlay()
    {
//        //If player is not initialized, initialize it
//        if(player == null)
//        {
//            playMusic();
//            play_pause.setImage(new Image(getClass().getResourceAsStream("img/pause.png")));
//
//        }
        //If status of the player is playing, pause the music, else play it
        if(player.getStatus() == MediaPlayer.Status.PLAYING)
        {
            player.pause();
            play_pause.setImage(new Image(getClass().getResourceAsStream("img/play.png")));
        }
        else
        {
            player.play();
            changeStatus( musicList.get(this.pos).getTitle());
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
    public void changeArtist(List<String> artistsName)
    {
        //Function to change the label
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < artistsName.size(); i++) {
            text.append(artistsName.get(i));

            if (i != artistsName.size() - 1) {
                text.append(", ");
            }
        }
        artistNameLabel.setText(text.toString());
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
            //Get Volume
            player.getVolume();

            //Create a new player with the new music and set the previous volume for it
            this.player = new MediaPlayer(new Media(musicList.get(this.pos).getResourceUrl()));

            //Update the music history by appending this music to it if its not into it
            this.musicHistory.add(musicList.get(this.pos));

            displayhistory();

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
            changeStatus( musicList.get(this.pos).getTitle());

            changeArtist(getArtistNameList(musicList.get(this.pos).getArtists()));

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
                //String text = String.format("Volume: %.0f %%", player.getVolume() * 100);
                volumeSlider.setValue(player.getVolume()*100);

                //Write out Volume
                //Volumelabel.setText(text);
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
                //String text = String.format("Volume: %.0f %%", player.getVolume() * 100);

                volumeSlider.setValue(player.getVolume()*100);

                //Write out Volume
                //Volumelabel.setText(text);
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

        if(loop){
            loop_icon.setImage(new Image(getClass().getResourceAsStream("img/loop2.png")));
        }
        else {
            loop_icon.setImage(new Image(getClass().getResourceAsStream("img/loop1.png")));
        }
    }


    boolean user=false;
    @FXML
    void user_selected() {

        if(user==false){
            userbox.setVisible(true);
            userbox.setDisable(false);
            user=true;
        }
        else {
            userbox.setVisible(false);
            userbox.setDisable(true);
            user=false;
        }
    }

    @FXML
    void logut() throws IOException {

        //Stop the player
        this.player.stop();

        new SceneSwitch(border, "login.fxml");
    }

    boolean color=false;
    @FXML
    void colors() {
        if(color==false){
            colorbox.setVisible(true);
            colorbox.setDisable(false);
            color=true;
        }
        else {
            colorbox.setVisible(false);
            colorbox.setDisable(true);
            color=false;

        }
    }


}