package com.xenoage.zong.player;

import static com.xenoage.zong.player.PlayerApp.pApp;

import com.xenoage.utils.jse.lang.LangResourceBundle;
import com.xenoage.zong.Voc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
		
		Parent root = FXMLLoader.load(
			getClass().getResource("../desktop/gui/AudioSettingsDialog.fxml"),
			new LangResourceBundle(Voc.values())); //../gui/Player.fxml"));
		Scene scene = new Scene(root);

		stage.setTitle(pApp().getNameAndVersion());
		stage.setScene(scene);
		stage.show();
	}

}
