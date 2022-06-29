package org.rwtodd.dither.colors;
import javafx.scene.paint.Color;

/**
 * ColorSelectors pick a color from a palette that matches
 * a given color most closely by some criteria.
 * @author Richard Todd
 */
public interface ColorSelector {
    Color nearestTo(Color input);
    ColorMetric getMetric();
}
