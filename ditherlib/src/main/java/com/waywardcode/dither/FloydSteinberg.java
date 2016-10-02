/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package com.waywardcode.dither;

import com.waywardcode.dither.colors.ColorSelector;

/**
 * The Floyd-Steinberg Error-diffusion algorithm
 * @author Richard Todd
 */
public class FloydSteinberg extends ErrDiffusionDither {
    
    public FloydSteinberg(ColorSelector cs) {
        super(cs, 
              new double[][] { { 7 }, { 3, 5, 1 } }, 
              16, 
              1);
    }
    
    @Override public String toString() { return "Floyd Steinberg Dither"; }        
}
