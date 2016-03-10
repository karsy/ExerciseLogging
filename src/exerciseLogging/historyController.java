package exerciseLogging;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.ArrayList;

public class historyController {

    private String URL = "jdbc:mysql://" + System.getenv("IP") + ":" + System.getenv("PORT") + "/" + System.getenv("DBNAME") + "?useSSL=false";
    private String username = System.getenv("USERNAME");
    private String password = System.getenv("PASSWORD");

    ArrayList<Exercise> exercises = new ArrayList<>(exercisesNamesQuery());
    ArrayList<Template> workouts = new ArrayList<>(workoutTemplateQuery());

    private boolean goals = false;

    @FXML
    private ListView historySelectListView;

    @FXML
    private ListView historyLoggedListView;

    @FXML
    private RadioButton historyByWorkoutRadioButton;

    @FXML
    private RadioButton historyByExerciseRadioButton;

    @FXML
    private RadioButton goalsRadioButton;

    public void initialize(){

        historyByWorkoutRadioButton.setOnAction(this::radioButtonSwitching);
        historyByExerciseRadioButton.setOnAction(this::radioButtonSwitching);
        goalsRadioButton.setOnAction(this::toggleGoals);

        historySelectListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            if(historySelectListView.getSelectionModel().getSelectedItem() == null){
                return;
            }
            historyLoggedListView.getItems().clear();

            if(historyByExerciseRadioButton.selectedProperty().get()){
                if(goals){
                    historyLoggedListView.getItems().addAll(goalsWithExerciseQuery());
                }else {
                    historyLoggedListView.getItems().addAll(workoutsWithExerciseQuery());
                }
           } else {
               int temp_id = ((Template) historySelectListView.getSelectionModel().getSelectedItem()).getId();
               if (goals){
                    historyLoggedListView.getItems().addAll(goalsWithTemplateQuery(temp_id));
               } else {
                   historyLoggedListView.getItems().addAll(resultsWithTemplateQuery(temp_id));
               }
            }
        }));

        historyLoggedListView.getSelectionModel().selectedItemProperty().addListener((observable -> {
            if (historyLoggedListView.getSelectionModel().getSelectedItem() == null){
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setGraphic(null);
            if (goals){
                Goal goal = (Goal) historyLoggedListView.getSelectionModel().getSelectedItem();
                alert.setTitle("Goals");
                alert.setHeaderText(getExerciseById(goal.getExercise_id()) + "-goal added on: " + goal.getCreated());
                alert.setContentText("Weight: " + String.valueOf(goal.getWeight()) +
                        "\nSets: " + String.valueOf(goal.getSets()) + "\nReps: " + String.valueOf(goal.getReps())
                        +"\nDistance: " + String.valueOf(goal.getDistance()) + "\nDuration: " + String.valueOf(goal.getDuration()+"\nAchieved: "
                + ((goal.getAchieved() == null) ? "Not yet achieved" : goal.getAchieved())));
            } else {
                Result result = (Result) historyLoggedListView.getSelectionModel().getSelectedItem();
                alert.setTitle("Results");
                alert.setHeaderText(result.getExercise_Name() + " performed " + result.getDateTime().toString());
                alert.setContentText("Weight: " + String.valueOf(result.getWeight()) +
                        "\nSets: " + String.valueOf(result.getSets()) + "\nReps: " + String.valueOf(result.getReps())
                        +"\nDistance: " + String.valueOf(result.getDistance()) + "\nDuration: " + String.valueOf(result.getDuration()));
            }
            alert.showAndWait();

            // handles a weird listView-related IndexOutOfBoundsException.
            Platform.runLater(() -> {
                historyLoggedListView.getSelectionModel().clearSelection();
            });

        }));
    }

    private void toggleGoals(ActionEvent actionEvent) {
        RadioButton goals = (RadioButton) actionEvent.getSource();
        this.goals = goals.selectedProperty().get();
        displayLoggedHistory();
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

    public void displayLoggedHistory(){
        if(historyByWorkoutRadioButton.selectedProperty().get()){
            historyByWorkout();
        } else if(historyByExerciseRadioButton.selectedProperty().get()) {
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
        displayLoggedHistory();
    }

    private ArrayList<Exercise> exercisesNamesQuery(){
        ArrayList<Exercise> exercises = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from Exercise");
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
            ResultSet myResultSet = myStatement.executeQuery("SELECT * from Template");
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
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM Result WHERE exercise_id = ?");
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
            PreparedStatement myStatement = myConnection.prepareStatement("select workout_id, exercise_id, weight, reps, sets, distance, duration, t.name from Result as r join (select template.name, template.id, workout.time_of_exercise from Template join Workout on Template.id = Workout.template_id) as t on r.workout_id = t.time_of_exercise where t.id = ?");
            myStatement.setString(1, String.valueOf(template_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()){
                Result result = new Result(myResultSet.getDate("r.workout_id"), myResultSet.getInt("r.exercise_id")
                , myResultSet.getFloat("r.weight"), myResultSet.getInt("r.reps"), myResultSet.getInt("r.sets")
                , myResultSet.getInt("distance"), myResultSet.getInt("duration"), getExerciseById(myResultSet.getInt("exercise_id")));
                results.add(result);
            }
            myConnection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    private String getExerciseById(int id){
        for (Exercise e: exercises){
            if (e.getId() == id){
                return e.getName();
            }
        }
        return null;
    }

    private ArrayList<Goal> goalsWithExerciseQuery(){
        Exercise exercise = (Exercise) historySelectListView.getSelectionModel().getSelectedItem();
        int ex_id = exercise.getId();
        ArrayList<Goal> goals = new ArrayList<>();
        try {
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM Goal WHERE exercise_id = ?");
            myStatement.setString(1, String.valueOf(ex_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                Goal goal = new Goal(myResultSet.getInt("reps"), myResultSet.getInt("sets"),
                        myResultSet.getInt("distance"), myResultSet.getInt("duration"), myResultSet.getInt("exercise_id"),
                        myResultSet.getFloat("weight"), myResultSet.getDate("created"), myResultSet.getDate("achieved"), getExerciseById(myResultSet.getInt("exercise_id")));
                goals.add(goal);
            }
            myConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goals;
    }

    private ArrayList<Goal> goalsWithTemplateQuery(int temp_id){
        ArrayList<Goal> goals = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("select distinct g.goal_number, g.reps, g.sets, g.distance, g.duration, g.exercise_id, g.weight, g.created, g.achieved from Goal as g join Exercise as e on g.exercise_id = e.id join TemplateExercise as te on e.id = te.exercise_id join Template as t on te.template_id = ?");
            myStatement.setString(1, String.valueOf(temp_id));
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()){
                Goal goal = new Goal(myResultSet.getInt("g.reps"), myResultSet.getInt("g.sets"),
                        myResultSet.getInt("g.distance"), myResultSet.getInt("g.duration"), myResultSet.getInt("g.exercise_id"),
                        myResultSet.getFloat("g.weight"), myResultSet.getDate("g.created"), myResultSet.getDate("g.achieved"),
                        getExerciseById(myResultSet.getInt("g.exercise_id")));
                goals.add(goal);
            }
            myConnection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return goals;
    }

}
