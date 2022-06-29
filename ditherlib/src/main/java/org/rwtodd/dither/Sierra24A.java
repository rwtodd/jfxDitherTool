package org.rwtodd.dither;

import org.rwtodd.dither.colors.ColorSelector;

/**
 *
 * @author richa
 */
public class Sierra24A extends ErrDiffusionDither {

    public Sierra24A(ColorSelector cs) {
        super(cs, 
              new double[][] { { 2 }, { 1, 1 } }, 
              4, 
              1);
    }
    
    @Override public String toString() { return "Sierra Lite 2-4A"; }        
}
