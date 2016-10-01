/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import com.waywardcode.dither.colors.StandardPalette;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.fxml.*;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
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
        
        // bind the contents of the colordisplay to the selected list...
        Bindings.bindContent(colorDisplay.getChildren(),
                new ListBinding<Node>() {
                    { bind(typicalList.valueProperty()); }
                    
            @Override
            protected ObservableList<Node> computeValue() {
                PaletteInfo pi = typicalList.getValue();
                if(pi == null) return null;
                
                final ObservableList<Node> answer = FXCollections.observableArrayList();
                for(Color c: pi.colors) {
                    Rectangle box = new Rectangle(20, 20, c);
                    box.getStyleClass().add("colorChoice");
                    answer.add(box);
                }
                return answer;
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
