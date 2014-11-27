package com.xenoage.zong.demos.simpledemo;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import java.awt.image.BufferedImage;
import java.io.File;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreDocFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.desktop.renderer.AwtBitmapPageRenderer;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;

/**
 * Controller for the main window (MainWindow.fxml).
 * 
 * @author Andreas Wenger
 */
public class MainWindow {

	//GUI elements
	@FXML private MenuItem mnuPlaybackStop;
	@FXML private MenuItem mnuPlaybackStart;
	@FXML private BorderPane pnlCanvas;
	@FXML private ImageView scoreView;

	//loaded document
	private int scoreIndex = 0;
	private ScoreDoc scoreDoc = null;
	private WritableImage scoreImage = null;
	
	@FXML public void initialize() {
		//load demo score
		loadScore("scores/BeetAnGeSample.xml");
	}

	@FXML void onFileOpen(ActionEvent event) {
		//open random MusicXML file
		File[] files = new File("scores").listFiles((d, n) -> n.endsWith(".xml"));
		scoreIndex = (scoreIndex + 1) % files.length;
		loadScore("scores/" + files[scoreIndex].getName());
	}

	@FXML void onFileSaveAsPDF(ActionEvent event) {
		saveAs(new PdfScoreDocFileOutput(), "pdf");
	}
	
	@FXML void onFileSaveAsPNG(ActionEvent event) {
		PngScoreDocFileOutput out = new PngScoreDocFileOutput();
		out.setJustOnePage(true);
		saveAs(out, "png");
	}
	
	@FXML void onFileSaveAsOGG(ActionEvent event) {
		saveAs(new OggScoreDocFileOutput(), "ogg");
	}
	
	private void saveAs(FileOutput<ScoreDoc> out, String extension) {
		String filePath = "demo." + extension;
		try {
			out.write(scoreDoc, new JseOutputStream(new File(filePath)), filePath);
			showMessageDialog(filePath + " saved.");
		} catch (Exception ex) {
			Err.handle(Report.error(ex));
		}
	}

	@FXML void onExit(ActionEvent event) {

	}

	@FXML void onPlaybackStart(ActionEvent event) {

	}

	@FXML void onPlaybackStop(ActionEvent event) {

	}
	
	@FXML void onAbout(ActionEvent event) {

	}
	
	private void loadScore(String filePath) {
		try {
			//load the score
			JseInputStream inStream = JsePlatformUtils.io().openFile(filePath);
			MusicXmlScoreDocFileInput scoreDocInput = new MusicXmlScoreDocFileInput();
			scoreDoc = scoreDocInput.read(inStream, filePath);
			//layout the first page
			Layout layout = scoreDoc.getLayout();
			layout.updateScoreLayouts(scoreDoc.getScore());
			//we have still no JavaFX renderer in Zong!, so we have to use
			//the Java2D/AWT renderer at the moment
			BufferedImage awtImage = AwtBitmapPageRenderer.paint(layout, 0, 2f);
			scoreImage = SwingFXUtils.toFXImage(awtImage, scoreImage);
			//set image to view
			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		}
		catch (Exception ex) {
			Err.handle(Report.error(ex));
		}
	}
	
	private void showMessageDialog(String message) {
		dialog().message(message).showInformation();
	}
	
	private Dialogs dialog() {
		return Dialogs.create().title(SimpleDemo.appName).styleClass(Dialog.STYLE_CLASS_NATIVE);
	}

}
