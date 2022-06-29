package org.rwtodd.dither;

import org.rwtodd.dither.colors.ColorSelector;

/**
 * The Jarvice, Judice, and Ninke Dithering algorithm.
 *    
 * @author Richard Todd
 */
public class JarvisJudiceNinke extends ErrDiffusionDither {

    public JarvisJudiceNinke(ColorSelector cs) {
        super(cs, 
              new double[][] { { 7, 5 }, { 3, 5, 7, 5, 3 }, { 1, 3, 5, 3, 1 } }, 
              48, 
              2);
    }
    
    @Override public String toString() { return "Jarvis Judice Ninke"; }        
    
}
