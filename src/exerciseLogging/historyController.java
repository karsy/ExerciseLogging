package exerciseLogging;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.StageStyle;


public class historyController {


    // test-lists for testing with tests (TEST-TEST-TEST)
    String[] list1 = {"hei", "på","deg"};
    String[] list2 = {"bla", "la", "ta"};

    @FXML
    private ListView historySelectListView;

    @FXML
    private ListView historyLoggedListView;

    @FXML
    private RadioButton historyByWorkoutRadioButton;

    @FXML
    private RadioButton historyByExerciseRadioButton;

    public void initialize(){

        historyByWorkoutRadioButton.setOnAction(this::radioButtonSwitching);
        historyByExerciseRadioButton.setOnAction(this::radioButtonSwitching);


        historySelectListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            historyLoggedListView.getItems().clear();
            historyLoggedListView.getItems().addAll(historySelectListView.getSelectionModel().getSelectedItem().toString());
            // TODO: the above should fill the LoggedListView with corresponding logged exercises/workouts.
        }));

        historyLoggedListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            // TODO: make this alert get the appropriate information from selected workout/exercise.
            Alert workoutAlert = new Alert(Alert.AlertType.INFORMATION);
            workoutAlert.initStyle(StageStyle.UTILITY);
            workoutAlert.setGraphic(null);
            workoutAlert.setTitle("Workout title");
            workoutAlert.setHeaderText("Workout on date: 1234567");
            workoutAlert.setContentText("*All the fucking data from this workout that we want to display.*");
            workoutAlert.showAndWait();

            // handles a weird listView-related IndexOutOfBoundsException.
            Platform.runLater(() -> {
                historyLoggedListView.getSelectionModel().clearSelection();
            });

        }));
    }

    public void historyByWorkout(){
        // TODO: change historySelectListView to be fed with workouts
        historySelectListView.getItems().clear();
        // this should be the list of all workouts
        historySelectListView.getItems().addAll(list1);
    }

    public void historyByExercise(){
        // TODO: change historySelectListView to be fed with exercises
        historySelectListView.getItems().clear();
        // this should be a list of all logged exercise-types
        historySelectListView.getItems().addAll(list2);
    }

    public void displayLoggedHistory(boolean historyByWorkout){
        if(historyByWorkout){
            historyByWorkout();
        } else {
            historyByExercise();
        }
    }

    public void radioButtonSwitching(Event event){
        RadioButton eventRadioButton = (RadioButton) event.getSource();
        RadioButton otherRadioButton;
        if (eventRadioButton == historyByWorkoutRadioButton) {
            otherRadioButton = historyByExerciseRadioButton;
        } else {
            otherRadioButton = historyByWorkoutRadioButton;
        }
        if (eventRadioButton.selectedProperty().get()){
            otherRadioButton.selectedProperty().set(false);
        } else {
            otherRadioButton.selectedProperty().set(true);
        }
        displayLoggedHistory(historyByWorkoutRadioButton.selectedProperty().get());
    }

}
