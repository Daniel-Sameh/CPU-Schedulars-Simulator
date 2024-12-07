package org.cpuscheduling;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.cpuscheduling.Scheduler.*;
import org.cpuscheduling.Process.Process;

import java.util.*;

public class GUI extends Application {

    private int contextSwitchTime = 2;
    private int agingTime = 5;
    private int PROCESS_INDEX = 1;

    private final List<XYChart.Series<Number, Number>> seriesList = new ArrayList<>();
    private HashMap<Integer, Color> colors = new HashMap<>() ;
    private final int time = 60;
    private final int maxProcesses = 10;

    private Timeline timeline;
    private LineChart<Number, Number> lineChart;
    private ScrollPane lineChartScrollPane = new ScrollPane();


    private final VBox inputFieldsContainer = new VBox(10);;
    private final ComboBox<String> schedulerComboBox = new ComboBox<>();
    private final TextField processIdField = new TextField();;
    private final TextField priorityField = new TextField();;
    private final TextField burstTimeField = new TextField();;
    private final TextField arrivalTimeField = new TextField();;
    private final TextField quantumField = new TextField();;
    private final ColorPicker colorPicker = new ColorPicker(); ;
    private final Button generateRandomColorButton = new Button("Randomize");
    private final TextField contextSwitchTimeField = new TextField();
    private final TextField agingTimeField = new TextField();

    Button addProcessButton = new Button("Add Process");

    private final TableView<ProcessForm> processTable = new TableView<>();
    TableColumn<ProcessForm, Number> pidColumn = new TableColumn<>("Process ID");
    TableColumn<ProcessForm, Number> priorityColumn = new TableColumn<>("Priority");
    TableColumn<ProcessForm, Number> burstColumn = new TableColumn<>("Burst Time");
    TableColumn<ProcessForm, Number> arrivalColumn = new TableColumn<>("Arrival Time");
    TableColumn<ProcessForm, Number> quantumColumn = new TableColumn<>("Quantum");
    TableColumn<ProcessForm, Color> colorColumn = new TableColumn<>("Color");

    public static class ProcessForm {
        private final SimpleIntegerProperty processId;
        private final SimpleIntegerProperty priority;
        private final SimpleIntegerProperty burstTime;
        private final SimpleIntegerProperty arrivalTime;
        private final SimpleIntegerProperty quantum;
        private final SimpleObjectProperty<Color> color;


        public ProcessForm(Integer pid, Integer priority, Integer burstTime, Integer arrivalTime, Integer quantum, Color color) {
            this.processId = new SimpleIntegerProperty(pid);
            this.priority = new SimpleIntegerProperty(priority);
            this.burstTime = new SimpleIntegerProperty(burstTime);
            this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
            this.quantum = new SimpleIntegerProperty(quantum);
            this.color = new SimpleObjectProperty<>(color);
        }
        public SimpleIntegerProperty processIdProperty() {
            return processId;
        }
        public SimpleIntegerProperty priorityProperty() {
            return priority;
        }
        public SimpleIntegerProperty burstTimeProperty() {
            return burstTime;
        }
        public SimpleIntegerProperty arrivalTimeProperty() {
            return arrivalTime;
        }
        public SimpleIntegerProperty quantumProperty() {
            return quantum;
        }
        public ObjectProperty<Color> colorProperty() {
            return color;
        }
    }

    Button simulateButton = new Button("Simulate");

    private final TextField AWTField = new TextField();;
    private final TextField ATATField = new TextField();;


    @Override
    public void start(Stage stage) {

        initSchedulerComboBox();
        initProcessTable();
        initNumberAxis();
        initFields();
        initTableColumns();
        updateInputFields(schedulerComboBox.getValue());
        updateTable(schedulerComboBox.getValue());


        simulateButton.setOnAction(tmp -> {
            if (!contextSwitchTimeField.getText().equals("")){
                contextSwitchTime = Integer.parseInt(contextSwitchTimeField.getText());
            }
            if (!agingTimeField.getText().equals("")){
                agingTime = Integer.parseInt(agingTimeField.getText());
            }

            ArrayList<Process> processes = getProcesses();
            Scheduler scheduler = null;
            switch (schedulerComboBox.getValue()) {
                case "Priority Scheduling":
                    scheduler = new PriorityScheduler(contextSwitchTime);
                    break;
                case "Shortest Job First":
                    scheduler = new SJFScheduler(contextSwitchTime, agingTime);
                    break;
                case "Shortest Remaining Time First":
                    scheduler = new SRTFScheduler(contextSwitchTime, agingTime);
                    break;
                default:
                    scheduler = new FCAIScheduler(contextSwitchTime);
            }
            for (Process p: processes) {
                scheduler.addProcess(p);
            }
            ArrayList<ExecutionRecord> records = scheduler.run();
            for (ExecutionRecord r: records) {
                System.out.println("PID: " + r.processIndex);
                System.out.println("StartTime: " + r.startTime);
                System.out.println("Running Time: " + r.runningTime);
                if (r.quantum != -1){
                    System.out.println("Quantum: " + r.quantum);
                }
                System.out.println("=======================================");
            }
            execute(records);
            double totalTurnAroundTime = 0;
            double totalWaitingTime = 0;
            for (Process p: processes) {
                totalTurnAroundTime += p.getTurnAroundTime();
                totalWaitingTime += p.getWaitingTime();
            };
            double ATAT = totalTurnAroundTime / processes.size();
            double AWT = totalWaitingTime / processes.size();

            System.out.println("Average Total TurnAround Time = "+ ATAT);
            System.out.println("Average Total Waiting Time = "+ AWT);
            System.out.println("-------------------------------------------");

            setATATFieldValue(ATAT + "");
            setAWTFieldValue(AWT + "");
        });

        generateRandomColorButton.setOnAction(
                tmp -> {
                    colorPicker.setValue(getRandomColor());
                }
        );

        addProcessButton.setOnAction(tmp -> addProcess() );

        schedulerComboBox.setOnAction(tmp -> {
            PROCESS_INDEX = 1;
            if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING)
                timeline.stop();
            lineChart.getData().clear();
            seriesList.clear();
            updateInputFields(schedulerComboBox.getValue());
            updateTable(schedulerComboBox.getValue());
            colors.clear();
        });

