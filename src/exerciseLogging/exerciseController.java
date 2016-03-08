package exerciseLogging;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class exerciseController {

    private String URL = "jdbc:mysql://" + System.getenv("IP") + ":" + System.getenv("PORT") + "/" + System.getenv("DBNAME") + "?useSSL=false";
    private String username = System.getenv("USERNAME");
    private String password = System.getenv("PASSWORD");

    private ArrayList<Category> categories = new ArrayList<>();
    private ObservableList<Category> categoriesObservable = FXCollections.observableList(categories);

    @FXML
    private Button goalsButton, addCategoryButton, addSubCategoryButton, addExerciseButton;

    @FXML
    private TextField subCategoryTextField, categoryTextField, exerciseNameTextField;

    @FXML
    private TextArea exerciseDescriptionTextArea;

    @FXML
    private ListView exercisesListView;

    @FXML
    private ComboBox subCategoryComboBox, categoryComboBox;

    Spinner<Double> weight = new Spinner<>();
    Spinner<Integer> reps = new Spinner<>();
    Spinner<Integer> sets = new Spinner<>();
    Spinner<Integer> distance = new Spinner<>();
    Spinner<Integer> duration = new Spinner<>();



    public void initialize(){
        exercisesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            getSubCategories();
        });
        subCategoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            getExercises();
        });
        getCategories();

        weight.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.25));
        reps.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        sets.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        distance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        duration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        return;
    }


    public void displayGoals(){
        Exercise ex = (Exercise)(exercisesListView.getSelectionModel().getSelectedItems().get(0));
        if (ex != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Make a new goal for the chosen exercise");
            Button save = new Button("Save");
            save.setOnAction((event)->addGoal());
            alert.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL);
            GridPane gp = new GridPane();
            gp.setHgap(10);
            gp.setVgap(10);

            gp.add(new Text(), 0, 0, 2, 1);
            gp.add(weight, 1, 1);
            gp.add(new Text("Weight"), 0, 1);
            gp.add(reps, 1, 2);
            gp.add(new Text("Reps"), 0, 2);
            gp.add(sets, 1, 3);
            gp.add(new Text("Sets"), 0, 3);
            gp.add(distance, 1, 4);
            gp.add(new Text("Distance"), 0, 4);
            gp.add(duration, 1, 5);
            gp.add(new Text("Duration"), 0, 5);
            gp.add(save, 0, 6);

            alert.getDialogPane().setContent(gp);
            alert.show();
        }
    }

    public void addGoal() {
        Exercise ex = (Exercise)(exercisesListView.getSelectionModel().getSelectedItems().get(0));
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Goal (weight, reps, sets, distance, duration, exercise_id)VALUES(?, ?, ?, ?, ?, ?)");
            myStatement.setString(1, String.valueOf(weight.getValue()));
            myStatement.setString(2, String.valueOf(reps.getValue()));
            myStatement.setString(3, String.valueOf(sets.getValue()));
            myStatement.setString(4, String.valueOf(distance.getValue()));
            myStatement.setString(5, String.valueOf(duration.getValue()));
            myStatement.setString(6, String.valueOf(ex.getId()));
            myStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
        categoryTextField.setText("");
        getCategories();
    }

    public void addCategory() {
        if (! categoryTextField.getText().isEmpty()){
            String text = categoryTextField.getText();
            try{
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Category VALUES(null, ?)");
                myStatement.setString(1, text);
                myStatement.execute();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        categoryTextField.setText("");
        getCategories();
    }

    public void addSubCategory(){
        Category cat = (Category)categoryComboBox.getSelectionModel().getSelectedItem();
        if (! subCategoryTextField.getText().isEmpty() && cat != null ){
            String text = subCategoryTextField.getText();

            try{
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO SubCategory VALUES(null, ?)", Statement.RETURN_GENERATED_KEYS);
                myStatement.setString(1, text);
                int ret = myStatement.executeUpdate();
                ResultSet rs = myStatement.getGeneratedKeys();
                int subCatID = -1;
                if (rs.next()) {
                    subCatID = rs.getInt(1);
                }
                System.out.println("Cat: " + cat.getId() + "Sub: " + subCatID);
                PreparedStatement myStatement2 = myConnection.prepareStatement("INSERT INTO CategoryRelations VALUES(?, ?)");
                myStatement2.setString(1, String.valueOf(cat.getId()));
                myStatement2.setString(2, String.valueOf(subCatID));
                myStatement2.execute();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        subCategoryTextField.setText("");
        getSubCategories();
    }

    public void addExercise(){
        if (! exerciseNameTextField.getText().isEmpty() && ! exerciseDescriptionTextArea.getText().isEmpty()){
            if (! categoryComboBox.getSelectionModel().isEmpty() && ! subCategoryComboBox.getSelectionModel().isEmpty()){
                Category subCat = (Category)subCategoryComboBox.getSelectionModel().getSelectedItem();
                try{
                    Connection myConnection = DriverManager.getConnection(URL, username, password);
                    PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Exercise VALUES(null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    myStatement.setString(1, exerciseNameTextField.getText());
                    myStatement.setString(2, exerciseDescriptionTextArea.getText());
                    myStatement.executeUpdate();
                    ResultSet rs = myStatement.getGeneratedKeys();
                    int exID = -1;
                    if (rs.next()) {
                        exID = rs.getInt(1);
                    }
                    PreparedStatement myStatement2 = myConnection.prepareStatement("INSERT INTO ExerciseSubCategory VALUES(?, ?)");
                    myStatement2.setString(1, String.valueOf(exID));
                    myStatement2.setString(2, String.valueOf(subCat.getId()));
                    myStatement2.execute();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            exerciseNameTextField.setText("");
            exerciseDescriptionTextArea.setText("");
            getExercises();
        }
    }

    private void getCategories(){
        ArrayList<Category> res = new ArrayList<>();
        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM Category");
            ResultSet rs = myStatement.executeQuery();
            while (rs.next()) {
                res.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        categoryComboBox.getSelectionModel().clearSelection();
        this.categoryComboBox.getItems().setAll(res);
    }

    private void getSubCategories() {
        ArrayList<Category> res = new ArrayList<>();
        Category cat = (Category) categoryComboBox.getSelectionModel().getSelectedItem();
        if (cat != null) {
            String query = "SELECT * FROM SubCategory AS SC JOIN CategoryRelations AS CR ON SC.id = CR.subcategory_id WHERE CR.category_id=?";
            try {
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement(query);
                myStatement.setString(1, String.valueOf(cat.getId()));
                ResultSet rs = myStatement.executeQuery();
                while (rs.next()) {
                    res.add(new Category(rs.getInt("id"), rs.getString("name")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        subCategoryComboBox.getSelectionModel().clearSelection();
        this.subCategoryComboBox.getItems().setAll(res);
        getExercises();
    }

    private void getExercises(){
        Category cat = (Category)categoryComboBox.getSelectionModel().getSelectedItem();
        Category subCat = (Category)subCategoryComboBox.getSelectionModel().getSelectedItem();
        ArrayList<Exercise> res = new ArrayList<>();
        if (cat != null && subCat != null) {
            try {
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM Exercise AS E JOIN ExerciseSubCategory AS S ON E.id=S.exercise_id WHERE S.subcategory_id= ?");
                myStatement.setString(1, String.valueOf(subCat.getId()));
                ResultSet rs = myStatement.executeQuery();
                while (rs.next()) {
                    res.add(new Exercise(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        exercisesListView.getSelectionModel().clearSelection();
        this.exercisesListView.getItems().setAll(res);
    }
}
