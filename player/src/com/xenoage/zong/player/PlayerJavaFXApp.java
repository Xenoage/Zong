package com.xenoage.zong.player;

import static com.xenoage.zong.desktop.gui.utils.FXUtils.createNodeFromFXML;
import static com.xenoage.zong.player.PlayerApp.pApp;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX main class.
 * 
 * @author Andreas Wenger
 */
public class PlayerJavaFXApp
	extends Application {

	@Override public void start(Stage stage)
		throws Exception {
		
		Parent root = createNodeFromFXML("/com/xenoage/zong/gui/Player.fxml");
		Scene scene = new Scene(root);
		stage.setTitle(pApp().getNameAndVersion());
		stage.setScene(scene);
		stage.show();
	}

}
