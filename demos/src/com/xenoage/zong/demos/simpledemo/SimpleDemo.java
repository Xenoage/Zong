package com.xenoage.zong.demos.simpledemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;

/**
 * Main class of this simple demo app.
 * 
 * @author Andreas Wenger
 */
public class SimpleDemo
	extends Application {
	
	/**
	 * Entry point.
	 */
	public static void main(String... args) {
		//initialize platform-dependent utilities, including I/O, logging, error handling...
		JseZongPlatformUtils.init("SimpleDemo");
		//start the JavaFX app
		Application.launch(SimpleDemo.class, args);
	}

	@Override public void start(Stage stage)
		throws Exception {
		//load main window FXML into stage and show it
		Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
    stage.setTitle("Simple Demo App based on Zong!");
    stage.setScene(new Scene(root));
    stage.show();
	}

}
