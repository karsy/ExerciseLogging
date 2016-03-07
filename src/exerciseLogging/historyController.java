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
               ArrayList<Result> workoutsWithExercise = workoutsWithExerciseQuery(observable.getClass());
               for(Result w: workoutsWithExercise){
                   historyLoggedListView.getItems().add(w);
               }
            } else {
               ArrayList<Result> workoutsWithTemplate = workoutsWithTemplateQuery(observable.getClass());
               for(Result w: workoutsWithTemplate){
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
            workoutAlert.setTitle("Result title");
            workoutAlert.setHeaderText("Result on date: 1234567");
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
        ArrayList<Template> templates = workoutTemplateQuery();
        historySelectListView.getItems().clear();
        historyLoggedListView.getItems().clear();
        // this should be the list of all workouts
        historySelectListView.getItems().addAll(templates);
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

    private ArrayList<Result> workoutsWithExerciseQuery(Object exercise) {
        int ex_id = ((Exercise) exercise).getId();
        ArrayList<Result> ex_results = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trainingdiary?useSSL=false", "user", "user");
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM result WHERE exercise_id = ?");
            myStatement.setString(1, String.valueOf(ex_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()){
                Result result = new Result(myResultSet.getDate("workout_id"), myResultSet.getInt("exercise_id"), myResultSet.getFloat("weight"),
                        myResultSet.getInt("reps"), myResultSet.getInt("sets"), myResultSet.getInt("distance"), myResultSet.getInt("duration"));
                ex_results.add(result);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        // return arraylist of these
        System.out.println(ex_results);
        return ex_results;
    }

    private ArrayList<Result> workoutsWithTemplateQuery(Object template) {
        // get temp.id

        // query for workout-entries with id

        // return arraylist of these switching between exercises with each temp or fill up?
        return null;

    }

}
