package com.os.cpu_scheduler.controller;

import com.os.cpu_scheduler.MainApplication;
import com.os.cpu_scheduler.adapter.ComboBoxAdapter;
import com.os.cpu_scheduler.adapter.TableViewAdapter;
import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.process.ProcessList;
import com.os.cpu_scheduler.schedulers.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private Text title;

    public MFXScrollPane scroll;
    @FXML
    private ImageView clipImg;

    @FXML
    public TextField turnaroundText;

    @FXML
    public TextField waitingText;

    @FXML
    public TextField cpuStatus;

    public Button toggleBtn;
    public ImageView dayNight;

    @FXML
    private HBox gantt;

    private ScaleTransition scaleTransition;
    private boolean isPriorityRemoved;
    private boolean isLiveSimulation;

    private double offsetX, offsetY;
    private ProcessList processList;
    private Scheduler scheduler;

    private boolean lightMode;

    @FXML
    private HBox fieldsBox;

    @FXML
    private MFXButton addProcessBtn;

    @FXML
    private Rectangle window;
    @FXML
    private MFXTableView<Process> processTable;

    @FXML
    private MFXComboBox<String> schedChoices;

    @FXML
    private MFXTextField processName, arrivalTime, burstTime, priority;

    @FXML
    private CheckBox liveSimCheckBox;

    @FXML
    private MFXButton startSimBtn;

    @FXML
    private AreaChart<Integer, Integer> graph;

    private final ArrayList<XYChart.Series<Integer, Integer>> seriesList = new ArrayList<>();

    // Temp

    private ObservableList<Process> observableList;
    private TableViewAdapter tableViewAdapter;

    private List<Color> colors;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ScrollUtils.addSmoothScrolling(scroll);

        gantt.setStyle("-fx-background-color: #303030;");

        Rectangle rectangle = new Rectangle(clipImg.getFitWidth(), clipImg.getFitHeight() - 15);
        rectangle.setArcHeight(50);
        rectangle.setArcWidth(50);
        clipImg.setClip(rectangle);

        Platform.runLater(() -> {
            Scene scene = window.getScene();
            scene.getStylesheets().add(getClass().getResource("/com/os/cpu_scheduler/css/main-style.css").toExternalForm());
        });

        processList = new ProcessList();
        processList.addProcess(new Process(0, 0, "null", -1));
        observableList = FXCollections.observableArrayList(
                new Process(0, 0, "null", -1)
        );
        setupTable();

        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(schedChoices);
        comboBoxAdapter.initComboBox(getChoicesList());

        graph.getXAxis().setLabel("Time (sec)");
        graph.getYAxis().setLabel("Remaining Time (sec)");

        int numberOfColors = 20;
        colors = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            double hue = (i * 360.0 / numberOfColors); // Calculate hue for smooth transition
            Color color = Color.hsb(hue, 0.6, 0.6); // Max saturation and brightness
            colors.add(color);
        }

    }

    private void setupTable() {
        tableViewAdapter = new TableViewAdapter(processTable, observableList);
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
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
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
        try {
            int arrTime = Integer.parseInt(arrivalTime.getText());
            int burst = Integer.parseInt(burstTime.getText());

            int p;
            try {
                p = Integer.parseInt(priority.getText());
            } catch (NumberFormatException e) {
                p = -1;
            }

            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
            series.setName(name);
            series.getData().add(new XYChart.Data<>(arrTime, burst));
            seriesList.add(series);
            graph.getData().add(series);
            clearEntry();

            Process process = new Process(arrTime, burst, name, p);
            processList.addProcess(process);


            observableList.add(process);
            processTable.update();
            System.out.println(observableList);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid inputs");
            alert.show();
        }
    }

    public void selectScheduler() {
        String schedulerName = schedChoices.getSelectedItem();
        if (schedulerName != null) {
            if (schedulerName.equals(SchedulerTypes.FCFS.getDescription())) {
                scheduler = new FCFS(processList);
                fieldsBox.getChildren().remove(priority);
                tableViewAdapter.removePriorityColumn();
                isPriorityRemoved = true;
            } else if (schedulerName.equals(SchedulerTypes.SJF_PREEMPTIVE.getDescription())) {
                scheduler = new SJF(processList);
                scheduler.setPreemptive(true);
                fieldsBox.getChildren().remove(priority);
                tableViewAdapter.removePriorityColumn();
                isPriorityRemoved = true;
            } else if (schedulerName.equals(SchedulerTypes.SJF_NON_PREEMPTIVE.getDescription())) {
                scheduler = new SJF(processList);
                scheduler.setPreemptive(false);
                fieldsBox.getChildren().remove(priority);
                tableViewAdapter.removePriorityColumn();
                isPriorityRemoved = true;
            } else if (schedulerName.equals(SchedulerTypes.PRIORITY_PREEMPTIVE.getDescription())) {
                scheduler = new Priority(processList);
                scheduler.setPreemptive(true);
                if (isPriorityRemoved) {
                    tableViewAdapter.addPriorityColumn();
                    fieldsBox.getChildren().add(priority);
                    isPriorityRemoved = false;
                }
            } else if (schedulerName.equals(SchedulerTypes.PRIORITY_NON_PREEMPTIVE.getDescription())) {
                scheduler = new Priority(processList);
                scheduler.setPreemptive(false);
                if (isPriorityRemoved) {
                    tableViewAdapter.addPriorityColumn();
                    fieldsBox.getChildren().add(priority);
                    isPriorityRemoved = false;
                }
            } else if (schedulerName.equals(SchedulerTypes.ROUND_ROBIN.getDescription())) {
                scheduler = new RoundRobin(processList);
                if (isPriorityRemoved) {
                    tableViewAdapter.addPriorityColumn();
                    fieldsBox.getChildren().add(priority);
                    isPriorityRemoved = false;
                }
            }
            setEditable(true);
        }
    }

    private void setEditable(boolean editable) {
        processName.setEditable(editable);
        priority.setEditable(editable);
        burstTime.setEditable(editable);
        arrivalTime.setEditable(editable);
    }

    private void clearEntry() {
        processName.clear();
        priority.clear();
        burstTime.clear();
        arrivalTime.clear();
        processName.requestFocus();
    }

    public void startSimulation() {
        if (isLiveSimulation) {
            Thread thread = new Thread(this::playLiveSim);
            thread.start();
        } else {
            playNonLiveSim();
        }
    }

    private void playNonLiveSim() {
        addProcessBtn.setDisable(true);
        if (!processList.isProcessesFinished()) {
//                Collections.sort(seriesList, Comparator.comparingInt(s -> s.getData().get(0).getYValue()));
//                graph.getData().sort(Comparator.comparing(series -> series.getData().get(0).getYValue()));

            scheduler.schedule();
            processTable.update();
            int idx = scheduler.getCurrentExecutingProcessIdx();

            int listIndex = processList.getProcesses().get(idx).getId();

            Platform.runLater(() -> seriesList.get(listIndex)
                    .getData()
                    .add(new XYChart.Data<>(getStartTime() + scheduler.getTime(),
                            processList.getProcesses().get(idx).getRemainingTime())));


            if (scheduler.isIdle()) {
                cpuStatus.setText("IDLE");
            } else {
                cpuStatus.setText(processList
                        .getProcesses()
                        .get(scheduler.getCurrentExecutingProcessIdx())
                        .getName());

                Rectangle rectangle = new Rectangle(20, 50);
                rectangle.setFill(colors.get(
                        scheduler.getCurrentExecutingProcessIdx()
                ));
                Text text = new Text(processList
                        .getProcesses()
                        .get(scheduler.getCurrentExecutingProcessIdx())
                        .getName());
                text.setFill(Color.WHITE);

                StackPane pane = new StackPane(rectangle, text);
                Text time = new Text(String.valueOf(scheduler.getTime() + getStartTime() - 1));
                time.setFill(Color.SKYBLUE.darker());

                VBox box = new VBox(pane, time);
                gantt.getChildren().add(box);
            }
        } else {
            for (var series : seriesList) {
                series.getData().remove(0);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Simulation Ended");
            alert.setHeaderText("Non Live Simulation Ended Successfully");
            alert.show();
            addProcessBtn.setDisable(false);
            waitingText.setText(String.valueOf(processList.avgWaitingTime()));
            turnaroundText.setText(String.valueOf(processList.avgTurnAroundTime()));
        }
    }


    private void playLiveSim() {
        startSimBtn.setDisable(true);


        while (!processList.isProcessesFinished()) {
//            Platform.runLater(() -> {
//                Collections.sort(seriesList, Comparator.comparingInt(s -> ((XYChart.Series<Integer, Integer>) s).getData().get(0).getYValue()).thenComparingInt((s1) -> 0));
//                graph.getData().sort(Comparator.comparingInt(series -> ((XYChart.Series<Integer, Integer>) series).getData().get(0).getYValue()).thenComparingInt((s1) -> 0));
//            });

//            seriesList = (ArrayList<XYChart.Series<Integer, Integer>>) seriesList.stream()
//                    .sorted(Comparator.comparingInt(s -> s.getData().get(0).getYValue()))
//                    .collect(Collectors.toList());
//
//            Platform.runLater(() -> {
//                graph.setData(
//                        FXCollections.observableArrayList(
//                                graph.getData().stream()
//                                        .sorted(Comparator.comparingInt(s -> s.getData().get(0).getYValue()))
//                                        .collect(Collectors.toList())
//                        )
//                );
//            });


            scheduler.schedule();
            Platform.runLater(() -> processTable.update());
            int idx = scheduler.getCurrentExecutingProcessIdx();

            int listIndex = processList.getProcesses().get(idx).getId();

            Platform.runLater(() -> seriesList.get(listIndex)
                    .getData()
                    .add(new XYChart.Data<>(getStartTime() + scheduler.getTime(),
                            processList.getProcesses().get(idx).getRemainingTime())));

            if (scheduler.isIdle()) {
                cpuStatus.setText("IDLE");
            } else {
                cpuStatus.setText(processList
                        .getProcesses()
                        .get(scheduler.getCurrentExecutingProcessIdx())
                        .getName());

                Platform.runLater(() -> {
                    Rectangle rectangle = new Rectangle(20, 50);
                    rectangle.setFill(colors
                            .get(scheduler.getCurrentExecutingProcessIdx())
                            .brighter()
                    );

                    Text text = new Text(processList
                            .getProcesses()
                            .get(scheduler.getCurrentExecutingProcessIdx())
                            .getName());
                    text.setFill(Color.WHITE);

                    StackPane pane = new StackPane(rectangle, text);
                    Text time = new Text(String.valueOf(scheduler.getTime() + getStartTime() - 1));
                    time.setFill(Color.SKYBLUE.darker());
                    VBox box = new VBox(pane, time);
                    gantt.getChildren().add(box);
                });

            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Can't Sleep");
            }
        }

        for (var series : seriesList) {
            series.getData().remove(0);
        }

        cpuStatus.setText("IDLE");

        startSimBtn.setDisable(false);
        waitingText.setText(String.valueOf(processList.avgWaitingTime()));
        turnaroundText.setText(String.valueOf(processList.avgTurnAroundTime()));
    }

    public void checkLiveSimulation() {
        isLiveSimulation = liveSimCheckBox.isSelected();
    }

    private int getStartTime() {
        int min = processList.getProcesses().get(0).getArrivalTime();
        for (int i = 1; i < processList.getProcesses().size(); i++) {
            if (processList.getProcesses().get(i).getArrivalTime() < min)
                min = processList.getProcesses().get(i).getArrivalTime();
        }
        return min;
    }


    public void toggleMode() {
        Scene scene = dayNight.getScene();
        if (!lightMode) {
            lightMode = true;
            dayNight.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/sun.png")));
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/com/os/cpu_scheduler/css/light-style.css").toExternalForm());

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), toggleBtn);
            translateTransition.setToX(clipImg.getFitWidth() - 40);
            translateTransition.play();
            clipImg.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/lightBackground.jpg")));
            title.setFill(Color.rgb(67, 68, 74));
            gantt.setStyle("-fx-background-color: #F0F0F0;");
        } else {
            lightMode = false;
            dayNight.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/moon.png")));
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/com/os/cpu_scheduler/css/main-style.css").toExternalForm());

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), toggleBtn);
            translateTransition.setToX(0);
            translateTransition.play();
            clipImg.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/nightBackground.jpg")));
            title.setFill(Color.WHITE);
            gantt.setStyle("-fx-background-color: #303030;");
        }
    }

    public void reset() {
        Process.resetCounter();
        processList = new ProcessList();
        processList.addProcess(new Process(0, 0, "null", -1));
        observableList = FXCollections.observableArrayList(
                new Process(0, 0, "null", -1)
        );

        tableViewAdapter.clearTable();
        processList.getProcesses().clear();
        processTable.setItems(observableList);
        observableList.remove(0);
        processTable.update();


        ComboBoxAdapter adapter = new ComboBoxAdapter(schedChoices);
        adapter.clear();
        adapter.initComboBox(getChoicesList());
        setEditable(false);

        seriesList.clear();
        graph.getData().clear();
        gantt.getChildren().clear();

        addProcessBtn.setDisable(false);
    }

    public void viewTeam() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("team-view.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1000, 475, Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void enlargeBtn(MouseEvent mouseEvent) {
        scaleTransition = new ScaleTransition(Duration.millis(250), ((MFXButton) mouseEvent.getSource()));
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
    }

    public void shrinkBtn(MouseEvent mouseEvent) {
        scaleTransition = new ScaleTransition(Duration.millis(250), ((MFXButton) mouseEvent.getSource()));
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
    }


    public void rotateBtn(MouseEvent mouseEvent) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), ((MFXButton) mouseEvent.getSource()));
        rotateTransition.setByAngle(180);
        rotateTransition.play();
    }
}
