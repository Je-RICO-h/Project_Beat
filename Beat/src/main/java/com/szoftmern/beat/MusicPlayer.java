package com.szoftmern.beat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.*;

import static com.szoftmern.beat.DatabaseManager.*;
import static com.szoftmern.beat.EntityUtil.*;
import static com.szoftmern.beat.UIController.loadCountriesIntoCombobox;
import static com.szoftmern.beat.UIController.settingLikeButton;
import static java.lang.Math.round;

public class MusicPlayer {
    @FXML
    private TextField newPlaylistName;
    @FXML
    protected Label newPlaylistNameInfoLabel;
    @FXML
    protected ListView<String> playlistList;
    @FXML
    private Label lejatszasilistaLabel;
    @FXML
    protected VBox oneArtistSongs;
    @FXML
    protected VBox playlistTracksVBox;
    @FXML
    protected Label oneArtistName;
    @FXML
    protected Label selectedPlaylistNameLabel;
    @FXML
    protected AnchorPane oneArtistbox;
    @FXML
    protected AnchorPane playlistbox;
    @FXML
    protected AnchorPane onePlaylistbox;
    @FXML
    protected ImageView movingItem;
    @FXML
    protected GridPane artistGrid;
    @FXML
    protected VBox favoriteListContainer;
    @FXML
    protected VBox userPlaylistsVBox;
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
    protected ImageView heart;
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

    private final SearchManager searchManager;
    private final TopMusicManager topMusicManager;
    private final HistoryManager historyManager;
    private final ArtistManager artistManager;
    private final FavoriteManager favoriteManager;
    private  final PlayListManager playListManager;
    private  final StatisticManager staticticManager;
    private boolean volumeInit = false;

    @FXML
    protected AnchorPane homebox;
    @FXML
    protected AnchorPane settingsbox;
    @FXML
    protected AnchorPane artistbox;
    @FXML
    protected AnchorPane favouritebox;
    @FXML
    protected AnchorPane statisticbox;

    @FXML
    protected Label artistLabel;
    @FXML
    protected Label allRegistrationNumber;
    @FXML
    protected VBox VBoxCountryUserDistributionChart;
    @FXML
    protected VBox VBoxCountryTrackDistributionChart;

    @FXML
    public Button saveButton;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField oldPasswordField;
    @FXML
    public TextField newPasswordField;
    @FXML
    public TextField newPasswordConfirmationField;
    @FXML
    private ComboBox<String> countryPicker;
    @FXML
    private ComboBox<String> genderPicker;
    @FXML
    protected Button badWordsToggleBtn;
    @FXML
    protected Label resetInfoLabel;

    private SettingsManager settingsManager;

    List<String> playlistItems = new ArrayList<>();

    @FXML
    public void initialize() {
        settingsManager = new SettingsManager(
                saveButton,
                usernameField,
                emailField,
                oldPasswordField,
                newPasswordField,
                newPasswordConfirmationField,
                genderPicker,
                countryPicker,
                badWordsToggleBtn,
                resetInfoLabel
        );

        //set homepage firs
        UIController.setMiddlePain(homebox, settingsbox, artistbox, favouritebox, statisticbox, oneArtistbox, playlistbox, onePlaylistbox);

        //set the country list
        loadCountriesIntoCombobox(countryPicker);

        //set the original data from database
        settingsManager.displayCurrentAccountInfo();

        //label setting in two lines
        lejatszasilistaLabel.setText("Lejátszási\nlisták");

        playlistList.setVisible(false);
        playlistList.setDisable(true);


        playListManager.loadUserPlaylists();
        playListManager.onClickedSaveTrackToSelectedPlaylist(playlistList);
    }


    //Constructor
    public MusicPlayer() {
        this.searchManager = new SearchManager(this);
        this.topMusicManager = new TopMusicManager(this);
        this.historyManager = new HistoryManager(this);
        this.artistManager = new ArtistManager(this);
        this.favoriteManager = new FavoriteManager(this);
        this.playListManager = new PlayListManager(this);
        this.staticticManager = new StatisticManager(this);
        this.musicList = getEveryTrack();

        this.pos = 0;

        topMusicManager.updateTopList();

        //Dummy init to access the property
        Media media = new Media(musicList.get(this.pos).getResourceUrl());
        this.player = new MediaPlayer(media);
    }


