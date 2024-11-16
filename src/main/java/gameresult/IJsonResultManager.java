package gameresult;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Interface for managing game results stored in a JSON file.
 */
public interface IJsonResultManager {

    /**
     * Adds a new game result to the JSON file.
     *
     * @param result the game result to add
     * @return the updated list of game results
     * @throws IOException if an I/O error occurs
     */
    List<GameResult> add(GameResult result) throws IOException;

    /**
     * Retrieves all game results from the JSON file.
     *
     * @return the list of game results
     * @throws IOException if an I/O error occurs
     */
    List<GameResult> getAll() throws IOException;

    /**
     * Retrieves the best game results, sorted by the number of moves in ascending order.
     *
     * @param limit the maximum number of results to retrieve
     * @return the list of the best game results
     * @throws IOException if an I/O error occurs
     */
    default List<GameResult> getBest(int limit) throws IOException {
        return getAll()
                .stream()
                .sorted(Comparator.comparing(GameResult::getDuration))
                .limit(limit)
                .toList();
    }
}
