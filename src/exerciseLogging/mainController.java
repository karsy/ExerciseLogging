package exerciseLogging;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;


public class mainController{

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
            // TODO: make a class for all objects to get their logged workouts or implement the above method with JDBC
        }));

        historyLoggedListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            // TODO: make this alert get the appropriate information from selected workout/exercise.
            Alert workoutAlert = new Alert(Alert.AlertType.INFORMATION);
            workoutAlert.setGraphic(null);
            workoutAlert.setTitle("Workout title");
            workoutAlert.setHeaderText("Workout on date: 1234567");
            workoutAlert.setContentText("*All the fucking data from this workout that we want to display.*");
            workoutAlert.showAndWait();
        }));
    }

    @FXML
    public void historyByWorkout(){
        // TODO: change historySelectListView to be fed with workouts
        historySelectListView.getItems().clear();
        historySelectListView.getItems().addAll(list1);
    }

    @FXML
    public void historyByExercise(){
        // TODO: change historySelectListView to be fed with exercises
        historySelectListView.getItems().clear();
        historySelectListView.getItems().addAll(list2);
    }

    @FXML
    public void displayLoggedHistory(boolean historyByWorkout){
        // TODO: change items in historyLoggedListView according to selection from historySelectListView
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
