package com.os.cpu_scheduler.controller;

import com.os.cpu_scheduler.MainApplication;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;


public class InitController implements Initializable {
    private double offsetX;
    private double offsetY;

    @FXML
    private ImageView panda;

    @FXML
    private Text welcome;

    @FXML
    private Arc arc;

    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/com/os/cpu_scheduler/font/LCD-BOLD.TTF"), 48);
        welcome.setFont(font);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        playSound(InitController.class.getResource("/com/os/cpu_scheduler/audio/typing.wav").getPath().replaceAll("%20", " "));
        pandaFloat();
        writeText();
        arcProgress();
    }


    public static void playSound(String filePath) {
        try {
            File file = new File(filePath);
            if (file == null) {
                throw new RuntimeException("File not found: " + filePath);
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void arcProgress() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(arc.lengthProperty(), -60)),
                new KeyFrame(Duration.millis(7000), new KeyValue(arc.lengthProperty(), 360))
        );

        timeline.play();
    }

    private void pandaFloat() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), panda);
        floating.setFromY(0);
        floating.setToY(-50);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.setInterpolator(Interpolator.EASE_BOTH);
        floating.setAutoReverse(true);
        floating.play();
    }

    private void writeText() {
        String text = "Welcome to Panda Scheduler";
        AtomicInteger current = new AtomicInteger();
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        e -> {
                            if (current.get() < text.length()) {
                                welcome.setText(welcome.getText() + text.charAt(current.get()));
                                current.getAndIncrement();
                            }
                        }
                )
        );

        timeline.setOnFinished(event -> {
            current.getAndIncrement();
            current.getAndIncrement();
            current.getAndIncrement();
            Timeline timeline2 = new Timeline(
                    new KeyFrame(
                            Duration.millis(100),
                            e -> {
                                current.getAndDecrement();
                                if (current.get() < text.length()) {
                                    welcome.setText(welcome.getText().substring(0, current.get()));
                                }
                            }
                    )
            );
            timeline2.setCycleCount(text.length() + 3);
            timeline2.play();
            timeline2.setOnFinished(e -> writeText2());
        });

        timeline.setCycleCount(text.length() + 5);
        timeline.play();
    }

    private void writeText2() {
        String text = "Let's Start";
        AtomicInteger current = new AtomicInteger();
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        e -> {
                            if (current.get() < text.length()) {
                                welcome.setText(welcome.getText() + text.charAt(current.get()));
                                current.getAndIncrement();
                            }
                        }
                )
        );

        timeline.setOnFinished(event -> {
            current.getAndIncrement();
            current.getAndIncrement();
            current.getAndIncrement();
            Timeline timeline2 = new Timeline(
                    new KeyFrame(
                            Duration.millis(100),
                            e -> {
                                current.getAndDecrement();
                                if (current.get() < text.length() && current.get() >= 0) {
                                    welcome.setText(welcome.getText().substring(0, current.get()));
                                }
                            }
                    )
            );
            timeline2.setCycleCount(text.length() + 10);
            timeline2.play();
            timeline2.setOnFinished(e -> {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
                Scene s = welcome.getScene();
                stage = (Stage) s.getWindow();
                stage.setTitle("Panda Scheduler");
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 1280, 720, Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.out.println("Couldn't load");
                }
            });
        });

        timeline.setCycleCount(text.length() + 5);
        timeline.play();
    }

    public void minimize(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

    public void maximize() {
//        Stage stage = ((Stage)((MFXFontIcon)mouseEvent.getSource()).getScene().getWindow());
//        stage.setMaximized(!stage.isMaximized());
//        if (stage.isMaximized()) {
//            window.setWidth(stage.getWidth());
//            window.setHeight(stage.getHeight());
//        } else {
//            window.setHeight(720);
//            window.setWidth(1280);
//        }
    }

    public void exit() {
        System.exit(0);
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

}