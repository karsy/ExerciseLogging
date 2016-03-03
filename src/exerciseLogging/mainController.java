package exerciseLogging;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;


public class mainController{

    // test-lists for testing with tests (TEST-TEST-TEST)
    String[] list1 = {"hei", "på","deg"};
    String[] list2 = {"bla", "la", "ta"};

    public void initialize(){

        initializeHistoryTab();
        initializeExercisesTab();
    }

    // HISTORY -----------------------

    @FXML
    private ListView historySelectListView;

    @FXML
    private ListView historyLoggedListView;

    @FXML
    private RadioButton historyByWorkoutRadioButton;

    @FXML
    private RadioButton historyByExerciseRadioButton;

    public void initializeHistoryTab(){

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

    // END OF HISTORY -----------------------

    // EXERCISES ----------------------------


    @FXML
    private Button goalsButton;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button addSubCategoryButton;

    @FXML
    private Button addExerciseButton;

    @FXML
    private TextField subCategoryTextField;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField exerciseNameTextField;

    @FXML
    private TextArea exerciseDescriptionTextArea;

    @FXML
    private ListView exercisesListView;

    @FXML
    private ComboBox subCategoryComboBox;

    @FXML
    private ComboBox categoryComboBox;

    public void initializeExercisesTab(){
        // TODO: add listeners to make everything work together
    }


    public void displayGoals(){
        // TODO: finish method with prompts
        // display goals based on what exericse is selected.
    }

    public void addCategory() {
        if (! categoryTextField.getText().isEmpty()){
            // TODO: finish method
            // add category to categories if its not already there.
        }
    }

    public void addSubCategory(){
        if (! subCategoryTextField.getText().isEmpty()){
            // TODO: finish method
            // add subcategory to subcategories if its not already there.
        }
    }

    public void addExercise(){
        if (! exerciseNameTextField.getText().isEmpty() && ! exerciseDescriptionTextArea.getText().isEmpty()){
            if (! categoryComboBox.getSelectionModel().isEmpty() && ! subCategoryComboBox.getSelectionModel().isEmpty()){
                // TODO: finish method
                // add exercise to exercises if its not already there.
            }
        }
    }

    public void changeExerciseSubCategory(){
        sortExerciseListView();
    }

    public void changeExerciseCategory(){
        sortExerciseListView();
    }

    private void sortExerciseListView(){
        // TODO: finish method
        /* if (categoryComboBox.getSelectionModel().getSelectedItem().getSubcategories().contains(
                subCategoryComboBox.getSelectionModel().getSelectedItem())) {
            exercisesListView.getItems().clear();
            // .addAll exercises that correspong to the subcategory
        }*/
    }

    // END OF EXERCISES ------------------
}
