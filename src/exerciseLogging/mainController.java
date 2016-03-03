package exerciseLogging;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;


public class mainController{

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


        historySelectListView.getSelectionModel().selectionModeProperty().addListener((observable -> {
            // historyLoggedListView.setItems(observable.getLoggedWorkouts());
            // TODO: make a class for all objects to get their logged workouts or implement the above method with JDBC?
            historyLoggedListView.getItems().clear();
            System.out.println(observable.toString());
            historyLoggedListView.getItems().add(observable);
        }));
    }

    @FXML
    public void historyByWorkout(){
        // TODO: change historySelectListView to be fed with workouts
    }

    @FXML
    public void historyByExercise(){
        // TODO: change historySelectListView to be fed with exercises
    }

    @FXML
    public void displayLoggedHistory(boolean historyByWorkout){
        // TODO: change items in historyLoggedListView according to selection from historySelectListView
        if(historyByWorkout){
            System.out.println("DISPLAYING LOGGED WORKOUTS BY WORKOUT");
        } else {
            System.out.println("DISPLAYING LOGGED WORKOUTS BY EXERCISE");
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
