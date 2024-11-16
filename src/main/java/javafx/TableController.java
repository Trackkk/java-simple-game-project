package javafx;

import gameresult.JsonResultManager;
import gameresult.GameResult;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;
import puzzle.game.PuzzleController;
import util.DurationUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

/**
 * Controller class for managing the game results table view.
 */
public class TableController {

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<GameResult> tableView;

    @FXML
    private TableColumn<GameResult, String> userName;

    @FXML
    private TableColumn<GameResult, Integer> numberOfMoves;

    @FXML
    private TableColumn<GameResult, String> duration;

    @FXML
    public TextField playerName;

    private ObservableList<GameResult> gameResults;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() throws IOException {
        userName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfMoves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        duration.setCellValueFactory(cellData -> {
            Duration gameDuration = cellData.getValue().getDuration();
            String formattedDuration = DurationUtil.formatDuration(gameDuration);
            return new ReadOnlyStringWrapper(formattedDuration);
        });
        loadGameResults();
        tableView.setItems(gameResults);
        registerKeyEventHandler();
        ObservableList<GameResult> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonResultManager(Path.of("gameresult.json")).getBest(10));
        tableView.setItems(observableList);
    }

    /**
     * Loads the game results from a JSON file and populates the table view.
     */
    private void loadGameResults() {
        try {
            var resultManager = new JsonResultManager(Paths.get("gameresult.json"));
            List<GameResult> results = resultManager.getAll();
            gameResults = FXCollections.observableArrayList(results);
        } catch (IOException e) {
            Logger.error("Failed to load game results: {}", e.getMessage());
            gameResults = FXCollections.observableArrayList();
        }
    }

    /**
     * Switches the scene to the puzzle game view if the player name is not empty.
     *
     * @param event the action event that triggered this method
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    public void switchScene(ActionEvent event) throws IOException {
        if (!playerName.getText().trim().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
            Parent root = fxmlLoader.load();

            PuzzleController puzzleController = fxmlLoader.getController();
            puzzleController.setPlayerName(playerName.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Logger.error("Player name cannot be empty");
        }
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> stackPane.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    /**
     * Handles key press events for quitting the application or switching scenes.
     *
     * @param keyEvent the key event that was pressed
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        } else if (keyEvent.getCode() == KeyCode.ENTER) {
            try {
                ActionEvent event = new ActionEvent(stackPane, stackPane.getScene().getWindow());
                switchScene(event);
            } catch (IOException e) {
                Logger.error("Failed to load the next scene: {}", e.getMessage());
            }
        }
    }
}
