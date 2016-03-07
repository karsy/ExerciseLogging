package exerciseLogging;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.ArrayList;

public class historyController {


    // test-lists for testing with tests (TEST-TEST-TEST)
    String[] list1 = {"hei", "på","deg"};
    String[] list2 = {"bla", "la", "ta"};

    ArrayList<Exercise> exercises = new ArrayList<>(exercisesNamesQuery());
    ArrayList<Template> workouts = new ArrayList<>(workoutTemplateQuery());

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
           if(historyByExerciseRadioButton.selectedProperty().get()){
               ArrayList<Workout> workoutsWithExercise = workoutsWithExerciseQuery(observable);
               for(Workout w: workoutsWithExercise){
                   historyLoggedListView.getItems().add(w);
               }
            } else {
               ArrayList<Workout> workoutsWithTemplate = workoutsWithTemplateQuery(observable);
               for(Workout w: workoutsWithTemplate){
                   historyLoggedListView.getItems().add(w);
               }
           }
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
        ArrayList<Template> times = workoutTemplateQuery();
        historySelectListView.getItems().clear();
        historyLoggedListView.getItems().clear();
        // this should be the list of all workouts
        historySelectListView.getItems().addAll(times);
    }

    public void historyByExercise(){
        // TODO: change historySelectListView to be fed with exercises
        ArrayList<Exercise> exercises = exercisesNamesQuery();
        historySelectListView.getItems().clear();
        historyLoggedListView.getItems().clear();
        historySelectListView.getItems().addAll(exercises);
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

    private ArrayList<Exercise> exercisesNamesQuery(){
        ArrayList<Exercise> exercises = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trainingdiary?useSSL=false", "user", "user");
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from exercise");
            while (myResultSet.next()){
                Exercise exercise = new Exercise(myResultSet.getInt("id"), myResultSet.getString("name"), myResultSet.getString("description"));
                exercises.add(exercise);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return exercises;
    }

    private ArrayList<Template> workoutTemplateQuery(){
        ArrayList<Template> templates = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trainingdiary?useSSL=false", "user", "user");
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from workout");
            while (myResultSet.next()){
                Template template = new Template(myResultSet.getInt("id"), myResultSet.getString("name"), myResultSet.getString("description"));
                templates.add(template);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return templates;
    }

    private ArrayList<Workout> workoutsWithExerciseQuery(Observable exercise) {
        // get ex.id

        // query for exercise-entries with id

        // return arraylist of these
        return null;
    }

    private ArrayList<Workout> workoutsWithTemplateQuery(Observable template) {
        // get temp.id

        // query for workout-entries with id

        // return arraylist of these switching between exercises with each temp or fill up?
        return null;

    }

}
