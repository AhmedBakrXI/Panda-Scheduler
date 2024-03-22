package com.os.cpu_scheduler.controller;

import com.os.cpu_scheduler.adapter.ComboBoxAdapter;
import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.schedulers.SchedulerTypes;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private double offsetX, offsetY;
    @FXML
    private Rectangle window;
    @FXML
    private MFXTableView<Process> processTable;

    @FXML
    private MFXComboBox<String> schedChoices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(schedChoices);
        comboBoxAdapter.initComboBox(getChoicesList());

    }

    private ArrayList<String> getChoicesList() {
        ArrayList<String> list = new ArrayList<>();
        for (SchedulerTypes type : SchedulerTypes.values()) {
            list.add(type.getDescription());
        }
        return list;
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

    public void minimize(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

    public void maximize(MouseEvent mouseEvent) {
        Stage stage = ((Stage)((MFXFontIcon)mouseEvent.getSource()).getScene().getWindow());
        stage.setMaximized(!stage.isMaximized());
        if (stage.isMaximized()) {
            window.setWidth(stage.getWidth());
            window.setHeight(stage.getHeight());
        } else {
            window.setHeight(720);
            window.setWidth(1280);
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void addProcess() {
    }

}
