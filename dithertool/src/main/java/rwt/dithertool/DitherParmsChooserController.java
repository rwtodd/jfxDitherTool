/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard Todd
 */
public class DitherParmsChooserController implements Initializable {
    @FXML private ComboBox<DitherParms.Algorithm> algoList;
    @FXML private ComboBox<DitherParms.Metric> metricList;
    
    private Stage myStage;
    private ObjectProperty<DitherParms> fromParent;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        algoList.getItems().addAll(DitherParms.Algorithm.values());
        metricList.getItems().addAll(DitherParms.Metric.values());
    }    
    
    public void tieToParent(Stage whereIAm, ObjectProperty<DitherParms> tgt) {
        myStage = whereIAm;
        fromParent = tgt;
        algoList.setValue(tgt.get().selectedAlgo);
        metricList.setValue(tgt.get().selectedMetric);
    }
    
    @FXML private void btnApply(ActionEvent ae) {
        fromParent.set( new DitherParms(algoList.getValue(), metricList.getValue()) );
    }
    
    @FXML private void btnClose(ActionEvent ae) {
        btnApply(ae);
        myStage.close();
    }

    
}
