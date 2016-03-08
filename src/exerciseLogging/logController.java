package exerciseLogging;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class logController {
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

        woHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        woHours.getEditor().setOnKeyTyped(onlyNumericFields());
        woHours.getEditor().setOnKeyPressed(allowArrowKeys(woHours));
        woHours.getEditor().focusedProperty().addListener(observable -> {updateField(woHours);});

        woMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,5));
        woMinutes.getEditor().setOnKeyTyped(onlyNumericFields());
        woMinutes.getEditor().setOnKeyPressed(allowArrowKeys(woMinutes));
        woMinutes.getEditor().focusedProperty().addListener(observable -> {updateField(woMinutes);});

        woDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,0,10));
        woDuration.getEditor().setOnKeyTyped(onlyNumericFields());
        woDuration.getEditor().setOnKeyPressed(allowArrowKeys(woDuration));
        woDuration.getEditor().focusedProperty().addListener(observable -> {updateField(woDuration);});

        woShape.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));
        woShape.getEditor().setOnKeyTyped(onlyNumericFields());
        woShape.getEditor().setOnKeyPressed(allowArrowKeys(woShape));
        woShape.getEditor().focusedProperty().addListener(observable -> {updateField(woShape);});

        woPerformance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));
        woPerformance.getEditor().setOnKeyTyped(onlyNumericFields());
        woPerformance.getEditor().setOnKeyPressed(allowArrowKeys(woPerformance));
        woPerformance.getEditor().focusedProperty().addListener(observable -> {updateField(woPerformance);});

        new ToggleGroup().getToggles().addAll(indoorButton,outdoorButton);
        indoorButton.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.ENTER){indoorButton.fire();}});
        outdoorButton.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.ENTER){outdoorButton.fire();}});

        woNotes.setOnKeyPressed(e -> {if (e.getCode()==KeyCode.TAB) {logButton.requestFocus();e.consume();}});
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
        System.out.println("loging stuff");
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
