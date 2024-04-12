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
    private static final int WIDTH = 20;
    private static final int HEIGHT = 50;

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
    private MFXComboBox<String> schedulerChoices;

    @FXML
    private MFXTextField processName, arrivalTime, burstTime, priority;

    @FXML
    private CheckBox liveSimCheckBox;

    @FXML
    private MFXButton startSimBtn;

    @FXML
    private AreaChart<Integer, Integer> graph;

    private ArrayList<XYChart.Series<Integer, Integer>> seriesList;

    private Stage cardsStage;

    private ObservableList<Process> observableList;
    private TableViewAdapter tableViewAdapter;

    private List<Color> colors;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardsStage = new Stage();
        cardsStage.initStyle(StageStyle.TRANSPARENT);
        ScrollUtils.addSmoothScrolling(scroll);
        gantt.setStyle("-fx-background-color: #303030;");
        setModeButtonStyle();
        initWindowStyle();
        initProcessList();
        setupTable();
        initComboBox();
        initGraph();
        initGanttColors();
    }

    /**
     * Initializes the Gantt chart colors. This method creates a list of colors that
     * will be used to represent the processes in the Gantt chart. The number of colors
     * is based on the number of processes, and the colors are generated in a smooth
     * transition from one color to another.
     */
    private void initGanttColors() {
        int numberOfColors = 20;
        colors = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            double hue = (i * 360.0 / numberOfColors); // Calculate hue for smooth transition
            Color color = Color.hsb(hue, 0.6, 0.6); // Max saturation and brightness
            colors.add(color);
        }
    }

    /**
     * Initializes the window style. This method adds the main-style.css file
     * to the scene's stylesheets.
     */
    private void initWindowStyle() {
        Platform.runLater(() -> {
            Scene scene = window.getScene();
            scene.getStylesheets().add(getClass().getResource("/com/os/cpu_scheduler/css/main-style.css").toExternalForm());
        });
    }

    private void initGraph() {
        seriesList = new ArrayList<>();
        graph.getXAxis().setLabel("Time (sec)");
        graph.getYAxis().setLabel("Remaining Time (sec)");
    }


    /**
     * Initializes the combo box with the available scheduling algorithms.
     */
    private void initComboBox() {
        // creates a new instance of the ComboBoxAdapter class
        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(schedulerChoices);
        // initializes the combo box with the available scheduling algorithms
        comboBoxAdapter.initComboBox(getChoicesList());
    }

    /**
     * Initializes the process list. This method creates a new ProcessList object
     * and adds a default process with an ID of 0, an arrival time of 0, a burst
     * time of -1, and a priority of -1.
     */
    private void initProcessList() {
        processList = new ProcessList();
        processList.addProcess(new Process(0, 0, "null", -1));
        observableList = FXCollections.observableArrayList(
                new Process(0, 0, "null", -1)
        );
    }

    /**
     * Sets the mode button style. This method creates a rectangle with an arc height and width of 50 and sets the clip of the mode button to the rectangle. This will create a circle with a border around the button when it is pressed.
     */
    private void setModeButtonStyle() {
        Rectangle rectangle = new Rectangle(clipImg.getFitWidth(), clipImg.getFitHeight() - 15);
        rectangle.setArcHeight(50);
        rectangle.setArcWidth(50);
        clipImg.setClip(rectangle);
    }

    /**
     * Initializes the table view with the process list.
     */
    private void setupTable() {
        tableViewAdapter = new TableViewAdapter(processTable, observableList);
        tableViewAdapter.setupTable(true);
        processList.getProcesses().remove(0);
        observableList.remove(0);
        processTable.update();
    }

    /**
     * Returns a list of the available scheduling algorithms.
     *
     * @return a list of the available scheduling algorithms
     */
    private ArrayList<String> getChoicesList() {
        // creates a new ArrayList
        ArrayList<String> list = new ArrayList<>();
        // iterates through each SchedulerTypes enum value
        for (SchedulerTypes type : SchedulerTypes.values()) {
            // adds the description of the enum value to the list
            list.add(type.getDescription());
        }
        // returns the list
        return list;
    }


    /**
     * Moves the window to the new position based on the mouse event.
     *
     * @param mouseEvent the mouse event
     */
    public void moveWindow(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((StackPane) mouseEvent.getSource()).getScene().getWindow());
        stage.setX(mouseEvent.getScreenX() - offsetX);
        stage.setY(mouseEvent.getScreenY() - offsetY);
    }

    /**
     * Sets the x and y coordinates of the mouse event relative to the stage.
     *
     * @param mouseEvent the mouse event
     */
    public void getXYOffsets(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }

    /**
     * Minimizes the app on clicking minimize button.
     *
     * @param mouseEvent mouse event from minimize button.
     */
    public void minimize(MouseEvent mouseEvent) {
        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

    public void maximize(MouseEvent mouseEvent) {
//        Stage stage = ((Stage) ((MFXFontIcon) mouseEvent.getSource()).getScene().getWindow());
//        stage.setMaximized(!stage.isMaximized());
//        if (stage.isMaximized()) {
//            window.setWidth(stage.getWidth());
//            window.setHeight(stage.getHeight());
//        } else {
//            window.setHeight(720);
//            window.setWidth(1280);
//        }
    }

    /**
     * Closes the application
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Adds new process to the processes list on clicking add process.
     * It pops up an alert if inputs are not valid.
     */
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
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid inputs");
            alert.show();
        }
    }

    /**
     * Chooses the Scheduler type using the combo box selection.
     */
    public void selectScheduler() {
        String schedulerName = schedulerChoices.getSelectedItem();
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

    /**
     * Sets the text fields editable or not
     *
     * @param editable boolean value indicating whether editable or not
     */
    private void setEditable(boolean editable) {
        processName.setEditable(editable);
        priority.setEditable(editable);
        burstTime.setEditable(editable);
        arrivalTime.setEditable(editable);
    }

    /**
     * Clears all text fields entries.
     */
    private void clearEntry() {
        processName.clear();
        priority.clear();
        burstTime.clear();
        arrivalTime.clear();
        processName.requestFocus();
    }

    /**
     * Chooses between live and non-live simulation on click of Start Simulation Button.
     */
    public void startSimulation() {
        if (isLiveSimulation) {
            Thread thread = new Thread(this::playLiveSim);
            thread.start();
        } else {
            playNonLiveSim();
        }
    }

    /**
     * Plays non live simulation.
     */
    private void playNonLiveSim() {
        addProcessBtn.setDisable(true);
        if (!processList.isProcessesFinished()) {
            scheduler.schedule();
            processTable.update();

            int idx = scheduler.getCurrentExecutingProcessIdx();
            int listIndex = processList.getProcesses().get(idx).getId();

            addToGraph(listIndex, idx);

            setSchedulerInfo();
        } else {
            declareAlert("Simulation Ended", "Non Live Simulation Ended Successfully");
        }
    }

    /**
     * Sets the scheduler info and updates gantt chart.
     */
    private void setSchedulerInfo() {
        if (scheduler.isIdle()) {
            cpuStatus.setText("IDLE");
        } else {
            cpuStatus.setText(processList
                    .getProcesses()
                    .get(scheduler.getCurrentExecutingProcessIdx())
                    .getName());

            var box = getGanttBox();
            Platform.runLater(() -> {
                gantt.getChildren().add(box);
            });
        }
    }

    /**
     * Method to draw a gantt box used for chart
     *
     * @return gantt box
     */
    private VBox getGanttBox() {
        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);
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
        return box;
    }

    /**
     * Pops up Alert
     *
     * @param title   title of alert
     * @param message heading message of alert
     */
    private void declareAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.show();
            addProcessBtn.setDisable(false);
            waitingText.setText(String.valueOf(processList.avgWaitingTime()));
            turnaroundText.setText(String.valueOf(processList.avgTurnAroundTime()));
        });
    }


    /**
     * Plays live simulation
     */
    private void playLiveSim() {
        startSimBtn.setDisable(true);

        while (!processList.isProcessesFinished()) {
            scheduler.schedule();
            Platform.runLater(() -> processTable.update());

            int idx = scheduler.getCurrentExecutingProcessIdx();
            int listIndex = processList.getProcesses().get(idx).getId();

            addToGraph(listIndex, idx);

            setSchedulerInfo();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Can't Sleep 😢");
            }
        }

        cpuStatus.setText("IDLE");

        waitingText.setText(String.valueOf(processList.avgWaitingTime()));
        turnaroundText.setText(String.valueOf(processList.avgTurnAroundTime()));

        declareAlert("Simulation Ended", "Live Simulation Ended Successfully");
    }

    /**
     * Add a new point to the graph
     *
     * @param listIndex the index of the point in series list
     * @param idx       the index of the process in processList
     */
    private void addToGraph(int listIndex, int idx) {
        Platform.runLater(() -> {
            seriesList.get(listIndex)
                    .getData()
                    .add(new XYChart.Data<>(getStartTime() + scheduler.getTime(),
                            processList.getProcesses().get(idx).getRemainingTime()));

            for (int i = 0; i < seriesList.size(); i++) {
                if (i != idx) {
                    seriesList.get(i)
                            .getData()
                            .add(new XYChart.Data<>(getStartTime() + scheduler.getTime(),
                                    processList.getProcesses().get(i).getRemainingTime()));
                }
            }
        });
    }

    /**
     * Checks if live simulation toggle box is checked or not.
     */
    public void checkLiveSimulation() {
        isLiveSimulation = liveSimCheckBox.isSelected();
    }

    /**
     * Returns the minimum arrival time of all processes in the process list which is the start
     * time of simulation.
     *
     * @return the minimum arrival time of all processes in the process list
     */
    private int getStartTime() {
        int min = processList.getProcesses().get(0).getArrivalTime();
        for (int i = 1; i < processList.getProcesses().size(); i++) {
            if (processList.getProcesses().get(i).getArrivalTime() < min)
                min = processList.getProcesses().get(i).getArrivalTime();
        }
        return min;
    }

    /**
     * Toggles between light and dark mode.
     */
    public void toggleMode() {
        Scene scene = dayNight.getScene();
        if (!lightMode) {
            lightMode = true;
            setLightMode(scene);
        } else {
            lightMode = false;
            setDarkMode(scene);
        }
    }


    /**
     * Sets the light mode to the scene
     *
     * @param scene scene to be set to light mode
     */
    private void setLightMode(Scene scene) {
        dayNight.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/sun.png")));
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/com/os/cpu_scheduler/css/light-style.css").toExternalForm());

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), toggleBtn);
        translateTransition.setToX(clipImg.getFitWidth() - 40);
        translateTransition.play();
        clipImg.setImage(new Image(getClass().getResourceAsStream("/com/os/cpu_scheduler/assets/lightBackground.jpg")));
        title.setFill(Color.rgb(67, 68, 74));
        gantt.setStyle("-fx-background-color: #F0F0F0;");
    }

    /**
     * Sets the dark mode to the scene
     *
     * @param scene scene to be set to dark mode
     */
    private void setDarkMode(Scene scene) {
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

    /**
     * Resets the simulation by clearing all the data structures and resetting the process counter.
     * This method is called when the "Reset" button is clicked.
     */
    public void reset() {
        setEditable(false);
        // resets the process counter
        Process.resetCounter();

        // initializes the process list and table view
        initProcessList();
        tableViewAdapter.clearTable();
        processList.getProcesses().clear();
        processTable.setItems(observableList);
        observableList.remove(0);
        processTable.update();

        // clears the scheduling algorithm combo box
        ComboBoxAdapter adapter = new ComboBoxAdapter(schedulerChoices);
        adapter.clear();
        adapter.initComboBox(getChoicesList());

        // clears the graph and Gantt chart data
        seriesList.clear();
        graph.getData().clear();
        gantt.getChildren().clear();

        // enables the "Add Process" and "Start Simulation" buttons
        addProcessBtn.setDisable(false);
        startSimBtn.setDisable(false);
    }

    /**
     * Opens a new stage displaying the team view.
     */
    public void viewTeam() {
        Platform.runLater(() -> {
            if (!cardsStage.isShowing()) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("team-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 475, Color.TRANSPARENT);
                    cardsStage.setScene(scene);
                    cardsStage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void enlargeBtn(MouseEvent mouseEvent) {
        scaleTransition = new ScaleTransition(Duration.millis(250), ((MFXButton) mouseEvent.getSource()));
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    public void shrinkBtn(MouseEvent mouseEvent) {
        scaleTransition = new ScaleTransition(Duration.millis(250), ((MFXButton) mouseEvent.getSource()));
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }


    /**
     * Rotates the button by 180 degrees.
     *
     * @param mouseEvent the mouse event
     */
    public void rotateBtn(MouseEvent mouseEvent) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), ((MFXButton) mouseEvent.getSource()));
        rotateTransition.setByAngle(180);
        rotateTransition.play();
    }
}
