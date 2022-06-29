package rwt.dithertool;

/**
 * A class to hold data about a selected dithering algorithm.  It will help the
 * UI figure out when to recalculate the image.
 * @author Richard Todd
 */
public record DitherParms(Algorithm algorithm, Metric metric) {
    public enum Algorithm {  
        NoDither("No Dithering"),
        Atkinson("Atkinson"),
        FloydSteinberg("Floyd-Steinberg"),
        Jarvis("Jarvis, Judice, Ninke"),
        Stucki("Stucki"),
        Sierra3("Sierra 3-Line"),
        Sierra24A("Sierra \"Lite\" 2-4A");
        
        private final String id;
        Algorithm(String idStr) {
            id = idStr;
        }
        
        @Override public String toString() { return id; }
    };
    
    public enum Metric {
        Euclidean("Euclidean Distance"),
        RGBLumin("RGB with Lumosity");
        
        private final String id;
        Metric(String idStr)  {
            id = idStr;
        }

        @Override public String toString() { return id; }        
    }    
}
