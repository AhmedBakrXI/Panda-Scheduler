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

public class TeamController implements Initializable {
    double offsetX;
    double offsetY;

    @FXML
    Text name;
    @FXML
    Text id;
    @FXML
    Text task;

    @FXML
    ImageView card;
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
    ImageView preview;


    PathTransition pathAnimation;
    PathTransition pathAnimation1;
    PathTransition pathAnimation2;
    PathTransition pathAnimation3;
    PathTransition pathAnimation4;
    PathTransition pathAnimation5;

    DropShadow dropShadow;

    Stage stage;

    private static final int SLEEP = 1600;
    private static final int DURATION = 10;

    public void moveWindow(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((StackPane) mouseEvent.getSource()).getScene().getWindow());
        stage.setX(mouseEvent.getScreenX() - offsetX);
        stage.setY(mouseEvent.getScreenY() - offsetY);
    }

    public void getXYOffsets(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }

    public void minimize(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

    public void maximize(MouseEvent mouseEvent) {
    }

    public void exit(MouseEvent mouseEvent) {
        ((Stage)((MFXFontIcon) (mouseEvent.getSource())).getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(247, 255, 70));

        card.setLayoutX(-120);
        card1.setLayoutX(-120);
        card2.setLayoutX(-120);
        card3.setLayoutX(-120);
        card4.setLayoutX(-120);
        card5.setLayoutX(-120);

        card1.setLayoutY(card.getLayoutY());
        card2.setLayoutY(card.getLayoutY());
        card3.setLayoutY(card.getLayoutY());
        card4.setLayoutY(card.getLayoutY());
        card5.setLayoutY(card.getLayoutY());

        Platform.runLater(() -> {
                     stage = (Stage) card.getScene().getWindow();
                }
        );


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

        new Thread(this::init).start();

    }

    public void init() {

        Path rectangle = new Path();
        rectangle.getElements().addAll(
                new MoveTo(0, card.getLayoutY() + card.getFitHeight() / 2),
                new LineTo(750, card.getLayoutY() + card.getFitHeight() / 2),
                new LineTo(750, card.getLayoutY() + card.getFitHeight() / 2 + 300),
                new LineTo(0, card.getLayoutY() + card.getFitHeight() / 2 + 300),
                new ClosePath()
        );

        pathAnimation = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation.setNode(card);
        pathAnimation.setInterpolator(Interpolator.LINEAR);
        pathAnimation.play();

        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pathAnimation1 = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation1.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation1.setNode(card1);
        pathAnimation1.setInterpolator(Interpolator.LINEAR);
        pathAnimation1.play();

        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pathAnimation2 = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation2.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation2.setNode(card2);
        pathAnimation2.setInterpolator(Interpolator.LINEAR);
        pathAnimation2.play();

        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pathAnimation3 = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation3.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation3.setNode(card3);
        pathAnimation3.setInterpolator(Interpolator.LINEAR);
        pathAnimation3.play();

        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pathAnimation4 = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation4.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation4.setNode(card4);
        pathAnimation4.setInterpolator(Interpolator.LINEAR);
        pathAnimation4.play();
        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pathAnimation5 = new PathTransition(Duration.seconds(DURATION), rectangle);
        pathAnimation5.setCycleCount(PathTransition.INDEFINITE);
        pathAnimation5.setNode(card5);
        pathAnimation5.setInterpolator(Interpolator.LINEAR);
        pathAnimation5.play();

    }

    public void stopAnimation(MouseEvent mouseEvent) {
        if (pathAnimation5 != null) {
            pathAnimation.pause();
            pathAnimation1.pause();
            pathAnimation2.pause();
            pathAnimation3.pause();
            pathAnimation4.pause();
            pathAnimation5.pause();
        }

        ((ImageView) mouseEvent.getSource()).setEffect(dropShadow);
        var scaleTransition = new ScaleTransition(Duration.millis(500), (ImageView) mouseEvent.getSource());
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.play();
    }

    public void playAnimation(MouseEvent mouseEvent) {
        if (pathAnimation5 != null) {
            pathAnimation.play();
            pathAnimation1.play();
            pathAnimation2.play();
            pathAnimation3.play();
            pathAnimation4.play();
            pathAnimation5.play();
        }

        ((ImageView) mouseEvent.getSource()).setEffect(null);
        var scaleTransition = new ScaleTransition(Duration.millis(500), (ImageView) mouseEvent.getSource());
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
    }


    public void display1() {
        name.setText("Name: Ahmed Mohammed Bakr");
        id.setText("ID: 2000037");
        task.setText("Task: UI Design & Implementation");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/AHMED BAKR.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display2() {
        name.setText("Name: Fathy Abdelhady");
        id.setText("ID: ??");
        task.setText("Task: Backend Team Leader");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/FATHY ABDELHADY.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display3() {
        name.setText("Name: Omar Saleh");
        id.setText("ID: ??");
        task.setText("Task: el OS 4a5syan");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/OMAR SALEH.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display4() {
        name.setText("Name: Yousef Wael");
        id.setText("ID: ??");
        task.setText("Task: bta3 python we AI");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/YOUSEF ASHMAWY.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display5() {
        name.setText("Name: Marwan Wael");
        id.setText("ID: ??");
        task.setText("Task: El Baraka");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/MARWAN WAEL.jpeg").toExternalForm(), 270, 366, true, true));
    }

    public void display6() {
        name.setText("Name: Ahmed Mohammed Ahmed Atwa");
        id.setText("ID: ??");
        task.setText("Task: SW Design");
        preview.setImage(new Image(getClass().getResource("/com/os/cpu_scheduler/cards/AHMED ATWA.jpeg").toExternalForm(), 270, 366, true, true));
    }

}