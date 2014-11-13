package com.xenoage.zong.player;

import static com.xenoage.utils.jse.javafx.Dialog.dialog;
import static com.xenoage.zong.player.Player.pApp;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;

import com.xenoage.zong.gui.PlayerFrame;

/**
 * JavaFX application for the {@link Player}.
 * 
 * @author Andreas Wenger
 */
public class PlayerFXApp
	extends Application {
	
	@Getter private static PlayerFXApp instance = null;
	@Getter private PlayerFrame frame = null;
	

	@Override public void start(Stage stage)
		throws Exception {
		instance = this;
		//create frame
		frame = dialog(PlayerFrame.class, stage);
		frame.setTitle(pApp().getName());
		//show frame
		frame.show();
	}

	@Override public void stop()
		throws Exception {
		super.stop();
		pApp().close();
	}

}
