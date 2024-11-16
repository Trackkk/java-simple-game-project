package util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * A simple stopwatch utility for tracking time in a game.
 */
public class Stopwatch {

    private final LongProperty millis;
    private final StringProperty time;
    private final Timeline timeline;

    /**
     * Constructs a new Stopwatch.
     */
    public Stopwatch() {
        millis = new SimpleLongProperty();
        time = new SimpleStringProperty();
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> millis.set(millis.get() + 1)),
                new KeyFrame(Duration.millis(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        time.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(millis.get(), "mm:ss:SSS"), millis));
    }

    /**
     * Gets the time property of the stopwatch.
     *
     * @return the time property
     */
    public StringProperty timeProperty() {
        return time;
    }

    /**
     * Starts the stopwatch.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the stopwatch.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Resets the stopwatch to zero.
     */
    public void reset() {
        millis.set(0);
    }

    public boolean isRunning() {
        return timeline.getStatus() == Animation.Status.RUNNING;
    }

}
