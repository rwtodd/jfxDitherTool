/*
 * Copyright Richard Todd. I put the code under the
 * GPL v2.0.  See the LICENSE file in the repository.
 * for more information.
 */
package rwt.dithertool;

import com.waywardcode.dither.Ditherer;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.*;
import javafx.geometry.Dimension2D;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import com.waywardcode.dither.colors.*;
import com.waywardcode.dither.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author richa
 */
public class MainWinController implements Initializable {
    // define the nodes from the fxml we need to access...
    @FXML private Label fnameLabel;
    @FXML private Label dimensionsLabel;
    @FXML private Label paletteLabel;
    @FXML private ImageView srcImage;
    @FXML private ImageView ditheredImage;
    
    // define the properties we care about ...
    private ObjectProperty<Dimension2D> scaledDim;
    private ObjectProperty<File> srcFile;
    private ObjectProperty<Ditherer> ditherer;
    private ObjectProperty<PaletteInfo> palette;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scaledDim = new SimpleObjectProperty<>(new Dimension2D(320, 200));
        srcFile = new SimpleObjectProperty<>(null);
        palette = new SimpleObjectProperty<>(new PaletteInfo(StandardPalette.EGA));        
        ditherer = new SimpleObjectProperty<>();
        
        // bind the ditherer to the palette....
        ditherer.bind(new ObjectBinding<Ditherer>() {
            { bind(palette); }
            
            @Override
            protected Ditherer computeValue() {
                ColorSelector selector = ColorSelectionFactory.getInstance(palette.get().colors, new NaiveMetric());
                return (new NaiveDither(selector));
            }
            
        });
        
        
        // bind dimensions to the UI label
        dimensionsLabel.textProperty().bind(new StringBinding() {
            private final Dim2DStringer stringer;
            
            {
                bind(scaledDim);
                stringer = new Dim2DStringer();
            }

            @Override
            protected String computeValue() {
                Dimension2D sd = scaledDim.get();
                return stringer.toString(sd);
            }
        });
       
        // bind the file selected to the UI label
        fnameLabel.textProperty().bind(new StringBinding() {
            {
                bind(srcFile);
            }

            @Override
            protected String computeValue() {
                File f = srcFile.get();
                return (f == null)?"No File":f.getName();
            }
        });
        
        // bind the palette selected to the palette label        
        paletteLabel.textProperty().bind(new StringBinding() { 
                { bind(palette); }

            @Override
            protected String computeValue() {
                return palette.get().name;
            }
                
        });
        
        //bind the source image to the dimensions and file...
        srcImage.imageProperty().bind(new ObjectBinding<Image>() { 
            {
                bind(srcFile, scaledDim);
            }

            @Override
            protected Image computeValue() {
                File f = srcFile.get();
                Dimension2D dim = scaledDim.get();
                if(f == null) return null;
                String url;
                try {
                    url = f.toURI().toURL().toExternalForm();
                } catch (MalformedURLException ex) {
                    System.err.println(ex);
                    url = f.toString();
                }
                
                Image im =  new Image(url, 
                                      dim.getWidth(), 
                                      dim.getHeight(), 
                                      true, 
                                      true);
                
                return im;
            }
        });
        
        // bind the target image to the src image and the ditherer...
        ditheredImage.imageProperty().bind(new ObjectBinding<Image>() { 
            {
                bind(srcImage.imageProperty(), ditherer);
            }

            @Override
            protected Image computeValue() {
                Image src = srcImage.imageProperty().get();
                Ditherer d = ditherer.get();
                if(src == null || d == null) {
                    return null;
                }
                return d.dither(src);
            }
        });
    }
    
    @FXML private void getSrcFile(ActionEvent ae) {
        FileChooser openf = new javafx.stage.FileChooser(); 
        openf.setTitle("Select Source File");
        File selected = openf.showOpenDialog(fnameLabel.getScene().getWindow());
        if(selected != null) {
            srcFile.set(selected);
        }
    }

    @FXML private void getNewDims(ActionEvent ae) {
        // create a child window...
        try {
          Stage dims = new Stage(StageStyle.DECORATED);
          FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxml/DimensionsChooser.fxml"));
          Parent root = ldr.load();
          DimensionsChooserController dcc = ldr.getController();
          dcc.tieToParent(dims, scaledDim);
          Scene sc = new Scene(root);
          dims.setScene(sc);
          dims.setTitle("Dimensions Chooser");
          dims.sizeToScene();
          dims.show();
        } catch(java.io.IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML private void getNewPalette(ActionEvent ae) {
        // create a child window...
        try {
          Stage pals = new Stage(StageStyle.DECORATED);
          FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxml/PaletteChooser.fxml"));
          Parent root = ldr.load();
          PaletteChooserController pcc = ldr.getController();
          pcc.tieToParent(pals, palette);
          Scene sc = new Scene(root);
          pals.setScene(sc);
          pals.setTitle("Palette Chooser");
          pals.sizeToScene();
          pals.show();
        } catch(java.io.IOException ex) {
            ex.printStackTrace();
        }
    }
}
