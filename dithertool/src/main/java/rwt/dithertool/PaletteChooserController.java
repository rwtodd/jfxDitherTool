/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import com.waywardcode.palette.PaletteDesigner;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author richard Todd
 */
 public class PaletteChooserController implements Initializable {

    @FXML private PaletteDesigner designer;
    
    private Stage myStage;
    private ObjectProperty<PaletteInfo> fromParent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing to do at the moment.
    }        
    
    public void tieToParent(Stage whereIAm, ObjectProperty<PaletteInfo> tgt, Image src) {
        myStage = whereIAm;
        fromParent = tgt;
        designer.setPalette(Optional.of(tgt.get().toString()), tgt.get().colors);
        designer.setQuantizationImage(src);
    }
    
    @FXML private void btnApply(ActionEvent ae) {
        final PaletteInfo pi = new PaletteInfo(designer.getPaletteName(), designer.getPaletteColors());
        fromParent.set(pi);
    }
    
    @FXML private void btnClose(ActionEvent ae) {
        btnApply(ae);
        myStage.close();
    }

}
