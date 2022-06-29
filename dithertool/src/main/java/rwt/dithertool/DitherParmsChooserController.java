package rwt.dithertool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard Todd
 */
public class DitherParmsChooserController {
    @FXML private ComboBox<DitherParms.Algorithm> algoList;
    @FXML private ComboBox<DitherParms.Metric> metricList;
    
    private Stage myStage;
    private ObjectProperty<DitherParms> fromParent;
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        algoList.getItems().addAll(DitherParms.Algorithm.values());
        metricList.getItems().addAll(DitherParms.Metric.values());
    }    
    
    public void tieToParent(Stage whereIAm, ObjectProperty<DitherParms> tgt) {
        myStage = whereIAm;
        fromParent = tgt;
        algoList.setValue(tgt.get().algorithm());
        metricList.setValue(tgt.get().metric());
    }
    
    @FXML private void btnApply(ActionEvent ae) {
        fromParent.set( new DitherParms(algoList.getValue(), metricList.getValue()) );
    }
    
    @FXML private void btnClose(ActionEvent ae) {
        btnApply(ae);
        myStage.close();
    }

    
}
