package com.os.cpu_scheduler.controller;

import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the main controller for the Team.fxml file. It initializes the position of the cards, initializes the animation for the cards, and initializes the background audio for the application.
 * It also contains several methods that are called when the user clicks on different buttons or icons. These methods display different cards and tasks for each team member.
 */
public class TeamController implements Initializable {

    /**
     * This variable stores the x and y coordinates of the mouse
     * when the user clicks and drags the window.
     * It is used to move the window to a new position.
     */
    private double offsetX;
    private double offsetY;

    /**
     * This variable stores a reference to the Stage object for the main window.
     * It is used to minimize or close the application window.
     */
    private Stage stage;

    /**
     * This variable stores a DropShadow object that is applied to the card when the animation is paused.
     * It is used to give the card a shadow effect.
     */
    private DropShadow dropShadow;

    /**
     * This variable stores a Path object that represents a rectangle.
     * It is used as the animation path for the cards.
     */
    private Path rectangle;

    /**
     * This variable stores a PathTransition object for the first card.
     * It is used to animate the first card along the rectangle path.
     */
    private PathTransition pathAnimation1;

    /**
     * This variable stores a PathTransition object for the second card.
     * It is used to animate the second card along the rectangle path.
     */
    private PathTransition pathAnimation2;

    /**
     * This variable stores a PathTransition object for the third card.
     * It is used to animate the third card along the rectangle path.
     */
    private PathTransition pathAnimation3;

    /**
     * This variable stores a PathTransition object for the fourth card.
     * It is used to animate the fourth card along the rectangle path.
     */
    private PathTransition pathAnimation4;

    /**
     * This variable stores a PathTransition object for the fifth card.
     * It is used to animate the fifth card along the rectangle path.
     */
    private PathTransition pathAnimation5;

    /**
     * This variable stores a PathTransition object for the sixth card.
     * It is used to animate the sixth card along the rectangle path.
     */
    private PathTransition pathAnimation6;

    @FXML
    Text name;
    @FXML
    Text id;
    @FXML
    Text task;

    @FXML
    ImageView card1;
    @FXML
    ImageView card2;
    @FXML
    ImageView card3;
    @FXML
    ImageView card4;
    @FXML
    ImageView card5;
    @FXML
    ImageView card6;

    @FXML
    ImageView preview;


