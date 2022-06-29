module rwt.dithertool {
   requires java.desktop;
   requires javafx.fxml;
   requires javafx.controls;
   requires javafx.swing;
   requires transitive javafx.graphics;

   requires org.rwtodd.dither;
   requires org.rwtodd.paldesign;
   opens rwt.dithertool to javafx.fxml;
   exports rwt.dithertool to javafx.graphics;
}
