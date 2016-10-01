/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package com.waywardcode.dither.colors;
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
