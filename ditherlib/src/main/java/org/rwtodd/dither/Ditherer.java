package org.rwtodd.dither;

import javafx.scene.image.*;

/**
 * As there are many dithering algorithms, the Ditherer interface
 * provides a common way to speak to them.
 * @author Richard Todd
 */
public interface Ditherer {
      Image dither(Image input);      
}
