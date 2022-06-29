package rwt.dithertool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.scene.control.ComboBox;
import javafx.fxml.*;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author richa
 */
public class DimensionsChooserController implements Initializable {
    
    private ObjectProperty<Dimension2D> fromParent;    
    private Stage myStage;
    
    @FXML private ComboBox<Dimension2D> typicalList;
    @FXML private TextField customWidth, customHeight;
    @FXML private HBox customOptions;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Dim2DStringer dimensionStringer = new Dim2DStringer();
        
        typicalList.setCellFactory(new Callback<ListView<Dimension2D>, ListCell<Dimension2D>>() {
            @Override
            public ListCell<Dimension2D> call(ListView<Dimension2D> param) {
                return new ListCell<Dimension2D>() {
                    @Override
                    protected void updateItem(Dimension2D item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(dimensionStringer.toString(item));
                    }
                };
            }
        });
        typicalList.setConverter(dimensionStringer);

        typicalList.valueProperty().addListener((xx, ov, nv) -> {
            boolean custom = nv.getHeight() < 1.0;
            customOptions.setVisible(custom);
            customOptions.setManaged(custom);
            if(myStage != null) myStage.sizeToScene();
        });
    }    
    
    public void tieToParent(Stage whereIAm, ObjectProperty<Dimension2D> tgt) {
        myStage = whereIAm;
        fromParent = tgt;
        typicalList.setValue(tgt.get());
    }
    
    @FXML private void btnApply(ActionEvent ae) {
        Dimension2D answer;
        if(customOptions.isVisible()) {
            answer = new Dimension2D(Double.valueOf(customWidth.getText()), 
                                     Double.valueOf(customHeight.getText()));
        } else {
            answer = typicalList.getValue();
        }
        fromParent.set(answer);
    }
    
    @FXML private void btnClose(ActionEvent ae) {
        btnApply(ae);
        myStage.close();
    }
}
