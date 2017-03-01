package com.xenoage.zong.demos.simplegui;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.renderer.awt.AwtLayoutRenderer;
import com.xenoage.zong.renderer.javafx.JfxLayoutRenderer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.awt.image.BufferedImage;


/**
 * Controller for the JavaFX main window (MainWindow.fxml).
 * 
 * @author Andreas Wenger
 */
public class MainWindow {
	
	//GUI elements
	@FXML private MenuItem mnuPlaybackStop;
	@FXML private MenuItem mnuPlaybackStart;
	@FXML private BorderPane pnlCanvas;
	@FXML private ImageView scoreView;

	//loaded content
	private Content content = new Content(this);
	private WritableImage scoreImage = null;
	
	//rendering engine
	private boolean useJavaFX = true;
	
	//zoom
	@Getter private float zoom = 2;
	private final float zoomFactor = 2;
	
	
	@FXML public void initialize() {
		if (SimpleGuiDemo.startDoc == null) {
			//load the first demo score
			content.loadNextScore();
		}
		else {
			//load the given start document
			content.loadScore(SimpleGuiDemo.startDoc);
		}
	}

	@FXML void onFileOpen(ActionEvent event) {
		content.loadNextScore();
	}

	@FXML void onFileSaveAsPDF(ActionEvent event) {
		content.saveAs("pdf");
	}
	
	@FXML void onFileSaveAsPNG(ActionEvent event) {
		content.saveAs("png");
	}
	
	@FXML void onFileSaveAsMID(ActionEvent event) {
		content.saveAs("mid");
	}
	
	@FXML void onFileSaveAsOGG(ActionEvent event) {
		content.saveAs("ogg");
	}

	@FXML void onExit(ActionEvent event) {
		SimpleGuiDemo.exit();
	}
	
	@FXML void onZoomIn(ActionEvent event) {
		zoom(zoomFactor);
	}
	
	@FXML void onZoomOut(ActionEvent event) {
		zoom(1 / zoomFactor);
	}
	
	private void zoom(float zoomFactor) {
		zoom *= zoomFactor;
		scoreImage = null;
		renderLayout(content.getLayout());
	}

	@FXML void onPlaybackStart(ActionEvent event) {
		Playback.start();
	}

	@FXML void onPlaybackStop(ActionEvent event) {
		Playback.stop();
	}
	
	@FXML void onAbout(ActionEvent event) {
		showMessageDialog("This little demo app shows how to use Zong! in your own software.\n" +
			"There are much more complex use cases, but it may be helpful for the beginning.\n\n" +
			"Any questions or ideas? Contact us: info@xenoage.com");
	}
	
	@FXML void onScoreClick(MouseEvent event) {
		content.onClick(new Point2f((float) event.getX(), (float) event.getY()));
	}
	
	public void renderLayout(Layout layout) {
		//run in JavaFX application thread
		Platform.runLater(() -> {
			
			if (useJavaFX) {
				//JavaFX renderer
				scoreImage = JfxLayoutRenderer.paintToImage(layout, 0, zoom);
			}
			else {
				//AWT renderer
				BufferedImage awtImage = AwtLayoutRenderer.paintToImage(layout, 0, zoom);
				scoreImage = SwingFXUtils.toFXImage(awtImage, scoreImage);
			}

			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		});
	}
	
	public void showMessageDialog(String message) {
		Alert dialog = dialog();
		dialog.setContentText(message);
		dialog.showAndWait();
	}

	public void handleKeyEvent(KeyEvent keyEvent) {
		/*
		if (keyEvent.getCode() == KeyCode.PLUS) {
			//edit the score
			//...
			content.onScoreUpdated();
		} */
	}
	
	private Alert dialog() {
		Alert ret = new Alert(AlertType.INFORMATION);
		ret.setTitle(SimpleGuiDemo.appName);
		return ret;
	}

}
