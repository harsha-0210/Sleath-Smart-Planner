import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class SmartAIPlanner extends Application {

    // Task class to store task details
    static class Task {
        private String name;
        private String description;
        private Date time;

        public Task(String name, String description, Date time) {
            this.name = name;
            this.description = description;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Date getTime() {
            return time;
        }
    }

    // List to store tasks
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart AI Planner");

        // Layout for task management
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Title
        Label title = new Label("Smart AI Planner");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Task name input
        TextField taskNameInput = new TextField();
        taskNameInput.setPromptText("Enter task name");

        // Task description input
        TextField taskDescriptionInput = new TextField();
        taskDescriptionInput.setPromptText("Enter task description");

        // Time picker for task
        DatePicker taskDatePicker = new DatePicker();
        taskDatePicker.setPromptText("Select task date");

        // Time input for hours and minutes
        TextField hourInput = new TextField();
        hourInput.setPromptText("Hour (0-23)");
        TextField minuteInput = new TextField();
        minuteInput.setPromptText("Minute (0-59)");

        // Button to add task
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(e -> {
            String taskName = taskNameInput.getText();
            String taskDescription = taskDescriptionInput.getText();
            var date = taskDatePicker.getValue();
            int hour, minute;

            try {
                hour = Integer.parseInt(hourInput.getText());
                minute = Integer.parseInt(minuteInput.getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid hour and minute.");
                return;
            }

            if (taskName.isEmpty() || date == null) {
                showAlert("Missing Details", "Task name and date are required.");
                return;
            }

            // Create the task time
            Calendar calendar = Calendar.getInstance();
            calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), hour, minute, 0);
            Date taskTime = calendar.getTime();

            // Create task and add to the list
            Task task = new Task(taskName, taskDescription, taskTime);
            tasks.add(task);

            // Schedule the task reminder
            scheduleTaskReminder(task);

            // Clear inputs and show confirmation
            taskNameInput.clear();
            taskDescriptionInput.clear();
            taskDatePicker.setValue(null);
            hourInput.clear();
            minuteInput.clear();
            showAlert("Task Added", "Your task has been added and scheduled!");
        });

        // Button to display tasks
        Button showTasksButton = new Button("Show Tasks");
        showTasksButton.setOnAction(e -> {
            StringBuilder taskList = new StringBuilder();
            for (Task task : tasks) {
                taskList.append(task.getName())
                        .append(" - ")
                        .append(task.getDescription())
                        .append(" at ")
                        .append(task.getTime())
                        .append("\n");
            }
            if (taskList.length() == 0) {
                taskList.append("No tasks scheduled.");
            }
            showAlert("Scheduled Tasks", taskList.toString());
        });

        // Layout and styling
        HBox timeInputLayout = new HBox(10, hourInput, minuteInput);
        layout.getChildren().addAll(
                title, taskNameInput, taskDescriptionInput,
                taskDatePicker, timeInputLayout,
                addTaskButton, showTasksButton
        );

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Function to schedule task reminders
    private void scheduleTaskReminder(Task task) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                showAlert("Task Reminder", "It's time for: " + task.getName() + "\n" + task.getDescription());
            }
        }, task.getTime());
    }

    // Function to show alerts
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
