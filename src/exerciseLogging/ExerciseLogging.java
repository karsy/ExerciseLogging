package exerciseLogging;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ExerciseLogging extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        Pane root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/main.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setHeight(340);
        primaryStage.setWidth(480);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
