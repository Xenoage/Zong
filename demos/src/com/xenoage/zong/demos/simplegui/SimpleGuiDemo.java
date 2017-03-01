package com.xenoage.zong.demos.simplegui;

import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.desktop.utils.error.GuiErrorProcessing;
import com.xenoage.zong.documents.ScoreDoc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of this simple GUI demo app.
 * 
 * @author Andreas Wenger
 */
public class SimpleGuiDemo
	extends Application {
	
	public static final String appName = "SimpleGuiDemo";
	public static final String appVersion = "0.1";
	
	public static MainWindow mainWindow;
	public static ScoreDoc startDoc;
	
	
	/**
	 * Entry point.
	 */
	public static void main(String... args)
		throws Exception {
		start(null, args);
	}
	
	public static void start(ScoreDoc startDoc, String... args)
		throws Exception {
		SimpleGuiDemo.startDoc = startDoc;
		initZong();
		//start the JavaFX app
		Application.launch(SimpleGuiDemo.class, args);
	}

	/**
	 * Initializes the Zong! engine.
	 */
	public static void initZong()
		throws Exception {
		//initialize platform-dependent utilities, including I/O
		JseZongPlatformUtils.init(appName);
		//init logging and error handling
		Log.init(new DesktopLogProcessing(appName + " " + appVersion));
		Err.init(new GuiErrorProcessing());
		//init audio engine
		SynthManager.init(false);
	}

	/**
	 * This method is called by JavaFX when the application is started.
	 */
	@Override public void start(Stage stage)
		throws Exception {
		//load main window FXML into stage and show it
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("MainWindow.fxml").openStream());
		mainWindow = (MainWindow) fxmlLoader.getController();
    stage.setTitle("Simple Demo App based on Zong!");
    Scene scene = new Scene(root);
    scene.setOnKeyPressed(mainWindow::handleKeyEvent);
    stage.setScene(scene);
    stage.setOnCloseRequest(e -> exit());
    stage.show();
	}
	
	/**
	 * Closes the app.
	 */
	public static void exit() {
		Playback.stop();
		System.exit(0);
	}

}
