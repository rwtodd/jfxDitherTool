/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import com.waywardcode.dither.colors.StandardPalette;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.fxml.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author richa
 */
public class PaletteChooserController implements Initializable {
    @FXML private ComboBox<PaletteInfo> typicalList;
    @FXML private Pane colorDisplay;
        
    private Stage myStage;
    private ObjectProperty<PaletteInfo> fromParent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for(StandardPalette sp: StandardPalette.values()) {
            typicalList.getItems().add(new PaletteInfo(sp));
        }

        typicalList.valueProperty().addListener((xx,ov,nv) -> {
            colorDisplay.getChildren().clear();
            for(Color c: nv.colors) {
                colorDisplay.getChildren().add(new Rectangle(20,20,c));
                if(myStage != null) myStage.sizeToScene();
            }
        });
    }        
    
    public void tieToParent(Stage whereIAm, ObjectProperty<PaletteInfo> tgt) {
        myStage = whereIAm;
        fromParent = tgt;
        typicalList.setValue(tgt.get());
    }
    
    @FXML private void btnApply(ActionEvent ae) {
        // will have to expand this if the user is allowed to alter the
        // colors one day
        fromParent.set(typicalList.getValue());
    }
    
    @FXML private void btnClose(ActionEvent ae) {
        btnApply(ae);
        myStage.close();
    }

}
