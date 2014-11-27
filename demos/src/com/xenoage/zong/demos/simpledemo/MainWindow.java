package com.xenoage.zong.demos.simpledemo;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
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
	@FXML private Canvas canvas;

	//loaded document
	private ScoreDoc scoreDoc = null;
	private WritableImage scoreImage = null;
	
	public void initialize() {
		//bind size of canvas to size of its parent pane
		//(canvas does not auto-resize in JavaFX 8)
		//canvas.widthProperty().bind(pnlCanvas.widthProperty());
		//canvas.heightProperty().bind(pnlCanvas.heightProperty());
		//load demo score
		loadScore("scores/BeetAnGeSample.xml");
	}

	@FXML void onFileOpen(ActionEvent event) {
		repaint();
	}

	@FXML void onFileSaveAs(ActionEvent event) {

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
			BufferedImage awtImage = AwtBitmapPageRenderer.paint(layout, 0, 1f);
			SwingFXUtils.toFXImage(awtImage, scoreImage);
			//repaint canvas
			repaint();
		}
		catch (Exception ex) {
			Err.handle(Report.error(ex));
		}
	}
	
	private void repaint() {
		if (scoreImage != null) {
			GraphicsContext graphics = canvas.getGraphicsContext2D();
			graphics.drawImage(scoreImage, 0, 0);
		}
	}

}
