package puzzle.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Simple Maze Game.
 */
public class PuzzleApplication extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML file
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/table.fxml"));
        stage.setTitle("Simple Maze Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
