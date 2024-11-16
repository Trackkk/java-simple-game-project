package gameresult;

import lombok.*;
import java.time.Duration;

/**
 * Represents a game result with player name, number of moves, and duration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
    @NonNull private String playerName;
    private int numberOfMoves;
    @NonNull private Duration duration;
}