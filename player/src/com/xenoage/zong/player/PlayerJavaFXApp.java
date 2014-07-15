package com.xenoage.zong.player;

import static com.xenoage.zong.player.Player.pApp;
import javafx.application.Application;
import javafx.stage.Stage;

import com.xenoage.zong.desktop.gui.utils.Dialog;
import com.xenoage.zong.gui.PlayerFrame;

/**
 * JavaFX main class.
 * 
 * @author Andreas Wenger
 */
public class PlayerJavaFXApp
	extends Application {

	@Override public void start(Stage ignored)
		throws Exception {
		
		Dialog.dialog(PlayerFrame.class).showDialog(null);
	}

	@Override public void stop()
		throws Exception {
		super.stop();
		pApp().close();
	}
	
	

}
