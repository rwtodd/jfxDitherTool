package org.rwtodd.dither;

import org.rwtodd.dither.colors.ColorSelector;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A class to do the work of a typical error diffusion ditherer...
 * @author richa
 */
public class ErrDiffusionDither implements Ditherer {
    private final  ColorSelector selector;
    private final  double[][] matrix;
    private final  double denominator;
    private final  int xoffs;  // how many pixels does the matrix reach back?

    public ErrDiffusionDither(final ColorSelector cs, final double[][] m, final double den, final int xo) {
        selector = cs;
        matrix = m;
        denominator = den;
        xoffs = xo;
    }
    
    /**
     * clip a value so it stays in the 0.0 to 1.0 range
     * @param in input value to be clipped
     * @return the clipped value
     */
    private double clip(double in) {
        if(in < 0.0) in = 0.0;
        else if (in > 1.0) in = 1.0;
        return in;
    }


    // rotate the error matrices to keep from
    // allocating new ones.
    private void rotateErrors(double[][] m) {
        // store off old current row and zero it out
        double[] temp = m[0];
        Arrays.fill(temp, 0.0);
        
        // rotate the other rows up
        for (int i = 0; i < m.length-1; i++) {
            m[i] = m[i+1];
        }
        
        // save the newly-zeroed row to the end to start filling
        m[m.length-1] = temp;
    }    
    
    @Override
    public Image dither(Image input) {
        // it's kind-of silly to have doubles for raster image width/height...
        final int width = (int) input.getWidth();
        final int height = (int) input.getHeight();
        
        double[][] error = new double[matrix.length][width*3];
                
        final WritableImage output = new WritableImage(width,height);
        final PixelReader pxr = input.getPixelReader();
        final PixelWriter pxw = output.getPixelWriter();
        
        for(int y=0 ; y < height; y++ ) {
            for(int x = 0; x < width; x++ ) {
                final int x3 = x*3;
                final Color orig = pxr.getColor(x, y);
                final Color adapted = new Color(clip(orig.getRed()+error[0][x3]),
                                                clip(orig.getGreen()+error[0][x3+1]),
                                                clip(orig.getBlue()+error[0][x3+2]),
                                                1.0);
                final Color nearest = selector.nearestTo(adapted);
                pxw.setColor(x, y, nearest);
                
                // calculate the error
                double rdiff = adapted.getRed() - nearest.getRed();
                double gdiff = adapted.getGreen() - nearest.getGreen();
                double bdiff = adapted.getBlue() - nearest.getBlue();
                
                // propagate the error
                
                // First row...
                for (int ex = x3+3, mIdx = 0; 
                    (mIdx < matrix[0].length) && (ex < error[0].length); 
                    ex+=3, mIdx++) {
                    error[0][ex]   += rdiff * matrix[0][mIdx] / denominator;
                    error[0][ex+1] += gdiff * matrix[0][mIdx] / denominator;
                    error[0][ex+2] += bdiff * matrix[0][mIdx] / denominator;
                }
                
                // Remaining rows ... calculate initial mIdx...
                // x = 0    xoffs = 2   so  initm = 2, initx = 0
                // x = 1    xoffs = 2   so  initm = 1, initx = 0
                // x = 2    xoffs = 2   so  initm = 0  initx = 0
                // x = 3    xoffs = 2   so  initm = 0  initx3 = 3
                final int initMatrixIndex = Math.max(0, xoffs - x);
                final int initX3 = Math.max(0, x - xoffs) * 3;
                for (int row = 1; row < matrix.length; row++) {
                    for (int ex = initX3, mIdx = initMatrixIndex; 
                          (mIdx < matrix[row].length) && (ex < error[row].length) ; 
                            ex+=3, mIdx++) {
                        error[row][ex]   += rdiff * matrix[row][mIdx] / denominator;
                        error[row][ex+1] += gdiff * matrix[row][mIdx] / denominator;
                        error[row][ex+2] += bdiff * matrix[row][mIdx] / denominator;                        
                    }
                }

            }
            
            rotateErrors(error);
        }
        
        return output;
    }
    
    @Override public String toString() { return "Error Diffusion Dither"; }
}
