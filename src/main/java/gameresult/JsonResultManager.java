package gameresult;

import lombok.NonNull;
import util.JacksonHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game results stored in a JSON file.
 */
public class JsonResultManager implements IJsonResultManager {

    private final Path filePath;

    /**
     * Constructs a new JsonResultManager with the specified file path.
     *
     * @param filePath the path to the JSON file
     */
    public JsonResultManager(@NonNull Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Adds a new game result to the JSON file.
     *
     * @param result the game result to add
     * @return the updated list of game results
     * @throws IOException if an I/O error occurs
     */
    @Override
    public List<GameResult> add(@NonNull GameResult result) throws IOException {
        var results = getAll();
        results.add(result);
        try (var out = Files.newOutputStream(filePath)) {
            JacksonHelper.writeList(out, results);
        }
        return results;
    }

    /**
     * Retrieves all game results from the JSON file.
     *
     * @return the list of game results
     * @throws IOException if an I/O error occurs
     */
    public List<GameResult> getAll() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (var in = Files.newInputStream(filePath)) {
            return JacksonHelper.readList(in, GameResult.class);
        }
    }
}
