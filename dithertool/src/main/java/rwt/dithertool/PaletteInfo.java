package rwt.dithertool;

import javafx.scene.paint.Color;

/**
 * Palette info records an array of colors, as well
 * as the name of the palette, suitable for display to a 
 * user.  I don't bother with encapsulation here. 
 * @author Richard Todd
 */
record PaletteInfo(String name, Color[] colors) {   
    @Override public String toString() { return name; }
}
