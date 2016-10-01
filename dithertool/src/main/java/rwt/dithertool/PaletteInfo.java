/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import javafx.scene.paint.Color;

/**
 * Palette info records an array of colors, as well
 * as the name of the palette, suitable for display to a 
 * user.  I don't bother with encapsulation here. 
 * @author Richard Todd
 */
class PaletteInfo {
    public final String name;
    public final Color[] colors;
   
    @Override public String toString() { return name; }
    
    public PaletteInfo(com.waywardcode.dither.colors.StandardPalette sp) {
        name = sp.toString();
        colors = sp.getColors();
    }
   
    public PaletteInfo(String n, Color[] c) { colors = c; name = n; }
}
