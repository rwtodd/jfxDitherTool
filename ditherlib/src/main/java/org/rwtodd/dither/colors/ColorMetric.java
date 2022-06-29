package org.rwtodd.dither.colors;
import javafx.scene.paint.Color;

/**
 * There's more than one way to decide which colors
 * are closest to each other.  This interface represents
 * a metric from (Color,Color) to distance.  Ideally,
 * any implementing classes should make sure that the result
 * is an actual metric space in the mathematical sense 
 * (triangle inequality, etc).
 * @author Richard Todd
 */
@FunctionalInterface
public interface ColorMetric {
    /** 
     * Compute the distance between two Colors by some method.
     * @param a The first Color
     * @param b The second Color
     * @return The distance between a and b, as a double.
     */
     double distance(Color a, Color b);
}
