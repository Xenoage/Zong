package com.xenoage.utils.jse.javafx;

import javafx.stage.Stage;

import com.xenoage.utils.lang.VocID;

/**
 * General functionality each JavaFX should provide.
 * 
 * Override this class and call {@link #init(JavaFXApp)} to
 * make it available for other classes.
 * 
 * @author Andreas Wenger
 */
public class JavaFXApp {

	private static JavaFXApp instance = new JavaFXApp();
	
	public static void init(JavaFXApp instance) {
		JavaFXApp.instance = instance;
	}
	
	public static JavaFXApp javaFXApp() {
		return instance;
	}
	
	public VocID[] getVoc() {
		return new VocID[0];
	}
	
	public void applyIcons(Stage stage) {
	}
	
	public String getName() {
		return "unknown";
	}
	
}
