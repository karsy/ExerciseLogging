package exerciseLogging;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.ArrayList;

public class historyController {

    private String URL = "jdbc:mysql://" + System.getenv("IP") + ":" + System.getenv("PORT") + "/" + System.getenv("DBNAME") + "?useSSL=false";
    private String username = System.getenv("USERNAME");
    private String password = System.getenv("PASSWORD");

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
            if(historySelectListView.getSelectionModel().getSelectedItem() == null){
                return;
            }
           if(historyByExerciseRadioButton.selectedProperty().get()){
               historyLoggedListView.getItems().setAll(workoutsWithExerciseQuery());
            } else {
               int temp_id = ((Template) historySelectListView.getSelectionModel().getSelectedItem()).getId();
               historyLoggedListView.getItems().setAll(resultsWithTemplateQuery(temp_id));
           }
        }));

        historyLoggedListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            if (historyLoggedListView.getSelectionModel().getSelectedItem() == null){
                return;
            }

            Alert workoutAlert = new Alert(Alert.AlertType.INFORMATION);
            workoutAlert.initStyle(StageStyle.UTILITY);
            workoutAlert.setGraphic(null);
            Result result = (Result) historyLoggedListView.getSelectionModel().getSelectedItem();
            workoutAlert.setTitle("Results");
            workoutAlert.setHeaderText(result.getExercise_Name() + " performed " + result.getDateTime().toString());
            workoutAlert.setContentText("Weight: " + String.valueOf(result.getWeight()) +
            "\nSets: " + String.valueOf(result.getSets()) + "\nReps: " + String.valueOf(result.getReps())
            +"\nDistance: " + String.valueOf(result.getDistance()) + "\nDuration: " + String.valueOf(result.getDuration()));
            workoutAlert.showAndWait();

            // handles a weird listView-related IndexOutOfBoundsException.
            Platform.runLater(() -> {
                historyLoggedListView.getSelectionModel().clearSelection();
            });

        }));
    }

    public void historyByWorkout(){
        ArrayList<Template> templates = workoutTemplateQuery();
        historySelectListView.getItems().clear();
        historyLoggedListView.getItems().clear();
        historySelectListView.getItems().addAll(templates);
    }

    public void historyByExercise(){
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
        if(historySelectListView.getItems() != null){
            historySelectListView.getSelectionModel().clearSelection();
        }
        RadioButton eventRadioButton = (RadioButton) event.getSource();
        RadioButton otherRadioButton = (eventRadioButton == historyByWorkoutRadioButton) ? historyByExerciseRadioButton: historyByWorkoutRadioButton;
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
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from exercise");
            while (myResultSet.next()){
                Exercise exercise = new Exercise(myResultSet.getInt("id"), myResultSet.getString("name"), myResultSet.getString("description"));
                exercises.add(exercise);
            }
            myConnection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return exercises;
    }

    private ArrayList<Template> workoutTemplateQuery(){
        ArrayList<Template> templates = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from template");
            while (myResultSet.next()){
                Template template = new Template(myResultSet.getInt("id"), myResultSet.getString("name"), myResultSet.getString("description"));
                templates.add(template);
            }
            myConnection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return templates;
    }

    private ArrayList<Result> workoutsWithExerciseQuery() {
        Exercise exercise = (Exercise) historySelectListView.getSelectionModel().getSelectedItem();
        int ex_id = exercise.getId();
        ArrayList<Result> ex_results = new ArrayList<>();
        try {
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM result WHERE exercise_id = ?");
            myStatement.setString(1, String.valueOf(ex_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                Result result = new Result(myResultSet.getDate("workout_id"), myResultSet.getInt("exercise_id"), myResultSet.getFloat("weight"),
                        myResultSet.getInt("reps"), myResultSet.getInt("sets"), myResultSet.getInt("distance"), myResultSet.getInt("duration"), exercise.getName());
                ex_results.add(result);
            }
            myConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex_results;
    }

    private ArrayList<Result> resultsWithTemplateQuery(int template_id){
        ArrayList<Result> results = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT r.workout_id, r.weight, r.reps, r.sets, r.distance, r.duration, t.name, t.exercise_id FROM result AS r JOIN (SELECT template_id, exercise_id, exercise.name FROM template JOIN templateexercise JOIN exercise WHERE template_id = ?) AS t GROUP BY r.workout_id");
            myStatement.setString(1, String.valueOf(template_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()){
                Result result = new Result(myResultSet.getDate("r.workout_id"), myResultSet.getInt("t.exercise_id")
                , myResultSet.getFloat("r.weight"), myResultSet.getInt("r.reps"), myResultSet.getInt("r.sets")
                , myResultSet.getInt("distance"), myResultSet.getInt("duration"), myResultSet.getString("t.name"));
                results.add(result);
            }
            myConnection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }
}
