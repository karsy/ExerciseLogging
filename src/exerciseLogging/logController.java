package exerciseLogging;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class logController {

    private String URL = "jdbc:mysql://localhost:3306/trainingdiary?useSSL=false";
    private String username = "user";
    private String password = "user";

    private ObservableList<String> templates = FXCollections.observableArrayList();
    private Map<String, Integer> templatesIdNameMap = new HashMap<>();
    private ObservableList<String> exercises = FXCollections.observableArrayList();
    private Map<String, Integer> exercisesIdNameMap = new HashMap<>();

    ListView exerciseView = new ListView();
    private Alert alert;

    Spinner<Double> weight = new Spinner<>();
    Spinner<Integer> reps = new Spinner<>();
    Spinner<Integer> sets = new Spinner<>();
    Spinner<Integer> distance = new Spinner<>();
    Spinner<Integer> duration = new Spinner<>();

    private ArrayList<Spinner> spinners = new ArrayList<>();

    @FXML
    private Text infoText;
    @FXML
    private RadioButton indoorButton;
    @FXML
    private RadioButton outdoorButton;

    @FXML
    private Text conditionTextFirstLine;
    @FXML
    private TextField conditionInputFirstLine;
    @FXML
    private Text conditionTextSecondLine;
    @FXML
    private TextField conditionInputSecondLine;
    @FXML
    private Spinner woDuration;
    @FXML
    private DatePicker woDate;
    @FXML
    private Spinner woHours;
    @FXML
    private Spinner woMinutes;
    @FXML
    private TextArea woNotes;
    @FXML
    private Spinner woShape;
    @FXML
    private Spinner woPerformance;
    @FXML
    private Button logButton;
    @FXML
    private ComboBox woTemplate;

    public void initialize(){
        woDate.setValue(LocalDate.now());

        weight.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.25));
        reps.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        sets.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        distance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        duration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        woHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));


        woMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,5));


        woDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,0,10));


        woShape.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));


        woPerformance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));


        spinners.addAll(Arrays.asList(woDuration,woHours,woMinutes,woShape,woPerformance, weight,reps,sets,distance,duration));
        for (Spinner spin: spinners){
            spin.setEditable(true);
            spin.getEditor().setOnKeyTyped(onlyNumericFields());
            spin.getEditor().setOnKeyPressed(allowArrowKeys(spin));
            spin.getEditor().focusedProperty().addListener(observable -> {updateField(spin);});
        }

        new ToggleGroup().getToggles().addAll(indoorButton,outdoorButton);
        indoorButton.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.ENTER){indoorButton.fire();}});
        outdoorButton.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.ENTER){outdoorButton.fire();}});

        woNotes.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.TAB) {logButton.requestFocus();e.consume();}});

        exerciseView.setItems(exercises);
        woTemplate.setItems(templates);
        woTemplate.focusedProperty().addListener(observable -> {if (woTemplate.isFocused()) { updateTemplates(); }});
    }

    private void updateTemplates() {
        templates.clear();
        templatesIdNameMap.clear();
        try {
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery("SELECT name, id FROM Template");
            while (myResultSet.next()){
                String stringToShow = myResultSet.getString("name");
                if (templates.contains(stringToShow)){stringToShow = stringToShow+" ("+String.valueOf(myResultSet.getInt("id")+")");}
                templates.add(stringToShow);
                templatesIdNameMap.put(stringToShow,myResultSet.getInt("id"));
            }
            myConnection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateExercises(){
        exercises.clear();
        exercisesIdNameMap.clear();
        String templateInUse;
        try { templateInUse = woTemplate.getSelectionModel().getSelectedItem().toString();}
        catch (Exception e){ infoText.setText("Choose a template");return; }
        try {
            int templateId = templatesIdNameMap.get(templateInUse);
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT * FROM Template AS T JOIN TemplateExercise AS TE ON T.id = TE.template_id JOIN Exercise AS E ON E.id = TE.exercise_id WHERE T.id = "+templateId);
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()){
                String stringToShow = myResultSet.getString("E.name");
                if (exercises.contains(stringToShow)){stringToShow = stringToShow+" ("+String.valueOf(myResultSet.getInt("E.id")+")");}
                exercises.add(stringToShow);
                exercisesIdNameMap.put(stringToShow,myResultSet.getInt("E.id"));
            }
            myConnection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private EventHandler<KeyEvent> allowArrowKeys(Spinner target) {
        return event -> {
            if (event.getCode() == KeyCode.UP){
                target.increment();
            }
            else if (event.getCode() == KeyCode.DOWN){
                target.decrement();
            }
        };
    }

    private void updateField(Spinner target) {
        if (!target.getEditor().focusedProperty().getValue()) {
            if (target.getEditor().textProperty().isEmpty().getValue()){
                target.getEditor().setText("0");
            }
            target.getValueFactory().setValue(target.getValueFactory().getConverter().fromString(target.getEditor().getText()));
        }
        else {
            Platform.runLater(() -> {
                if (!target.getEditor().getText().isEmpty()) {
                    target.getEditor().selectAll();
                }
            });
        }
    }

    private EventHandler<KeyEvent> onlyNumericFields () {
        return event -> {
            if (!event.getCharacter().matches("[0-9]")){
                event.consume();
            }
        };
    }

    @FXML
    public void logWorkout(ActionEvent actionEvent) {
        infoText.setText("");
        if (!(indoorButton.isSelected() || outdoorButton.isSelected())){ infoText.setText("Please select location"); return; }
        try { Integer.valueOf(conditionInputSecondLine.getText()); }
        catch (Exception e){ infoText.setText("Not integer in "+conditionTextSecondLine.getText()+" field"); return; }


        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Register workout results");
        alert.setTitle("<3");

        updateExercises();
        exerciseView.getSelectionModel().selectFirst();

        Button save = new Button("Save");
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> saveResults());

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.add(new Text("In weight, use arrows for decimal values"), 0, 0, 2, 1);
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
        gp.add(new Text("(for this exercise)"),1,6);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(exerciseView,gp);
        alert.getDialogPane().setContent(hbox);
        alert.show();
        addWorkout();

    }

    private void addWorkout() {
        int conditionId = 0;
        if (indoorButton.isSelected()){
            try {
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Indoor (air_condition, audience) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
                myStatement.setString(1, conditionInputFirstLine.getText());
                myStatement.setInt(2, Integer.valueOf(conditionInputSecondLine.getText()));
                myStatement.execute();
                ResultSet rs = myStatement.getGeneratedKeys();
                if (rs.next()){
                    PreparedStatement myStatement2 = myConnection.prepareStatement("INSERT INTO ConditionType (outdoor_id, indoor_id) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
                    myStatement2.setInt(1,1);
                    myStatement2.setInt(2, rs.getInt(1));
                    myStatement2.execute();
                    ResultSet rs2 = myStatement2.getGeneratedKeys();
                    if (rs2.next()){conditionId=rs2.getInt(1);}
                }
                myConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Connection myConnection = DriverManager.getConnection(URL, username, password);
                PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Outdoor (weather, temperature) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
                myStatement.setString(1, conditionInputFirstLine.getText());
                myStatement.setInt(2, Integer.valueOf(conditionInputSecondLine.getText()));
                myStatement.execute();
                ResultSet rs = myStatement.getGeneratedKeys();
                if (rs.next()){
                    PreparedStatement myStatement2 = myConnection.prepareStatement("INSERT INTO ConditionType (outdoor_id, indoor_id) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
                    myStatement2.setInt(1, rs.getInt(1));
                    myStatement2.setInt(2,1);
                    myStatement2.execute();
                    ResultSet rs2 = myStatement2.getGeneratedKeys();
                    if (rs2.next()){conditionId=rs2.getInt(1);}
                }
                myConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            int year = woDate.getValue().getYear()-1900;
            int month = woDate.getValue().getMonthValue()-1;
            int day = woDate.getValue().getDayOfMonth();
            int hour = Integer.valueOf(woHours.getValue().toString());
            int minutes = Integer.valueOf(woMinutes.getValue().toString());
            Timestamp datetime = new Timestamp(year,month,day,hour,minutes,0,0);

            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Workout (time_of_exercise, duration, shape, performance, note, condition_id, template_id) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            myStatement.setTimestamp(1, datetime);
            myStatement.setInt(2, Integer.valueOf(woDuration.getValue().toString()));
            myStatement.setInt(3, Integer.valueOf(woShape.getValue().toString()));
            myStatement.setInt(4, Integer.valueOf(woPerformance.getValue().toString()));
            myStatement.setString(5, woNotes.getText());
            myStatement.setInt(6, conditionId);
            myStatement.setInt(7, templatesIdNameMap.get(woTemplate.getSelectionModel().getSelectedItem().toString()));
            myStatement.execute();
            myConnection.close();
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
            infoText.setText("Already existing workout");
            alert.close();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveResults() {
        int year = woDate.getValue().getYear()-1900;
        int month = woDate.getValue().getMonthValue()-1;
        int day = woDate.getValue().getDayOfMonth();
        int hour = Integer.valueOf(woHours.getValue().toString());
        int minutes = Integer.valueOf(woMinutes.getValue().toString());
        Timestamp datetime = new Timestamp(year,month,day,hour,minutes,0,0);
        int exerciseId = exercisesIdNameMap.get(exerciseView.getSelectionModel().getSelectedItem().toString());

        try{
            Connection myConnection = DriverManager.getConnection(URL, username, password);
            PreparedStatement myStatement = myConnection.prepareStatement("INSERT INTO Result (workout_id, exercise_id, weight, reps, sets, distance, duration) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            myStatement.setTimestamp(1, datetime);
            myStatement.setInt(2, exerciseId);
            myStatement.setFloat(3, Float.parseFloat(weight.getValue().toString()));
            myStatement.setInt(4, reps.getValue());
            myStatement.setInt(5, sets.getValue());
            myStatement.setInt(6, distance.getValue());
            myStatement.setInt(7, duration.getValue());
            myStatement.execute();

        } catch (Exception e){
            e.printStackTrace();
        }
        exerciseView.getSelectionModel().selectNext();
    }

    public void indoorButtonPressed(ActionEvent actionEvent) {
        conditionTextFirstLine.setText("Air Condition");
        conditionTextSecondLine.setText("Audience");
    }

    public void outdoorButtonPressed(ActionEvent actionEvent) {
        conditionTextFirstLine.setText("Weather");
        conditionTextSecondLine.setText("Temperature");
    }
}