        initStage(stage);
        stage.show();
    }

    public Color getRandomColor(){
        return new Color(Math.random() , Math.random() , Math.random() , 1);
    }

    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public void initSchedulerComboBox(){
        schedulerComboBox.getItems().addAll(
                "Priority Scheduling",
                "Shortest Job First",
                "Shortest Remaining Time First",
                "FCAI Scheduling"
        );
        schedulerComboBox.setValue("Priority Scheduling");
    }
    private void initFields(){
        processIdField.setPromptText("Process ID");
        burstTimeField.setPromptText("Burst Time");
        priorityField.setPromptText("Priority");
        arrivalTimeField.setPromptText("Arrival Time");
        quantumField.setPromptText("Quantum");
        contextSwitchTimeField.setPromptText("Context Switch Time");
        agingTimeField.setPromptText("Aging Time");

        AWTField.setEditable(false);
        ATATField.setEditable(false);

    }
    public void initProcessTable(){
        processTable.setMaxWidth(500);
        processTable.setMaxHeight(300);
    }
    private void initTableColumns(){
        pidColumn.setCellValueFactory(data -> data.getValue().processIdProperty());
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        burstColumn.setCellValueFactory(data -> data.getValue().burstTimeProperty());
        arrivalColumn.setCellValueFactory(data -> data.getValue().arrivalTimeProperty());
        quantumColumn.setCellValueFactory(data -> data.getValue().quantumProperty());
        colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        colorColumn.setCellFactory(tmp -> new TableCell<ProcessForm, Color>() {
            @Override
            protected void updateItem(Color color, boolean empty) {
                super.updateItem(color, empty);
                if (empty || color == null)
                    setStyle("");
                else
                    setStyle("-fx-background-color: " + toRgbString(color)  + ";");
            }
        });
    }
    public void initNumberAxis(){
        NumberAxis xAxis = new NumberAxis(0, time, 1);
        xAxis.setLabel("Time");
        NumberAxis yAxis = new NumberAxis(0, maxProcesses, 1);
        yAxis.setLabel("Process Index");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("");
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setPrefWidth(1500);

        lineChartScrollPane.setContent(lineChart);
        lineChartScrollPane.setFitToHeight(true);
        lineChartScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        lineChartScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
    public void initStage(Stage stage){
        Image icon = new Image(getClass().getResourceAsStream("/org/cpuscheduling/cpuIcon.png"));
        stage.getIcons().add(icon);

        HBox colorBox = new HBox(10, generateRandomColorButton, colorPicker);
        colorBox.setAlignment(Pos.CENTER);

        Label label = new Label("AWT");
        label.setMinWidth(60);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        HBox AWTBox = new HBox(10, label, AWTField);
        AWTBox.setAlignment(Pos.CENTER);
        AWTBox.setPadding(new Insets(10, 0, 0, 0));

        Label label1 = new Label("ATAT");
        label1.setMinWidth(60);
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        HBox ATATBox = new HBox(10, label1, ATATField);
        ATATBox.setAlignment(Pos.CENTER);
        ATATBox.setPadding(new Insets(0, 0, 20, 0));

        VBox outputFields = new VBox(10, AWTBox, ATATBox);

        Label contextLabel = new Label("Context Switching Time: ");
        contextLabel.setMinWidth(150);
        HBox contextBox = new HBox(10, contextLabel, contextSwitchTimeField);

        Label agingLabel = new Label("Aging Time: ");
        agingLabel.setMinWidth(150);
        HBox agingBox = new HBox(10, agingLabel, agingTimeField);

        VBox LeftBox = new VBox(10, schedulerComboBox,  inputFieldsContainer, colorBox, addProcessButton, processTable, contextBox, agingBox, simulateButton, outputFields);
        LeftBox.setPadding(new Insets(10, 20, 0, 20));
        LeftBox.setAlignment(Pos.CENTER);
        LeftBox.setMinWidth(400);

        HBox root = new HBox(10, LeftBox, lineChartScrollPane);


        Scene scene = new Scene(root, 1100, 750);
        stage.setScene(scene);
        stage.setTitle("CPU Scheduling Simulator");

    }

    private void addInputField(String labelText, TextField textField) {
        Label label = new Label(labelText);
        label.setMinWidth(80);
        HBox hbox = new HBox(10, label, textField);
        hbox.setAlignment(Pos.CENTER);
        inputFieldsContainer.getChildren().add(hbox);
    }
    private void updateInputFields(String scheduler) {

        inputFieldsContainer.getChildren().clear();
        inputFieldsContainer.setAlignment(Pos.CENTER);

        addInputField("Process ID:", processIdField);
        addInputField("Arrival Time:", arrivalTimeField);
        addInputField("Burst Time:", burstTimeField);
        if (scheduler.equals("Priority Scheduling") || scheduler.equals("FCAI Scheduling")) {
            addInputField("Priority:", priorityField);
        }
        if (scheduler.equals("FCAI Scheduling")) {
            addInputField("Quantum:", quantumField);
        }

    }
    private void updateTable(String scheduler){
        processTable.getItems().clear();

        processTable.getColumns().clear();
        processTable.getColumns().addAll(colorColumn, pidColumn, burstColumn, arrivalColumn);

        if (scheduler.equals("Priority Scheduling") || scheduler.equals("FCAI Scheduling")) {
            processTable.getColumns().add(priorityColumn);
        }
        if (scheduler.equals("FCAI Scheduling")) {
            processTable.getColumns().add(quantumColumn);
        }
    }
    private void addProcess(){
        boolean isPriorityScheduling = schedulerComboBox.getValue().equals("Priority Scheduling");
        boolean isFcai = schedulerComboBox.getValue().equals("FCAI Scheduling");

        try {
            Integer processId = Integer.parseInt(processIdField.getText());
            Integer priority = ( isPriorityScheduling || isFcai ? Integer.parseInt(priorityField.getText()) : -1);
            Integer burstTime = Integer.parseInt(burstTimeField.getText());
            Integer arrivalTime = Integer.parseInt(arrivalTimeField.getText());
            Integer quantum = (isFcai ? Integer.parseInt(quantumField.getText()) : -1);

            Color color = colorPicker.getValue();
            colors.put(PROCESS_INDEX++, color);

            ProcessForm newProcessForm = new ProcessForm(processId, priority, burstTime, arrivalTime, quantum, color);
            processTable.getItems().add(newProcessForm);

            processIdField.clear();
            priorityField.clear();
            burstTimeField.clear();
            arrivalTimeField.clear();
            quantumField.clear();
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields with appropriate input!").showAndWait();
        }
    }

    private ArrayList<Process> getProcesses(){
        ObservableList<ProcessForm> allProcessForms = processTable.getItems();

        ArrayList<Process> processes = new ArrayList<>();
        int index = 1;
        for (ProcessForm form : allProcessForms) {
            Process current = new Process(form.processId.get(), form.arrivalTime.get(), form.burstTime.get());
            if (form.priority.get() != -1) {
                current.setProperty("priority", form.priority.get());
            }
            if (form.quantum.get() != -1) {
                current.setProperty("quantum", form.quantum.get());
            }
            current.setProperty("index", index++);
            processes.add(current);
        }
        return processes;
    }
    public void setAWTFieldValue(String value) {
        AWTField.setText(value);
    }
    public void setATATFieldValue(String value) {
        ATATField.setText(value);
    }
    private void execute(List<ExecutionRecord> records){
        lineChart.getData().clear();
        seriesList.clear();

        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING)
            timeline.stop();

        for (int i=0 ; i<records.size() ; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            seriesList.add(series);
            lineChart.getData().add(series);
        }

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        final int[] timeIndex = {0,0};
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), tmp -> {
            if (timeIndex[0] > time) {
                timeline.stop();
                return;
            }
            if (timeIndex[1] < records.size()) {
                ExecutionRecord record = records.get(timeIndex[1]);
                if (timeIndex[0] >= record.startTime) {
                    Color color = colors.get(record.processIndex);
                    XYChart.Series<Number, Number> series = seriesList.get(timeIndex[1]);
                    series.getNode().setStyle("-fx-stroke: " + toRgbString(color) + ";");
                    XYChart.Data<Number, Number> data = new XYChart.Data<>(timeIndex[0], record.processIndex);
                    series.getData().add(data);
                    data.getNode().setStyle("-fx-background-color: " + toRgbString(color) + ";");
                }
                if(timeIndex[0] >= record.startTime + record.runningTime) {
                    timeIndex[1]++; timeIndex[0]--;
                }
            }
            else{

                timeline.stop();
            }
            timeIndex[0]++;
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
