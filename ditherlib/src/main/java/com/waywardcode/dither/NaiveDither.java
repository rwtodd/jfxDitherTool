/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package com.waywardcode.dither;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import com.waywardcode.dither.colors.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Performs no actual dithering... just replaces each 
 * pixel with the nearest match, ignoring any error 
 * accumulation that a "real" dither would do.
 * @author Richard Todd
 */
public final class NaiveDither implements Ditherer {

    private final ColorSelector selector;
    
    public NaiveDither(ColorSelector cs) {
        selector = cs;
    }
    
    @Override
    public Image dither(Image input) {
        // it's kind-of silly to have doubles for raster image width/height...
        final int width = (int) input.getWidth();
        final int height = (int) input.getHeight();
        
        final WritableImage output = new WritableImage(width,height);
        final PixelReader pxr = input.getPixelReader();
        final PixelWriter pxw = output.getPixelWriter();
        
        for(int y=0 ; y < height; y++ ) {
            for(int x = 0; x < width; x++ ) {
                 pxw.setColor(x, y, selector.nearestTo(pxr.getColor(x, y)));
            }
        }
        
        return output;
    }
    
}
