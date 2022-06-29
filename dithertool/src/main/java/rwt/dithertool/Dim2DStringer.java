package rwt.dithertool;

import javafx.geometry.Dimension2D;
import javafx.util.StringConverter;

// package-local class that helps this window display dimensions better...
class Dim2DStringer extends StringConverter<Dimension2D> {
    @Override
    public String toString(Dimension2D object) {
        if(object == null) {
            return "";
        }
        
        if(object.getHeight() < 1.0) {
            return "Use Custom Values";
        }
        
        return String.format("%d x %d", (int)object.getWidth(), (int)object.getHeight());
    }

    @Override
    public Dimension2D fromString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
