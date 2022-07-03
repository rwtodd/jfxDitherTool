package rwt.dithertool;

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
import org.rwtodd.dither.colors.*;
import org.rwtodd.dither.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author richa
 */
public class MainWinController implements Initializable {
	// define the nodes from the fxml we need to access...
	@FXML
	private Label fnameLabel;
	@FXML
	private Label dimensionsLabel;
	@FXML
	private Label paletteLabel;
	@FXML
	private Label ditherLabel;
	@FXML
	private ImageView srcImage;
	@FXML
	private ImageView ditheredImage;

	// define the properties we care about ...
	private ObjectProperty<Dimension2D> scaledDim;
	private ObjectProperty<File> srcFile;
	private ObjectProperty<Image> origImage;
	private Dimension2D origDimensions;
	private ObjectProperty<PaletteInfo> palette;
	private ObjectProperty<DitherParms> ditherParms;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		origImage = new SimpleObjectProperty<>(null);
		origDimensions = null;
		scaledDim = new SimpleObjectProperty<>(new Dimension2D(320, 200));
		srcFile = new SimpleObjectProperty<>(null);
		palette = new SimpleObjectProperty<>(new PaletteInfo("Monochrome", new Color[] { Color.BLACK, Color.WHITE }));
		ditherParms = new SimpleObjectProperty<>(
				new DitherParms(DitherParms.Algorithm.NoDither, DitherParms.Metric.Euclidean));

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
				return (f == null) ? "No File" : f.getName();
			}
		});

		// bind the palette selected to the palette label
		paletteLabel.textProperty().bind(new StringBinding() {
			{
				bind(palette);
			}

			@Override
			protected String computeValue() {
				return palette.get().name();
			}

		});

		// bind the current ditherparms to the UI label
		ditherLabel.textProperty().bind(new StringBinding() {
			{
				bind(ditherParms);
			}

			@Override
			protected String computeValue() {
				return ditherParms.get().algorithm().toString();
			}

		});

		// bind the original image to the file...
		origImage.bind(new ObjectBinding<Image>() {
			{
				bind(srcFile);
			}

			@Override
			protected Image computeValue() {
				var f = srcFile.get();
				if (f == null)
					return null;
				String url;
				try {
					url = f.toURI().toURL().toExternalForm();
				} catch (MalformedURLException ex) {
					System.err.println(ex);
					url = f.toString();
				}
				Image im = new Image(url);
				origDimensions = new Dimension2D(im.getWidth(),im.getHeight()); 
				scaledDim.set(origDimensions);
				return im;
			}
		});
		
		// bind the source image to the dimensions and file...
		srcImage.imageProperty().bind(new ObjectBinding<Image>() {
			{
				bind(origImage, scaledDim);
			}

			@Override
			protected Image computeValue() {
				var orig = origImage.get();
				Dimension2D dim = scaledDim.get();
				if (orig == null || dim == null)
					return null;
				ImageView iv = new ImageView(orig);
				iv.setPreserveRatio(true);
				iv.setFitHeight(dim.getHeight());
				iv.setFitWidth(dim.getWidth());
				iv.setSmooth(true);
				return iv.snapshot(null, null);
			}
		});

		// bind the target image to the src image and the ditherer...
		ditheredImage.imageProperty().bind(new ObjectBinding<Image>() {
			{
				bind(srcImage.imageProperty(), palette, ditherParms);
			}

			@Override
			protected Image computeValue() {
				Image src = srcImage.imageProperty().get();
				DitherParms dp = ditherParms.get();
				PaletteInfo pi = palette.get();
				if (src == null || dp == null || pi == null) {
					return null;
				}

				// maybe make a factory later, but for now just switches...
				ColorMetric cm;
				switch (dp.metric()) {
				case RGBLumin:
					cm = new RGBLumosityMetric();
					break;
				default:
					cm = new NaiveMetric();
					break;
				}
				ColorSelector selector = ColorSelectionFactory.getInstance(pi.colors(), cm);

				Ditherer dither;
				switch (dp.algorithm()) {
				case Atkinson:
					dither = new Atkinson(selector);
					break;
				case FloydSteinberg:
					dither = new FloydSteinberg(selector);
					break;
				case Jarvis:
					dither = new JarvisJudiceNinke(selector);
					break;
				case Stucki:
					dither = new Stucki(selector);
					break;
				case Sierra3:
					dither = new Sierra3(selector);
					break;
				case Sierra24A:
					dither = new Sierra24A(selector);
					break;
				default:
					dither = new NaiveDither(selector);
				}
				return dither.dither(src);
			}
		});
	}

	@FXML
	private void getSrcFile(ActionEvent ae) {
		FileChooser openf = new javafx.stage.FileChooser();
		openf.setTitle("Select Source File");
		File selected = openf.showOpenDialog(fnameLabel.getScene().getWindow());
		if (selected != null) {
			srcFile.set(selected);
		}
	}

	@FXML
	private void getNewDims(ActionEvent ae) {
		// create a child window...
		try {
			Stage dims = new Stage(StageStyle.DECORATED);
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("/rwt/fxml/DimensionsChooser.fxml"));
			Parent root = ldr.load();
			DimensionsChooserController dcc = ldr.getController();
			dcc.tieToParent(dims, scaledDim, origDimensions);
			Scene sc = new Scene(root);
			dims.setScene(sc);
			dims.setTitle("Dimensions Chooser");
			dims.sizeToScene();
			dims.show();
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	private void getNewPalette(ActionEvent ae) {
		// create a child window...
		try {
			Stage pals = new Stage(StageStyle.DECORATED);
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("/rwt/fxml/PaletteChooser.fxml"));
			Parent root = ldr.load();
			PaletteChooserController pcc = ldr.getController();
			pcc.tieToParent(pals, palette, srcImage.imageProperty());
			Scene sc = new Scene(root);
			pals.setScene(sc);
			pals.setTitle("Palette Chooser");
			pals.sizeToScene();
			pals.show();
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	private void getNewDither(ActionEvent ae) {
		// create a child window...
		try {
			Stage dipm = new Stage(StageStyle.DECORATED);
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("/rwt/fxml/DitherParmsChooser.fxml"));
			Parent root = ldr.load();
			DitherParmsChooserController dpcc = ldr.getController();
			dpcc.tieToParent(dipm, ditherParms);
			Scene sc = new Scene(root);
			dipm.setScene(sc);
			dipm.setTitle("Dither Chooser");
			dipm.sizeToScene();
			dipm.show();
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	private void onSave(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save GIF File");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GIF Files", "*.GIF"),
				new FileChooser.ExtensionFilter("PNG Files", "*.PNG"));
		fileChooser.setInitialFileName(srcFile.get().getName() + "_dithered.gif");
		File file = fileChooser.showSaveDialog(fnameLabel.getScene().getWindow());
		if (file == null)
			return;

		try {
			Image im = ditheredImage.getImage(); // .snapshot(null,null);
			java.awt.image.BufferedImage bim = javafx.embed.swing.SwingFXUtils.fromFXImage(im, null);
			String format = file.getName().toLowerCase().endsWith(".png") ? "png" : "gif";
			javax.imageio.ImageIO.write(bim, format, file);
		} catch (java.io.IOException e) {
			System.err.println(e.toString());
		}
	}

}