    private static final int PERIOD = 1600;
    private static final int DURATION = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(247, 255, 70));

        initCardsPosition();

        Platform.runLater(() -> {
            stage = (Stage) card1.getScene().getWindow();
        });


        playBackgroundAudio();

        new Thread(this::initCardAnimation).start();

    }

    /**
     * Initializes the position of the cards.
     */
    private void initCardsPosition() {
        card1.setLayoutX(-120);
        card2.setLayoutX(-120);
        card3.setLayoutX(-120);
        card4.setLayoutX(-120);
        card5.setLayoutX(-120);
        card6.setLayoutX(-120);

        card2.setLayoutY(card1.getLayoutY());
        card3.setLayoutY(card1.getLayoutY());
        card4.setLayoutY(card1.getLayoutY());
        card5.setLayoutY(card1.getLayoutY());
        card6.setLayoutY(card1.getLayoutY());
    }

    public void moveWindow(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((StackPane) mouseEvent.getSource()).getScene().getWindow());
        stage.setX(mouseEvent.getScreenX() - offsetX);
        stage.setY(mouseEvent.getScreenY() - offsetY);
    }

    public void getXYOffsets(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }


    /**
     * Minimizes the application window.
     *
     * @param mouseEvent the mouse event that triggered the minimize action
     */
    public void minimize(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

    public void maximize(MouseEvent mouseEvent) {
    }


    /**
     * Exits the window
     *
     * @param mouseEvent the mouse event that triggered the exit action
     */
    public void exit(MouseEvent mouseEvent) {
        ((Stage) ((MFXFontIcon) (mouseEvent.getSource())).getScene().getWindow()).close();
    }


    /**
     * Plays the background audio for the application.
     * The audio file is loaded from the resources folder and played using the Java Sound API.
     * The audio is paused and stopped when the application window is hidden or closed.
     */
    private void playBackgroundAudio() {
        File audio = new File(getClass().getResource("/com/os/cpu_scheduler/audio/background-music.wav").getPath().replaceAll("%20", " "));
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audio);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            new Thread(() -> {
                while (true) {
                    if (stage != null && !stage.isShowing()) {
                        clip.close();
                    } else {
                        if (!clip.isRunning() && stage != null) {
                            if (!clip.isOpen()) {
                                try {
                                    clip.open(audioInputStream);
                                } catch (LineUnavailableException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            clip.setMicrosecondPosition(0);
                            clip.start();
                        }
                    }
                }
            }).start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the animation for the cards.
     * This method creates a Path object that represents a rectangle,
     * and then creates a PathTransition for each card.
     * The PathTransition animates the card along the Path object, repeating indefinitely.
     */
    public void initCardAnimation() {

        Path rectangle = getRectanglePath();

        pathAnimation1 = playPathAnimation(card1, rectangle);

        sleep();

        pathAnimation2 = playPathAnimation(card2, rectangle);

        sleep();

        pathAnimation3 = playPathAnimation(card3, rectangle);

        sleep();

        pathAnimation4 = playPathAnimation(card4, rectangle);

        sleep();

        pathAnimation5 = playPathAnimation(card5, rectangle);

        sleep();

        pathAnimation6 = playPathAnimation(card6, rectangle);

    }

    /**
     * Returns a Path object that represents a rectangle. The rectangle is used as
     * the animation path for the cards.
     *
     * @return a Path object that represents a rectangle
     */
    private Path getRectanglePath() {

        Path path = new Path();
        path.getElements().addAll(new MoveTo(0, card1.getLayoutY() + card1.getFitHeight() / 2), new LineTo(750, card1.getLayoutY() + card1.getFitHeight() / 2), new LineTo(750, card1.getLayoutY() + card1.getFitHeight() / 2 + 300), new LineTo(0, card1.getLayoutY() + card1.getFitHeight() / 2 + 300), new ClosePath());
        return path;

    }

    /**
     * Creates and plays a PathTransition for the specified card. The PathTransition animates the card
     * along a Path object, repeating indefinitely.
     *
     * @param card the ImageView object representing the card
     * @param path the Path object representing the animation path
     * @return the PathTransition object that was created
     */
    private PathTransition playPathAnimation(ImageView card, Path path) {
        PathTransition pathTransition = new PathTransition(Duration.seconds(DURATION), path);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setNode(card);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.play();
        return pathTransition;

    }

    /**
     * Pauses all card animations.
     */
    public void stopAnimation(MouseEvent mouseEvent) {

        if (pathAnimation6 != null) {
            pathAnimation1.pause();
            pathAnimation2.pause();
            pathAnimation3.pause();
            pathAnimation4.pause();
            pathAnimation5.pause();
            pathAnimation6.pause();
        }

        ((ImageView) mouseEvent.getSource()).setEffect(dropShadow);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), (ImageView) mouseEvent.getSource());
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.play();

    }

    /**
     * Plays all card animations.
     *
     * @param mouseEvent the MouseEvent that triggered the play action
     */
    public void playAnimation(MouseEvent mouseEvent) {

        if (pathAnimation6 != null) {
            pathAnimation1.play();
            pathAnimation2.play();
            pathAnimation3.play();
            pathAnimation4.play();
            pathAnimation5.play();
            pathAnimation6.play();
        }

        ((ImageView) mouseEvent.getSource()).setEffect(null);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), (ImageView) mouseEvent.getSource());
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();

    }


    /**
     * Sleeps for @PERIOD seconds
     */
    private void sleep() {
        try {
            Thread.sleep(TeamController.PERIOD);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void display1() {
        name.setText("Name: Ahmed Mohammed Bakr");
        id.setText("ID: 2000037");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/AHMED BAKR.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display2() {
        name.setText("Name: Fathy Abdelhady");
        id.setText("ID: 2001152");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/FATHY ABDELHADY.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display3() {
        name.setText("Name: Omar Saleh");
        id.setText("ID: 2001993");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/OMAR SALEH.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display4() {
        name.setText("Name: Yousef Wael Ashmawy");
        id.setText("ID: 2001430");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/YOUSEF ASHMAWY.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display5() {
        name.setText("Name: Marwan Wael");
        id.setText("ID: 2001244");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/MARWAN WAEL.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display6() {
        name.setText("Name: Ahmed Mohammed Atwa");
        id.setText("ID: 2001391");
        task.setText(null);
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/AHMED ATWA.jpeg").toExternalForm(), 270, 366, true, true));
    }

}