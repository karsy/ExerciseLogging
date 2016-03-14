package exerciseLogging;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class templatesController {

    private final String URL = "jdbc:mysql://" + System.getenv("IP") + ":" + System.getenv("PORT") + "/" + System.getenv("DBNAME") + "?useSSL=false";
    private final String username = System.getenv("USERNAME");
    private final String password = System.getenv("PASSWORD");

    @FXML
    private TextField descField;

    @FXML
    private TextField nameField;

    @FXML
    private ListView<Exercise> exerciseListView;

    @FXML
    private ListView<Exercise> selectedListView;

    @FXML
    private ListView<Template> templateListView;

    private ObservableList<Exercise> exercises = FXCollections.observableArrayList();
    private ObservableList<Exercise> chosenExercises = FXCollections.observableArrayList();
    private ObservableList<Template> templates = FXCollections.observableArrayList();

    private Map<Integer, Exercise> exerciseMap = new HashMap<>();
    private Map<Integer, Template> templateMap = new HashMap<>();

    private Template currentTemplate;

    @FXML
    private void initialize() {

        loadExercises();
        loadTemplates();

        exerciseListView.setItems(exercises);
        selectedListView.setItems(chosenExercises);
        templateListView.setItems(templates);

        exerciseListView.setOnMousePressed(event -> selectExercise());
        selectedListView.setOnMousePressed(event -> removeExercise());
        templateListView.setOnMousePressed(event -> selectTemplate());

        templates.addAll(templateMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
        exercises.addAll(exerciseMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentTemplate.setName(newValue);
            templateListView.setItems(null);
            templateListView.setItems(templates);
        });
        descField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentTemplate.setDescription(newValue);
            templateListView.setItems(null);
            templateListView.setItems(templates);
        });
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
                    templateMap.put(templateId, template);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectExercise() {
        int index = exerciseListView.getSelectionModel().getSelectedIndex();
        if (index == -1 || currentTemplate == null) {
            return;
        }
        Exercise chosen = exercises.get(index);
        exercises.remove(index);
        chosenExercises.add(chosen);
        exerciseListView.getSelectionModel().clearSelection();

        currentTemplate.addExercise(chosen);
    }

    private void removeExercise() {
        int index = selectedListView.getSelectionModel().getSelectedIndex();
        if (index == -1 || currentTemplate == null) {
            return;
        }
        Exercise chosen = chosenExercises.get(index);
        exercises.add(chosen);
        chosenExercises.remove(index);
        selectedListView.getSelectionModel().clearSelection();

        currentTemplate.removeExercise(chosen);
    }

    private void selectTemplate() {
        int index = templateListView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            return;
        }

        if (currentTemplate != null)
            saveTemplate(null);

        currentTemplate = templates.get(index);

        nameField.setText(currentTemplate.getName());
        descField.setText(currentTemplate.getDescription());

        resetExercises();

        for (Exercise exercise : currentTemplate.getExercises()) {
            int exerciseIndex = exercises.indexOf(exercise);
            exerciseListView.getSelectionModel().select(exerciseIndex);
            selectExercise();
        }
    }

    @FXML
    private void saveTemplate(ActionEvent event) {
        String description = descField.getText();
        String name = nameField.getText();

        currentTemplate.setName(name);
        currentTemplate.setDescription(description);

        if (event != null) {
            storeTemplates();
        }
    }

    public void storeTemplates() {
        for (Template template: templates) {
            if (template.getId() != -1) {
                updateTemplate(template);
            } else {
                int newId = saveNewTemplate(template);
                template.setId(newId);
            }
        }
    }

    private void updateTemplate(Template template) {
        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            PreparedStatement statement = conn.prepareStatement("UPDATE Template SET name = ?, description = ? WHERE id = " + template.getId());
            statement.setString(1, template.getName());
            statement.setString(2, template.getDescription());
            statement.executeUpdate();

            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM TemplateExercise WHERE id = ?");
            deleteStatement.setInt(1, template.getId());
            deleteStatement.executeUpdate();

            for (Exercise exercise : template.getExercises()) {
                PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO TemplateExercise VALUES(?, ?)");
                insertStatement.setInt(1, template.getId());
                insertStatement.setInt(2, exercise.getId());
                insertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int saveNewTemplate(Template template) {
        int templateId = -1;
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Template VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            myStatement.setString(1, template.getName());
            myStatement.setString(2, template.getDescription());
            myStatement.executeUpdate();
            ResultSet rs = myStatement.getGeneratedKeys();
            if (rs.next()) {
                templateId = rs.getInt(1);
            }

            for (Exercise exercise : template.getExercises()) {
                PreparedStatement myStatement2 = myConnection.prepareStatement("INSERT INTO TemplateExercise VALUES(?, ?)");
                myStatement2.setInt(1, templateId);
                myStatement2.setInt(2, exercise.getId());
                myStatement2.execute();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return templateId;
    }

    public void newTemplate(ActionEvent actionEvent) {
        if (currentTemplate != null)
            saveTemplate(null);

        currentTemplate = new Template(-1, "Name", "Description");
        nameField.setText(currentTemplate.getName());
        descField.setText(currentTemplate.getDescription());
        resetExercises();
        templates.add(currentTemplate);
    }

    public void deleteTemplate(ActionEvent actionEvent) {
        if (currentTemplate.getId() != -1) {
            try {
                Connection conn = DriverManager.getConnection(URL, username, password);
                PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM Template WHERE id = ?");
                deleteStatement.setInt(1, currentTemplate.getId());
                deleteStatement.executeUpdate();

                PreparedStatement statement = conn.prepareStatement("DELETE FROM TemplateExercise WHERE template_id = ?");
                statement.setInt(1, currentTemplate.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        templates.remove(currentTemplate);
        templateListView.getSelectionModel().select(1);
        selectTemplate();
        
    }

    private void resetExercises() {
        while (chosenExercises.size() > 0) {
            selectedListView.getSelectionModel().select(0);
            removeExercise();
        }
    }
}
