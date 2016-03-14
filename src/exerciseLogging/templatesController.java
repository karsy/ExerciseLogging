package exerciseLogging;


import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class templatesController {

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

    @FXML
    private void initialize() {

        // Random test exercises
        for (int i = 0; i < 3; i++) {
            exerciseList.add(new Exercise(i, "Exercise" + i, "lolol"));
        }

        exercises.setItems(exerciseList);
        selectedExercises.setItems(chosenExercises);

        exercises.setOnMousePressed(selectExercise);
        selectedExercises.setOnMousePressed(removeExercise);
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
