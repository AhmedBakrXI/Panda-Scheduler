package com.os.cpu_scheduler.controller;

import com.os.cpu_scheduler.adapter.ComboBoxAdapter;
import com.os.cpu_scheduler.adapter.TableViewAdapter;
import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.process.ProcessList;
import com.os.cpu_scheduler.schedulers.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

import java.util.ResourceBundle;

public class MainController implements Initializable {
    private double offsetX, offsetY;
    private ProcessList processList;

    private Scheduler scheduler;

    @FXML
    private HBox fieldsBox;

    @FXML
    private Rectangle window;
    @FXML
    private MFXTableView<Process> processTable;

    @FXML
    private MFXComboBox<String> schedChoices;

    @FXML
    private MFXTextField processName, arrivalTime, burstTime, priority;

    // Temp

    private ObservableList<Process> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        processList = new ProcessList();
        processList.addProcess(new Process(0, 0, "null", -1));
        observableList = FXCollections.observableArrayList(
                new Process(0, 0, "null", -1)
        );
        setupTable();

        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(schedChoices);
        comboBoxAdapter.initComboBox(getChoicesList());

    }

    private void setupTable() {
        TableViewAdapter tableViewAdapter = new TableViewAdapter(processTable, observableList);
        tableViewAdapter.setupTable(true);
        processList.getProcesses().remove(0);
        observableList.remove(0);
        processTable.update();
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
        String name = processName.getText();
        int arrTime = Integer.parseInt(arrivalTime.getText());
        int burst = Integer.parseInt(burstTime.getText());
        int p;
        try {
            p = Integer.parseInt(priority.getText());
        } catch (NumberFormatException e) {
            p = -1;
        }

        Process process = new Process(arrTime, burst, name, p);
        processList.addProcess(process);
        observableList.add(process);
        processTable.update();
    }

    public void selectScheduler() {
        String schedulerName = schedChoices.getSelectedItem();
        if (schedulerName.equals(SchedulerTypes.FCFS.getDescription())) {
            scheduler = new FCFS(processList);
        } else if (schedulerName.equals(SchedulerTypes.SJF.getDescription())) {
            scheduler = new SJF(processList);
            fieldsBox.getChildren().remove(priority);
        } else if (schedulerName.equals(SchedulerTypes.PRIORITY.getDescription())) {
            scheduler = new Priority(processList);
        } else if (schedulerName.equals(SchedulerTypes.ROUND_ROBIN.getDescription())) {
            scheduler = new RoundRobin(processList);
        }
        setEditable(true);
    }

    private void setEditable(boolean editable) {
        processName.setEditable(editable);
        priority.setEditable(editable);
        burstTime.setEditable(editable);
        arrivalTime.setEditable(editable);
    }
    public void startSimulation() {
        scheduler.schedule();
        processTable.update();
    }


}
