package rwt.dithertool;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author richa
 */
public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent p = FXMLLoader.load(getClass().getResource("/rwt/fxml/MainWin.fxml"));
        Scene sc = new Scene(p);
        stage.setScene(sc);
        stage.setTitle("DitherTool JFX");
        stage.show();
    }
    
}
