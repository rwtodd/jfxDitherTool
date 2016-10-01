/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package com.waywardcode.dither;

import javafx.scene.image.*;

/**
 * As there are many dithering algorithms, the Ditherer interface
 * provides a common way to speak to them.
 * @author Richard Todd
 */
public interface Ditherer {
      Image dither(Image input);      
}
