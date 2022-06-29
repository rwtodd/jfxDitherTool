package rwt.dithertool;

import java.util.Optional;

import org.rwtodd.paldesign.PaletteDesigner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author richard Todd
 */
 public class PaletteChooserController {

    @FXML private PaletteDesigner designer;
    
    private Stage myStage;
    private ObjectProperty<PaletteInfo> fromParent;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // nothing to do at the moment.
    }        
    
    public void tieToParent(final Stage whereIAm, ObjectProperty<PaletteInfo> tgt, ObjectProperty<Image> src) {
        myStage = whereIAm;
        fromParent = tgt; 
        designer.setPalette(Optional.of(tgt.get().toString()), tgt.get().colors());
        designer.setQuantizationImage(src);
        designer.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				whereIAm.sizeToScene();
			}	
		});
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
