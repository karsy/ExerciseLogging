package exerciseLogging;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class exerciseController {

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

    public void initialize(){
        // TODO: add listeners to make everything work together
        return;
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

}
