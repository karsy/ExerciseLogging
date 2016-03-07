package exerciseLogging;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.Event.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;

public class logController {
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
        woHours.getEditor().focusedProperty().addListener(observable -> {
            updateField(woHours);
        });

        woMinutes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,5));
        woMinutes.getEditor().setOnKeyTyped(onlyNumericFields());
        woMinutes.getEditor().focusedProperty().addListener(observable -> {
            updateField(woMinutes);
        });

        woDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,0,10));
        woDuration.getEditor().setOnKeyTyped(onlyNumericFields());
        woDuration.getEditor().focusedProperty().addListener(observable -> {
            updateField(woDuration);
        });

        woShape.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));
        woShape.getEditor().setOnKeyTyped(onlyNumericFields());
        woShape.getEditor().focusedProperty().addListener(observable -> {
            updateField(woShape);
        });

        woPerformance.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,5));
        woPerformance.getEditor().setOnKeyTyped(onlyNumericFields());
        woPerformance.getEditor().focusedProperty().addListener(observable -> {
            updateField(woPerformance);
        });

    }

    private void updateField(Spinner target) {
        if (!target.getEditor().focusedProperty().getValue()) {
            if (target.getEditor().textProperty().isEmpty().getValue()){
                target.getEditor().setText("0");
            }
            target.getValueFactory().setValue(target.getValueFactory().getConverter().fromString(target.getEditor().getText()));
        }
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!target.getEditor().getText().isEmpty()) {
                        target.getEditor().selectAll();
                    }
                }
            });
        }
    }

    private EventHandler<KeyEvent> onlyNumericFields () {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!event.getCharacter().matches("[0-9]")){
                    event.consume();
                }
            }
        };
    }

    @FXML
    public void logWorkout(ActionEvent actionEvent) {
        System.out.println("loging stuff");
    }

}
