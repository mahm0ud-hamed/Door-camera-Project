/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author asem
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    protected MediaView mediaVew;
    @FXML
    private Button choosebtn;
    @FXML
    private Button playbtn;
    @FXML
    private Button PauseBtn;
    private String mediaPath;
    private MediaPlayer mediaplayer;
    @FXML
    private Button stopBtn;
    @FXML
    private Button fastBtn;
    @FXML
    private Button slowBtn;
    @FXML
    private Button plsTenSecBtn;
    @FXML
    private Button minusTenSecBtn;
    @FXML
    private Slider sliderBar;
    @FXML
    private Button LiveBtn;
    @FXML
    private BorderPane borderpane;
    @FXML
    private HBox ButtonHbox;
    @FXML
    private Label timecount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        playbtn.getStyleClass().add("playbtn");
        //  LiveBtn.getStyleClass().add("LiveBtn");
        //ButtonHbox.getStyleClass().add("playbtn");
        timecount = new Label("mm:MM:mm");
    }

    @FXML
    private void PauseButtonAction(ActionEvent event) {

        mediaplayer.pause();
    }

    @FXML
    private void ChooseFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        mediaPath = file.toURI().toString();
        if (mediaPath != null) {
            Media media = new Media(mediaPath);
            mediaplayer = new MediaPlayer(media);
            mediaVew.setMediaPlayer(mediaplayer);

            /*double click to full screen */
            DoubleProperty WidthpProp = mediaVew.fitWidthProperty();
            DoubleProperty HeightProp = mediaVew.fitHeightProperty();
            HeightProp.bind(Bindings.selectDouble(mediaVew.sceneProperty(), "Height"));
            WidthpProp.bind(Bindings.selectDouble(mediaVew.sceneProperty(), "width"));

            /* handling of movement of the slider bar */
            mediaplayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                sliderBar.setValue(newValue.toSeconds());
                updateCounter(newValue.toSeconds());

            });

            /* get the value od duration and assignn it to the silder bar when the mouse is pressed in specific location*/
            sliderBar.setOnMousePressed((MouseEvent e) -> {
                mediaplayer.seek(Duration.seconds(sliderBar.getValue()));
            });

            /* get the value video duration when the mouse dragged the slider bar to an specific duration */
            sliderBar.setOnMouseDragged((MouseEvent e) -> {
                mediaplayer.seek(Duration.seconds(sliderBar.getValue()));
            });

            /* thread that will get the total duration of the video and git it to the progrees 
            bar to make progress bar reach to the end when video end */
            mediaplayer.setOnReady(() -> {

                Duration total = media.getDuration();
                sliderBar.setMax(total.toSeconds());
              //S  startTimeCounter();

            });

            mediaplayer.setVolume(1);
            mediaplayer.play();
        }
    }

   /* private void startTimeCounter() {
        // Create a Timeline to update the remaining time counter every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateCounter()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
*/
    private void updateCounter(double currentTime) {
        // Convert the current time to hours, minutes, seconds
        int hours = (int) (currentTime / 3600); // 3600 seconds in an hour
        int minutes = (int) ((currentTime % 3600) / 60); // 60 seconds in a minute
        int seconds = (int) (currentTime % 60); // Remaining seconds

        // Format the time as HH:MM:SS
        String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        // Print debugging information
        System.out.println("Updating Label: " + timeText);  // Debugging

        // Update the label on the JavaFX Application Thread
        Platform.runLater(() -> {
            timecount.setText(timeText);  // Ensure label is updated on the JavaFX thread
        });
    }

    /*   public void setScene(Scene scene){
        
    }*/

    @FXML
    private void playButtonAction(ActionEvent event) {

        mediaplayer.play();
        mediaplayer.setRate(1);

    }

    @FXML
    private void StopButtonAction(ActionEvent event) {

        mediaplayer.stop();
    }

    @FXML
    private void fastButtonAction(ActionEvent event) {

        mediaplayer.setRate(2);
    }

    @FXML
    private void SlowButtonAction(ActionEvent event) {
        mediaplayer.setRate(0.5);
    }

    @FXML
    private void plsTenSecondAction(ActionEvent event) {
        mediaplayer.seek(mediaplayer.getCurrentTime().add(Duration.seconds(10)));
    }

    @FXML
    private void MinusTenSecondAction(ActionEvent event) {

        mediaplayer.seek(mediaplayer.getCurrentTime().add(Duration.seconds(-10)));

    }

    @FXML
    private void LiveStreamAcion(ActionEvent event) {

        try {
            BorderPane Live = FXMLLoader.load(getClass().getResource("LiveView.fxml"));
            borderpane.setCenter(Live);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author asem
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    protected MediaView mediaVew;
    @FXML
    private Button choosebtn;
    @FXML
    private Button playbtn;
    @FXML
    private Button PauseBtn;
    private String mediaPath;
    private MediaPlayer mediaplayer;
    @FXML
    private Button stopBtn;
    @FXML
    private Button fastBtn;
    @FXML
    private Button slowBtn;
    @FXML
    private Button plsTenSecBtn;
    @FXML
    private Button minusTenSecBtn;
    @FXML
    private Slider sliderBar;
    @FXML
    private Button LiveBtn;
    @FXML
    private BorderPane borderpane;
    @FXML
    private HBox ButtonHbox;
    @FXML
    private Label timecount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        playbtn.getStyleClass().add("playbtn");
        //  LiveBtn.getStyleClass().add("LiveBtn");
        //ButtonHbox.getStyleClass().add("playbtn");
        timecount = new Label("mm:MM:mm");
    }

    @FXML
    private void PauseButtonAction(ActionEvent event) {

        mediaplayer.pause();
    }

    @FXML
    private void ChooseFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        mediaPath = file.toURI().toString();
        if (mediaPath != null) {
            Media media = new Media(mediaPath);
            mediaplayer = new MediaPlayer(media);
            mediaVew.setMediaPlayer(mediaplayer);

            /*double click to full screen */
            DoubleProperty WidthpProp = mediaVew.fitWidthProperty();
            DoubleProperty HeightProp = mediaVew.fitHeightProperty();
            HeightProp.bind(Bindings.selectDouble(mediaVew.sceneProperty(), "Height"));
            WidthpProp.bind(Bindings.selectDouble(mediaVew.sceneProperty(), "width"));

            /* handling of movement of the slider bar */
            mediaplayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                sliderBar.setValue(newValue.toSeconds());
                updateCounter(newValue.toSeconds());

            });

            /* get the value od duration and assignn it to the silder bar when the mouse is pressed in specific location*/
            sliderBar.setOnMousePressed((MouseEvent e) -> {
                mediaplayer.seek(Duration.seconds(sliderBar.getValue()));
            });

            /* get the value video duration when the mouse dragged the slider bar to an specific duration */
            sliderBar.setOnMouseDragged((MouseEvent e) -> {
                mediaplayer.seek(Duration.seconds(sliderBar.getValue()));
            });

            /* thread that will get the total duration of the video and git it to the progrees 
            bar to make progress bar reach to the end when video end */
            mediaplayer.setOnReady(() -> {

                Duration total = media.getDuration();
                sliderBar.setMax(total.toSeconds());
              //S  startTimeCounter();

            });

            mediaplayer.setVolume(1);
            mediaplayer.play();
        }
    }

   /* private void startTimeCounter() {
        // Create a Timeline to update the remaining time counter every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateCounter()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
*/
    private void updateCounter(double currentTime) {
        // Convert the current time to hours, minutes, seconds
        int hours = (int) (currentTime / 3600); // 3600 seconds in an hour
        int minutes = (int) ((currentTime % 3600) / 60); // 60 seconds in a minute
        int seconds = (int) (currentTime % 60); // Remaining seconds

        // Format the time as HH:MM:SS
        String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        // Print debugging information
        System.out.println("Updating Label: " + timeText);  // Debugging

        // Update the label on the JavaFX Application Thread
        Platform.runLater(() -> {
            timecount.setText(timeText);  // Ensure label is updated on the JavaFX thread
        });
    }

    /*   public void setScene(Scene scene){
        
    }*/

    @FXML
    private void playButtonAction(ActionEvent event) {

        mediaplayer.play();
        mediaplayer.setRate(1);

    }

    @FXML
    private void StopButtonAction(ActionEvent event) {

        mediaplayer.stop();
    }

    @FXML
    private void fastButtonAction(ActionEvent event) {

        mediaplayer.setRate(2);
    }

    @FXML
    private void SlowButtonAction(ActionEvent event) {
        mediaplayer.setRate(0.5);
    }

    @FXML
    private void plsTenSecondAction(ActionEvent event) {
        mediaplayer.seek(mediaplayer.getCurrentTime().add(Duration.seconds(10)));
    }

    @FXML
    private void MinusTenSecondAction(ActionEvent event) {

        mediaplayer.seek(mediaplayer.getCurrentTime().add(Duration.seconds(-10)));

    }

    @FXML
    private void LiveStreamAcion(ActionEvent event) {

        try {
            BorderPane Live = FXMLLoader.load(getClass().getResource("LiveView.fxml"));
            borderpane.setCenter(Live);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