    //Init for the volume slider
    public void initVolumeSlider() {

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
            movingItem.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/giphy2.png"))));

        } else {
            player.play();
//            changeStatus(musicList.get(this.pos).getTitle());
            play_pause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/pause.png"))));
            movingItem.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/giphy.gif"))));
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
        artistNameLabel.setText(text.substring(1, text.length() - 1) + " ");

        if (artistNameLabel.getText().length() > 17) {
            UIController.movingLabel(artistNameLabel);
        } else {
            UIController.stopTimeLine();
        }
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

            incrementListenCount(musicList.get(this.pos));

            liked = isLiked(musicList.get(this.pos));

            settingLikeButton(this, liked);
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
    public void like() {
        favoriteManager.onPressedLikeButton(liked);
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


    @FXML
    void logout(ActionEvent event) {
        //Stop the player
        this.player.stop();

        updateDatabaseCountryPlayCount();
        updateDatabaseTrackPlayCount();

        UIController.makeNewStage(event,"login.fxml");
        userbox.setVisible(false);
        userbox.setDisable(true);
        user = false;

        // log out the user
        DatabaseManager.loggedInUser.setLoggedIn(false);
        DatabaseManager.userDAO.saveEntity(DatabaseManager.loggedInUser);

        System.out.println("User " + DatabaseManager.loggedInUser.getName() + " logged out successfully");
        DatabaseManager.loggedInUser = null;
    }


    boolean user = false;
    @FXML
    void user_selected() {

        if(user){
            userbox.setVisible(false);
            userbox.setDisable(true);
            user = false;
        }
        else {
            userbox.setVisible(true);
            userbox.setDisable(false);
            user = true;
        }
    }


    @FXML
    void settings_selected() {
        UIController.setMiddlePain(settingsbox, homebox, artistbox, favouritebox, statisticbox, oneArtistbox,playlistbox,onePlaylistbox);

        userbox.setVisible(false);
        userbox.setDisable(true);
        user = false;
    }


    @FXML
    void home_selected() {
        UIController.setMiddlePain(homebox, settingsbox, artistbox, favouritebox, statisticbox, oneArtistbox,playlistbox,onePlaylistbox);
    }


    @FXML
    void artist_selected() {
        //set the list of artist to Előadók
        artistGrid.getChildren().clear();
        artistManager.writeArtistToBlock();
        UIController.setMiddlePain(artistbox, homebox, settingsbox, favouritebox, statisticbox, oneArtistbox,playlistbox,onePlaylistbox);
    }


    @FXML
    void favourite_selected() {
        favoriteManager.writeFavoriteTracks();
        UIController.setMiddlePain(favouritebox, artistbox, homebox, settingsbox, statisticbox, oneArtistbox,playlistbox,onePlaylistbox);
    }


    @FXML
    void statistic_selected() {
        VBoxCountryUserDistributionChart.getChildren().clear();
        VBoxCountryTrackDistributionChart.getChildren().clear();
        staticticManager.writeAllRegistrationNumber();
        staticticManager.populateCountryUserDistributionChart();
        staticticManager.populateCountryTrackDistributionChart();
        UIController.setMiddlePain(statisticbox, artistbox, homebox, settingsbox, favouritebox, oneArtistbox,playlistbox,onePlaylistbox);
    }


    @FXML
    void logo_selected() {
        home_selected();
    }


    //Save the new settings data
    @FXML
    void save_newData() {
        try {
            settingsManager.uploadNewUserAccountInfoToDatabase();
        } catch (IncorrectInformationException e) {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    void toggleBadWords() {
        settingsManager.toggleBadWordsFlag();
    }

    @FXML
    void playlist_selected() {
        UIController.setMiddlePain(playlistbox,statisticbox, artistbox, homebox, settingsbox, favouritebox, oneArtistbox,onePlaylistbox);
    }

    boolean addplaylistIcon = false;
    @FXML
    void addPlaylistIcon_selected() {

        if(addplaylistIcon) {
            playlistList.setVisible(false);
            playlistList.setDisable(true);

            addplaylistIcon = false;
        } else {
            playlistList.setVisible(true);
            playlistList.setDisable(false);

            addplaylistIcon = true;
        }
    }

    @FXML
    void leaveSearchContainer() {
        searchcontener.setVisible(false);
        searchcontener.setDisable(true);
        searchTextField.setText("");
    }

    @FXML
    void leaveAddPlaylistIcon() {
        playlistList.setVisible(false);
        playlistList.setDisable(true);
        addplaylistIcon = false;
    }

    @FXML
    void newPlaylistAdd_selected() {
        playListManager.createNewPlaylist(newPlaylistName.getText());

        if(!newPlaylistName.getText().isEmpty()) {
            playlistItems.add(newPlaylistName.getText());
        }

        newPlaylistName.clear();
    }
}
