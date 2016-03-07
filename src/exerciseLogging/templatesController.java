package exerciseLogging;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class templatesController {

    @FXML
    private TextField descField;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private void saveTemplate(ActionEvent event) {
        String description = descField.getText();
        String name = nameField.getText();

    }
}
