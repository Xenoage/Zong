package com.xenoage.zong.demos.simplegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.desktop.utils.error.GuiErrorProcessing;

/**
 * Main class of this simple GUI demo app.
 * 
 * @author Andreas Wenger
 */
public class SimpleGuiDemo
	extends Application {
	
	public static final String appName = "SimpleDemo";
	public static final String appVersion = "0.1";
	
	
	/**
	 * Entry point.
	 */
	public static void main(String... args)
		throws Exception {
		//initialize platform-dependent utilities, including I/O
		JseZongPlatformUtils.init(appName);
		//init logging and error handling
		Log.init(new DesktopLogProcessing(appName + " " + appVersion));
		Err.init(new GuiErrorProcessing());
		//init audio engine
		SynthManager.init(false);
		//start the JavaFX app
		Application.launch(SimpleGuiDemo.class, args);
	}

	/**
	 * This method is called by JavaFX when the application is started.
	 */
	@Override public void start(Stage stage)
		throws Exception {
		//load main window FXML into stage and show it
		Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
    stage.setTitle("Simple Demo App based on Zong!");
    stage.setScene(new Scene(root));
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
