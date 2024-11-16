package puzzle.game;

import gameresult.JsonResultManager;
import gameresult.GameResult;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.tinylog.Logger;
import puzzle.model.Direction;
import puzzle.model.PuzzleState;
import util.Stopwatch;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

/**
 * Controller class for managing the puzzle game.
 */
public class PuzzleController {

    @FXML
    private GridPane grid;

    @FXML
    private Label goalLabel;

    @FXML
    private TextField numberOfMovesField;

    private String playerName;

    @FXML
    private Label stopwatchLabel;

    private final Stopwatch stopwatch = new Stopwatch();

    private PuzzleState state;

    private Circle ball;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private Instant startTime;

    private Duration duration;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        stopwatchLabel.textProperty().bind(stopwatch.timeProperty());
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    /**
     * Sets the player name.
     *
     * @param playerName the name of the player
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        Logger.info("Player name: {}", playerName);
    }

    /**
     * Switches the scene to the table view.
     *
     * @param event the action event that triggered this method
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    public void switchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/table.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Restarts the game by resetting the state and updating the UI.
     */
    private void restartGame() {
        createState();
        clearAndPopulateGrid();
        numberOfMoves.set(0);
        stopwatch.stop();
        stopwatch.reset();
    }

    /**
     * Creates a new puzzle state.
     */
    private void createState() {
        state = new PuzzleState();
        state.solvedProperty().addListener(this::handleSolved);
    }

    /**
     * Clears the grid and populates it with the initial game elements.
     */
    private void clearAndPopulateGrid() {
        createGridLines();
        createWalls();
        addGoalLabel();
        ball = createBall();
        updateBallPosition();
    }

    /**
     * Adds the goal label to the grid.
     */
    private void addGoalLabel() {
        var goal = state.getGoalPosition();
        GridPane.setRowIndex(goalLabel, goal.row());
        GridPane.setColumnIndex(goalLabel, goal.col());
        grid.getChildren().add(goalLabel);
    }

    /**
     * Creates a new ball.
     *
     * @return the created ball
     */
    private Circle createBall() {
        ball = new Circle(50, Color.BLUE);
        return ball;
    }

    /**
     * Updates the ball's position in the grid.
     */
    private void updateBallPosition() {
        var position = state.getBallPosition();
        GridPane.setRowIndex(ball, position.row());
        GridPane.setColumnIndex(ball, position.col());
        if (!grid.getChildren().contains(ball)) {
            grid.getChildren().add(ball);
        }
    }

    /**
     * Creates walls in the grid based on the puzzle state.
     */
    private void createWalls() {
        for (var wallPosition : state.getWalls()) {
            createWall(wallPosition.getRow(), wallPosition.getCol(), wallPosition.getWall());
        }
    }

    /**
     * Creates a single wall in the grid.
     *
     * @param row  the row index
     * @param col  the column index
     * @param wall the wall type
     */
    private void createWall(int row, int col, PuzzleState.Wall wall) {
        Rectangle wallShape = new Rectangle();
        switch (wall) {
            case HORIZONTAL:
                wallShape.setWidth(100);
                wallShape.setHeight(12);
                GridPane.setRowIndex(wallShape, row);
                GridPane.setColumnIndex(wallShape, col);
                wallShape.setTranslateY(-50);
                break;
            case VERTICAL:
                wallShape.setWidth(12);
                wallShape.setHeight(100);
                GridPane.setRowIndex(wallShape, row);
                GridPane.setColumnIndex(wallShape, col + 1);
                wallShape.setTranslateX(-6);
                break;
        }
        wallShape.setFill(Color.BLACK);
        grid.getChildren().add(wallShape);
    }

    /**
     * Creates grid lines in the grid pane.
     */
    private void createGridLines() {
        grid.getChildren().clear();
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var col = 0; col < grid.getColumnCount(); col++) {
                var gridLines = createPane();
                grid.add(gridLines, col, row);
            }
        }
    }

    /**
     * Creates a stack pane for grid lines.
     *
     * @return the created stack pane
     */
    private StackPane createPane() {
        var pane = new StackPane();
        pane.getStyleClass().add("pane");
        Rectangle border = createRectangle();
        pane.getChildren().add(border);
        return pane;
    }

    /**
     * Creates a rectangle for grid lines.
     *
     * @return the created rectangle
     */
    private Rectangle createRectangle() {
        Rectangle border = new Rectangle(99, 99);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        return border;
    }

    /**
     * Handles the event when the puzzle is solved.
     *
     * @param observableValue the observable value
     * @param oldValue        the old value
     * @param newValue        the new value
     */
    private void handleSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            duration = Duration.between(startTime, Instant.now());
            Platform.runLater(this::showSolvedAlert);
        }
    }

    /**
     * Shows an alert when the puzzle is solved.
     */
    private void showSolvedAlert() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        stopwatch.stop();
        alert.setHeaderText("Game Over");
        alert.setContentText("Congratulations, you have solved the maze!");
        alert.showAndWait();
        addJson();
        restartGame();
    }

    /**
     * Adds the game result to the JSON file.
     */
    private void addJson() {
        try {
            var resultManager = new JsonResultManager(Paths.get("gameresult.json"));
            var result = new GameResult(playerName, numberOfMoves.get(), duration);
            resultManager.add(result);
        } catch (IOException e) {
            Logger.error("Failed to save game result: {}", e.getMessage());
        }
    }

    /**
     * Registers the key event handler for the grid.
     */
    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    /**
     * Handles key press events for game controls.
     *
     * @param keyEvent the key event
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (restartKeyCombination.match(keyEvent)) {
            Logger.debug("Restarting game");
            restartGame();
        } else if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        } else if (keyEvent.getCode() == KeyCode.UP) {
            Logger.debug("UP pressed");
            makeMoveIfLegal(Direction.UP);
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            Logger.debug("RIGHT pressed");
            makeMoveIfLegal(Direction.RIGHT);
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            Logger.debug("DOWN pressed");
            makeMoveIfLegal(Direction.DOWN);
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            Logger.debug("LEFT pressed");
            makeMoveIfLegal(Direction.LEFT);
        } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            Logger.debug("BACKSPACE pressed");
            try {
                ActionEvent event = new ActionEvent(grid, grid.getScene().getWindow());
                switchScene(event);
            } catch (IOException e) {
                Logger.error("Failed to load the next scene: {}", e.getMessage());
            }
        }
    }

    private void startDurationAndStopWatchIfNotRunning() {
        if (!stopwatch.isRunning()) {
            startTime = Instant.now();
            stopwatch.start();
        }
    }

    /**
     * Makes a move in the specified direction if it is legal.
     *
     * @param direction the direction to move
     */
    private void makeMoveIfLegal(Direction direction) {
        if (state.isLegalMove(direction)) {
            startDurationAndStopWatchIfNotRunning();
            Logger.info("Moving {}", direction);
            state.makeMove(direction);
            Logger.trace("New state after move: {}", state);
            updateBallPosition();
            numberOfMoves.set(numberOfMoves.get() + 1);
        } else {
            Logger.warn("Illegal move: {}", direction);
        }
    }

    /**
     * Binds the number of moves to the text field.
     */
    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    /**
     * Restarts the game when the restart button is pressed.
     */
    @FXML
    public void onRestart() {
        Logger.debug("Restarting game");
        restartGame();
    }

    /**
     * Exits the game when the quit button is pressed.
     */
    @FXML
    public void onQuit() {
        Logger.debug("Exiting");
        Platform.exit();
    }
}
