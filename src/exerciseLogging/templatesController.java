package exerciseLogging;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class templatesController {

    private final String URL = "jdbc:mysql://" + System.getenv("IP") + ":" + System.getenv("PORT") + "/" + System.getenv("DBNAME") + "?useSSL=false";
    private final String username = System.getenv("USERNAME");
    private final String password = System.getenv("PASSWORD");

    @FXML
    private TextField descField;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private ListView<Exercise> exercises;

    @FXML
    private ListView<Exercise> selectedExercises;

    @FXML
    private ListView<Template> templateList;

    private ObservableList<Exercise> exerciseList = FXCollections.observableArrayList();
    private ObservableList<Exercise> chosenExercises = FXCollections.observableArrayList();
    private ObservableList<Template> templates = FXCollections.observableArrayList();

    private Map<Integer, Exercise> exerciseMap = new HashMap<>();
    private Map<Integer, Template> templateMap = new HashMap<>();

    @FXML
    private void initialize() {

        loadExercises();
        loadTemplates();

        exercises.setItems(exerciseList);
        selectedExercises.setItems(chosenExercises);
        templateList.setItems(templates);

        exercises.setOnMousePressed(selectExercise);
        selectedExercises.setOnMousePressed(removeExercise);
    }

    private void loadExercises() {
        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            Statement s = conn.createStatement();
            if (s.execute("SELECT * FROM Exercise")) {
                ResultSet results = s.getResultSet();
                while (results.next()) {

                    int id = results.getInt("id");
                    exerciseMap.put(id, new Exercise(id, results.getString("name"), results.getString("description")));

                    exerciseList.add(new Exercise(results.getInt("id"), results.getString("name"), results.getString("description")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTemplates() {
        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            Statement s = conn.createStatement();
            if (s.execute("SELECT * FROM Template AS T JOIN TemplateExercise AS TE ON T.id = TE.template_id JOIN Exercise AS E ON E.id = TE.exercise_id")) {
                ResultSet results = s.getResultSet();
                while(results.next()) {
                    int templateId = results.getInt("T.id");
                    Template template = templateMap.get(templateId);
                    if (template == null) {
                        template = new Template(templateId, results.getString("T.name"), results.getString("T.description"));
                    }

                    int exerciseId = results.getInt("E.id");
                    Exercise exercise = exerciseMap.get(exerciseId);
                    if (exercise == null)
                        continue;

                    template.addExercise(exercise);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private EventHandler<? super MouseEvent> selectExercise = (e) -> {
        int index = exercises.getSelectionModel().getSelectedIndex();
        Exercise chosen = exerciseList.get(index);
        exerciseList.remove(index);
        chosenExercises.add(chosen);
        exercises.getSelectionModel().clearSelection();
    };

    private EventHandler<? super MouseEvent> removeExercise = (e) -> {
        int index = selectedExercises.getSelectionModel().getSelectedIndex();
        Exercise chosen = chosenExercises.get(index);
        exerciseList.add(chosen);
        chosenExercises.remove(index);
        selectedExercises.getSelectionModel().clearSelection();
    };

    @FXML
    private void saveTemplate(ActionEvent event) {
        String description = descField.getText();
        String name = nameField.getText();
    }

    
}
