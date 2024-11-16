package util;

import java.time.Duration;

/**
 * Utility class for formatting durations.
 */
public class DurationUtil {

    /**
     * Formats a given duration to a string representation with minutes, seconds, and milliseconds.
     *
     * @param duration the duration to format
     * @return the formatted duration string
     */
    public static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%03d",
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart());
    }
}
